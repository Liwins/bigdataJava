package cn.riversky.base;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 消费者，线程应该与partition相对应，一般有几个partition对应其倍数
 * 按照group分
 *@author  Created by admin on 2017/12/12.
 */
public class ConsumerTwo implements Runnable {
    private  String title ;
    public KafkaStream<byte[],byte[]> stream;

    public ConsumerTwo(String topic,KafkaStream<byte[], byte[]> stream) {
        this.title=topic;
        this.stream = stream;
    }
    @Override
    public void run() {
        ConsumerIterator<byte[],byte[]> it=stream.iterator();
        /**
         * 不停从it从读到新来的消息，在等待新的消息时，hasNext()会阻塞，
         * 如果调用 ConsumerConnector#shutdown      hasNext()会返回false
         */
        while (it.hasNext()){
            MessageAndMetadata<byte[],byte[]> data=it.next();
            String topic=data.topic();
            int partition=data.partition();
            long offset=data.offset();

            String msg=new String(data.message());
            System.out.println(String.format("Consumer: [%s],  Topic: [%s],  PartitionId: [%d], Offset: [%d], msg: [%s]",
                    title, topic, partition, offset, msg));
        }
        System.out.println(String.format("Consumer: [%s] exiting ...", title));
    }
    public static void main(String[] args) {
        Properties prop=new Properties();
        prop.put("group.id","doubi");
        prop.put("zookeeper.connect","datanode1:2181,datanode2:2181,datanode3:2181");
        prop.put("auto.offset,reset","largest");
        prop.put("auto.commit.interval.ms","1000");
        prop.put("partition.assignment.stategy","roundrobin");
        String topic1="test1";
        String first="first";

        ConsumerConfig conf=new ConsumerConfig(prop);
        /**
         * 工厂方法获取consumer,只要Consumer在就等待新消息不会退出
         */
        ConsumerConnector consumerConn=Consumer.createJavaConsumerConnector(conf);
        /**
         * 定义一个map容器,存放主体
         * 分成5个消费者
         */
        Map<String,Integer> topicCountMap =new HashMap<>(16);
        topicCountMap.put(topic1,5);
        /**
         * 中String是topic， List<KafkaStream<byte[], byte[]>是对应的流
         * 根据主题取出响应的流数据
         */
        Map<String,List<KafkaStream<byte[],byte[]>>> topicStreamsMap=consumerConn.createMessageStreams(topicCountMap);

        List<KafkaStream<byte[],byte[]>> streams=topicStreamsMap.get(topic1);

        ExecutorService executor= Executors.newFixedThreadPool(5);

        for (int i = 0; i < streams.size(); i++) {
            executor.execute(new ConsumerTwo("消费者"+(i+1),streams.get(i)));
        }
    }


}
