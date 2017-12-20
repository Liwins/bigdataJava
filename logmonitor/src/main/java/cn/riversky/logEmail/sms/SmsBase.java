package cn.riversky.logEmail.sms;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 这部分需要根据购买的接口，定义其实现部分
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/15.
 */
public class SmsBase {
    private static Logger logger= Logger.getLogger(SmsBase.class);
    private static final String USER_ID="riversky";
    private static final String PASSWORD="riversky";
    public static boolean sendSms(String mobile,String content){
        HttpURLConnection httpconn=null;
        BufferedReader rd=null;
        String result="";
        try{
            StringBuilder sb=new StringBuilder();
            sb.append("http://service.winic.org:8009/sys_port/gate/index.asp?");
            //以下是get参数
            sb.append("id=").append(URLEncoder.encode(USER_ID,"gb2312"));
            sb.append("&pwd=").append(PASSWORD);
            sb.append("&to=").append(mobile);
            sb.append("&content=").append(URLEncoder.encode(content,"gb2312"));
            sb.append("&time=").append("");
            URL url=new URL(sb.toString());
            httpconn= (HttpURLConnection) url.openConnection();
            rd=new BufferedReader(new InputStreamReader(httpconn.getInputStream()));
            result=rd.readLine();
            System.out.println("==================="+result);
            rd.close();
        }catch (MalformedURLException e){
            logger.info(e);
        }catch (IOException e){
            logger.info(e);
        }finally {
            if(rd!=null){
                try {
                    rd.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(httpconn!=null){
                httpconn.disconnect();
            }
            httpconn=null;
        }
        if(StringUtils.isNotBlank(result)){
            if(result.substring(0,3).equals("000")){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(sendSms("15236287175","helloword"));
    }
}
