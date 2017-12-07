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
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * 清理無效的日志，标记位为无效
 * Created by admin on 2017/12/5.
 */
public class WeblogPreValid {
    static class WeblogPreVailidMapper extends Mapper<LongWritable,Text,Text,WebLogBean>{
        // 用来存储网站url分类数据
        Set<String> pages = new HashSet<String>();
        Text k = new Text();
        NullWritable v = NullWritable.get();
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            pages.add("/cdrom/");
            pages.add("/soft/");
        }

        /**
         * 按照ipmap
         * @param key
         * @param value
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line=value.toString();
            WebLogBean webLogBean= WebLogParser.parse(line);
            WebLogParser.filtStaticResource(webLogBean,pages);
            if(webLogBean.isValid()){
                k.set(webLogBean.getRemote_addr());
                context.write(k,webLogBean);
            }
        }
    }
    static class WeblogPreprocessReducer extends Reducer<Text,WebLogBean,NullWritable,WebLogBean>{
        @Override
        protected void reduce(Text key, Iterable<WebLogBean> values, Context context) throws IOException, InterruptedException {

            for(WebLogBean value:values){
                context.write(NullWritable.get(), value);
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf=new Configuration();
        Job job=Job.getInstance(conf);

        job.setJarByClass(WeblogPreValid.class);
        job.setMapperClass(WeblogPreVailidMapper.class);
        job.setReducerClass(WeblogPreprocessReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(WebLogBean.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(WebLogBean.class);

        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        boolean res=job.waitForCompletion(true);
        System.exit(res?1:0);
    }
}
