package cn.riversky.logAnalyze.storm.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/18.
 */
public class RecordBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
    private Map<String,Map<String,Object>> appData;
    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {

    }

    @Override
    public int getBatchSize() {
        return appData.get("pv").size();
    }
}
