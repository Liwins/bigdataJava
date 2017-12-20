package cn.riversky.logEmail.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cn.riversky.logEmail.domain.Message;
import cn.riversky.logEmail.utils.MonitorHandler;
import org.apache.log4j.Logger;

/**
 * 规则过滤
 * 注意这里是继承的BaseBasicBolt,是由Storm框架自动调用ack方法
 * BaseRichBolt是用户手动调用ack方法
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/15.
 */
public class FilterBolt extends BaseBasicBolt {
    private static Logger logger=Logger.getLogger(FilterBolt.class);


    @Override
    public void execute(Tuple input, BasicOutputCollector  collector) {
        String line=input.getString(0);
        Message message= MonitorHandler.parse(line);
        if(message==null){
            return ;
        }
        if(MonitorHandler.rigger(message)){
            //发射的信息是values,
            collector.emit(new Values(message.getAppId(),message));
        }
        //定时更新规则信息
        MonitorHandler.scheduleLoad();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer decliarer) {
        decliarer.declare(new Fields("appId","message"));
    }
}
