package cn.riversky.rjoin;

import cn.riversky.rjoindemo.InfoBean;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by admin on 2017/11/27.
 */
public class RJoinMR {

    static class JoinMapper extends Mapper<LongWritable,Text,Text,DetailOne>{
        DetailOne detail=new DetailOne();
        Text k = new Text();
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            FileSplit sp= (FileSplit) context.getInputSplit();
            String name=sp.getPath().getName();
            String line=value.toString();
            String[]word=line.split("\t");
            String pid="";
//           String oid,  String pid, String pname,String flag
            if(word[0].length()==5){
                detail.setAll("",word[0],word[1],"1");
                pid=word[0];
            }else {
                detail.setAll(word[0],word[2],"","2");
                pid=word[2];
            }
            k.set(pid);
            context.write(k,detail);
        }
    }

    static class RJoinReducer extends Reducer<Text, DetailOne, DetailOne, NullWritable> {

        @Override
        protected void reduce(Text pid, Iterable<DetailOne> beans, Context context) throws IOException, InterruptedException {
            InfoBean pdBean = new InfoBean();
            ArrayList<DetailOne> orderBeans = new ArrayList<DetailOne>();

            for (DetailOne bean : beans) {
                if ("1".equals(bean.getFlag())) {	//产品的
                    try {
                        BeanUtils.copyProperties(pdBean, bean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    DetailOne odbean = new DetailOne();
                    try {
                        BeanUtils.copyProperties(odbean, bean);
                        orderBeans.add(odbean);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            // 拼接两类数据形成最终结果
            for (DetailOne bean : orderBeans) {

                bean.setPname(pdBean.getPname());

                context.write(bean, NullWritable.get());
            }
        }
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf=new Configuration();
        conf.set("mapred.remote.os", "Linux");
        System.setProperty("HADOOP_USER_NAME", "hadoop");
        conf.set("mapreduce.app-submission.cross-platform","true");
        conf.set("mapred.textoutputformat.separator", "\t");
        Job job=Job.getInstance(conf);
        job.setJar("E:\\injipro\\mapred\\target\\iotsp.jar");
//        job.setJarByClass(RJoinMR.class);
        job.setMapperClass(JoinMapper.class);
        job.setReducerClass(RJoinReducer.class);

        // 指定mapper输出数据的kv类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(DetailOne.class);

        // 指定最终输出的数据的kv类型
        job.setOutputKeyClass(DetailOne.class);
        job.setOutputValueClass(NullWritable.class);

        Path out=new Path(args[1]);
        FileSystem fs=FileSystem.get(conf);
        if(fs.exists(out)){
            fs.delete(out,true);
        }
        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        boolean res=job.waitForCompletion(true);
        System.exit( res?0:1);
    }
}
