package cn.riversky.logAnalyze.storm.utils;

import cn.riversky.logAnalyze.storm.constant.LogTypeConstant;
import cn.riversky.logAnalyze.storm.dao.LogAnalyzeDao;
import cn.riversky.logAnalyze.storm.domain.LogAnalyzeJob;
import cn.riversky.logAnalyze.storm.domain.LogAnalyzeJobDetail;
import cn.riversky.logAnalyze.storm.domain.LogMessage;
import com.google.gson.Gson;
import redis.clients.jedis.ShardedJedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志分析核心类
 *
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/19.
 */
public class LogAnalyzeHandler {
    //定时加载配置文件
    private static boolean reloaded = false;
    //用来不能存job信息，key为jobType,value为该类别下所有的任务。
    private static Map<String, List<LogAnalyzeJob>> jobMap;
    //用来保存job的判断条件，key为jobId,value为list，list中封装了很多判断条件。
    private static Map<String, List<LogAnalyzeJobDetail>> jobDetail;
    private static ShardedJedis jedis=JedisPoolUtils.getShardedJedisPool().getResource();
    static {
        jobMap = loadJobMap();
        jobDetail = loadJobDetailMap();
    }

    /**
     * 解析每一行的数据（json->object）
     *
     * @param line
     * @return
     */
    public static LogMessage parser(String line) {
        LogMessage l = new Gson().fromJson(line, LogMessage.class);
        return l;
    }

    /**
     * pv 在redis中是String, key:log:{jobId}:pv:{yyyyMMdd} ,value=pv数量
     * uv 使用java-boolmFilter计算
     *
     * @param logmessage
     */
    public static void process(LogMessage logmessage) {
        if (jobMap == null || jobDetail == null) {
            loadDataModel();
        }
        List<LogAnalyzeJob> analyzejobList = jobMap.get(logmessage.getType() + "");
        for (LogAnalyzeJob logjob : analyzejobList) {
            boolean isMatch = false;
            List<LogAnalyzeJobDetail> logjobDetails = jobDetail.get(logjob.getJobId());
            for (LogAnalyzeJobDetail jobDetil : logjobDetails) {
                //jobDetail，指定和kakfa输入过来的数据中的requesturl比较
                //获取kafka输入过来的数据的requestUrl的值
                String fieldValueInlog = logmessage.getCompareFiledValue(jobDetil.getField());
                //1包含2等于3正则
                if (jobDetil.getCompare() == 1 &&jobDetil.getValue().contains(fieldValueInlog)) {
                    isMatch = true;
                } else if (jobDetil.getCompare() == 2 && jobDetil.getValue().equals(fieldValueInlog)) {
                    isMatch = true;
                } else {
                    isMatch = false;
                }
                if (!isMatch) {
                    break;
                }
                if (isMatch) {

//                Jedis jedis1=new Jedis("datanode1");
//                jedis1.auth("riversky");
                    //设置pv
                    String pvKey = "log:" + logjob.getJobName() + ":pv:" + DateUtils.getDate();
                    String uvKey = "log:" + logjob.getJobName() + ":uv:" + DateUtils.getDate();

                    jedis.incr(pvKey);
                    //设置uv
                    jedis.sadd(uvKey, logmessage.getUserName());
                }
            }

        }
    }

    private static void loadDataModel() {
        if (jobMap == null) {
            jobMap = loadJobMap();
        }
        if (jobDetail == null) {
            jobDetail = loadJobDetailMap();
        }
    }

    private static Map<String, List<LogAnalyzeJob>> loadJobMap() {

        Map<String, List<LogAnalyzeJob>> map = new HashMap<String, List<LogAnalyzeJob>>(8);
        List<LogAnalyzeJob> logAnalyzeJobList = new LogAnalyzeDao().loadJobList();
        System.out.println(logAnalyzeJobList);
        for (LogAnalyzeJob logAnalyzeJob : logAnalyzeJobList) {
            int jobType = logAnalyzeJob.getJobType();
            if (isValidType(jobType)) {
                List<LogAnalyzeJob> jobList = map.get(jobType + "");
                if (jobList == null || jobList.size() == 0) {
                    jobList = new ArrayList<>();
                    map.put(jobType + "", jobList);
                }
                jobList.add(logAnalyzeJob);
            }
        }
        System.out.println("job:  " + map);
        return map;
    }

    public static boolean isValidType(int jobType) {
        if (jobType == LogTypeConstant.BUY || jobType == LogTypeConstant.CLICK
                || jobType == LogTypeConstant.VIEW || jobType == LogTypeConstant.SEARCH) {
            return true;
        }
        return false;
    }

    /**
     * 逻辑就是从数据库中取出来数据，构造到当前的map容器中
     *
     * @return
     */
    private static Map<String, List<LogAnalyzeJobDetail>> loadJobDetailMap() {
        Map<String, List<LogAnalyzeJobDetail>> map = new HashMap<String, List<LogAnalyzeJobDetail>>();
        List<LogAnalyzeJobDetail> jobdetail = new LogAnalyzeDao().loadAnalyzeJobDetailList();
        for (LogAnalyzeJobDetail jobDetail : jobdetail) {
            int jobId = jobDetail.getJobId();
            List<LogAnalyzeJobDetail> jobDetails = map.get(jobId + "");
            if (jobDetails == null || jobDetails.size() == 0) {
                jobDetails = new ArrayList<>();
                map.put(jobId + "", jobDetails);
            }
            jobDetails.add(jobDetail);
        }
        return map;
    }
    public static synchronized void reloadModeData(){
        if(reloaded){
            jobMap=loadJobMap();
            jobDetail=loadJobDetailMap();
            reloaded=false;
        }
    }

    /**
     * 定时加载配置信息
     * 配合reloadDataModel模块一起使用。
     * 主要实现原理如下：
     * 1，获取分钟的数据值，当分钟数据是10的倍数，就会触发reloadDataModel方法，简称reload时间。
     * 2，reloadDataModel方式是线程安全的，在当前worker中只有一个现成能够操作。
     * 3，为了保证当前线程操作完毕之后，其他线程不再重复操作，设置了一个标识符reloaded。
     * 在非reload时间段时，reloaded一直被置为true；
     * 在reload时间段时，第一个线程进入reloadDataModel后，加载完毕之后会将reloaded置为false。
     */
    public static void scheduleLoad() {
        String date = DateUtils.getDateTime();
        int now = Integer.parseInt(date.split(":")[1]);
        if (now % 10 == 0) {
            reloadModeData();
        }else {
            reloaded=true;
        }
    }

}
