package cn.riversky.wordcount;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * Created by admin on 2017/12/8.
 */
public class MySplitBolt extends BaseRichBolt {
    OutputCollector collector;
    /**
     * 初始化方法
     * @param map
     * @param topologyContext
     * @param outputCollector
     */
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        collector=outputCollector;
    }

    /**
     * 这里的业务有点类似mr，collector的作用是写数据
     * @param tuple
     */
    @Override
    public void execute(Tuple tuple) {
        String line=tuple.getString(0);
        String[] words=line.split(" ");
        for(String word:words){
            collector.emit(new Values(word,1));
        }
    }

    /**
     * 这里的field与execute中的Values对应
     * @param outputFieldsDeclarer
     */
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("word","num"));
    }
}
