package com.by.blcu.core.controller;

import com.by.blcu.core.authentication.JWTToken;
import com.by.blcu.core.authentication.JWTUtil;
import com.by.blcu.core.constant.ProjectConstant;
import com.by.blcu.core.domain.Response;
import com.by.blcu.core.domain.User;
import com.by.blcu.core.properties.ShiroProperties;
import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.*;
import com.by.blcu.mall.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
public class LoginController {

    @Resource
    private RedisService redisService;
    @Resource
    private ShiroProperties shiroProperties;

    @PostMapping("/login")
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
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(SpringContextUtil.getBean(ShiroProperties.class).getJwtTimeOut());
        String expireTimeStr = DateUtils.formatFullTime(expireTime);
        JWTToken jwtToken = new JWTToken(token, expireTimeStr);

        //2.添加用户登录记录，更新用户登录时间（database + redis）
        this.saveTokenToRedis(token, request);

        //4.封装返回前端信息
        Map<String, Object> userInfo = this.generateUserInfo(jwtToken, user);
        return new RetResult().setCode(RetCode.SUCCESS).setMsg("认证成功").setData(userInfo);
    }

    /**
     * 存储token到redis
     * 失效时间与jwt配置时间一致
     */
    private void saveTokenToRedis(String token, HttpServletRequest request){
        String key = ProjectConstant.TOKEN_CACHE_PREFIX + token + "." + IPUtil.getIpAddr(request);
        Long expireTime = shiroProperties.getJwtTimeOut();
        this.redisService.setWithExpire(key, token, expireTime);
    }

    /**
     * 生成前端需要的用户信息，包括：
     * 1. token
     * 2. user
     *
     * @param token token
     * @param user  用户信息
     * @return UserInfo
     */
    private Map<String, Object> generateUserInfo(JWTToken token, User user) {
        String username = user.getUsername();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("token", token.getToken());
        userInfo.put("exipreTime", token.getExipreAt());

        user.setPassword("Secret");
        userInfo.put("user", user);
        return userInfo;
    }
}
