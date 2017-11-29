package cn.riversky.liuliangsort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
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
    static class LiuMappre extends Mapper<LongWritable, Text, Liu, Text> {
        Text v = new Text();
        Liu liu = new Liu();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] word = line.split("\t");
            v = new Text(word[0]);
            Long up = Long.parseLong(word[1]);
            Long down = Long.parseLong(word[2]);
            liu.setUpAndDown(up, down);
            context.write(liu, v);
        }
    }

    static class LiuReducer extends Reducer<Liu, Text, Text, Liu> {
        @Override
        protected void reduce(Liu key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            context.write(values.iterator().next(), key);
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
        job.setMapOutputKeyClass(Liu.class);
        job.setMapOutputValueClass(Text.class);
//        配置输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Liu.class);
//        指定job的输入原始文件所在的目录
//        FileInputFormat.setInputPaths(job,new Path("/wordcount/input"));
        FileInputFormat.setInputPaths(job, new Path(args[0]));
//        删除结果的目录
        FileSystem fs = FileSystem.get(conf);
        Path outpath = new Path(args[1]);
        if (fs.exists(outpath)) {
            fs.delete(outpath, true);
        }
        FileOutputFormat.setOutputPath(job, outpath);
//        将job中的配置的先关参数配置给yarn
//        job.submit();
        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
