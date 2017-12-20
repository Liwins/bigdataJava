package cn.riversky.logEmail.domain;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/14.
 */
public class Rule {
    /**
     * 规则编号
     */
    private int id;
    /**
     * 规则名称
     */
    private String name;
    /**
     * 规则过滤的关键字
     */
    private String keyword;
    /**
     * 规则是否可用
     */
    private int isValid;
    /**
     * 规则所属的应用
     */
    private int appId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getIsValid() {
        return isValid;
    }

    public void setIsValid(int isValid) {
        this.isValid = isValid;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", keyword='" + keyword + '\'' +
                ", isValid=" + isValid +
                ", appId=" + appId +
                '}';
    }
}
