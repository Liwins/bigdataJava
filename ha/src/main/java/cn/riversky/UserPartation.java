package cn.riversky;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Created by admin on 2017/11/30.
 */
public class UserPartation extends Partitioner<User,NullWritable> {
    @Override
    public int getPartition(User user, NullWritable nullWritable, int i) {
        return user.getId()%10;
    }
}
