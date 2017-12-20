package cn.riversky.logEmail.utils;

import cn.riversky.logEmail.dao.LogMonitorDao;
import cn.riversky.logEmail.domain.*;
import cn.riversky.logEmail.mail.MailInfo;
import cn.riversky.logEmail.mail.MessageSender;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志监控的核心类，包含了日志监控系统所有的核心处理。
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/14.
 */
public class MonitorHandler {
    private  static Logger logger= Logger.getLogger(MonitorHandler.class);
    /**
     * appid为key,List为其中的规则
     */
    private static Map<String,List<Rule>> ruleMap;
    /**
     * appId为key,List为该ip下的用户
     */
    private static Map<String,List<User>> userMap;
    /**
     * 用来封装所有的应用信息
     */
    private static List<App> applist;
    /**
     * 用来封装所有的用户
     */
    private static List<User> userList;
    /**
     * 定时加载配置文件的标识
     */
    private static boolean reloaded=false;
    /**
     * 定时加载配置文件
     */
    private static long nextReload=01;
    static{
        load();
    }

    /**
     * 解析输入的日志，将数据按照一定的规则进行分割。
     * 判断日志是否合法，主要校验日志所属应用的appId是否存在
     * @param line 一条日志
     * @return
     */
    public static Message parse(String line){
        //日志内容分为两个部分：由五个$$$$$符号进行分割，第一部分为appid,第二部分为日志内容。
        String []messageArr=line.split("\\$\\$\\$\\$\\$");
        //对日志进行校验
        if(messageArr.length!=2){
            return null;
        }
        if(StringUtils.isBlank(messageArr[0])||StringUtils.isBlank(messageArr[1])){
            return null;
        }
        //检验当前日志的应用是否经过授权
        if(appIdisValid(messageArr[0])){
            Message message=new Message();
            message.setAppId(messageArr[0].trim());
            message.setLine(messageArr[1]);
            return message;
        }
        return null;
    }
    /**
     * 验证appid是否经过授权
     */
    private static boolean appIdisValid(String appId) {
        try {
            for (App app : applist) {
                if (app.getId() == Integer.parseInt(appId)) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 校验message是否触发规则
     * @param message
     * @return
     */
    public static boolean rigger(Message message){
        if(ruleMap==null){
            load();
        }
        //从规则中加载
        List<Rule> rules=ruleMap.get(message.getAppId());
        for(Rule rule:rules){
            if(message.getLine().contains(rule.getKeyword())){
                //注意这里，通过传递的引用进行设置规则
                message.setRuleId(rule.getId()+"");
                message.setKeyword(rule.getKeyword());
                return true;
            }
        }
        return false;
    }
    /**
     * 加载数据模型，主要是用户列表、应用管理表、组合规则模型、组合用户模型。
     */
    public static synchronized void load() {
        if (userList == null) {
            userList = loadUserList();
        }
        if (applist == null) {
            applist = loadAppList();
        }
        if (ruleMap == null) {
            ruleMap = loadRuleMap();
        }
        if (userMap == null) {
            userMap = loadUserMap();
        }
    }
    /**
     * 访问数据库获取所有有效的app列表
     * @return
     */
    private static List<App> loadAppList() {
        return new LogMonitorDao().getAppList();
    }

    /**
     * 访问数据库获取所有有效用户的列表
     * @return
     */
    private static List<User> loadUserList() {
        return new LogMonitorDao().getUserList();
    }
    /**
     * 封装应用与用户对应的map
     * @return
     */
    private static Map<String, List<User>> loadUserMap() {
        //以应用的appId为key，以应用的所有负责人的userList对象为value。
        //HashMap<String, List<User>>
        HashMap<String, List<User>> map = new HashMap<String, List<User>>();
        for (App app : applist) {
            String userIds = app.getUserId();
            List<User> userListInApp = map.get(app.getId());
            if (userListInApp == null) {
                userListInApp = new ArrayList<User>();
                map.put(app.getId() + "", userListInApp);
            }
            String[] userIdArr = userIds.split(",");
            for (String userId : userIdArr) {
                userListInApp.add(queryUserById(userId));
            }
            map.put(app.getId() + "", userListInApp);
        }
        return map;
    }

    /**
     *  封装应用与规则的map
     * @return
     */
    private static Map<String, List<Rule>> loadRuleMap() {
        Map<String, List<Rule>> map = new HashMap<String, List<Rule>>();
        LogMonitorDao logMonitorDao = new LogMonitorDao();
        List<Rule> ruleList = logMonitorDao.getRuleList();
        //将代表rule的list转化成一个map，转化的逻辑是，
        // 从rule.getAppId作为map的key，然后将rule对象作为value传入map
        //Map<appId,ruleList>  一个appid的规则信息，保存在一个list中。
        for (Rule rule : ruleList) {
            List<Rule> ruleListByAppId = map.get(rule.getAppId()+"");
            if (ruleListByAppId == null) {
                ruleListByAppId = new ArrayList<Rule>();
                map.put(rule.getAppId() + "", ruleListByAppId);
            }
            ruleListByAppId.add(rule);
            map.put(rule.getAppId() + "", ruleListByAppId);
        }
        return map;
    }

    /**
     * 通过用户编号获取用户的JavaBean
     * @param userId
     * @return
     */
    private static User queryUserById(String userId) {
        for (User user : userList) {
            if (user.getId() == Integer.parseInt(userId)) {
                return user;
            }
        }
        return null;
    }

    /**
     * 通过app编号，获取当前app的所有负责人列表
     * @param appId
     * @return
     */
    public static List<User> getUserIdsByAppId(String appId) {
        return userMap.get(appId);
    }

    /**
     * 告警模块，用来发送邮件和短信
     * 短信功能由于收费这里，不测试
     * @param appId
     * @param message
     */
    public static void notifly(String appId,Message message){
        List<User> users=getUserIdsByAppId(appId);
        //发送邮件
        if(sendMail(appId,users,message)){
            message.setIsEmail(1);
        }
        //发送短信
        if(sendSMS(appId,users,message)){
            message.setIsPhone(1);
        }
    }



    /**
     * 发送短信模块
     * 由于收费，该模块默认不开启，默认发送成功
     * 由于发送短信是外部接口，其并发性无法保证，会影响Storm程序的效率。可以将短信数据发送到外部的
     * 消息队列中，然后创建一个worker去发送短信。
     * @param appId
     * @param users
     * @param message
     * @return
     */
    private static boolean sendSMS(String appId,List<User> users,Message message){
        List<String> mobileList=new ArrayList<String>();
        for(User user:users){
            mobileList.add(user.getMobile());
        }
        for(App app:applist){
            //根据Appid获取其应用名称
            if(app.getId()==Integer.parseInt(appId.trim())){
                message.setAppName(app.getName());
                break;
            }
        }
        String content="应用["+message.getAppName()+"]在"+DateUtils.getDateTime()+" 触发规则"+message.getRuleId()+",关键字:"+message.getKeyword();
//        return SmsBase.sendSms(listToStringFormat(mobileList),content);
        return true;
    }
    private static boolean sendMail(String appId, List<User> users, Message message) {
        List<String> receiver=new ArrayList<String>();
        for(User user:users){
            receiver.add(user.getEmail());
        }
        for(App app:applist){
            if(app.getId()==Integer.parseInt(appId.trim())){
                message.setAppName(app.getName());
                break;
            }
        }
        if(receiver.size()>=1){
            String date=DateUtils.getDateTime();
            String content="应用["+message.getAppName()+"]在"+DateUtils.getDateTime()+" 触发规则"+message.getRuleId()+",关键字:"+message.getKeyword();
            MailInfo mailInfo=new MailInfo("系统监控日志",content,receiver,null);
            return MessageSender.sendMail(mailInfo);
        }
        return false;
    }

    /**
     * 保存触发规则的信息，将触发信息写入mysql中。
     * @param record
     */
    public static void save(Record record){
        new LogMonitorDao().saveRecord(record);
    }

    /**
     * 主要给邮件接受者，和邮件抄送转换使用
     * @param list
     * @return
     */
    public static String listToStringFormat(List<String> list){
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<list.size();i++){
            if(i==list.size()-1){
                sb.append(list.get(i));
            }else {
                sb.append(list.get(i)).append(",");
            }
        }
        return sb.toString();
    }

    public static synchronized void reloadDataModel(){
//        * thread 1  reloaded = true   ----> reloaded = false
//        * thread 2  reloaded = false
//        * thread 2  reloaded = false
//        * thread 2  reloaded = false

        if(reloaded){
            long start=System.currentTimeMillis();
            userList=loadUserList();
            applist=loadAppList();
            ruleMap=loadRuleMap();
            userMap=loadUserMap();
            reloaded=false;
            nextReload=01;
            logger.info("配置文件reload完成，时间："+DateUtils.getDateTime()+" 耗时："+ (System.currentTimeMillis()-start));
        }
    }

    /**
     * 定时再在配置信息
     * 配合reloadDataModel模块使用
     * 主要实现原理：
     */
    public static void scheduleLoad(){
        String date=DateUtils.getDateTime();
        int now=Integer.parseInt(date.split(":")[1]);
        /**
         * 每隔两分钟更新一次内存中的日志系统数据（其实放在redis中也是一种方案）
         */
        if(now%2==0){
            reloadDataModel();
        }else {
            reloaded=true;
        }
    }
}
