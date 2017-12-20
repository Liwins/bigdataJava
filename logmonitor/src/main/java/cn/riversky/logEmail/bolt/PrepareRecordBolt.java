package cn.riversky.logEmail.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cn.riversky.logEmail.domain.Message;
import cn.riversky.logEmail.domain.Record;
import cn.riversky.logEmail.utils.MonitorHandler;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/15.
 */
public class PrepareRecordBolt extends BaseBasicBolt{
    private static Logger logger = Logger.getLogger(PrepareRecordBolt.class);
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        Message message= (Message) input.getValueByField("message");
        String appId=input.getStringByField("appId");
        /**
         * 通过消息和appId上报软件错误，将监控进行邮件或电话报警通知
         */
        MonitorHandler.notifly(appId,message);
        Record record=new Record();
        //根据spring的BeanUtils可以避免抛出异常
        BeanUtils.copyProperties(message,record);
        record.setAppId(Integer.parseInt(message.getAppId()));
        collector.emit(new Values(record));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("record"));
    }


}
