package cn.riversky.logEmail.domain;

import java.io.Serializable;

/**
 * 描述发送的信息
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/14.
 */
public class Message implements Serializable{

    private static final long serialVersionUID = 3535294245419726264L;
    /**
     * 消息所属服务器编号
     */
    private String appId;
    /**
     * 消息内容
     */
    private String line;
    /**
     * 规则编号
     */
    private String ruleId;
    /**
     * 规则关键词
     */
    private String keyword;
    /**
     * 是否已发送邮件
     */
    private int isEmail;
    /**
     * 是否已发送短信
     */
    private int isPhone;
    /**
     * 应用名称
     */
    private String appName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getIsEmail() {
        return isEmail;
    }

    public void setIsEmail(int isEmail) {
        this.isEmail = isEmail;
    }

    public int getIsPhone() {
        return isPhone;
    }

    public void setIsPhone(int isPhone) {
        this.isPhone = isPhone;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "Message{" +
                "appId='" + appId + '\'' +
                ", line='" + line + '\'' +
                ", ruleId='" + ruleId + '\'' +
                ", keyword='" + keyword + '\'' +
                ", isEmail=" + isEmail +
                ", isPhone=" + isPhone +
                ", appName='" + appName + '\'' +
                '}';
    }
}
