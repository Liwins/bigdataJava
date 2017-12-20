package cn.riversky.logAnalyze.storm.spout;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.List;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/19.
 */
public class StringScheme implements Scheme{

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
