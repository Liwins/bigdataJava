package cn.riversky.logEmail.utils;

import redis.clients.jedis.Jedis;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/14.
 */
public class JedisUtils {
    private static final String LINKNAME="datanode1";
    private static final String PASS="riversky";
    private static final int PORT=6379;

    public static Jedis getJedis(){
        Jedis jedis=new Jedis(LINKNAME,PORT);
        jedis.auth(PASS);
        return jedis;
    }
}
