package cn.riversky.logAnalyze.app;

import cn.riversky.logAnalyze.app.callback.DayAppendCallBack;
import cn.riversky.logAnalyze.app.callback.HalfAppendCallBack;
import cn.riversky.logAnalyze.app.callback.HourAppendCallBack;
import cn.riversky.logAnalyze.app.callback.OneMinuteCallBack;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 模拟前端数据的更新(这部分由ajax)
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/18.
 */
public class ReportDataWorker {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
//        //计算每分钟的增量数据，并将增量数据保存到mysql数据库中
        scheduledExecutorService.scheduleAtFixedRate(new OneMinuteCallBack(), 0, 60, TimeUnit.SECONDS);
        //计算每半个小时的增量数据，并将增量数据保存到mysql数据库中
        scheduledExecutorService.scheduleAtFixedRate(new HalfAppendCallBack(), 0, 60, TimeUnit.SECONDS);
//        //计算每小时的增量数据，并将增量数据保存到mysql数据库中
        scheduledExecutorService.scheduleAtFixedRate(new HourAppendCallBack(), 0, 60, TimeUnit.SECONDS);
////        //计算每天的全量，并将增量数据保存到mysql数据库中
        scheduledExecutorService.scheduleAtFixedRate(new DayAppendCallBack(), 0, 60, TimeUnit.SECONDS);
    }
}
