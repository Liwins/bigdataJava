package cn.riversky.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Created by admin on 2017/12/2.
 */
public class JsonToObj extends UDF{
public String evaluate(String field){
Rate ob=null;
ObjectMapper objectMapper=new ObjectMapper();
try {
ob=objectMapper.readValue(field,Rate.class);
} catch (IOException e) {
e.printStackTrace();
}
return ob==null?"":ob.toString();
}
}
