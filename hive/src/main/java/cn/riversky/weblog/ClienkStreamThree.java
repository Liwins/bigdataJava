package cn.riversky.weblog;

import cn.riversky.weblog.bean.WebLogBean;
import cn.riversky.weblog.bean.WebLogParser;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

/**
 * 将清洗之后的日志梳理出点击流pageviews模型数据
 * 输入数据是清洗过后的结果数据
 * 区分出每一次会话，给每一次visit（session）增加了session-id（随机uuid）
 * 梳理出每一次会话中所访问的每个页面（请求时间，url，停留时长，以及该页面在这次session中的序号）
 * 保留referral_url，body_bytes_send，useragent
 * Created by admin on 2017/12/4.
 */
public class ClienkStreamThree {
    static class ClickStreamVisitMapper extends Mapper<LongWritable, Text, Text, WebLogBean> {
        Text k = new Text();
        WebLogBean v = new WebLogBean();

        /**
         * 注意这里的数据是已经转换过得sessionid  start-time   out-time   start-page   out-page   pagecounts  ......
         *
         * @param key
         * @param value
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] fields = line.split("\001");
            if (fields.length < 9) return;
            v.set("true".equals(fields[0]) ? true : false, fields[1], fields[2], fields[3], fields[4], fields[5], fields[6], fields[7], fields[8]);
            //只有有效记录才进入后续处理
            if (v.isValid()) {
                k.set(v.getRemote_addr());
                context.write(k, v);
            }

        }
    }

    static class ClickStreamReducer extends Reducer<Text, WebLogBean, NullWritable, Text> {
        Text v = new Text();

        @Override
        protected void reduce(Text key, Iterable<WebLogBean> values, Context context) throws IOException, InterruptedException {
            ArrayList<WebLogBean> beans = new ArrayList<>();
            // 先将一个用户的所有访问记录中的时间拿出来排序
            try {
                for (WebLogBean value : values) {
                    WebLogBean webLogBean = new WebLogBean();
                    try {
                        BeanUtils.copyProperties(webLogBean, value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    beans.add(webLogBean);
                }
                Collections.sort(beans);
                /**
                 * 以下逻辑为：从有序bean中分辨出各次visit，并对一次visit中所访问的page按顺序标号step
                 */
                int step = 1;
                String session = UUID.randomUUID().toString();
                for (int i = 0; i < beans.size(); i++) {
                    WebLogBean bean = beans.get(i);
                    if (1 == beans.size()) {
                        // 设置默认停留市场为60s
                        v.set(session + "\001" + key.toString() + "\001" + bean.getRemote_user() + "\001" + bean.getTime_local() + "\001" + bean.getRequest() + "\001" + step + "\001" + (60) + "\001" + bean.getHttp_referer() + "\001" + bean.getHttp_user_agent() + "\001" + bean.getBody_bytes_sent() + "\001"
                                + bean.getStatus());
                        context.write(NullWritable.get(), v);
                        session = UUID.randomUUID().toString();
                        break;
                    }
                    // 如果不止1条数据，则将第一条跳过不输出，遍历第二条时再输出
                    if (i == 0) {
                        continue;
                    }
                    // 求近两次时间差

                    long timeDiff = timeDiff(WebLogParser.df2.parse(bean.getTime_local()), WebLogParser.df2.parse(beans.get(i - 1).getTime_local()));
                    // 如果本次-上次时间差<30分钟，则输出前一次的页面访问信息
                    if (timeDiff < 30 * 60 * 1000) {

                        v.set(session + "\001" + key.toString() + "\001" + beans.get(i - 1).getRemote_user() + "\001" + beans.get(i - 1).getTime_local() + "\001" + beans.get(i - 1).getRequest() + "\001" + step + "\001" + (timeDiff / 1000) + "\001" + beans.get(i - 1).getHttp_referer() + "\001"
                                + beans.get(i - 1).getHttp_user_agent() + "\001" + beans.get(i - 1).getBody_bytes_sent() + "\001" + beans.get(i - 1).getStatus());
                        context.write(NullWritable.get(), v);
                        step++;
                    } else {

                        // 如果本次-上次时间差>30分钟，则输出前一次的页面访问信息且将step重置，以分隔为新的visit
                        v.set(session + "\001" + key.toString() + "\001" + beans.get(i - 1).getRemote_user() + "\001" + beans.get(i - 1).getTime_local() + "\001" + beans.get(i - 1).getRequest() + "\001" + (step) + "\001" + (60) + "\001" + beans.get(i - 1).getHttp_referer() + "\001"
                                + beans.get(i - 1).getHttp_user_agent() + "\001" + beans.get(i - 1).getBody_bytes_sent() + "\001" + beans.get(i - 1).getStatus());
                        context.write(NullWritable.get(), v);
                        // 输出完上一条之后，重置step编号
                        step = 1;
                        session = UUID.randomUUID().toString();
                    }
                    // 如果此次遍历的是最后一条，则将本条直接输出
                    if (i == beans.size() - 1) {
                        // 设置默认停留市场为60s
                        v.set(session + "\001" + key.toString() + "\001" + bean.getRemote_user() + "\001" + bean.getTime_local() + "\001" + bean.getRequest() + "\001" + step + "\001" + (60) + "\001" + bean.getHttp_referer() + "\001" + bean.getHttp_user_agent() + "\001" + bean.getBody_bytes_sent() + "\001" + bean.getStatus());
                        context.write(NullWritable.get(), v);
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }


        }

        private long timeDiff(Date time1, Date time2) throws ParseException {

            return time1.getTime() - time2.getTime();

        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(ClienkStreamThree.class);
        job.setMapperClass(ClickStreamVisitMapper.class);
        job.setReducerClass(ClickStreamReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(WebLogBean.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        boolean res=job.waitForCompletion(true);
        System.exit(res?1:0);
    }
}
