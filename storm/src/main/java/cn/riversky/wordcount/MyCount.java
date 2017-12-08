package cn.riversky.wordcount;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

/**
 * Created by admin on 2017/12/8.
 */
public class MyCount {

    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
//        1准备一个TopologyBuilder
        TopologyBuilder topologyBuilder=new TopologyBuilder();
        topologyBuilder.setSpout("mySpout",new Myspot(),2);
        topologyBuilder.setBolt("mybolt1",new MySplitBolt(),2).shuffleGrouping("mySpout");
        topologyBuilder.setBolt("mybolt2",new MyBolt(),3).fieldsGrouping("mybolt1",new Fields("word"));


        //创建配置
        Config config=new Config();
        config.setNumWorkers(2);
        //提交任务，本地方式和集群模式
//        StormSubmitter.submitTopology("mywordcount",config,topologyBuilder.createTopology());
        LocalCluster localCluster=new LocalCluster();
        localCluster.submitTopology("mywordcount",config,topologyBuilder.createTopology());
    }
}
