package cn.riversky.mrhbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过mr操作hbase
 * 这时候需要通过该方式对hbase进行行数据进行改变
 * 这里实现word count
 * Created by admin on 2017/12/7.
 */
public class HBaseMr {
    static Configuration config=null;
    static{
        config= HBaseConfiguration.create();
        config.set("hbase.zookeeper.quorum", "datanode1,datanode2,datanode3");
        config.set("hbase.zookeeper.property.clientPort", "2181");
    }

    /**
     * 表信息
     */
    public static final String tableName="word";//表1
    public static final String columFam="content";
    public static final String col="info";
    public static final String tableName2="stat";
    /**
     * 初始化表结构
     */
    public static void initTable(){
        HTable table=null;
        HBaseAdmin admin=null;
        try{
            admin=new HBaseAdmin(config);
            //如果存在删除表
            if(admin.tableExists(tableName)||admin.tableExists(tableName2)){
                System.out.println("表存在，进行车从简");
                admin.disableTable(tableName);
                admin.disableTable(tableName2);
                admin.deleteTable(tableName);
                admin.deleteTable(tableName2);
            }
            /*    创建表       */
            HTableDescriptor descriptor=new HTableDescriptor(tableName);
            HTableDescriptor descriptor2=new HTableDescriptor(tableName2);
            HColumnDescriptor columnDescriptor=new HColumnDescriptor(columFam);
            descriptor.addFamily(columnDescriptor);
            admin.createTable(descriptor);
            HColumnDescriptor cf=new HColumnDescriptor(columFam);
            descriptor2.addFamily(cf);
            admin.createTable(descriptor2);
            //添加数据
            table=new HTable(config,tableName);
            table.setAutoFlush(false);
            table.setWriteBufferSize(500);
            List<Put> puts=new ArrayList<Put>();
            //id
            Put put1=new Put(Bytes.toBytes("1"));
            put1.add(columFam.getBytes(),col.getBytes(),"The Apache Hadoop software library is a framework".getBytes());
            puts.add(put1);
            Put put2=new Put(Bytes.toBytes("2"));
            put2.add(columFam.getBytes(),col.getBytes(),"The common utilities that support the other Hadoop modules".getBytes());
            puts.add(put2);
            Put put3=new Put(Bytes.toBytes("3"));
            put3.add(columFam.getBytes(),col.getBytes(),"The common utilities that support the other Hadoop modules".getBytes());
            puts.add(put3);
            Put put4=new Put(Bytes.toBytes("4"));
            put4.add(columFam.getBytes(),col.getBytes(),"The common utilities that support the other Hadoop modules".getBytes());
            puts.add(put4);
            Put put5=new Put(Bytes.toBytes("5"));
            put5.add(columFam.getBytes(),col.getBytes(),"The common utilities that support the other Hadoop modules".getBytes());
            puts.add(put5);
            table.put(puts);
            table.flushCommits();
            puts.clear();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(table!=null){
                try {
                    table.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 有点类似与scanner
     */
    public static class HbaMapper extends TableMapper<Text,IntWritable>{
        private IntWritable one=new IntWritable(1);
        private Text word=new Text();
        @Override
        //输入的类型为：key：rowKey； value：一行数据的结果集Result
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
            String words=Bytes.toString(value.getValue(columFam.getBytes(),col.getBytes()));
            //按照空格分割
            String[] its=words.split(" ");
            for (int i = 0; i < its.length; i++) {
                word.set(its[i]);
                context.write(word,one);
            }
        }
    }

    /**
     * 这里需要注意的是ImmutableBytesWritable作为输出，表示rowkey的类型
     */
    public static class HbaseReduce extends TableReducer<Text,IntWritable,ImmutableBytesWritable>{
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int count=0;
            for(IntWritable i:values){
                count++;
            }
            Put put=new Put(key.toString().getBytes());
            put.add(columFam.getBytes(),col.getBytes(),String.valueOf(count).getBytes());
            //这里不需要使用table来进行操作
          //写到hbase,需要指定rowkey、put
            context.write(new ImmutableBytesWritable(Bytes.toBytes(key.toString())),put);
            context.write(new ImmutableBytesWritable(key.toString().getBytes()),put);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        config.set("mapred.remote.os", "Linux");
        System.setProperty("HADOOP_USER_NAME", "hadoop");
        config.set("mapreduce.app-submission.cross-platform","true");
        config.set("mapred.job.tracker", "namenode:9001");
        //初始化表
        initTable();//初始化表
        //创建job
//        Job job =Job.getInstance(config);
        Job job =new Job(config,"HBaseMr");
        job.setJarByClass(HBaseMr.class);
        Scan scan=new Scan();
        //可以指定查询的某一列
        scan.addColumn(columFam.getBytes(),col.getBytes());
        //指定查询的mapper,设置表明，scan,mapper,以及输出的key,value
        TableMapReduceUtil.initTableMapperJob(tableName,scan,HbaMapper.class,Text.class,IntWritable.class,job);
        //创建写入的reducer
        TableMapReduceUtil.initTableReducerJob(tableName2,HbaseReduce.class,job);
        System.exit( job.waitForCompletion(true) ?0:1);
    }
}
