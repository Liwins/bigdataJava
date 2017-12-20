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
public class HalfAppendCallBack implements Runnable {
    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.MINUTE) % 30 == 0) {
            String endTime = DateUtils.getDateTime(calendar);
            String startTime = DateUtils.before30Minute(calendar);
            LogAnalyzeDao logAnalyzeDao = new LogAnalyzeDao();
            List<BaseRecord> baseRecordList = logAnalyzeDao.sumRecordValue(startTime, endTime);
            logAnalyzeDao.saveHalfAppendRecord(baseRecordList);
        }
    }
}
