package cn.riversky.consumer;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Created by admin on 2017/12/9.
 */
public class KafkaConsumerSimple {
    private final static String TOPIC="first";

    public static void main(String[] args) {
        Properties prop=new Properties();
        prop.put("zookeeper.connect","datanode1:2181,datanode2:2181,datanode3:2181");
        prop.put("group.id","testGroup");
        prop.put("zookeeper.session.timeout.ms","400");
        prop.put("zookeeper.sync.time.ms","200");
        prop.put("auto.commit.interval.ms","1000");
        ConsumerConnector consumer= Consumer.createJavaConsumerConnector(new ConsumerConfig(prop));
        //消费数据
        Map<String ,Integer> topicCountMap=new HashMap<String ,Integer>(16);
        topicCountMap.put(TOPIC,new Integer(1));
        Map<String,List<KafkaStream<byte[],byte[]>>> consumerMap=consumer.createMessageStreams(topicCountMap);
        KafkaStream<byte[],byte[]> stream=consumerMap.get(TOPIC).get(0);
        ConsumerIterator<byte[],byte[]> it=stream.iterator();
        while (it.hasNext()){
            System.out.println(new String(it.next().message()));
        }
    }
}
