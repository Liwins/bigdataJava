package cn.riversky.list;

import cn.riversky.logEmail.utils.JedisUtils;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;

/**
 * <String ,List<..>>
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/14.
 */
public class ListMain {
    public static String DUILIE="队列1";
    public static void main(String[] args) {
        Jedis jedis = JedisUtils.getJedis();
        jedis.lpush(DUILIE, "乔峰", "duanyu","李四", "张三","虚竹", "鸠摩智");
        for (String name : jedis.lrange(DUILIE, 0, -1)) {
            System.out.print(name + "   ");
        }
        System.out.println();
//        王语嫣插队到第一名
        jedis.rpush(DUILIE, "王语嫣");
        for (String name : jedis.lrange(DUILIE, 0, -1)) {
            System.out.print(name + "   ");
        }
        System.out.println();
        //鸠摩智不开心，正好慕容复来了，让慕容在自己前面
        jedis.linsert(DUILIE, BinaryClient.LIST_POSITION.AFTER,"鸠摩智","慕容复");
        for (String name : jedis.lrange(DUILIE, 0, -1)) {
            System.out.print(name + "   ");
        }
        System.out.println();
        //王语嫣消费
        jedis.rpop(DUILIE);
        for (String name : jedis.lrange(DUILIE, 0, -1)) {
            System.out.print(name + "   ");
        }
        System.out.println();
        //又来个插队的偷偷消费
        jedis.rpush(DUILIE,"jingzhongyue");
        for (String name : jedis.lrange(DUILIE, 0, -1)) {
            System.out.print(name + "   ");
        }
        System.out.println();
        //消费2-4
        String result=jedis.ltrim(DUILIE,2,5);
        if("OK".equals(result)){
            for (String name : jedis.lrange(DUILIE, 0, -1)) {
                System.out.print(name + "   ");
            }
        }
        System.out.println();
        //这时候，乔峰发现我，大战三遍，我全身而退
        String rees=jedis.ltrim(DUILIE,0,2);
        if("OK".equals(rees)){
            for (String name : jedis.lrange(DUILIE, 0, -1)) {
                System.out.print(name + "   ");
            }
        }
        System.out.println();
    }
}
