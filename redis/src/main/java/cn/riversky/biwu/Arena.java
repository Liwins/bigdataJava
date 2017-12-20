package cn.riversky.biwu;

import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/13.
 */
public class Arena implements Runnable {
    private Random random=new Random();
    private String redisKey;
    private Jedis jedis;
    private String arenaName;

    /**
     * 舞台名称和对应的key尖子
     * @param redisKey
     * @param arenaName
     */
    public Arena(String redisKey, String arenaName) {
        this.redisKey = redisKey;
        this.arenaName = arenaName;
    }

    @Override
    public void run() {
        jedis=new Jedis("datanode1",6397);
        jedis.auth("riversky");
        if(!jedis.exists(redisKey)){
            jedis.set(redisKey,"0");
            System.out.println("创建"+redisKey);
        }
        String[] daxias=new String[]{"郭竞","黄蓉","令狐冲","杨过","林冲","乔峰",
                "李四","张三","王重阳","大uanyu"};
                while (true){
//                    try {
//                        Thread.sleep(100L);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    int p1=random.nextInt(daxias.length);
                    int p2=random.nextInt(daxias.length);
                    while (p1==p2){
                        p2=random.nextInt(daxias.length);
                    }
                    System.out.println(daxias[p1]+"   v s   "+daxias[p2]);
                    jedis.incr(redisKey);
                }
    }
}
