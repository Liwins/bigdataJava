package cn.riversky.map;

import cn.riversky.logEmail.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis的map结构
 * <rediskey,Map<key,value>
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/14.
 */
public class MapMain {
    public static void main(String[] args) {
        Jedis jedis=JedisUtils.getJedis();
        jedis.del("daxia:jingzhouyue");
        //创建一个对象
        jedis.hset("daxia:jingzhouyue","姓名","不为人知");
        jedis.hset("daxia:jingzhouyue","年龄","18");
        jedis.hset("daxia:jingzhouyue","技能","杀人于悟性");
//        打印对象
        Map<String,String > jingzhouyue=jedis.hgetAll("daxia:jingzhouyue");
        for(Map.Entry entry:jingzhouyue.entrySet()){
            System.out.println(entry.getKey()+"     "+entry.getValue());
        }
        System.out.println();
        //获取所有字段信息
        Set<String> fields=jedis.hkeys("daxia:jingzhouyue");
        System.out.println("hkeys  ");
        for(String field:fields){
            System.out.println(field);
        }
        System.out.println();
        //获取所有指的信息
        List<String> values=jedis.hvals("daxia:jingzhouyue");
        System.out.println("hvals  ");
        for(String value:values){
            System.out.println(value);
        }
        //获取打下的字段值
        String age=jedis.hget("daxia:jingzhouyue","年龄");
        System.out.println("jingzhouyue:年龄----------"+age);
        jedis.hincrBy("daxia:jingzhouyue","年龄",2);
        System.out.println("修改荆州月年龄为"+jedis.hget("daxia:jingzhouyue","年龄"));
        System.out.println();

        //删除某一个key和value
        jedis.hdel("daxia:jingzhouyue","姓名");
        Set<String> fieldss=jedis.hkeys("daxia:jingzhouyue");
        System.out.println("hkeys  ");
        for(String field:fieldss){
            System.out.println(field);
        }
    }
}
