package cn.riversky.inverindex;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 1按照文件名+单词 进行次数统计
 hello--a.txt 2
 hello--b.txt 2
 2mapreducer进行单词-》文件名次数的设计
 hello a.txt-->2  b.txt-->3
 * Created by admin on 2017/11/28.
 */
public class InverIndexStepOne {

    public static class OneStepMap extends Mapper<LongWritable,Text,Text,IntWritable>{
        Text k = new Text();
        IntWritable v = new IntWritable(1);
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line=value.toString();
            String[]words=line.split(" ");
            FileSplit fileSplit= (FileSplit) context.getInputSplit();
            String name=fileSplit.getPath().getName();
            for(String word:words){
                k.set(word+"--"+name);
                context.write(k,v);
            }
        }
    }
    public static class OneStepReduce extends Reducer<Text,IntWritable,Text,IntWritable>{
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int count=0;
            for(IntWritable value:values){
                count++;
            }
            context.write(key,new IntWritable(count));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf=new Configuration();
//        conf.set("mapred.textoutputformat.separator", "\t");
        conf.set("mapred.remote.os", "Linux");
        System.setProperty("HADOOP_USER_NAME", "hadoop");
        conf.set("mapreduce.app-submission.cross-platform","true");
        Job job = Job.getInstance(conf);

        // 指定本程序的jar包所在的本地路径
        // job.setJarByClass(RJoin.class);
        job.setJar("E:\\injipro\\mapred\\target\\iotsp.jar");
        job.setMapperClass(OneStepMap.class);
        job.setReducerClass(OneStepReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        Path out=new Path("/inveridex/outputonestep");
        FileSystem fs=FileSystem.get(conf);
        if(fs.exists(out)){
            fs.delete(out,true);
        }
        FileInputFormat.setInputPaths(job,new Path("/inveridex/input"));
        FileOutputFormat.setOutputPath(job,out);
        boolean res=job.waitForCompletion(true);
        System.exit(res?0:1);


    }
}
