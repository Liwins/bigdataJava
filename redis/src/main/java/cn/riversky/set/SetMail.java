package cn.riversky.set;

import cn.riversky.logEmail.utils.JedisUtils;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * String ,Set<..>>
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/14.
 */
public class SetMail {

    private static String BIWU="biwu:dengji";
    private static String GUOJI="guoji:dengji";
    public static void main(String[] args) {
        Jedis jedis= JedisUtils.getJedis();
        String[] daxias=new String[]{"郭竞","黄蓉","杨过","林冲","令狐冲",
                "鲁智深","下龙女","续租","孤独求败","张三丰",
                "王重阳","张无忌","东方不败","乔峰","张三","乔峰","估计in个","慕容复",
                "乔峰","张三","黄蓉","杨过"};
        jedis.sadd(BIWU,daxias);
        Set<String> daxiaSet=jedis.smembers(BIWU);
        for(String name:daxiaSet){
            System.out.print(name+"  ");
        }
        System.out.println();
        //判断是否等级
        boolean isComing =jedis.sismember(BIWU,"jingzhongyue");
        if(!isComing){
            System.out.println("jingzhongyue未登记");
        }
//        计算等级个数
        long totalNum=jedis.scard(BIWU);
        System.out.println("大侠个数："+totalNum);
        System.out.println("另外一个国际会议");
        //另外一个国际会议
        String []guojiArr=new String[]{
                "孤独求败","张三丰",
                "王重阳","张无忌","东方不败","乔峰","jignzhongyue"
        };
        jedis.sadd(GUOJI,guojiArr);
        Set<String> gojis=jedis.smembers(GUOJI);
        for(String guoj:gojis){
            System.out.print(guoj+ "    ");
        }
        System.out.println();
        System.out.println("计算交集");
        Set<String> jiaoji=jedis.sinter(BIWU,GUOJI);
        for(String jiao:jiaoji){
            System.out.print(jiao+"     ");
        }
        System.out.println();
        System.out.println("计算并集");
        Set<String> bingji=jedis.sunion(BIWU,GUOJI);
        for(String jiao:bingji){
            System.out.print(jiao+"     ");
        }
        System.out.println();
        System.out.println("乔峰退出国际,求差集，GUOJI-BIWU");
        System.out.println();
        jedis.srem(BIWU,"乔峰");
        Set<String> chaji=jedis.sdiff(GUOJI,BIWU);
        for(String jiao:chaji){
            System.out.print(jiao+"     ");
        }
    }
}
