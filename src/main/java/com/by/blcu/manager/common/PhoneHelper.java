package com.by.blcu.manager.common;

import com.by.blcu.core.utils.PhoneUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

/**
 * 发送短信
 */
@Service
public class PhoneHelper {
    private final Logger logger = LoggerFactory.getLogger(PhoneHelper.class);
    @Resource
    private PhoneEums phoneEums;
    @Resource
    private RedisHelper redisHelper;

    /**
     * 发送短信
     * @param phone 手机号
     * @param messgae 内容
     * @return
     */
    @Async
    public boolean Send(String phone,String messgae){
        //易信通短信发送
        return  phoneEums.sendMessage(phone,messgae);
        //return true;
    }

    /**
     *  发送短信验证码
     * @param phone 手机号
     * @param type 类型: 1注册，2登录，3找回密码
     * @return
     */
    public boolean SendVCode(String phone,Integer type){
        //验证码模板
        String sendTemplete = "你好，北语网院非学历平台验证码：%s， 20分钟内有效，使用后失效。";
        if(type==1){
            //注册
            String code = redisHelper.getPhoneRegist(phone);
            logger.info("发送前，查找手机注册密码的验证码：{redis:"+code+"}");
            if(StringHelper.IsNullOrWhiteSpace(code)){
                code = StringHelper.generateCode(4);
            }
            else{
                logger.info("已发过了，需20分钟后才能再发");
                return true;
            }
            redisHelper.setPhoneRegist(phone,code);
            logger.info("已发送手机注册的验证码："+code);
            String message =String.format(sendTemplete,code);
            return Send(phone,message);
        }
        else if(type==2){
            //登录
            String code = redisHelper.getPhoneLogin(phone);
            logger.info("发送前，查找手机登录密码的验证码：{redis:"+code+"}");
            if(StringHelper.IsNullOrWhiteSpace(code)){
                code = StringHelper.generateCode(4);
            }else{
                logger.info("已发过了，需20分钟后才能再发");
                return true;
            }
            redisHelper.setPhoneLogin(phone,code);
            logger.info("已发送手机登录的验证码："+code);
            String message =String.format(sendTemplete,code);
            return Send(phone,message);
        }
        else if(type==3){
            //找回密码
            String code = redisHelper.getPhoneFindPassword(phone);
            logger.info("发送前，查找手机找回密码的验证码：{redis:"+code+"}");
            if(StringHelper.IsNullOrWhiteSpace(code)){
                code = StringHelper.generateCode(4);
            }else{
                logger.info("已发过了，需20分钟后才能再发");
                return true;
            }
            redisHelper.setPhoneFindPassword(phone,code);
            logger.info("已发送手机找回密码的验证码："+code);
            String message =String.format(sendTemplete,code);
            return Send(phone,message);
        }
        else if(type==4){
            //绑定
            String code = redisHelper.getPhoneBind(phone);
            logger.info("发送前，查找手机绑定的验证码：{redis:"+code+"}");
            if(StringHelper.IsNullOrWhiteSpace(code)){
                code = StringHelper.generateCode(4);
            }else{
                logger.info("已发过了，需20分钟后才能再发");
                return true;
            }
            redisHelper.setPhoneBind(phone,code);
            logger.info("已发送手机绑定的验证码："+code);
            String message =String.format(sendTemplete,code);
            return Send(phone,message);
        }
        else{
            return false;
        }
    }

    //验证短信验证码
    public boolean VerifyVCode(String phone,Integer type,String message){
       if(type==1){
           //注册
           String code = redisHelper.getPhoneRegist(phone);
           logger.info("验证手机注册的验证码：{redis:"+code+",code:"+message+"}");
           if(StringHelper.isEmpty(message) || StringHelper.isEmpty(code)){
               return false;
           }
           if(message.equals(code)){
               redisHelper.setUserValidate(phone,UserValidateEnum.PhoneRegist,true);
               redisHelper.cleanPhoneRegist(phone);
               return true;
           }
       }
       else if(type ==2){
            //登录
           String code = redisHelper.getPhoneLogin(phone);
           logger.info("验证手机登录的验证码：{redis:"+code+",code:"+message+"}");
           if(StringHelper.isEmpty(message) || StringHelper.isEmpty(code)){
               return false;
           }
           if(message.equals(code)){
               redisHelper.setUserValidate(phone,UserValidateEnum.PhoneLogin,true);
               redisHelper.cleanPhoneLogin(phone);
               return true;
           }
       }
       else if(type==3){
            //找回密码
           String code = redisHelper.getPhoneFindPassword(phone);
           logger.info("验证手机找回密码的验证码：{redis:"+code+",code:"+message+"}");
           if(StringHelper.isEmpty(message) || StringHelper.isEmpty(code)){
               return false;
           }
           if(message.equals(code)){
               redisHelper.setUserValidate(phone,UserValidateEnum.PhoneFindPassword,true);
               redisHelper.cleanPhoneFindPassword(phone);
               return true;
           }
       }
       else if(type==4){
           //找回密码
           String code = redisHelper.getPhoneBind(phone);
           logger.info("验证手机注册的验证码：{redis:"+code+",code:"+message+"}");
           if(StringHelper.isEmpty(message) || StringHelper.isEmpty(code)){
               return false;
           }
           if(message.equals(code)){
               redisHelper.setUserValidate(phone,UserValidateEnum.PhoneBind,true);
               redisHelper.cleanPhoneBind(phone);
               return true;
           }
       }
       return false;
    }
    //验证短信验证码 第二阶段
    public boolean VerifyVCode(String phone,Integer type){
        if(type==1){
            boolean validate = redisHelper.getUserValidate(phone,UserValidateEnum.PhoneRegist);
            logger.info("验证手机注册的验证码是否验证过：{redis:"+ validate +"}");
            if(validate){
                redisHelper.setUserValidate(phone,UserValidateEnum.PhoneRegist,false);
                return true;
            }
        }
        else if(type ==2){
            boolean validate = redisHelper.getUserValidate(phone,UserValidateEnum.PhoneLogin);
            logger.info("验证手机注册的验证码是否验证过：{redis:"+ validate +"}");
            if(validate){
                redisHelper.setUserValidate(phone,UserValidateEnum.PhoneLogin,false);
                return true;
            }
        }
        else if(type==3){
            boolean validate = redisHelper.getUserValidate(phone,UserValidateEnum.PhoneFindPassword);
            logger.info("验证手机注册的验证码是否验证过：{redis:"+ validate +"}");
            if(validate){
                redisHelper.setUserValidate(phone,UserValidateEnum.PhoneFindPassword,false);
                return true;
            }
        }
        else if(type==4){
            boolean validate = redisHelper.getUserValidate(phone,UserValidateEnum.PhoneBind);
            logger.info("验证手机绑定的验证码是否验证过：{redis:"+ validate +"}");
            if(validate){
                redisHelper.setUserValidate(phone,UserValidateEnum.PhoneBind,false);
                return true;
            }
        }
        return false;
    }

}
