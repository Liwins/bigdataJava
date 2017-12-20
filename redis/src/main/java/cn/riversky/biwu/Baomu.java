package cn.riversky.biwu;

import redis.clients.jedis.Jedis;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/13.
 */
public class Baomu implements Runnable{

    private String redisKey;
    private Jedis jedis=null;
    public Baomu(String redisKey) {
        this.redisKey = redisKey;
    }

    @Override
    public void run() {
        jedis=new Jedis("datanode1",6371);
        jedis.auth("riversky");
        while (true){
            try {
                System.out.println("共比武次数："+jedis.get(redisKey));
//                Thread.sleep(1000L);
            }catch (Exception e){
                System.out.println("舞台被损坏");
                e.printStackTrace();
            }
        }
    }
}
