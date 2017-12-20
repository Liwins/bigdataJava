package cn.riversky;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * 基本string的测试
 * @author Created by admin on 2017/12/11.
 */
public class RedisTest {
    @Test
    public void testConnect() throws InterruptedException {
        Jedis jedis=new Jedis("datanode1",6379);
        jedis.auth("riversky");
//        String response=jedis.ping();
//        jedis.set("totalAmount","1");
//        jedis.incrBy("totalAmount",3);
//        System.out.println(jedis.get("totalAmount"));

        /**
         * 一次插入多条数据
         */
//        jedis.mset("aaa","myselcaozuo","bbb","shazi","ccc","sshkuangjia");
//        System.out.println(jedis.get("aaa"));
        /**
         * 自动过期字段
         */
        jedis.setex("wumai",10,"我们或在陷阱中");
        while (jedis.exists("wumai")){
            System.out.println("雾霾还在");
            Thread.sleep(1000L);
        }
    }
}
