package cn.riversky.map;

import cn.riversky.logEmail.utils.JedisUtils;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 购物车
 * 使用String存放商品信息 <shop:product:id,productjson>
 * 使用map 存放用户的购物车 <shop:cart:userid,map<shop:product:id,num>>
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/14.
 */
public class Cart {
    private Jedis jedis= JedisUtils.getJedis();

    /**
     * 修改购物车中的商品
     * @param username 用户名
     * @param productId 商品编号
     * @param num 操作商品的数量
     */
    public void updateProduct2Cart(String username,String productId,int num){
        if(jedis.hexists("shop:cart:"+username,productId)){
            jedis.hset("shop:cart:"+username,productId,"0");
        }
        jedis.hincrBy("shop:cart:"+username,productId,num);
    }

    public static void main(String[] args) {
        //初始化商品信息
        initData();
        //创建购物车
        Cart cart=new Cart();
        //创建用户
        String username="liudehua";
        //往用户购物车中添加商品
        cart.updateProduct2Cart(username,"1223234",10);
        cart.updateProduct2Cart(username,"12232sfd34",103);
        cart.updateProduct2Cart(username,"1223sdffds234",120);
        //打印当前用户购物车
        List<Product> products=cart.getProductsByUserName(username);
        for (Product product:products){
            System.out.println(product);
        }
    }

    private static void initData() {
        Jedis jedis=JedisUtils.getJedis();
        System.out.println("初始化信息---------");
        Product product= new Product("1223234","马六甲，同款帽子",new BigDecimal("124"));
        Product product1= new Product("12232sfd34","周杰伦，同款二级",new BigDecimal("40"));
        Product product2= new Product("1223sdffds234","坦克玩具",new BigDecimal("230"));
        //采用String方式存储对象
        jedis.set("shop:product:"+product.getId(),new Gson().toJson(product));
        jedis.set("shop:product:"+product1.getId(),new Gson().toJson(product1));
        jedis.set("shop:product:"+product2.getId(),new Gson().toJson(product2));
        //打印所有产品
        Set<String> allProductKesy=jedis.keys("shop:product:*");
        for(String key:allProductKesy){
            System.out.println(new Gson().fromJson(jedis.get(key),Product.class));
        }
        System.out.println("购物车初始化完毕============================");
    }

    private List<Product> getProductsByUserName(String username) {
        List<Product> products=new ArrayList<>();
        Map<String,String> productMap=jedis.hgetAll("shop:cart:"+username);
        if(productMap==null||productMap.size()==0){
            return products;
        }
        for(Map.Entry entry:productMap.entrySet()){
            Product product=new Product();
            String id= (String) entry.getKey();
            product.setId(id);
            int num=Integer.parseInt((String) entry.getValue());
            product.setNum(num>0?num:0);
            String shopid="shop:product:"+id;

            Product temp=new Gson().fromJson(jedis.get(shopid),Product.class);
            product.setPrice(temp.getPrice());
            product.setTitle(temp.getTitle());
            products.add(product);
        }
        return products;
    }
}
