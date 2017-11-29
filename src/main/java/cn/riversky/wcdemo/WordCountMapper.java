package cn.riversky.wcdemo;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
* 1:默认情况下，是mr框架所读到的一行文本的起始偏移量，Long,
* 但是在hadoop中有更精简的序列化接口，所以不直接用long,而是用LongWritable
* 2默认情况下，是mr框架所读到的一行文本的内容，String ,同样用hadoop的Text代替
* 3,用户自动自定义的输出的key，此处是String ，使用hadoop的text
* 4 是用户自定义逻辑处理完成之后输出数据中的value，在此处是单词的次数 int.使用hadoop的IntWritable
*/
public class WordCountMapper extends Mapper<LongWritable,Text,Text,IntWritable> {
/**
* map阶段的逻辑方法，maptask会对每一行的输入数据调用一次我们自定义的map()方法。
* @param key
* @param value
* @param context
* @throws IOException
* @throws InterruptedException
*/
@Override
protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
String line=value.toString();
String[]words=line.split(",");
for (String word:words){
context.write(new Text(word),new IntWritable(1));
}
}
}
