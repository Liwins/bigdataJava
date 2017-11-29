package cn.riversky.wcdemo;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
* 四个参数为，map的text和Intwritable,表示单词，和1
* 后两个参数表示“单词”“统计数”
* Created by admin on 2017/11/25.
*/
public class WordCountReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
/**
* 需要注意的是，每次reduce是对其中的一个key进行的运算，所以count代表的是这个key的
* @param key
* @param values
* @param context
* @throws IOException
* @throws InterruptedException
*/
@Override
protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

int count=0;
Iterator<IntWritable> iterator=values.iterator();
while (iterator.hasNext()){
count+=iterator.next().get();
}
context.write(key,new IntWritable(count));
}
}
