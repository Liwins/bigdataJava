package cn.riversky.ackfail;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created by admin on 2017/12/11.
 */
public class MySpoutWith extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private Random random;
    private Map<String,Values> buffer=new HashMap<>();
    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        collector=spoutOutputCollector;
    }

    @Override
    public void nextTuple() {
        String[] sentences=new String[]{"the cow jumped over the moon",
        "the cow jumped over the moon","the cow jumped over the moon","the cow jumped over the moon"
        };
        String sentence=sentences[random.nextInt(sentences.length)];
        String messageId= UUID.randomUUID().toString().replace("-","");
        Values tuple=new Values(sentence);
        collector.emit(tuple,messageId);
        buffer.put(messageId,tuple);
        try{
            Thread.sleep(20000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
outputFieldsDeclarer.declare(new Fields("sentence"));
        random=new Random();
    }
    @Override
    public void ack(Object msgId) {
        System.out.println("消息处理成功，id"+msgId);
       buffer.remove(msgId);
    }

    @Override
    public void fail(Object msgId) {
        System.out.println("消息处理失败，id= " + msgId);
        Values tuple=buffer.get(msgId);
        collector.emit(tuple,msgId);
    }


}
