package com.by.blcu.core.authentication;

import com.by.blcu.core.constant.ProjectConstant;
import com.by.blcu.core.domain.User;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.IPUtil;
import com.by.blcu.core.utils.SystemUtils;
import com.by.blcu.mall.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * shiro
 * 包含认证，授权
 *
 */
public class ShiroRealm extends AuthorizingRealm {

    @Resource
    RedisService redisService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }


    /**
     * 用户授权
     *
     * 获取用户角色和权限
     * @param token
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection token) {
        String username = JWTUtil.getUsername(token.toString());

//临时方法（根据用户信息获取）可移除，直接根据 username 获取权限资源
        User user = SystemUtils.getUser(username);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

// 临时方法（模拟获取用户角色集合）
        simpleAuthorizationInfo.setRoles(user.getRole());
        // 待开发  Set<String> roleSet = userManager.getUserRoles(username);

// 临时方法（模拟获取用户权限集合）
        simpleAuthorizationInfo.setStringPermissions(user.getPermission());
        // 待开发 Set<String> permissionSet = userManager.getUserPermissions(username);

        return simpleAuthorizationInfo;
    }

    /**
     * 用户认证
     *
     * @param authenticationToken 身份认证 token
     * @return AuthenticationInfo 身份认证信息
     * @throws AuthenticationException 认证相关异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.JWTFilter 中获取已解密的 token
        String token = (String) authenticationToken.getCredentials();

        //2.获取 ip 准备查询 redis
        HttpServletRequest request = IPUtil.getHttpServletRequest();
        String ip = IPUtil.getIpAddr(request);
        String encryptToken = ApplicationUtils.encryptToken(token);

        String key = ProjectConstant.TOKEN_CACHE_PREFIX + encryptToken + "." + ip;
        String tokenInRedis = null;
        try {
            tokenInRedis = this.redisService.get(key);
        }catch (Exception e){}

        //3.判定 token 失效情况
        if(StringUtils.isBlank(tokenInRedis))
            throw new AuthenticationException("token已过期");

        String username = JWTUtil.getUsername(token);
        if (StringUtils.isBlank(username))
            throw new AuthenticationException("token校验不通过");

        //4.通过用户名查询用户信息
        User user = SystemUtils.getUser(username);

        if (user == null)
            throw new AuthenticationException("用户名或密码错误");
        if (!JWTUtil.verify(token, username, user.getPassword()))
            throw new AuthenticationException("token校验不通过");
        return new SimpleAuthenticationInfo(token, token, "shiro_realm");
    }
}
