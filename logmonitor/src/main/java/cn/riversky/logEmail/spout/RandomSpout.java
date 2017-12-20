package cn.riversky.logEmail.spout;

import backtype.storm.spout.Scheme;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 这里是测试用的
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/15.
 */
public class RandomSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private TopologyContext context;
    private List list;
    private final Scheme scheme;

    public RandomSpout(Scheme scheme) {
        super();
        this.scheme=scheme;
    }

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.context=topologyContext;
        this.collector=spoutOutputCollector;
        list=new ArrayList();
        list.add("2$$$$$606  [main] INFO  cn.riversky.logEmail.mail.MessageSender ");
    }

    @Override
    public void nextTuple() {
        String msg= (String) list.get(0);
        collector.emit(this.scheme.deserialize(msg.getBytes()));
        try{
            Thread.sleep(100000L);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(scheme.getOutputFields());
    }
}
