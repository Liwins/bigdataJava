package cn.riversky.province;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Created by admin on 2017/11/26.
 */
public class ProvincePartitioner extends Partitioner<Text, Liu> {
    @Override
    public int getPartition(Text text, Liu liu, int i) {
        if (text.toString().length() >= 3) {
            int bianhao = Integer.parseInt(text.toString().substring(0, 3));
            return bianhao % 10;
        } else
            return 0;
    }
}
