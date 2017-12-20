package cn.riversky.logAnalyze.storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import cn.riversky.logAnalyze.storm.domain.LogMessage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 这里随机构造消息来源
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/19.
 */
public class RandomSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private TopologyContext context;
    private List<LogMessage> list;
    @Override
    public void open(Map map, TopologyContext context, SpoutOutputCollector collector) {
        this.collector=collector;
        this.context=context;
        list=new ArrayList<>();
        list.add(new LogMessage(1,"http://www.rivesky.cn/product?id=12","http://www.rivesky.cn","riversky"));
        list.add(new LogMessage(1,"http://www.rivesky.cn/item?id=12","http://www.rivesky.cn","riversky"));
        list.add(new LogMessage(1,"http://www.rivesky.cn/admin?id=12","http://www.rivesky.cn","riversky"));
        list.add(new LogMessage(1,"http://www.rivesky.cn/login?id=12","http://www.rivesky.cn","riversky"));
    }

    @Override
    public void nextTuple() {
        final Random rand=new Random();
        LogMessage msg=list.get(new Random().nextInt(4));
        collector.emit(new Values(new Gson().toJson(msg)));
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("paymentInfo"));
    }
}
