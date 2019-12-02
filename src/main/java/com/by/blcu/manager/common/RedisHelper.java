package com.by.blcu.manager.common;

import com.alibaba.fastjson.JSON;
import com.by.blcu.core.configurer.ShiroConfigurer;
import com.by.blcu.core.utils.RedisUtil;
import com.by.blcu.manager.model.ManagerAreas;
import com.by.blcu.manager.model.extend.PermissionOrgExtend;
import com.by.blcu.manager.model.extend.RoleOrgExtend;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis帮助类-Manager中使用
 */
@Service
public class RedisHelper {

    @Resource
    private RedisUtil redisService;

    /**
     * 验证码过期时间(秒)
     */
    public final Long ExpectTimeVCode =1200L;


    //region 验证码

    //region 手机验证码

    /**
     * 注册
     */
    public final String VCode_Phone_Regist="manager:web:vcode:phone.regist:";
    public void setPhoneRegist(String key,String value){
        redisService.setEx(VCode_Phone_Regist+key,value, ExpectTimeVCode, TimeUnit.SECONDS);
    }
    public String getPhoneRegist(String key){
        String keyValue="";
        keyValue = redisService.get(VCode_Phone_Regist+key);
        return keyValue;
    }
    public void cleanPhoneRegist(String key){
        redisService.delete(VCode_Phone_Regist+key);
    }
    public long ttlPhoneRegist(String key){
        return redisService.getExpire(VCode_Phone_Regist+key,TimeUnit.SECONDS);
    }

    /**
     * 登录
     */
    public final String VCode_Phone_Login="manager:web:vcode:phone.login:";
    public void setPhoneLogin(String key,String value){
        redisService.setEx(VCode_Phone_Login+key,value, ExpectTimeVCode, TimeUnit.SECONDS);
    }
    public String getPhoneLogin(String key){
        String keyValue="";
        keyValue = redisService.get(VCode_Phone_Login+key);
        return keyValue;
    }
    public void cleanPhoneLogin(String key){
        redisService.delete(VCode_Phone_Login+key);
    }
    public long ttlPhoneLogin(String key){
        return redisService.getExpire(VCode_Phone_Login+key,TimeUnit.SECONDS);
    }
    /**
     * 找回
     */
    public final String VCode_Phone_FindPassword="manager:web:vcode:phone.findpassword:";
    public void setPhoneFindPassword(String key,String value){
        redisService.setEx(VCode_Phone_FindPassword+key,value, ExpectTimeVCode, TimeUnit.SECONDS);
    }
    public String getPhoneFindPassword(String key){
        String keyValue="";
        keyValue = redisService.get(VCode_Phone_FindPassword+key);
        return keyValue;
    }
    public void cleanPhoneFindPassword(String key){
        redisService.delete(VCode_Phone_FindPassword+key);
    }
    public long ttlPhoneFindPassword(String key){
        return redisService.getExpire(VCode_Phone_FindPassword+key,TimeUnit.SECONDS);
    }
    /**
     * 绑定
     */
    public final String VCode_Phone_Bind="manager:web:vcode:phone.bind:";
    public void setPhoneBind(String key,String value){
        redisService.setEx(VCode_Phone_Bind+key,value, ExpectTimeVCode, TimeUnit.SECONDS);
    }
    public String getPhoneBind(String key){
        String keyValue="";
        keyValue = redisService.get(VCode_Phone_Bind+key);
        return keyValue;
    }
    public void cleanPhoneBind(String key){
        redisService.delete(VCode_Phone_Bind+key);
    }
    public long ttlPhoneBind(String key){
        return redisService.getExpire(VCode_Phone_Bind+key,TimeUnit.SECONDS);
    }
    //endregion

    //region 邮箱验证码

    /**
     * 注册
     */
    public final String VCode_Email_Regist="manager:web:vcode:email.regist:";
    public void setEmailRegist(String key,String value){
        redisService.setEx(VCode_Email_Regist+key,value, ExpectTimeVCode, TimeUnit.SECONDS);
    }
    public String getEmailRegist(String key){
        String keyValue="";
        keyValue = redisService.get(VCode_Email_Regist+key);
        return keyValue;
    }
    public void cleanEmailRegist(String key){
        redisService.delete(VCode_Email_Regist+key);
    }

