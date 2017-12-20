package cn.riversky.logAnalyze.storm.dao;

import cn.riversky.logEmail.dao.DataSourceUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/18.
 */
public class DataSourceUtils {
    private static Logger logger = Logger.getLogger(DataSourceUtil.class);
    private static DataSource dataSource;
    static {
        dataSource=new ComboPooledDataSource("logAnalyze");
    }
    public static synchronized DataSource getDataSource(){
        if(dataSource==null){
            dataSource=new ComboPooledDataSource();
        }
        return dataSource;
    }

    public static void main(String[] args) {
        JdbcTemplate template=new JdbcTemplate(dataSource);
//        String sql = "SELECT `id`,`jobId`,`value`,`compare`,`createUser` FROM `log_analyze`.`log_analyze_job_condition` ";
//        System.out.println(template.query(sql, new BeanPropertyRowMapper<LogAnalyzeJobDetail>(LogAnalyzeJobDetail.class)));
    }
}
