package cn.riversky.biwu;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/13.
 */
public class Counter {
    /**
     * 计算武林大会三个擂台的比武次数
     * @param args
     */
    public static void main(String[] args) {
        ExecutorService executorService= Executors.newFixedThreadPool(10);
        executorService.submit(new Arena("biwu","笑傲江湖"));
        executorService.submit(new Arena("biwu","神雕侠侣"));
        executorService.submit(new Arena("biwu","一天屠龙"));
        executorService.submit(new Baomu("biwu"));
    }
}
