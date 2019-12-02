package com.by.blcu.core.baseservice.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

//use for sensitivewords filter
@Service
@Slf4j
public class SensitiveRedisScriptService extends BaseRedisScriptService {

    public static final String SENSITIVE_FILTER = "script/filter.lua";
    private DefaultRedisScript<Long> scriptCheckSensitiveWords;

    public static final String GET_SENSITIVE_FILTER = "script/getSensWords.lua";
    private DefaultRedisScript<List> scriptGetSenstiveWords;

    @Override
    public void initScript() {
        scriptCheckSensitiveWords = new DefaultRedisScript<Long>();
        scriptCheckSensitiveWords.setResultType(Long.class);
        scriptCheckSensitiveWords.setScriptSource(new ResourceScriptSource(new ClassPathResource(SENSITIVE_FILTER)));

        scriptGetSenstiveWords = new DefaultRedisScript<List>();
        scriptGetSenstiveWords.setResultType(List.class);
        scriptGetSenstiveWords.setScriptSource(new ResourceScriptSource(new ClassPathResource(GET_SENSITIVE_FILTER)));
    }


    @PostConstruct
    public void init(){
        initScript();
    }

    public Long checkSensitve(List<String> keyList, Map<String,Object> argvMap) {

       Long result = super.runScript(scriptCheckSensitiveWords, keyList, argvMap);
        return result;
    }

    public List<String> getSensitive(List<String> keyList, Map<String,Object> argvMap) {

        List<String> result = super.runScript1(scriptGetSenstiveWords, keyList, argvMap);
        return result;
    }

}

