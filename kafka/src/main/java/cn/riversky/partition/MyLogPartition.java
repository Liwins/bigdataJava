package cn.riversky.partition;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;
import org.apache.log4j.Logger;

/**
 * Created by admin on 2017/12/12.
 */
public class MyLogPartition implements Partitioner{
    private static Logger logger=Logger.getLogger(MyLogPartition.class);

    public MyLogPartition(VerifiableProperties props) {
    }

    @Override
    public int partition(Object key, int numPartitions) {
        /**
         * 取模存放
         */
        return Integer.parseInt(key.toString())%numPartitions;
        /**
         * 都返回分片为0编号的区上个
         */
//        return 0;
    }
}
