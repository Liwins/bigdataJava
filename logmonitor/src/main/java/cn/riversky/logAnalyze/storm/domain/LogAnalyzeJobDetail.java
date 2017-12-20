package cn.riversky.logAnalyze.storm.domain;

/**
 *详细描述
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/18.
 */
public class LogAnalyzeJobDetail {
    /**
     * 编号
     */
    private int id;
    /**
     * 任务编号
     */
    private int jobId;
    /**
     * 用来比较的字段
     */
    private String field;
    /**
     * 参与比较的字段值
     */
    private String value;
    /**
     * 比较方式1：包含2等于
     */
    private int compare;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCompare() {
        return compare;
    }

    public void setCompare(int compare) {
        this.compare = compare;
    }

    @Override
    public String toString() {
        return "LogAnalyzeJobDetail{" +
                "id=" + id +
                ", jobId=" + jobId +
                ", field='" + field + '\'' +
                ", value='" + value + '\'' +
                ", compare=" + compare +
                '}';
    }
}
