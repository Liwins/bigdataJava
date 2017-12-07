package cn.riversky;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by admin on 2017/12/6.
 */
public class HbaseTest {

    static Configuration conf = null;
    private Connection connection = null;
    private Table table = null;

    /**
     * 初始化鏈接
     *
     * @throws IOException
     */
    @Before
    public void init() throws IOException {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "datanode1,datanode2,datanode3");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        connection = ConnectionFactory.createConnection(conf);
        table = connection.getTable(TableName.valueOf("user"));
    }

    /**
     * 生成表
     *
     * @throws IOException
     */
    @Test
    public void createTable() throws IOException {
        HBaseAdmin admin = new HBaseAdmin(conf);
        TableName table = TableName.valueOf("test3");
        HTableDescriptor desc = new HTableDescriptor(table);
        HColumnDescriptor family = new HColumnDescriptor("info");
        desc.addFamily(family);
        HColumnDescriptor family3 = new HColumnDescriptor("info2");
        desc.addFamily(family3);
        admin.createTable(desc);
    }

    /**
     * 刪除表
     *
     * @throws IOException
     */
    @Test
    public void delteTable() throws IOException {
        HBaseAdmin admin = new HBaseAdmin(conf);
        admin.disableTable("test3");
        admin.deleteTable("test3");
        admin.close();
    }

    /**
     * 向表中添加数据
     *
     * @throws IOException
     */
    @Test
    public void insertDate() throws IOException {
        //id
        Put put = new Put(Bytes.toBytes("wangsenfeng_12345"));
        //family info:name info:sex
        put.add(Bytes.toBytes("info1"), Bytes.toBytes("name"), Bytes.toBytes("lisi"));
        put.add(Bytes.toBytes("info1"), Bytes.toBytes("sex"), Bytes.toBytes("nan"));
        put.add(Bytes.toBytes("info1"), Bytes.toBytes("adress"), Bytes.toBytes("hainan"));
        put.add(Bytes.toBytes("info1"), Bytes.toBytes("age"), Bytes.toBytes(12));
        table.put(put);
    }

    /**
     * 与增加相同，id相关通告即可
     */
    public void updateData() {

    }

    /**
     * 删除操作
     *
     * @throws IOException
     */
    @Test
    public void delete() throws IOException {
        Delete delete = new Delete(Bytes.toBytes("wangsenfeng_1234"));
//        delete.addColumn(Bytes.toBytes("info2"),Bytes.toBytes("name"));
        delete.addFamily(Bytes.toBytes("info1"));
        table.delete(delete);
    }

    /**
     * 单挑查询
     *
     * @throws IOException
     */
    @Test
    public void quaryDate() throws IOException {
        Get get = new Get(Bytes.toBytes("wangsenfeng_1234"));
        Result result = table.get(get);
        byte[] value = result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("age"));
        byte[] value1 = result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("sex"));
        System.out.println(Bytes.toInt(value));
//        get.addFamily(Bytes.toBytes("info2"));


    }

    /**
     * 全表扫描
     *
     * @throws IOException
     */
    @Test
    public void scanDate() throws IOException {
        //设置全表送啊秒
        ResultScanner scanner = table.getScanner(new Scan());
        for (Result sc : scanner) {

            byte[] value = sc.getValue(Bytes.toBytes("info2"), Bytes.toBytes("age"));
            byte[] value2 = sc.getValue(Bytes.toBytes("info2"), Bytes.toBytes("sex"));
            System.out.print(Bytes.toString(sc.getRow()) + "   ");
            System.out.print(Bytes.toString(value2) + "   ");
//            System.out.print(Bytes.toInt(value)+"   ");
            System.out.println();
        }
    }

    /**
     * 全表扫描
     *
     * @throws IOException
     */
    @Test
    public void scanDataLie() throws IOException {
        //设置全表送啊秒
        Scan scan = new Scan();
        scan.addColumn(Bytes.toBytes("info1"), Bytes.toBytes("sex"));
        ResultScanner scanner = table.getScanner(scan);
        for (Result sc : scanner) {

            byte[] value2 = sc.getValue(Bytes.toBytes("info1"), Bytes.toBytes("sex"));
            System.out.println(Bytes.toString(sc.getRow()) + "   " + Bytes.toString(value2));
            System.out.println(Bytes.toString(sc.value()));
        }
    }

    /**
     * 通过过滤器来进行查找
     * 正则，对列值进行过滤
     * 种类1  值过滤器
     * 种类2 前缀过滤器
     * 种类3 多个列明前缀过滤器
     * 种类4 rowkey过滤器
     *
     * @throws IOException
     */
    @Test
    public void scanDataByFilte2r() throws IOException {
        Scan scan = new Scan();
        SingleColumnValueFilter filter=new SingleColumnValueFilter(Bytes.toBytes("info1"),Bytes.toBytes("sex"), CompareFilter.CompareOp.EQUAL,Bytes.toBytes("nan"));
        scan.setFilter(filter);
        ResultScanner scanner = table.getScanner(scan);
        for(Result sc:scanner){
            System.out.println(Bytes.toString(sc.getRow()));
            byte[] value = sc.getValue(Bytes.toBytes("info1"), Bytes.toBytes("name"));
            byte[] value2 = sc.getValue(Bytes.toBytes("info1"), Bytes.toBytes("sex"));
            byte[] value3 = sc.getValue(Bytes.toBytes("info1"), Bytes.toBytes("age"));
            System.out.print(Bytes.toString(value)+"    "+Bytes.toString(value2)+"  "+Bytes.toString(value3));
            System.out.println();
        }
    }

    /**
     * 匹配前缀名
     * @throws IOException
     */
    public void scanDataByFilter(){
        RowFilter f=new RowFilter(CompareFilter.CompareOp.EQUAL,new RegexStringComparator("^wangsenfeng_1"));
    }
    @After
    public void close() throws IOException {
        table.close();
        connection.close();
    }
}
