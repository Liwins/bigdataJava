package cn.riversky.data;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *@author  Created by admin on 2017/12/13.
 */
public class OrderBolt extends BaseRichBolt{
    private JedisPool pool;
    /**
     * 将类别查询的数据放到内存中
     */
    private Map<String,String> map =  new HashMap<>();
    /**
     * 预定义
     * @param map
     * @param topologyContext
     * @param outputCollector
     */
    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        mapinit();

        JedisPoolConfig conf=new JedisPoolConfig();
        //设置最大空闲的jedis实例
        conf.setMaxIdle(5);
        //控制一个pool可分配多少个Jedis实例，通过pool.getResource()来获取；
        //如果赋值为-1表示不限制，如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted（耗尽）
        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true,则得到的jedis是实例均是可用的
        conf.setMaxTotal(100*1000);
        //当borrow()一个实例时，最大等待时间，如果超过了，则抛出异常信息
        conf.setMaxWaitMillis(30);
        conf.setTestOnBorrow(true);
        conf.setTestOnReturn(true);
        pool=new JedisPool(conf,"127.0.0.1",6379);
    }

    /**
     * 由于这里仅仅做测试，这里仅仅对总金额做设置
     * @param input
     */
    @Override
    public void execute(Tuple input) {
        Jedis jedis=pool.getResource();
        String string=input.getValue(0).toString();
//        System.out.println(string);
        OrderInfo orderInfo=new Gson().fromJson(string,OrderInfo.class);
        //整个网站，各个业务线，各个品类，各个店铺，各个品牌，每个商品
        //获取整个网站的金额统计指标
//        String totalAmount=jedis.get("totalAmount");
        jedis.incrBy("totalAmount",orderInfo.getPayPrice());
        System.out.println("总金额+："+orderInfo.getPayPrice());
        //获取商品所属业务的指标信息
//        String bid=jedis.get(bid+"Amout")
//        jedis.incrBy(bid+"Amout",orderInfo.getProductPrice());
        jedis.close();
    }
    private void   mapinit(){
        map.put("b","3c");
        map.put("c","phone");
        map.put("s","121");
        map.put("p","iphone");
    }
private String getBubyProductId(String productId,String type){
    //key:value
    //index:productID:info-->Map
    //productId----<各个业务线，各个品类，各个店铺，各个品牌，每个商品>


    return map.get(type);
}
    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }
}
