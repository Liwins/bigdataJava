package cn.riversky;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * E:\injipro\mapred\ha\target\iotsp.jar
 * Hello world!
 */
public class  TestHa{
    public static class RjMapper extends Mapper<LongWritable,Text,User,NullWritable>{
        private int count=20050146;
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line=value.toString();
            String[] words=line.split(",");
            if("Name".equals(words[0])||words.length<33){
                return;
            }
            int id;
            try{

               id=Integer.parseInt(words[words.length-1]);
            }catch (NumberFormatException e){
                id=count++;

            }
             String name=words[0];
             String idCard=words[4];
             String sex=words[5];
             String born=words[6];
             String address=words[7];
             String withSex=words[9];
             String currentDate=sex==withSex?"同":"异";
             String mobile=words[19];
             String email=words[22];
            User user=new User(id,name,idCard,sex,born,address,withSex,currentDate,mobile,email);
            context.write(user,NullWritable.get());
        }
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf=new Configuration();
        conf.set("mapred.remote.os", "Linux");
        System.setProperty("HADOOP_USER_NAME", "hadoop");
        conf.set("mapreduce.app-submission.cross-platform", "true");
        Job job=Job.getInstance(conf);
//        job.setJar("E:\\injipro\\mapred\\ha\\target\\iotsp.jar");
        job.setJarByClass(TestHa.class);
        job.setMapperClass(RjMapper.class);
        job.setOutputKeyClass(User.class);
        job.setOutputValueClass(NullWritable.class);
//        job.setPartitionerClass(UserPartation.class);
        job.setNumReduceTasks(0);
        Path input=new Path("/rjinput/");
        Path output=new Path("/rjoutput/out3");
        FileSystem fs=FileSystem.get(conf);
        if(fs.exists(output)){
            fs.delete(output,true);
        }
        FileInputFormat.setInputPaths(job,input);
        FileOutputFormat.setOutputPath(job,output);
        boolean res=job.waitForCompletion(true);
        System.exit(res?0:1);
    }
}
