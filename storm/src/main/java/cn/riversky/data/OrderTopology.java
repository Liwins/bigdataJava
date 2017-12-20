package cn.riversky.data;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.*;

import java.util.UUID;

/**
 * Created by admin on 2017/12/13.
 */
public class OrderTopology {
    private static String TOPICNAME="orderMq";
    private static String ZKROOT="/myKafka";
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        BrokerHosts hosts=new ZkHosts("datanode1:2181,datanode2:2181,datanode3:2181");
        SpoutConfig spoutConfig=new SpoutConfig(hosts,TOPICNAME,ZKROOT, UUID.randomUUID().toString());
        spoutConfig.scheme=new SchemeAsMultiScheme(new StringScheme());
        KafkaSpout kafkaSpout=new KafkaSpout(spoutConfig);
        TopologyBuilder topologyBuilder=new TopologyBuilder();
        topologyBuilder.setSpout("kafkaSpout",kafkaSpout,1);
        topologyBuilder.setBolt("mybolt",new OrderBolt(),1).shuffleGrouping("kafkaSpout");
        Config config=new Config();
        config.setNumWorkers(1);
        /**
         * 两种方式
         */
        if(args.length>0){
            StormSubmitter.submitTopology(args[0],config,topologyBuilder.createTopology());
        }else {
            LocalCluster cluster=new LocalCluster();
            cluster.submitTopology("storm2Kafka",config,topologyBuilder.createTopology());
        }


    }
}
