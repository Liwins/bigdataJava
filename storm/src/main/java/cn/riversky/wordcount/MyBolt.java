package cn.riversky.wordcount;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/12/8.
 */
public class MyBolt extends BaseRichBolt {
//    OutputCollector collector;
    Map<String,Integer> map=new HashMap<String,Integer>();
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
//        collector=outputCollector;
    }

    /**
     * 这里通过
     * @param tuple
     */
    @Override
    public void execute(Tuple tuple) {
        String word=tuple.getString(0);
        Integer num=tuple.getInteger(1);
        System.out.println(Thread.currentThread().getId()+"     word:"+word);
        if(map.containsKey(word)){
            Integer count=map.get(word);
            map.put(word,count+num);
        }else {
            map.put(word,num);
        }
//        System.out.println("count:"+map);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
//没有下线
    }
}
