package com.by.blcu.manager.common;

import com.by.blcu.core.utils.ApplicationUtils;
import javafx.application.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

//图片相关类
@Service
public class ImgHelper {
    private final Logger logger = LoggerFactory.getLogger(ImgHelper.class);
    @Resource
    private RedisHelper redisHelper;

    /**
     * 缓存图片验证码
     * @return
     */
    public String SendVCode(String code){
        String key = ApplicationUtils.getUUID();
        logger.info("生成图片的验证码：{key:"+key+",code:"+code+"}");
        redisHelper.setImgVCode(key,code);
        return key;
    }

    /**
     * 验证图片验证码
     * @param key
     * @param code
     * @return
     */
    public boolean VerifyVCode(String key,String code){
        if(StringHelper.IsNullOrWhiteSpace(code)){
            return false;
        }
        String codeRedis = redisHelper.getImgVCode(key);
        logger.info("验证图片的验证码：{redis:"+codeRedis+",code:"+code+"}");
        if(StringHelper.IsNullOrWhiteSpace(codeRedis)){
            return false;
        }
        if(code.toLowerCase().equals(codeRedis.toLowerCase())){
            redisHelper.setUserValidate(key,UserValidateEnum.ImgVCode,true);
            redisHelper.cleanImgVCode(code);
            return true;
        }

        return false;
    }

    /**
     * 验证图片验证码
     * @param phone
     * @return
     */
    public boolean VerifyVCode(String phone){
        boolean validate = redisHelper.getUserValidate(phone,UserValidateEnum.ImgVCode);
        logger.info("验证图片的验证码是否验证过：{redis:"+ validate +"}");
        if(validate){
            redisHelper.setUserValidate(phone,UserValidateEnum.ImgVCode,false);
            return true;
        }
        return false;
    }

}
