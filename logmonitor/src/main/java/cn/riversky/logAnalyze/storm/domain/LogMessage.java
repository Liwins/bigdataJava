package cn.riversky.logAnalyze.storm.domain;

import java.io.Serializable;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/18.
 */
public class LogMessage implements Serializable{
    private static final long serialVersionUID = -177052278713128104L;
    /**
     * 日志类型1浏览日志，2点击日志，3搜索日志，4购买日志
     */
    private int type;
    /**
     * 标签标识
     */
    private String hrefTag;
    /**
     * 标签对应的标识，主要针对a标签之后的内容
     */
    private String hrefContent;
    /**
     * 来源网址
     */
    private String referrerUrl;
    /**
     * 来源网址
     */
    private String requestUrl;
    /**
     * 点击时间
     */
    private String clickTime;
    /**
     * 浏览器类型
     */
    private String appName;
    /**
     * 浏览器版本
     */
    private String appVersion;
    /**
     * 浏览器语言
     */
    private String language;
    /**
     * 操作系统
     */
    private String platform;
    /**
     * 屏幕尺寸
     */
    private String screen;
    /**
     * 鼠标点击使坐标
     */
    private String coordinate;
    /**
     * 产生点击流的系统编号
     */
    private String systemId;
    /**
     * 用户名称
     */
    private String userName;

    public LogMessage(int type, String referrerUrl, String requestUrl, String userName) {
        this.type = type;
        this.referrerUrl = referrerUrl;
        this.requestUrl = requestUrl;
        this.userName = userName;
    }

    /**
     * 获取消息中的数据（属要是分析其中的类型）
     * @param field
     * @return
     */
    public String getCompareFiledValue(String field){
        if("hrefTag".equals(field)){
            return hrefTag;
        }else if ("referrerUrl".equalsIgnoreCase(field)){
            return referrerUrl;
        }else if("requestUrl".equalsIgnoreCase(field)){
            return requestUrl;
        }
        return "";
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHrefTag() {
        return hrefTag;
    }

    public void setHrefTag(String hrefTag) {
        this.hrefTag = hrefTag;
    }

    public String getHrefContent() {
        return hrefContent;
    }

    public void setHrefContent(String hrefContent) {
        this.hrefContent = hrefContent;
    }

    public String getReferrerUrl() {
        return referrerUrl;
    }

    public void setReferrerUrl(String referrerUrl) {
        this.referrerUrl = referrerUrl;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getClickTime() {
        return clickTime;
    }

    public void setClickTime(String clickTime) {
        this.clickTime = clickTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "LogMessage{" +
                "type=" + type +
                ", hrefTag='" + hrefTag + '\'' +
                ", hrefContent='" + hrefContent + '\'' +
                ", referrerUrl='" + referrerUrl + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", clickTime='" + clickTime + '\'' +
                ", appName='" + appName + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", language='" + language + '\'' +
                ", platform='" + platform + '\'' +
                ", screen='" + screen + '\'' +
                ", coordinate='" + coordinate + '\'' +
                ", systemId='" + systemId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
