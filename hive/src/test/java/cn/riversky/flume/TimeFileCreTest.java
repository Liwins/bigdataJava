package cn.riversky.flume;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2017/12/5.
 */
public class TimeFileCreTest {
    @Test
    public void createTime() throws IOException, ParseException {
        StringBuilder builder=new StringBuilder();
        String year;
        String month;
        String day;
        String hour;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy,MM,dd,HH");
        Date date=simpleDateFormat.parse("2017,12,03,00");
        BufferedWriter bw=new BufferedWriter(new FileWriter("F:\\大数据\\传智播客\\学习资料架\\day14\\项目代码\\step-5-etl脚本\\v_time.txt"));

        for (int i = 0; i < 1000; i++) {
            builder.append(simpleDateFormat.format(date)+"\n");
            if(i%100==0){
                bw.write(builder.toString());
                builder=new StringBuilder();
            }
            date.setTime(date.getTime()+60*60*1000);
        }
          bw.flush();
        bw.close();
    }
}