    /**
     * 登录
     */
    public final String VCode_Email_Login="manager:web:vcode:email.login:";
    public void setEmailLogin(String key,String value){
        redisService.setEx(VCode_Email_Login+key,value, ExpectTimeVCode, TimeUnit.SECONDS);
    }
    public String getEmailLogin(String key){
        String keyValue="";
        keyValue = redisService.get(VCode_Email_Login+key);
        return keyValue;
    }
    public void cleanEmailLogin(String key){
        redisService.delete(VCode_Email_Login+key);
    }

    /**
     * 找回
     */
    public final String VCode_Email_FindPassword="manager:web:vcode:email.findpassword:";
    public void setEmailFindPassword(String key,String value){
        redisService.setEx(VCode_Email_FindPassword+key,value, ExpectTimeVCode, TimeUnit.SECONDS);
    }
    public String getEmailFindPassword(String key){
        String keyValue="";
        keyValue = redisService.get(VCode_Email_FindPassword+key);
        return keyValue;
    }
    public void cleanEmailFindPassword(String key){
        redisService.delete(VCode_Email_FindPassword+key);
    }
    public long ttlEmailFindPassword(String key){
         return redisService.getExpire(VCode_Email_FindPassword+key,TimeUnit.SECONDS);
    }

    /**
     * 绑定
     */
    public final String VCode_Email_Bind="manager:web:vcode:email.bind:";
    public void setEmailBind(String key,String value){
        redisService.setEx(VCode_Email_Bind+key,value, ExpectTimeVCode, TimeUnit.SECONDS);
    }
    public String getEmailBind(String key){
        String keyValue="";
        keyValue = redisService.get(VCode_Email_Bind+key);
        return keyValue;
    }
    public void cleanEmailBind(String key){
        redisService.delete(VCode_Email_Bind+key);
    }
    public long ttlEmailBind(String key){
        return redisService.getExpire(VCode_Email_Bind+key,TimeUnit.SECONDS);
    }


    //endregion

    //region 图片验证码

    /**
     * 图片验证码
     */
    public final String VCode_Img_Regist="manager:web:vcode:img:";
    public void setImgVCode(String key,String value){
        redisService.setEx(VCode_Img_Regist+key,value, ExpectTimeVCode, TimeUnit.SECONDS);
    }
    public String getImgVCode(String key){
        String keyValue="";
        keyValue = redisService.get(VCode_Img_Regist+key);
        return keyValue;
    }
    public void cleanImgVCode(String key){
        redisService.delete(VCode_Img_Regist+key);
    }

    //endregion

    //region 用户验证类

    /**
     * 用于用户验证，包括验证码等
     */
    public final String VCode_User_Validate="manager:web:validate:user:";
    public void setUserValidate(String key,UserValidateEnum userValidateEnum, boolean value){
        redisService.setBit(VCode_User_Validate+key,userValidateEnum.getIndex(),value);
        redisService.expire(VCode_User_Validate+key,ExpectTimeVCode,TimeUnit.SECONDS);
    }
    public boolean getUserValidate(String key,UserValidateEnum userValidateEnum){
        boolean keyValue=false;
        keyValue = redisService.getBit(VCode_User_Validate+key,userValidateEnum.getIndex());
        return keyValue;
    }
    public void cleanUserValidate(String key){
        redisService.delete(VCode_User_Validate+key);
    }

    //endregion

    //endregion

    //region 登录后用户相关

