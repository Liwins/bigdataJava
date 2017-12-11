package cn.riversky;

import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Created by admin on 2017/12/11.
 */
public class RedisTest {
    @Test
    public void testConnect(){
        Jedis jedis=new Jedis("datanode1",6379);
        jedis.auth("riversky");
        String response=jedis.ping();
        System.out.println(response);
    }
}
