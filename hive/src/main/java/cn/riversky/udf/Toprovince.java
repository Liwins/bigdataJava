package cn.riversky.udf;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.HashMap;

/**
 * Created by admin on 2017/12/1.
 */
public class Toprovince  extends UDF{
public static HashMap<String,String> provinceMap=new HashMap<String,String>();
static {
provinceMap.put("136","北京");
provinceMap.put("137","上海");
provinceMap.put("138","广州");
}
public String evaluate(String field){
String result=field.substring(0,3);
if(provinceMap.get(result)!=null){
return provinceMap.get(result);
}
else return "huoxign";
}
}
