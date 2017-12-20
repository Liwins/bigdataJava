package cn.riversky.logAnalyze.storm.dao;

import cn.riversky.logAnalyze.app.domain.BaseRecord;
import cn.riversky.logAnalyze.storm.domain.LogAnalyzeJob;
import cn.riversky.logAnalyze.storm.domain.LogAnalyzeJobDetail;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/18.
 */
public class LogAnalyzeDao {
    private static Logger logger = Logger.getLogger(LogAnalyzeDao.class);
    private JdbcTemplate jdbcTemplate;

    public LogAnalyzeDao() {
        jdbcTemplate = new JdbcTemplate(DataSourceUtils.getDataSource());
    }

    public List<LogAnalyzeJob> loadJobList() {
        String sql = "SELECT `jobId`,`jobName`,`jobType` " +
                " FROM `log_analyze`.`log_analyze_job`" +
                " WHERE STATUS= 1";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LogAnalyzeJob.class));
    }

    public List<LogAnalyzeJobDetail> loadAnalyzeJobDetailList() {
        String sql = "SELECT condi.`jobId`,condi.`field`,condi.`value`,condi.`compare` " +
                " FROM `log_analyze`.`log_analyze_job` AS job " +
                " LEFT JOIN `log_analyze`.`log_analyze_job_condition` AS condi  " +
                " ON job.`jobId` = condi.`jobId` " +
                " WHERE job.`status` =1";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LogAnalyzeJobDetail.class));
    }

    public int[][] saveMinuteAppendRecord(List<BaseRecord> appendDataList) {
        String sql = "INSERT INTO `log_analyze_job_nimute_append` (`indexName`,`pv`,`uv`,`executeTime`,`createTime` ) " +
                "VALUES (?,?,?,?,?)";
        return savaAppendRecord(appendDataList, sql);
    }

    public int[][] saveHalfAppendRecord(List<BaseRecord> appendDataList) {
        String sql = "INSERT INTO `log_analyze_job_half_append` (`indexName`,`pv`,`uv`,`executeTime`,`createTime` ) " +
                "VALUES (?,?,?,?,?)";
        return savaAppendRecord(appendDataList, sql);
    }

    public int[][] saveHourAppendRecord(List<BaseRecord> appendDataList) {
        String sql = "INSERT INTO `log_analyze_job_hour_append` (`indexName`,`pv`,`uv`,`executeTime`,`createTime` ) " +
                "VALUES (?,?,?,?,?)";
        return savaAppendRecord(appendDataList, sql);
    }

    public int[][] saveDayAppendRecord(List<BaseRecord> appendDataList) {
        String sql = "INSERT INTO `log_analyze_job_day` (`indexName`,`pv`,`uv`,`executeTime`,`createTime` ) " +
                "VALUES (?,?,?,?,?)";
        return savaAppendRecord(appendDataList, sql);
    }

    /**
     * 批处理添加数据
     *
     * @param appendDataList
     * @param sql
     * @return
     */
    public int[][] savaAppendRecord(List<BaseRecord> appendDataList, String sql) {
        return jdbcTemplate.batchUpdate(sql, appendDataList, appendDataList.size(), new ParameterizedPreparedStatementSetter<BaseRecord>() {
            @Override
            public void setValues(PreparedStatement preparedStatement, BaseRecord baseRecord) throws SQLException {
                preparedStatement.setString(1, baseRecord.getIndexName());
                preparedStatement.setInt(2, baseRecord.getPv());
                preparedStatement.setLong(3, baseRecord.getUv());
                preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                preparedStatement.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

            }
        });
    }
    /**
     * 批处理添加数据
     *
     * @param appendDataList
     * @param sql
     * @return
     */
    public int[][] savaAppendRecord1(List<BaseRecord> appendDataList, String sql) {
        return jdbcTemplate.batchUpdate(sql, appendDataList, appendDataList.size(), (ParameterizedPreparedStatementSetter<BaseRecord>) new RecordBatchPreparedStatementSetter());
    }

    /**
     * 根据时间段查询基本记录和和
     */
    public List<BaseRecord> sumRecordValue(String startTime, String endTime) {
        String sql = "SELECT indexName,SUM(pv) AS pv,SUM(uv) AS uv FROM `log_analyze_job_nimute_append` " +
                " WHERE  executeTime BETWEEN  '" + startTime + "' AND '" +endTime+"' "+
                " GROUP BY indexName";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<BaseRecord>(BaseRecord.class));
    }

    public static void main(String[] args) {
//        System.out.println( new LogAnalyzeDao().sumRecordValue("2015-11-17 15:02:57", "2015-11-17 15:39:41"));
        System.out.println( new LogAnalyzeDao().loadAnalyzeJobDetailList());
        System.out.println( new LogAnalyzeDao().loadJobList());
    }
}
