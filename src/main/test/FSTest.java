import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.Set;

/**
 * Created by admin on 2017/11/27.
 */
public class FSTest {

    public static void main(String[]args) throws Exception {
        String path1="E:/injipro/mapred/src/srcdata/mapjoincache/pdts.txt";
        String path2="/mapjoincache/pdts.txt";
        addP(path1,path2);
//        System.setProperty("user.name", "hadoop");
//    getprop();
    }

    public static void getprop(){
        Properties strings= System.getProperties();
        Set<Object> keys=strings.keySet();
        for(Object key:keys){
            System.out.println(key+":"+System.getProperty(key.toString()));
        }
    }
    public static void addP(String path,String pathhdfs) throws URISyntaxException, IOException, InterruptedException {
        Configuration conf=new Configuration();
        FileSystem fs=FileSystem.get(new URI("hdfs://namenode:9000"),conf,"hadoop");

        Path pathhd=new Path(pathhdfs);
        if(fs.exists(pathhd)){
            fs.delete(pathhd,true);
        }

        BufferedInputStream bi=new BufferedInputStream(new FileInputStream(path));
        OutputStream out1=fs.create(pathhd);
        IOUtils.copy(bi,out1);
        bi.close();
        out1.close();
        fs.close();
    }
    public static void addP() throws Exception {

        Configuration conf = new Configuration();
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://namenode:9000"), conf, "hadoop");

        BufferedInputStream bi=new BufferedInputStream(new FileInputStream("F:\\大数据\\传智播客\\学习资料架\\day09\\shizhan_03_hadoop\\files\\join\\order.txt"));
        OutputStream out1=fileSystem.create(new Path("/rjoin/input/order.txt"));
        IOUtils.copy(bi,out1);
        BufferedInputStream bi2=new BufferedInputStream(new FileInputStream("F:\\大数据\\传智播客\\学习资料架\\day09\\shizhan_03_hadoop\\files\\join\\product.txt"));
        OutputStream out2=fileSystem.create(new Path("/rjoin/input/product.txt"));
        IOUtils.copy(bi2,out2);
        bi.close();
        bi2.close();
        out1.close();
        out2.close();
        fileSystem.close();
    }
}
