package cn.riversky.logEmail;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import cn.riversky.logEmail.bolt.FilterBolt;
import cn.riversky.logEmail.bolt.PrepareRecordBolt;
import cn.riversky.logEmail.bolt.SaveMessage2MySql;
import cn.riversky.logEmail.spout.RandomSpout;
import org.apache.log4j.Logger;
import storm.kafka.StringScheme;

/**
 * 日志监控系统驱动类
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/14.
 */
public class LogMonitorTopologyMain {
    private static  Logger logger=Logger.getLogger(LogMonitorTopologyMain.class);
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        TopologyBuilder builder=new TopologyBuilder();
        builder.setSpout("kafka-spout",new RandomSpout(new StringScheme()),3);
        builder.setBolt("filter-bolt",new FilterBolt(),3).shuffleGrouping("kafka-spout");
        builder.setBolt("prepareRecord-bolt",new PrepareRecordBolt(),2).fieldsGrouping("filter-bolt",new Fields("appId"));
        builder.setBolt("saveMessage-bolt",new SaveMessage2MySql(),2).shuffleGrouping("prepareRecord-bolt");

//        启动topology配置信息
        Config config=new Config();
        //设置为本地调试环境
        config.setDebug(true);
        if (args != null && args.length > 0) {
            //定义你希望集群分配多少个工作进程给你来执行这个topology
            config.setNumWorkers(3);
            //向集群提交topology
            StormSubmitter.submitTopologyWithProgressBar(args[0], config, builder.createTopology());
        } else {
            config.setMaxTaskParallelism(3);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("word-count", config, builder.createTopology());
            Utils.sleep(10000000);
            cluster.shutdown();
        }
    }
}
