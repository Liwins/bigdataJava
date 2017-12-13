package cn.riversky.kafkastorm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import java.util.Map;

/**
 * Created by admin on 2017/12/13.
 */
public class MyKafkaBolt1 extends BaseRichBolt {
//    OutputCollector collector=null;
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
//        collector=outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
//        String line=new String((byte[]) tuple.getValue(0));
        System.out.println(tuple.getValue(0).toString());
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
