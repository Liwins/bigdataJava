package cn.riversky.data;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * 订单生成器，发送到kafka集群中
 *@author  Created by admin on 2017/12/13.
 */
public class OrderMqSender {
    private static String TOPIC="orderMq";
    public static void main(String[] args) {
        Properties prop=new Properties();
        prop.put("serializer.class","kafka.serializer.StringEncoder");
        prop.put("metadata.broker.list","datanode1:9092,datanode2:9092,datanode3:9092");
        prop.put("request.required.acks","1");
        prop.put("partitioner.class","kafka.producer.DefaultPartitioner");
        Producer<String,String> producer=new Producer<String, String>(new ProducerConfig(prop));
        for (int messageNo = 0; messageNo < 10; messageNo++) {
            producer.send(new KeyedMessage<String, String>(TOPIC,messageNo+"",new OrderInfo().random()));
//            每隔1S产生一个订单
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
