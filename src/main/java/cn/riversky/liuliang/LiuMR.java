package cn.riversky.liuliang;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by admin on 2017/11/25.
 */
public class LiuMR {
    static class LiuMappre extends Mapper<LongWritable, Text, Text, Liu> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
//将一行内容转成string
            String line = value.toString();
//切分字段
            String[] fields = line.split("\t");
//取出手机号
            String phoneNbr = fields[1];
//取出上行流量下行流量
            long upFlow = Long.parseLong(fields[fields.length - 3]);
            long dFlow = Long.parseLong(fields[fields.length - 2]);

            context.write(new Text(phoneNbr), new Liu(upFlow, dFlow));
        }
    }

    static class LiuReducer extends Reducer<Text, Liu, Text, Liu> {
        @Override
        protected void reduce(Text key, Iterable<Liu> values, Context context) throws IOException, InterruptedException {
            long sumUp = 0;
            long sumDown = 0;
            for (Liu liu : values) {
                sumUp += liu.getUpFlow();
                sumDown += liu.getDownFlow();
            }
            Liu res = new Liu(sumUp, sumDown);
            context.write(key, res);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("mapreduce.framework.name", "yarn");
        conf.set("yarn.resoucemanager.hostname", "namenode");
        Job job = Job.getInstance(conf);
//指定本程序的jar包所在路径的位置。
        job.setJarByClass(LiuMR.class);
//        为任务分配map  reduce
        job.setMapperClass(LiuMappre.class);
        job.setReducerClass(LiuReducer.class);
//        配置map的输出类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Liu.class);
//        配置输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Liu.class);
//        指定job的输入原始文件所在的目录
//        FileInputFormat.setInputPaths(job,new Path("/wordcount/input"));
        FileInputFormat.setInputPaths(job, new Path(args[0]));
//        删除结果的目录
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
//        将job中的配置的先关参数配置给yarn
//        job.submit();
        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
