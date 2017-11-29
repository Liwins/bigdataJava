import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by admin on 2017/11/28.
 */
public class HDFSTest {
    private Configuration config=null;
    private FileSystem fs=null;
    @Before
    public void inti() throws Exception {
        config=new Configuration();
//        config.set("","");
        fs=FileSystem.get(new URI("hdfs://namenode:9000"),config,"hadoop");
    }
    public void defaultConfiPrint(){
        Iterator<Map.Entry<String,String>> iterator=config.iterator();
        while (iterator.hasNext()){
            Map.Entry entry=iterator.next();
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }
    public void testMKdir() throws Exception{
        boolean mkdirs=fs.mkdirs(new Path("/data/clickLog/20151226"));
        if(mkdirs){
            System.out.println("创建成功");
        }else {
            System.out.println("创建失败");
        }
    }
    public void testDelete() throws IOException {
        //第二个参数为递归删除
        boolean dleteresult=fs.delete(new Path("/testmkdir"),true);
        if(dleteresult){
            System.out.println("删除成功");
        }else {
            System.out.println("删除失败");
        }
    }

    /**
     * 迭代器的方式可以避免将数据全部放到java的容器中。
     * @throws IOException
     */
    public void testLs() throws IOException {
        //第二个参数为是不是要递归
        RemoteIterator<LocatedFileStatus>listfile= fs.listFiles(new Path("/cenos-6.5-hadoop-2.6.4.tar.gz"),true);
        while (listfile.hasNext()){
//            System.out.println(listfile.next().toString());
            LocatedFileStatus lif=listfile.next();
            BlockLocation[] blockLocation=lif.getBlockLocations();
            for (BlockLocation bl:blockLocation){
                System.out.println("偏移量："+ bl.getOffset());
                System.out.println("块长度"+bl.getLength());
                String[]datanode=bl.getHosts();
                for(String dano:datanode){

                    System.out.print(dano+"-----");
                }
            }
        }
    }

    /**
     * 这种方式不建议，因为会直接加载到内存。
     * @throws IOException
     */
    public void testLs2() throws IOException {
        //第二个参数为是不是要递归
        FileStatus[]stateArray=fs.listStatus(new Path("/"));
        for (FileStatus fileStatus:stateArray){
            if(fileStatus.isDirectory()){
                System.out.println("目录"+fileStatus.getPath());
            }else {

                System.out.println("文件"+fileStatus.getPath());
            }

        }
    }
    /**
     * 使用流的方式，可以对文件指定偏移量的获取
     */
    public void testStreamUpload() throws IOException {
        //第二个参数，如果有覆盖掉
        FSDataOutputStream out=fs.create(new Path("/inveridex/input/c.txt"),true);
        InputStream inputStream=new FileInputStream("F:\\大数据\\传智播客\\学习资料架\\day10\\shizhan_03_hadoop\\srcdata\\inverindexinput\\c.txt");
        BufferedInputStream in=new BufferedInputStream(inputStream);
        IOUtils.copy(in,out);

    }
    /**
     * 使用流的方式，可以对文件指定偏移量的获取
     * in.seek(60M)就可以达到偏移效果
     */
    @Test
    public void testStreamDownload() throws IOException {
        FSDataInputStream in=fs.open(new Path("/inveridex/outputonestep/part-r-00000"));
//            FSDataInputStream in=fs.open(new Path("/matlab_2014a_3987"));
//        OutputStream out=new FileOutputStream(new File("E:\\matlab_2014a_3987.rar.copy"));
//        in.seek(3);
//        IOUtils.copy(in,out);
        IOUtils.copy(in,System.out);
    }

}
