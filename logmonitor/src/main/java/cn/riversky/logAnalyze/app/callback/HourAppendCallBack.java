package cn.riversky.logAnalyze.app.callback;

import cn.riversky.logAnalyze.app.domain.BaseRecord;
import cn.riversky.logAnalyze.storm.dao.LogAnalyzeDao;
import cn.riversky.logAnalyze.storm.utils.DateUtils;

import java.util.Calendar;
import java.util.List;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/19.
 */
public class HourAppendCallBack implements Runnable {
    @Override
    public void run()  {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.MINUTE));
        if (calendar.get(Calendar.MINUTE) == 59) {
            //获取所有的增量数据
            String endTime = DateUtils.getDateTime(calendar);
            String startTime = DateUtils.beforeOneHour(calendar);
            LogAnalyzeDao logAnalyzeDao = new LogAnalyzeDao();
            List<BaseRecord> baseRecordList = logAnalyzeDao.sumRecordValue(startTime, endTime);
            logAnalyzeDao.saveHourAppendRecord(baseRecordList);
        }
    }
}
