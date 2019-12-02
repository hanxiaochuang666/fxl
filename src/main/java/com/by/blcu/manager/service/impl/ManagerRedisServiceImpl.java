package com.by.blcu.manager.service.impl;

import com.by.blcu.manager.service.ManagerRedisService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class ManagerRedisServiceImpl implements ManagerRedisService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void setWithExpire(String key, String value, long expire) {
        stringRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    @Override
    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

    @Override
    public Boolean zAdd(String key, String value, double score) {
        return stringRedisTemplate.opsForZSet().add(key, value, score);
    }

    @Override
    public Set<String> zRange(String key, long min, long max) {
        return stringRedisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    @Override
    public Long zRemove(String key, Object... values) {
        return stringRedisTemplate.opsForZSet().remove(key, values);
    }



}
