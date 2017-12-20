package cn.riversky.logEmail.dao;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;

/**
 * Created by admin on 2017/12/6.
 */
public class HBaseDao {
    static Configuration conf= HBaseConfiguration.create();
    {
        conf.set("hbase.zookeeper.quorum", "datanode1,datanode2,datanode3");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("mapred.remote.os", "Linux");
        System.setProperty("HADOOP_USER_NAME", "hadoop");
        conf.set("mapreduce.app-submission.cross-platform", "true");

    }
    public static void createTable(String table,String columnFamily) throws IOException {
        HBaseAdmin client=new HBaseAdmin(conf);
        if(client.tableExists(table)){
            System.out.println("table:"+table+"存在");
            System.exit(0);
        }else {
            HTableDescriptor tableDesc=new HTableDescriptor(TableName.valueOf(table));
            tableDesc.addFamily(new HColumnDescriptor(columnFamily));
            client.createTable(tableDesc);
            System.out.println("create table"+table+" success:");
        }
        client.close();
    }

    public static void main(String[] args) throws IOException {
        String tableName="role";
        String clomnFimily="info2";
        HBaseDao.createTable(tableName,clomnFimily);
    }
}
