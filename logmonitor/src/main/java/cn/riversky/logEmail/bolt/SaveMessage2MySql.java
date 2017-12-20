package cn.riversky.logEmail.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import cn.riversky.logEmail.domain.Record;
import cn.riversky.logEmail.utils.MonitorHandler;

/**
 * 这里是处理结果的终点数据保存到mysql/redis
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/15.
 */
public class SaveMessage2MySql extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        Record record= (Record) input.getValueByField("record");
        MonitorHandler.save(record);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
