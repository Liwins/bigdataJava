package cn.riversky.weblog;

import cn.riversky.weblog.bean.WebLogBean;
import cn.riversky.weblog.bean.WebLogParser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * 用戶停留时间
 * Created by admin on 2017/12/5.
 */
public class UserStayTime {
    static class UserStayTimeMapper extends Mapper<LongWritable, Text, Text, Text> {
        Text k=new Text();
        Text v=new Text();

        /**
         * 注意这里如果求精确的话，需要将ip设置成用户的id
         * 按照ip，和访问时间点统计
         * @param key
         * @param value
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            //设置计数器
            Counter invalidCounter = context.getCounter("parsetime", "invalidbean");
            String line=value.toString();
            WebLogBean bean= WebLogParser.parse(line);
            if(bean.isValid()){
                String remote_addr=bean.getRemote_addr();
                String time_local=bean.getTime_local();
                k.set(remote_addr);
                v.set(time_local);
                context.write(k,v);
            }else {
                invalidCounter.increment(1);

            }
        }
    }
    static class UserStayTimeReducer extends Reducer<Text, Text, Text, Text> {
        Text v = new Text();

        /**
         * 按照ip进行map的结果
         * @param key
         * @param values
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            Counter staytimeerr=context.getCounter("parsetime","staytimeerr");
            ArrayList<Date>times=new ArrayList<Date>();
            try{
                for(Text value:values){
                    times.add(toDate(value));
                }
                Collections.sort(times);
                HashMap<Date[],Long> stayTime=getStayTime(times);
                Set<Map.Entry<Date[],Long>> entrySet=stayTime.entrySet();
                for (Map.Entry<Date[],Long> ent:entrySet){
                    //输出 remote_add 开始时间 结束时间 时长
                    v.set(toStr(ent.getKey()[0])+"\t"+toStr(ent.getKey()[1])+"\t"+ent.getValue());
                    context.write(key,v);
                }
            }catch (ParseException e){
                staytimeerr.increment(1);
            }

        }

        private String toStr(Date date) {
            return WebLogParser.df2.format(date);

        }

        /**
         * 连续时常请求中，求每次访问的停留时长
         * @param times
         * @return
         */
        private HashMap<Date[], Long> getStayTime(ArrayList<Date> times) {
            HashMap<Date[], Long> stayTime = new HashMap<Date[], Long>();
            int size=times.size();
            Date[]dates=new Date[2];
            dates[0]= times.get(0);
            //如果times元素中只有一个，则是该用户的单请求，则直接返回
            if(size<1)return null;
            if(size<2){
                dates[1]=dates[0];
                stayTime.put(dates,0L);
                return stayTime;
            }
            for (int i = 0; i < size - 1; i++) {
                if(timeDiff(times.get(i+1),times.get(i))>30*60*1000){
                 //如果两次请求时间间隔差超过30分钟，则将“本次-批首的时间查存入 hashmap完成一次访问停留处理”
                    dates[1]= times.get(i);
                    stayTime.put(dates,timeDiff(dates[1],dates[0]));
                    //同时，重置批次
                    dates=new Date[2];
                    dates[0]=times.get(i+1);
                }else {
                    if(i==size-2){
                        dates[1]=times.get(i+1);
                        stayTime.put(dates,timeDiff(dates[1],dates[0]));
                    }
                }
            }
            return stayTime;
        }

        private long timeDiff(Date date, Date date1) {
            return date.getTime()-date1.getTime();
        }

        private Date toDate(Text value) throws ParseException {
            return WebLogParser.df1.parse(value.toString());
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf=new Configuration();
        Job job= Job.getInstance(conf);
        job.setJarByClass(UserStayTime.class);
        job.setMapperClass(UserStayTimeMapper.class);
        job.setReducerClass(UserStayTimeReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        boolean res=job.waitForCompletion(true);
        System.exit(res?1:0);
    }
}
