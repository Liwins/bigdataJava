package cn.riversky.logAnalyze.app.dao;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存数据；缓存上一分钟的数据，用来做cache
 * 整点的时候，cache层中的数据，需要清理掉
 * @author riversky E-mail:riversky@126.com
 * @version 创建时间 ： 2017/12/18.
 */
public class CacheData {
    private static Map<String, Integer> pvMap = new HashMap<>();
    private static Map<String, Long> uvMap = new HashMap<>();

    /**
     * 根据pv和indexName更新当前内存缓存中的数据
     * @param pv
     * @param indexName
     * @return
     */
    public static int getPv(int pv,String indexName){
        Integer cacheVale=pvMap.get(indexName);
        if(cacheVale==null){
            cacheVale=0;
            pvMap.put(indexName,cacheVale);
        }
        if(pv>cacheVale){
            pvMap.put(indexName,pv);
            return pv-cacheVale.intValue();
        }
        return 0;
    }

    /**
     * 获取更新后的
     * @param uv
     * @param indexName
     * @return
     */
    public static long getUv(long uv,String indexName){
        Long cacheValue=uvMap.get(indexName);
        if(cacheValue==null){
            cacheValue=0L;
            uvMap.put(indexName,cacheValue);
        }
        //返回增量
        if(uv>uvMap.get(indexName)){
            uvMap.put(indexName,uv);
            return uv-cacheValue;
        }
        //如果新值小于旧值，直接返回0
        return 0;
    }
    public static Map<String, Integer> getPvMap() {
        return pvMap;
    }

    public static void setPvMap(Map<String, Integer> pvMap) {
        CacheData.pvMap = pvMap;
    }

    public static Map<String, Long> getUvMap() {
        return uvMap;
    }

    public static void setUvMap(Map<String, Long> uvMap) {
        CacheData.uvMap = uvMap;
    }
}
