package cn.riversky.wcdemo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * 相当于一个yarn集群的客户端
 * 需要在此封装我们mr程序相关运行参数，指定jar包
 * 最后提交给yarn
 * Created by admin on 2017/11/25.
 */
public class WordCountDriver {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        System.setProperty("HADOOP_USER_NAME", "hadoop");
//        conf.set("mapreduce.framework.name", "local");
//        conf.set("fs.defaultFS", "hdfs://namenode:9000/");
//        conf.set("mapreduce.framework.name", "yarn");
//        conf.set("yarn.resourcemanager.hostname", "namenode");
//        conf.set("mapred.jar", "E:\\injipro\\mapred\\target\\iotsp.jar");
        conf.set("mapred.remote.os", "Linux");
        conf.set("mapreduce.app-submission.cross-platform","true");
        Job job = Job.getInstance(conf);
//指定本程序的jar包所在路径的位置。
        job.setJarByClass(WordCountDriver.class);
//        job.setJar("E:\\injipro\\mapred\\target\\iotsp.jar");
//        为任务分配map  reduce
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);
//        配置map的输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
//        配置输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
//        指定job的输入原始文件所在的目录
//        FileInputFormat.setInputPaths(job,new Path("/wordcount/input"));
        FileInputFormat.setInputPaths(job, new Path(args[0]));
//        删除结果的目录
        Path output=new Path(args[1]);
        FileSystem fileSystem=FileSystem.get(conf);
        if(fileSystem.exists(output)){
            fileSystem.delete(output,true);
        }
        FileOutputFormat.setOutputPath(job, output);
//        将job中的配置的先关参数配置给yarn
//        job.submit();
        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
