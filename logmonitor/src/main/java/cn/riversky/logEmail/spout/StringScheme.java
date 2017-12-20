package cn.riversky.logEmail.spout;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.List;

/**
 * 实现Strom的Scheme，可以实现对bytes数据转换或者其他处理的作用
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/15.
 */
public class StringScheme implements Scheme {
    @Override
    public List<Object> deserialize(byte[] bytes) {
        try {

            return new Values(new String(bytes));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public Fields getOutputFields() {
        return new Fields("line");
    }
}
