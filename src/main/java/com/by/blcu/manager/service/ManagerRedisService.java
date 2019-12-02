package com.by.blcu.manager.service;

import java.util.Map;
import java.util.Set;

/**
 * Redis 工具类
 */
public interface ManagerRedisService {


    /**
     * 初始化 set 失效时间，保证后续可以更新，单位 s
     * @param key
     * @param value
     * @param expire
     */
    void setWithExpire(String key, String value, long expire);

    /**
     * 删除匹配所有键值对
     * @param key
     * @return
     */
    Boolean delete(String key);

    /**
     * 存放有序集合
     * @param key
     * @param value
     * @param score 排序号
     */
    Boolean zAdd(String key, String value, double score);

    /**
     * 获取集合元素 从小到大
     * @param key
     * @param min
     * @param max
     */
    Set<String> zRange(String key, long min, long max);

    /**
     * 删除特定元素
     */
    Long zRemove(String key, Object... values);

}
