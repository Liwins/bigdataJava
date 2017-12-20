package cn.riversky.logAnalyze.app.domain;

import java.util.Date;

/**
 * 前端可视化后台的基本类
 * 字段：rdiskey,indexName，pv,uv,ProcessTime
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/18.
 */
public class BaseRecord {
    private String indexName;
    private String redisKey;
    private int pv;
    private long uv;
    private Date processTime;

    public BaseRecord() {
    }

    public BaseRecord(String indexName, int pv, long uv, Date processTime) {
        this.indexName = indexName;
        this.pv = pv;
        this.uv = uv;
        this.processTime = processTime;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getRedisKey() {
        return redisKey;
    }

    public void setRedisKey(String redisKey) {
        this.redisKey = redisKey;
    }

    public int getPv() {
        return pv;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public long getUv() {
        return uv;
    }

    public void setUv(long uv) {
        this.uv = uv;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    @Override
    public String toString() {
        return "BaseRecord{" +
                "indexName='" + indexName + '\'' +
                ", redisKey='" + redisKey + '\'' +
                ", pv=" + pv +
                ", uv=" + uv +
                ", processTime=" + processTime +
                '}';
    }
}
