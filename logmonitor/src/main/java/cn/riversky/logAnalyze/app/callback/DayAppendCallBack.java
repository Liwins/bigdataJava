package cn.riversky.logAnalyze.app.callback;

import cn.riversky.logAnalyze.app.domain.BaseRecord;
import cn.riversky.logAnalyze.storm.dao.LogAnalyzeDao;
import cn.riversky.logAnalyze.storm.utils.DateUtils;

import java.util.Calendar;
import java.util.List;

/**
 * 计算全天的数据增量
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/18.
 */
public class DayAppendCallBack implements Runnable {
    @Override
    public void run() {
        Calendar calendar=Calendar.getInstance();
        if(calendar.get(Calendar.MINUTE)==0&&calendar.get(Calendar.HOUR)==0){
            String endTime= DateUtils.getDateTime(calendar);
            String startTime=DateUtils.beforeOneDay(calendar);
            LogAnalyzeDao logAnalyzeDao=new LogAnalyzeDao();
            List<BaseRecord> baseRecords=logAnalyzeDao.sumRecordValue(startTime,endTime);
            logAnalyzeDao.saveDayAppendRecord(baseRecords);
        }
    }
}
