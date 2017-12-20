package cn.riversky.logAnalyze.storm.utils;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.LinkedList;
import java.util.List;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/19.
 */
public class JedisPoolUtils {
    private static ShardedJedisPool jedisPoolUtils;
    static {
        JedisPoolConfig config=new JedisPoolConfig();
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(5);
        //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        config.setMaxTotal(-1);
        //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(5);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);

        //创建四个redis服务实例，并封装在list中
        List<JedisShardInfo> list=new LinkedList<JedisShardInfo>();
        JedisShardInfo jedisShardInfo=new JedisShardInfo("datanode1",6379);
        jedisShardInfo.setPassword("riversky");
        list.add(jedisShardInfo);
        //创建具有分片功能的Jedis连接池
        jedisPoolUtils=new ShardedJedisPool(config,list) ;
    }
    public static ShardedJedisPool getShardedJedisPool(){
        return jedisPoolUtils;
    }

    public static void main(String[] args) {
        ShardedJedis jedis=JedisPoolUtils.getShardedJedisPool().getResource();

//        jedis.set("1","riversky");
//        jedis.set("2","计算机学院");
//        jedis.set("3","计算机应用技术");
//        jedis.set("4","java ");
        System.out.println(jedis.get("1"));
        System.out.println(jedis.get("2"));
        System.out.println(jedis.get("3"));
        System.out.println(jedis.get("4"));
    }

}
