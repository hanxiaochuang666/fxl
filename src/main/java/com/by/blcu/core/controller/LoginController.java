package com.by.blcu.core.controller;

import com.alibaba.fastjson.JSON;
import com.by.blcu.core.authentication.JWTToken;
import com.by.blcu.core.authentication.JWTUtil;
import com.by.blcu.core.constant.ProjectConstant;
import com.by.blcu.core.constant.RedisBusinessKeyConst;
import com.by.blcu.core.domain.Response;
import com.by.blcu.core.domain.ActiveUser;
import com.by.blcu.core.domain.User;
import com.by.blcu.core.configurer.ShiroConfigurer;
import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.*;
import com.by.blcu.mall.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Validated
@RestController
public class LoginController {

    @Resource
    private RedisService redisService;
    @Resource
    private ShiroConfigurer shiroProperties;

    /*@PostMapping("/login")
    public RetResult login(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password, HttpServletRequest request) throws Exception {
        username = StringUtils.lowerCase(username);
        password = MD5Util.encrypt(username, password);

        final String errorMessage = "用户名或密码错误";
        User user = SystemUtils.getUser(username);

        //1.需要自定义错误用户前台回显（用户应有锁定状态的判定）
        if (user == null)
            throw new Exception(errorMessage);
        if (!StringUtils.equals(user.getPassword(), password))
            throw new Exception(errorMessage);

        //2.生成 Token
        String token = ApplicationUtils.encryptToken(JWTUtil.sign(username, password));
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(SpringContextUtil.getBean(ShiroConfigurer.class).getJwtTimeOut());
        String expireTimeStr = DateUtils.formatFullTime(expireTime);
        JWTToken jwtToken = new JWTToken(token, expireTimeStr);

        //3.添加用户登录记录，更新用户登录时间（database + redis）
        String userId = this.saveTokenToRedis(user, jwtToken, request);
        user.setUserId(userId);//临时ID，用于登出

        //4.封装返回前端信息
        Map<String, Object> userInfo = this.generateUserInfo(jwtToken, user);
        return new RetResult().setCode(RetCode.SUCCESS).setMsg("认证成功").setData(userInfo);
    }

    @GetMapping("/logout/{id}")
    public void logout(@NotBlank(message = "{required}") @PathVariable String id) throws Exception{
        Long nowTime = Long.valueOf(DateUtils.formatFullTime(LocalDateTime.now()));

        //1.首先获取登录集合
        Set<String> userOnlineSet = redisService.zRange(RedisBusinessKeyConst.Authentication.TOKEN_ACTIVE_USER, nowTime, Long.MAX_VALUE);
        //2.查找id定位在线用户
        ActiveUser logoutUser = null;
        String logoutUserString = "";
        for(String userOnline : userOnlineSet){
            ActiveUser user = JSON.parseObject(userOnline, ActiveUser.class);
            if(id.equals(user.getId())){
                logoutUser = user;
                logoutUserString = userOnline;
            }
        }
        //3.在线列表移除用户 移除关联 Token 信息
        if(logoutUser != null && StringUtils.isNotBlank(logoutUserString)){
            redisService.zRemove(RedisBusinessKeyConst.Authentication.TOKEN_ACTIVE_USER, logoutUserString);
            redisService.delete(RedisBusinessKeyConst.Authentication.TOKEN_CACHE_PREFIX + logoutUser.getToken() + "." + logoutUser.getIp());
        }
    }

    *//**
     * 存储token到redis
     * 失效时间与jwt配置时间一致
     */
    private void saveTokenToRedis(String token, HttpServletRequest request){
        String key = RedisBusinessKeyConst.Authentication.TOKEN_CACHE_PREFIX + token + "." + IPUtil.getIpAddr(request);
/*
    private String saveTokenToRedis(User user, JWTToken token, HttpServletRequest request){
        String ip = IPUtil.getIpAddr(request);

        //1.构建在线用户
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUsername(user.getUsername());
        activeUser.setIp(ip);
        activeUser.setToken(token.getToken());

        //2.存放 在线用户（组，用户信息，过期时间）
        this.redisService.zAdd(RedisBusinessKeyConst.Authentication.TOKEN_ACTIVE_USER, JSON.toJSONString(activeUser)
                                , Double.valueOf(token.getExipreAt()));

        //3.存放 Token
        String key = RedisBusinessKeyConst.Authentication.TOKEN_CACHE_PREFIX + token.getToken() + "." + ip;

        Long expireTime = shiroProperties.getJwtTimeOut();
        this.redisService.setWithExpire(key, token.getToken(), expireTime);

        return activeUser.getId();*/
    }

    /**
     * 生成前端需要的用户信息，包括：
     * 1. token
     * 2. user
     *
     * @param token token
     * @param user  用户信息
     * @return UserInfo
     *//*
    private Map<String, Object> generateUserInfo(JWTToken token, User user) {
        String username = user.getUsername();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("token", token.getToken());
        userInfo.put("exipreTime", token.getExipreAt());

        user.setPassword("Secret");
        userInfo.put("user", user);
        return userInfo;
    }*/
}
