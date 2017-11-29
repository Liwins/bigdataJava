package cn.riversky.inverindex;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
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
 * Created by admin on 2017/11/28.
 */
public class InverIndexStepTwo {
    public static class StepTwoMap extends Mapper<LongWritable,Text,Text,Text>{
        Text key2=new Text();
        Text value2=new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            FileSplit split= (FileSplit) context.getInputSplit();
            String name=split.getPath().getName();
            if("part-r-00000".equals(name)){
                String line=value.toString();
                String[]words=line.split("--");
                key2.set(words[0]);
                value2.set(words[1]);
                context.write(key2,value2);
            }else {
                return;
            }

        }
    }
    public static class StepTwoReduce extends Reducer<Text,Text,Text,Text>{
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            StringBuffer sb=new StringBuffer();
            for(Text value:values){
                sb.append(value.toString());
                sb.append("   ");
            }
            context.write(key,new Text(sb.toString()));
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
        job.setMapperClass(StepTwoMap.class);
        job.setReducerClass(StepTwoReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        Path out=new Path("/inveridex/outputtwostep");
        FileSystem fs=FileSystem.get(conf);
        if(fs.exists(out)){
            fs.delete(out,true);
        }
        FileInputFormat.setInputPaths(job,new Path("/inveridex/outputonestep"));
        FileOutputFormat.setOutputPath(job,out);
        boolean res=job.waitForCompletion(true);
        System.exit(res?0:1);


    }
}
