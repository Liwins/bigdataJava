package cn.riversky.producer;


import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;


/**
 * Created by admin on 2017/12/9.
 */
public class KafkaProducerSimple {
    private final static String TOPIC="first";

    public static void main(String[] args) throws InterruptedException {
        //设置配置信息
        Properties props=new Properties();
        props.put("serializer.class","kafka.serializer.StringEncoder");
        props.put("metadata.broker.list","datanode1:9092,datanode2:9092,datanode3:9092");
        props.put("request.required.acks","1");
        Producer<Integer,String> producer=new Producer<Integer, String>(new ProducerConfig(props));
        //发送数据
        int messageNo=1;
        while (true){
            String messStr=new String("message_"+messageNo);
            producer.send(new KeyedMessage<Integer, String>(TOPIC,messStr));
            messageNo++;
            Thread.sleep(1000L);
        }
    }
}
