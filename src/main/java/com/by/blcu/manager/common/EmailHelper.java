package com.by.blcu.manager.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.baseservice.captcode.CaptchaCodeService;
import com.by.blcu.core.baseservice.redis.BaseRedisService;
import org.mapstruct.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.*;

/**
 * 邮件发送
 */
@Service
public class EmailHelper {

    @Value("${mail.fromMail.sender}")
    private String sender;

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private RedisHelper redisHelper;

    private final Logger logger = LoggerFactory.getLogger(CaptchaCodeService.class);
    /**
     * 发送邮件
     * @param toEmail 要发送到的邮箱
     *  @param subject 主题
     * @param messgae 内容
     * @return
     */
    public boolean Send(String toEmail,String subject,String messgae){

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(messgae);
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
        }
        return true;
    }

    /**
     * 邮箱发送验证码
     * @param toEmail 邮箱
     * @param type 类型
     * @return
     */
    public boolean SendVCode(String toEmail,Integer type){
        String sendTemplete="你好，北语网院非学历平台验证码：[%s]， 20分钟内有效，使用后失效。";
        if(type==1){
            //注册
        }
        else if(type==2){
            //登录
        }
        else if(type==3){
            //找回密码
            //判断已存在，不再重新生成验证码
            String code = redisHelper.getEmailFindPassword(toEmail);
            logger.info("发送前，查找邮件找回密码的验证码：{redis:"+code+"}");
            if(StringHelper.IsNullOrWhiteSpace(code)){
                code = StringHelper.generateCode(4);
            }
            redisHelper.setEmailFindPassword(toEmail,code);
            String verifyMessage=String.format(sendTemplete,code);
            logger.info("已发送邮件找回密码的验证码："+verifyMessage);
            return Send(toEmail,"北语网院非学历综合平台验证码",verifyMessage);
        }
        else if(type==4){
            //绑定
            String code = redisHelper.getEmailBind(toEmail);
            logger.info("发送前，查找绑定邮箱的验证码：{redis:"+code+"}");
            if(StringHelper.IsNullOrWhiteSpace(code)){
                code = StringHelper.generateCode(4);
            }
            redisHelper.setEmailBind(toEmail,code);
            String verifyMessage=String.format(sendTemplete,code);
            logger.info("已发送绑定邮箱的验证码："+verifyMessage);
            return Send(toEmail,"北语网院非学历综合平台验证码",verifyMessage);
        }
        return false;
    }
    //验证邮件验证码
    public boolean VerifyVCode(String toEmail,Integer type,String messgae){
        if(type==1){
            //注册
        }
        else if(type==2){
            //登录
        }
        else if(type==3) {
            //找回密码
            String code = redisHelper.getEmailFindPassword(toEmail);
            logger.info("验证邮箱找回密码的验证码：{redis:"+code+",code:"+messgae+"}");
            if(StringHelper.isEmpty(messgae) || StringHelper.isEmpty(code)){
                return false;
            }
            if(messgae.equals(code)){
                redisHelper.setUserValidate(toEmail,UserValidateEnum.EmailFindPassword,true);
                redisHelper.cleanEmailFindPassword(toEmail);
                return true;
            }
        }
        else if(type==4){
            //绑定
            String code = redisHelper.getEmailBind(toEmail);
            logger.info("验证邮箱绑定的验证码：{redis:"+code+",code:"+messgae+"}");
            if(StringHelper.isEmpty(messgae) || StringHelper.isEmpty(code)){
                return false;
            }
            if(messgae.equals(code)){
                redisHelper.setUserValidate(toEmail,UserValidateEnum.EmailBind,true);
                redisHelper.cleanEmailBind(toEmail);
                return true;
            }
        }
        return false;
    }
    //验证邮件验证码 第二阶段
    public boolean VerifyVCode(String toEmail,Integer type){
        if(type==1){
            //注册
        }
        else if(type==2){
            //登录
        }
        else if(type==3) {
            //找回密码
            boolean validate = redisHelper.getUserValidate(toEmail,UserValidateEnum.EmailFindPassword);
            logger.info("验证邮箱找回密码的验证码是否验证过：{redis:"+ validate +"}");
            if(validate){
                redisHelper.setUserValidate(toEmail,UserValidateEnum.EmailFindPassword,false);
                return true;
            }
        }
        else if(type==4){
            //绑定
            boolean validate = redisHelper.getUserValidate(toEmail,UserValidateEnum.EmailBind);
            logger.info("验证邮箱绑定的验证码是否验证过：{redis:"+ validate +"}");
            if(validate){
                redisHelper.setUserValidate(toEmail,UserValidateEnum.EmailBind,false);
                return true;
            }
        }
//        String code = redisHelper.getEmailFindPassword(toEmail);
//        logger.info("验证邮件找回密码的验证码：{redis:"+ code +"}");
//        if(StringHelper.isEmpty(code)){
//            return false;
//        }
        return false;
    }
}
