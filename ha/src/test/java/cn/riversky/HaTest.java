package cn.riversky;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Unit test for simple App.
 */
public class HaTest {

//    @Test
    public void testString(){
        String line="叶孙任,,,ID,352230197601161211,M,19760116,周宁县李墩镇东山村前垅56号,,F,,CHN,35,3501,,,,,,,15265219320,,,,,,,,,,0,2011-7-2 10:10:45,6000006";
        String line2="姚远凡,,,ID,420711197509192410,M,19750919,湖北省黄石市黄石港区红旗桥湖滨大道1270-25号,,F,,,42,4201,,,,,,13971210584,13971210584,,,汉,,,,,,,0,2012-12-28 8:48:54,20000010";

        String[]words=line.split(",");
        System.out.println(words[9]);
        System.out.println(words.length);
    }

    /**
     * 根据路径获取当前目录的所有文件的名称
     *
     * @param path C:\Users\admin\Pictures\社工裤\2000W
     * @return
     */
    public ArrayList<String> getFileNamesByPath(String path) {
        File file = new File(path);
        File[] filenames = file.listFiles();
        ArrayList<String> pathnames = new ArrayList<>();
        for (File filename : filenames) {
            if (filename.isFile()) {
                String pathnameByFiles = filename.getPath();
                pathnames.add(pathnameByFiles);
            }
        }
        return pathnames;
    }

    @Test
    public void testUpload() throws IOException {
        System.out.println("hello");
//        uploadBydir("C:\\Users\\admin\\Pictures\\社工裤\\2000W", "/rjinput1/");
        System.out.println("完成");
    }

    /**
     * @param localdir E:\npm-debug.log
     * @param hadir    /testupload/npm-debug.log
     * @throws IOException
     */
    public void uploadBydir(String localdir, String hadir) throws IOException {
        Configuration conf = new Configuration();
        conf.set("mapred.remote.os", "Linux");
        System.setProperty("HADOOP_USER_NAME", "hadoop");
        conf.set("mapreduce.app-submission.cross-platform", "true");
        FileSystem fs = FileSystem.get(conf);
//        File file=new File("");
        Path uploadPath = new Path(hadir);
        if (fs.exists(uploadPath)) {
            fs.delete(uploadPath, true);
        }
        ArrayList<String> upfiles = getFileNamesByPath(localdir);
        for (String upfile : upfiles) {
            String[]upfilename=upfile.split("\\\\");
            fs.copyFromLocalFile(new Path(upfile), new Path(uploadPath.toString()+"/"+upfilename[upfilename.length-1]));
        }
        fs.close();
    }
}
