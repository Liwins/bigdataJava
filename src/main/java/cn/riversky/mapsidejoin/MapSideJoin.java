package cn.riversky.mapsidejoin;

import org.apache.commons.lang.StringUtils;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/11/28.
 */
public class MapSideJoin {
    public static class MapSideJoinMapper extends Mapper<LongWritable,Text,Text,NullWritable> {

        Map<String,String>pdInfoMap=new HashMap<String,String >();
        Text k=new Text();

        @Override
        protected void setup(Context context) throws IOException {

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("pdts.txt")));
            String line;
            while (StringUtils.isNotEmpty(line=br.readLine())){
                String[]fileds=line.split(",");
                pdInfoMap.put(fileds[0],fileds[1]);
            }
            br.close();
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            String orderLine=value.toString();
            String[]wrods=orderLine.split("\t");
            String pdName=pdInfoMap.get(wrods[1]);
            k.set(orderLine+"\t"+pdName);
            context.write(k,NullWritable.get());
        }
    }

    public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException, InterruptedException {
        Configuration conf=new Configuration();
        conf.set("mapred.textoutputformat.separator", "\t");
        conf.set("mapred.remote.os", "Linux");
        System.setProperty("HADOOP_USER_NAME", "hadoop");
        conf.set("mapreduce.app-submission.cross-platform","true");
        Job job=Job.getInstance(conf);
//        job.setJarByClass(MapSideJoin.class);
        job.setJar("E:\\injipro\\mapred\\target\\iotsp.jar");
        job.setMapperClass(MapSideJoinMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        FileInputFormat.setInputPaths(job,new Path("/mapjoininput"));
        Path outputPath=new Path("/temp/output");
        FileSystem fs=FileSystem.get(conf);
        if(fs.exists(outputPath)){
            fs.delete(outputPath,true);
        }
        FileOutputFormat.setOutputPath(job,outputPath);
        // 指定需要缓存一个文件到所有的maptask运行节点工作目录
		/* job.addArchiveToClassPath(archive); */// 缓存jar包到task运行节点的classpath中
		/* job.addFileToClassPath(file); */// 缓存普通文件到task运行节点的classpath中
		/* job.addCacheArchive(uri); */// 缓存压缩包文件到task运行节点的工作目录
		/* job.addCacheFile(uri) */// 缓存普通文件到task运行节点的工作目录

        // 将产品表文件缓存到task工作节点的工作目录中去E:\injipro\mapred\src\srcdata\mapjoincache
        // 将产品表文件缓存到task工作节点的工作目录中去E:\injipro\mapred\src\srcdata\mapjoincache
        job.addCacheFile(new URI("hdfs://namenode:9000/mapjoincache/pdts.txt"));
        job.setNumReduceTasks(0);
        boolean res=job.waitForCompletion(true);
        System.exit(res?0:1);
    }
}
