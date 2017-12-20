package cn.riversky.logEmail.mail;

import java.util.List;
import java.util.Properties;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/15.
 */
public class MailInfo {
    /**
     * 发送邮件的服务器IP
     */
    private String mailServerHost;
    /**
     * 发送邮件的服务器端口
     */
    private String mailServerPort = "25";
    /**
     * 登陆邮件发送服务器的用户名
     */
    private String userName;
    /**
     * 登陆邮件发送服务器的密码
     */
    private String userPassword;
    /**
     * 邮件发送者的地址
     */
    private String fromAddress;
    /**
     * 邮件接收者的地址
     */
    private String toAddress;
    /**
     * 邮件抄送者的地址
     */
    private String ccAddress;
    /**
     * 邮件发送者的名称，显示在他人邮件的发件人
     */
    private String fromUserName = "日志监控平台";
    /**
     * 邮件主题
     */
    private String mailSubject;
    /**
     * 邮件的文本内容
     */
    private String mailContent;
    /**
     * 是否需要身份验证
     */
    private boolean authValidate = true;
    /**
     * 邮件会话属性
     */
    private Properties properties;

    public MailInfo() {
    }

    /**
     * 基本信息构造
     * @param title
     * @param content
     * @param receiver
     * @param ccList
     */
    public MailInfo(String title,String content,List<String> receiver,List<String> ccList){
        this.mailServerHost=MailCenterConstant.SMTP_SERVER;
        userName=MailCenterConstant.USER;
        userPassword=MailCenterConstant.PWD;
        fromAddress=MailCenterConstant.FROM_ADDRESS;
        toAddress=listToStringFormat(receiver);
        ccAddress=ccList==null?"":listToStringFormat(ccList);
        mailSubject=title;
        mailContent=content;
    }
    private synchronized String listToStringFormat(List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                stringBuilder.append(list.get(i));
            } else {
                stringBuilder.append(list.get(i)).append(",");
            }
        }
        return stringBuilder.toString();
    }
    public String getMailServerHost() {
        return mailServerHost;
    }

    public void setMailServerHost(String mailServerHost) {
        this.mailServerHost = mailServerHost;
    }

    public String getMailServerPort() {
        return mailServerPort;
    }

    public void setMailServerPort(String mailServerPort) {
        this.mailServerPort = mailServerPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getCcAddress() {
        return ccAddress;
    }

    public void setCcAddress(String ccAddress) {
        this.ccAddress = ccAddress;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public boolean isAuthValidate() {
        return authValidate;
    }

    public void setAuthValidate(boolean authValidate) {
        this.authValidate = authValidate;
    }

    public Properties getProperties() {
        Properties p=new Properties();
        p.put("mail.smtp.host",this.getMailServerHost());
        p.put("mail.smtp.port",this.getMailServerPort());
        p.put("mail.smtp.auth",authValidate?"true":"false");
        p.put("mail.smtp.starttls.enable",true);
        return p;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
