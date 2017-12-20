package cn.riversky.logEmail.domain;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/14.
 */
public class App{
    /**
     * 应用编号
     */
    private int id;
    /**
     * 应用名称
     */
    private String name;
    /**
     * 应用是否在线
     */
    private int isOnline;
    /**
     * 应用类型
     */
    private int typeId;
    /**
     * 应用负责人，逗号隔开
     */
    private String userId;

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

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "App{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isOnline=" + isOnline +
                ", typeId=" + typeId +
                ", userId='" + userId + '\'' +
                '}';
    }
}
