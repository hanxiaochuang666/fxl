package com.by.blcu.core.baseservice.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

//base redis script service .
@Service
@Slf4j
public abstract class BaseRedisScriptService {

    @Resource
    private RedisTemplate<String, ?> redisTemplate;

    //call to init all related scripts.
    public abstract void initScript();


    public Long runScript(DefaultRedisScript<Long> script, List<String> keyList, Map<String,Object> argvMap) {

        Long result = redisTemplate.execute(script, keyList, (Object) argvMap);
        System.out.println(result);
        return result;
    }

    public List runScript1(DefaultRedisScript<List> script, List<String> keyList, Map<String,Object> argvMap) {

        List result = redisTemplate.execute(script, keyList, (Object) argvMap);
        System.out.println(result);
        return result;
    }
}

