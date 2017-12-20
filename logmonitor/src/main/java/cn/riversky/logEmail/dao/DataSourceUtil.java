package cn.riversky.logEmail.dao;

import cn.riversky.logEmail.domain.Rule;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/14.
 */
public class DataSourceUtil {
    private static Logger logger= Logger.getLogger(DataSourceUtil.class);
    private static DataSource dataSource;
    static {
        dataSource=new ComboPooledDataSource("logMonitor");
    }
    public static synchronized DataSource getDataSource(){
        if(dataSource==null){
            dataSource=new ComboPooledDataSource();
        }
        return dataSource;
    }

    public static void main(String[] args) {
        JdbcTemplate jdbcTemplate=new JdbcTemplate(dataSource);
        String sql = "SELECT `id`,`name`,`keyword`,`isValid`,`appId` FROM `monitor`.`log_monitor_rule` WHERE isValid =1";
        List<Rule> rules=jdbcTemplate.query(sql, new BeanPropertyRowMapper<Rule>(Rule.class));
        System.out.println(rules);
    }
}
