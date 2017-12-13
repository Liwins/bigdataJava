package cn.riversky.kafkastorm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import storm.kafka.*;

import java.util.UUID;

/**
 * kafka的消费这，作为storm的spout
 * @author Created by admin on 2017/12/13.
 */
public class KafkaAndStormTopologyMain {
    private static String TOPICNAME="test2";
    private static String ZKROOT="/stormKafka/"+TOPICNAME;
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        BrokerHosts hosts=new ZkHosts("datanode1:2181,datanode2:2181,datanode3:2181");
        SpoutConfig spoutConfig=new SpoutConfig(hosts,TOPICNAME,ZKROOT, UUID.randomUUID().toString());
//        SpoutConfig spoutConfig=new SpoutConfig(hosts,TOPICNAME,ZKROOT, UUID.randomUUID().toString());
        spoutConfig.scheme=new SchemeAsMultiScheme(new StringScheme());
        KafkaSpout kafkaSpout=new KafkaSpout(spoutConfig);

        TopologyBuilder topologyBuilder=new TopologyBuilder();

        /**
         * 这里采用storm-kafka的包进行配置
         */
        topologyBuilder.setSpout("kafkaSpout",kafkaSpout,1);
        /**
         * 处理业务
         */
        topologyBuilder.setBolt("mybolt",new MyKafkaBolt1(),1).shuffleGrouping("kafkaSpout");

        Config config=new Config();
        config.setNumWorkers(2);
        /**
         * 集群模式
         */
//        StormSubmitter.submitTopology("test1",config,topologyBuilder.createTopology());
        /**
         * 本地模式
         */
        LocalCluster localCluster=new LocalCluster();
        localCluster.submitTopology("mywordcount",config,topologyBuilder.createTopology());
    }
}
