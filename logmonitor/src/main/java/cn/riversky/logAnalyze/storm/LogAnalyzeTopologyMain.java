package cn.riversky.logAnalyze.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import cn.riversky.logAnalyze.storm.bolt.MessageFilerBolt;
import cn.riversky.logAnalyze.storm.bolt.ProcessMessage;
import cn.riversky.logAnalyze.storm.spout.RandomSpout;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/18.
 */
public class LogAnalyzeTopologyMain {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        TopologyBuilder builder=new TopologyBuilder();
        builder.setSpout("kafka-spot",new RandomSpout(),3);
        builder.setBolt("messfilter",new MessageFilerBolt(),3).shuffleGrouping("kafka-spot");
        builder.setBolt("processBolt",new ProcessMessage(),2).fieldsGrouping("messfilter",new Fields("type"));
        Config topologConf = new Config();
        if (args != null && args.length > 0) {
            topologConf.setNumWorkers(2);
            StormSubmitter.submitTopologyWithProgressBar(args[0], topologConf, builder.createTopology());
        } else {
            topologConf.setMaxTaskParallelism(3);
            LocalCluster cluster=new LocalCluster();
            cluster.submitTopology("LogAnalyzeTopologyMain",topologConf,builder.createTopology());
            Utils.sleep(10000000);
            cluster.shutdown();
        }
    }
}
