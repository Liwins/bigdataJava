package cn.riversky.ackfail;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

/**
 * Created by admin on 2017/12/11.
 */
public class AckFailTopology {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, InterruptedException {
        TopologyBuilder builder=new TopologyBuilder();
        builder.setSpout("myspout",new MySpoutWith(),1);
        builder.setBolt("mybolt",new MyBoltWith(),1).shuffleGrouping("mySpout");
        Config config=new Config();
        String name=AckFailTopology.class.getSimpleName();
        if(args!=null&&args.length>0){
            String nimbus=args[0];
            config.put(Config.NIMBUS_HOST,nimbus);
            config.setNumWorkers(1);
            StormSubmitter.submitTopologyWithProgressBar(name,config,builder.createTopology());

        }else {
            LocalCluster cluster=new LocalCluster();
            cluster.submitTopology(name,config,builder.createTopology());
            Thread.sleep(60*60*1000);
            cluster.shutdown();
        }
    }
}
