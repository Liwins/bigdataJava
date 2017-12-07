package cn.riversky.weblog;

import cn.riversky.weblog.bean.WebLogBean;
import cn.riversky.weblog.bean.WebLogParser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by admin on 2017/12/4.
 */
public class WeblogPrepreccess {

    public static class LogMapper extends Mapper<LongWritable,Text,Text,NullWritable>{
        //存储网站url分类数据
        //将有意义的路径检测放进来
        Set<String> pages=new HashSet<String>();
        Text k=new Text();
        NullWritable v=NullWritable.get();
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            pages.add("/cdrom/");
            pages.add("/soft/");
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line=value.toString();
            WebLogBean webLogBean= WebLogParser.parse(line);
            //仅保留感兴趣的路径
            WebLogParser.filtStaticResource(webLogBean,pages);
            k.set(webLogBean.toString());
            context.write(k,v);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(WeblogPrepreccess.class);
        job.setMapperClass(LogMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        job.setNumReduceTasks(0);
        boolean res=job.waitForCompletion(true);
        System.exit(res?1:0);
    }
}
