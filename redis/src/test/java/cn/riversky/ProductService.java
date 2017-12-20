package cn.riversky;

import cn.riversky.saveProduct.Person;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.*;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/14.
 */
public class ProductService {
    Jedis jedis=new Jedis("datanode1",6379);
    @Before
    public void init(){
        jedis.auth("riversky");
    }
    @Test
    public void saveProduct2Redis() throws IOException, ClassNotFoundException {
        Person person=new Person("刘德华",12);
        /**
         * 保存json
         */
        jedis.set("user:liudehua:json", new Gson().toJson(person));
        System.out.println(jedis.get("user:liudehua:json"));
        //保存字符串
        jedis.set("user:liudehua:str", person.toString());
        System.out.println(jedis.get("user:liudehua:str"));
        //保存序列化文件
        jedis.set("user:liudehua:obj".getBytes(),getBytesByProduct(person));
        byte[]productBytes=jedis.get("user:liudehua:obj".getBytes());
        Person pByte=getProductByBytes(productBytes);
        System.out.println(pByte.getName()+" "+pByte.getAge());
    }

    /**
     * 对象的反序列化
     * @param productBytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private Person getProductByBytes(byte[] productBytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis=new ByteArrayInputStream(productBytes);
        ObjectInputStream ois=new ObjectInputStream(bis);
        Person person= (Person) ois.readObject();
        return person;
    }

    /**
     * 对象的序列化
     * @param person
     * @return
     * @throws IOException
     */
    private byte[] getBytesByProduct(Person person) throws IOException {
        ByteArrayOutputStream ba=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(ba);
        oos.writeObject(person);
        oos.flush();
        return ba.toByteArray();
    }
}
