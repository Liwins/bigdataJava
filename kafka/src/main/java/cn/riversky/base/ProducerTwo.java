package cn.riversky.base;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;
import java.util.UUID;

/**
 * @author Created by admin on 2017/12/12.
 */
public class ProducerTwo {
    public static final String TOPIC = "test1";

    public static void main(String[] args) throws InterruptedException {
        Properties props = new Properties();
/**
* key.serializer.class默认为serializer.class
*/
        props.put("serializer.class", "kafka.serializer.StringEncoder");
/**
* kafka broker对应的主机，格式为host1:port1,host2:port2
*/
        props.put("metadata.broker.list", "datanode1:9092,datanode2:9092,datanode3:9092");
/**
* request.required.acks,设置发送数据是否需要服务端的反馈,有三个值0,1,-1
* 0，意味着producer永远不会等待一个来自broker的ack，这就是0.7版本的行为。
* 这个选项提供了最低的延迟，但是持久化的保证是最弱的，当server挂掉的时候会丢失一些数据。
* 1，意味着在leader replica已经接收到数据后，producer会得到一个ack。
* 这个选项提供了更好的持久性，因为在server确认请求成功处理后，client才会返回。
* 如果刚写到leader上，还没来得及复制leader就挂了，那么消息才可能会丢失。
* -1，意味着在所有的ISR都接收到数据后，producer才得到一个ack。
* 这个选项提供了最好的持久性，只要还有一个replica存活，那么数据就不会丢失
*/
        props.put("request.required.acks", "1");
/**
 * 可选配置，如果不配置，则使用默认的partitioner partitioner.class
 * 默认值：kafka.producer.DefaultPartitioner
 * 用来把消息分到各个partition中，默认行为是对key进行hash。
 */
        props.put("partitioner.class", "cn.riversky.partition.MyLogPartition");
/**
 * 注意使用javaapi的producer,创建的时候，传入producerConfig（）；
 */
        Producer<String, String> producer = new Producer<String, String>(new ProducerConfig(props));
        int count = 0;
        while (true) {
/**
 * 这里传入的三个参数分别为 topic   k v
 */
            KeyedMessage<String, String> message = new KeyedMessage<String, String>(TOPIC, count + "", "appid" + UUID.randomUUID() + "message" + count);
            Thread.sleep(100L);
/**
 * 调用producer的方法发送数据，
 * 注意：这里需要指定partitionkey,用来配合自定义的MylogPartitioner进行数据分发
 */
            producer.send(message);
            count++;
        }
    }
}
