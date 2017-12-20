package cn.riversky.logAnalyze.storm.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import cn.riversky.logAnalyze.storm.domain.LogMessage;
import cn.riversky.logAnalyze.storm.utils.LogAnalyzeHandler;

/**
 * 消息处理bolt
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/19.
 */
public class ProcessMessage extends BaseBasicBolt {
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        LogMessage logMessage= (LogMessage) tuple.getValueByField("message");
        LogAnalyzeHandler.process(logMessage);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
