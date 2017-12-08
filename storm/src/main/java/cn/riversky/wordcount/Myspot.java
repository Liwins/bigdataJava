package cn.riversky.wordcount;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * Created by admin on 2017/12/8.
 */
public class Myspot extends BaseRichSpout {
    SpoutOutputCollector collector;
    /**
     * 初始化方法,将容器传递
     * @param map
     * @param topologyContext
     * @param spoutOutputCollector
     */
    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        collector=spoutOutputCollector;
    }

    /**
     * 这里模拟数据的放入
     */
    @Override
    public void nextTuple() {
        collector.emit(new Values(" i am lilei lo han mei mei "));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("lo"));
    }
}
