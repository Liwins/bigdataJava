package cn.riversky.logAnalyze.storm.domain;

/**
 * 任务分析描述类
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/18.
 */
public class LogAnalyzeJob {
    /**
     * 任务id
     */
    private String jobId;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 任务类型1，浏览日志,2点击日志,3搜索日志，4购买日志
     */
    private int jobType;
    /**
     * 所属业务线
     */
    private int bussinessId;
    /**
     * 状态1，在线2，下线
     */
    private int status;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    public int getBussinessId() {
        return bussinessId;
    }

    public void setBussinessId(int bussinessId) {
        this.bussinessId = bussinessId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LogAnalyzeJob{" +
                "jobId='" + jobId + '\'' +
                ", jobName='" + jobName + '\'' +
                ", jobType=" + jobType +
                ", bussinessId=" + bussinessId +
                ", status=" + status +
                '}';
    }
}
