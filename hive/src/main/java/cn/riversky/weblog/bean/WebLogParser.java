package cn.riversky.weblog.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Set;

/**
 * Created by admin on 2017/12/5.
 */
public class WebLogParser {
    public static SimpleDateFormat df1=new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US);
    public static SimpleDateFormat df2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
    public static WebLogBean parse(String line){
        WebLogBean webLogBean=new WebLogBean();
        String[]word=line.split(" ");
        if(word.length>11){
            webLogBean.setRemote_addr(word[0]);
            webLogBean.setRemote_user(word[1]);
            String time_local =formatDate(word[3].substring(1));
            if(null==time_local) time_local="-invalid_time-";
            webLogBean.setTime_local(time_local);
            webLogBean.setRequest(word[6]);
            webLogBean.setStatus(word[8]);
            webLogBean.setBody_bytes_sent(word[9]);
            webLogBean.setHttp_referer(word[10]);
            if(word.length>12){
                StringBuffer sb=new StringBuffer();
                for (int i = 11; i < word.length; i++) {
                    sb.append(word[i]);
                }
                webLogBean.setHttp_user_agent(sb.toString());
            }else {
                webLogBean.setHttp_user_agent(word[11]);
            }
            if (Integer.parseInt(webLogBean.getStatus()) >= 400) {// 大于400，HTTP错误
                webLogBean.setValid(false);
            }
            if("-invalid_time-".equals(webLogBean.getTime_local())){
                webLogBean.setValid(false);
            }
        }else {
            webLogBean.setValid(false);
        }
        return webLogBean;
    }
    public static void filtStaticResource(WebLogBean bean, Set<String> pages){
        if(!pages.contains(bean.getRequest())){
            bean.setValid(false);
        }
    }
    private static String formatDate(String time_local) {
        try {
            return df2.format(df1.parse(time_local));
        } catch (ParseException e) {
            return null;
        }
    }
}