    /**
     * 登录后用户权限相关前缀
     */
    public final String  UserPermissions="manager:user:permission:";
    public void setUserPermission(String key, Set<PermissionOrgExtend> value){
        Long expireTime = new ShiroConfigurer().getJwtTimeOut();
        String redisValue = "";
        if(!StringHelper.IsNullOrEmpty(value)){
            redisValue=JSON.toJSONString(value);
        }
        redisService.setEx(UserPermissions+key,redisValue,expireTime, TimeUnit.SECONDS);
    }
    public Set<PermissionOrgExtend> getUserPermission(String key){
        String keyValue="";
        keyValue = redisService.get(UserPermissions+key);
        if(!StringHelper.IsNullOrWhiteSpace(keyValue)){
            try{
                Set<PermissionOrgExtend> model = new HashSet<>(JSON.parseArray(keyValue,PermissionOrgExtend.class));
                return model;
            }
            catch (Exception ex){
                return null;
            }
        }
        return null;
    }
    public void cleanUserPermission(String key){
        if(!StringHelper.IsNullOrWhiteSpace(key)){
            redisService.delete(key);
        }
        else{
            Set<String> keys = redisService.keys(UserPermissions+"*");
            redisService.delete(keys);
        }
    }

    /**
     * 登录后用户角色相关前缀
     */
    public final String  UserRoles="manager:user:role:";
    public void setUserRole(String key,Set<RoleOrgExtend> value){
        Long expireTime = new ShiroConfigurer().getJwtTimeOut();
        String redisValue = "";
        if(!StringHelper.IsNullOrEmpty(value)){
            redisValue= JSON.toJSONString(value);
        }
        redisService.setEx(UserRoles+key,redisValue,expireTime, TimeUnit.SECONDS);
    }
    public Set<RoleOrgExtend> getUserRole(String key){
        String keyValue="";
        keyValue = redisService.get(UserRoles+key);
        if(!StringHelper.IsNullOrWhiteSpace(keyValue)){
            try{
                Set<RoleOrgExtend> model = new HashSet<>(JSON.parseArray(keyValue,RoleOrgExtend.class));
                return model;
            }
            catch (Exception ex){
                return null;
            }
        }
        return null;
    }
    public void cleanUserRole(String key){
        if(!StringHelper.IsNullOrWhiteSpace(key)){
            redisService.delete(key);
        }
        else{
            Set<String> keys = redisService.keys(UserRoles+"*");
            redisService.delete(keys);
        }
    }

    /**
     * 登录后用户信息相关前缀
     */
    public final String  UserSession="manager:user:info:";
    public void setUserSession(String key,UserSessionHelper value){
        Long expireTime = new ShiroConfigurer().getJwtTimeOut();
        String redisValue = "";
        if(value!=null){
            redisValue= JSON.toJSONString(value);
        }
        redisService.setEx(UserSession+key,redisValue,expireTime, TimeUnit.SECONDS);
    }
    public UserSessionHelper getUserSession(String key){
        String keyValue="";
        keyValue = redisService.get(UserSession+key);
        if(!StringHelper.IsNullOrWhiteSpace(keyValue)){
            try{
                UserSessionHelper model = JSON.parseObject(keyValue,UserSessionHelper.class);
                return model;
            }
            catch (Exception ex){
                return null;
            }
        }
        return null;
    }
    public void cleanUserSession(String key){
        redisService.delete(UserSession+key);
    }


    //endregion

    //region 省市区

    /**
     * 省市区
     */
    public final String  AreaList="manager:web:area:";
    public void setAreaList(String key, List<ManagerAreas> value){
        Long expireTime = new ShiroConfigurer().getJwtTimeOut();
        String redisValue = "";
        if(!StringHelper.IsNullOrEmpty(value)){
            redisValue=JSON.toJSONString(value);
        }
        redisService.set(AreaList+key,redisValue);
    }
    public List<ManagerAreas> getAreaList(String key){
        String keyValue="";
        keyValue = redisService.get(AreaList+key);
        if(!StringHelper.IsNullOrWhiteSpace(keyValue)){
            try{
                List<ManagerAreas> model = new ArrayList<>(JSON.parseArray(keyValue,ManagerAreas.class));
                return model;
            }
            catch (Exception ex){
                return null;
            }
        }
        return null;
    }

    //endregion
}
