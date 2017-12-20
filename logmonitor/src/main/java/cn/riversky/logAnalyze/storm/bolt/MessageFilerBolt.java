package cn.riversky.logAnalyze.storm.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cn.riversky.logAnalyze.storm.domain.LogMessage;
import cn.riversky.logAnalyze.storm.utils.LogAnalyzeHandler;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/19.
 */
public class MessageFilerBolt extends BaseBasicBolt {



    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
//        获取数据 应该从kafka
        String line=tuple.getString(0);
        //解析日志数据
        LogMessage logMessage= LogAnalyzeHandler.parser(line);
        if(logMessage==null||!LogAnalyzeHandler.isValidType(logMessage.getType())){
            return;
        }
        basicOutputCollector.emit(new Values(logMessage.getType(),logMessage));
        //定时更新规则信息
        LogAnalyzeHandler.scheduleLoad();
    }
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("type","message"));
    }
}
