package com.by.blcu.mall.service;

import java.util.List;
import java.util.Set;

/**
 * @author 
 * @Description: redis常用方法
 * @date 2019
 */
public interface RedisService {

    /**
     * 设置给定 key 的值。如果 key 已经存储其他值， SET 就覆写旧值，且无视类型。
     * @param key
     * @param value
     * @return
     */
    boolean set(String key, String value);

    /**
     * 初始化 set 失效时间，保证后续可以更新，单位 s
     * @param key
     * @param value
     * @param expire
     */
    void setWithExpire(String key, String value, long expire);

    /**
     * 获取指定 key 的值。如果 key 不存在，返回 nil 。如果key 储存的值不是字符串类型，返回一个错误。
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 设置 key 的过期时间。key 过期后将不再可用。
     * @param key
     * @param expire
     * @return
     */
    boolean expire(String key, long expire);

    /**
     * 存集合
     * @param key
     * @param list
     * @param <T>
     * @return
     */
    <T> boolean setList(String key, List<T> list);

    /**
     * 取集合
     * @param key
     * @param clz
     * @param <T>
     * @return
     */
    <T> List<T> getList(String key, Class<T> clz);

    /**
     * 将一个或多个值插入到列表头部。 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
     * 当 key 存在但不是列表类型时，返回一个错误。
     * @param key
     * @param obj
     * @return
     */
    long lpush(String key, Object obj);

    /**
     * 将一个或多个值插入到列表的尾部(最右边)。
     * 如果列表不存在，一个空列表会被创建并执行 RPUSH 操作。 当列表存在但不是列表类型时，返回一个错误。
     * @param key
     * @param obj
     * @return
     */
    long rpush(String key, Object obj);

    /**
     * 移除并返回列表的第一个元素。
     * @param key
     * @return
     */
    String lpop(String key);

    /**
     * 删除已存在的键。不存在的 key 会被忽略。
     * @param key
     * @return
     */
    long del(final String key);


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

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    Boolean hasKey(String key);
}
