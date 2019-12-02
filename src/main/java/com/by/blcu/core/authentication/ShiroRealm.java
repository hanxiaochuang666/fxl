package com.by.blcu.core.authentication;

import com.by.blcu.core.constant.RedisBusinessKeyConst;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.IPUtil;
import com.by.blcu.mall.service.RedisService;
import com.by.blcu.manager.common.ManagerHelper;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.model.extend.PermissionOrgExtend;
import com.by.blcu.manager.model.extend.RoleOrgExtend;
import com.by.blcu.manager.service.SsoUserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * shiro
 * 包含认证，授权
 *
 */
public class ShiroRealm extends AuthorizingRealm {

    private static Logger log = LoggerFactory.getLogger(ShiroRealm.class);
    @Resource
    RedisService redisService;
    @Resource
    SsoUserService ssoUserService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    public  boolean isPermitted(PrincipalCollection principals, String permission){
        String username = JWTUtil.getUserInfo(principals.toString()).get("username").asString();
        boolean isAdmin=false;
        if(ManagerHelper.isEnable && ManagerHelper.UserName.equals(username)){
            isAdmin=true;
        }
        return isAdmin||super.isPermitted(principals,permission);
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
        String username = JWTUtil.getUserInfo(token.toString()).get("username").asString();
        Set<String> roleSet = new HashSet<>();
        Set<String> permissionSet = new HashSet<>();

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

         //获取角色集合
        Set<RoleOrgExtend> userRolesInter = ssoUserService.getUserRolesInter(username);
        if(userRolesInter != null){
            userRolesInter.forEach(roleOrgs -> {
                if(roleOrgs.getRoleList() != null)
                    roleOrgs.getRoleList().forEach(role -> roleSet.add(role));
            });
        }
        simpleAuthorizationInfo.setRoles(roleSet);

        //获取权限集合
        Set<PermissionOrgExtend> userPermissionsInter = ssoUserService.getUserPermissionsInter(username);
        if(userPermissionsInter != null){
            userPermissionsInter.forEach(permissionOrgs -> {
                if(permissionOrgs.getPermissionList() != null)
                    permissionOrgs.getPermissionList().forEach(permission -> permissionSet.add(permission));
            });
        }
        simpleAuthorizationInfo.setStringPermissions(permissionSet);

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

        String key = RedisBusinessKeyConst.Authentication.TOKEN_CACHE_PREFIX + encryptToken + "." + ip;
        String tokenInRedis = null;
        try {
            tokenInRedis = this.redisService.get(key);
        }catch (Exception e){}

        //3.判定 token 失效情况
        if(StringUtils.isBlank(tokenInRedis))
            throw new AuthenticationException("token已过期");

        String username = JWTUtil.getUserInfo(token.toString()).get("username").asString();
        if (StringUtils.isBlank(username))
            throw new AuthenticationException("token校验不通过");

        //4.通过用户名查询用户信息
        SsoUser user=null;
        if(username.equals(ManagerHelper.UserName)){
            user = ManagerHelper.ssoUser;
            if (user == null)
                throw new AuthenticationException("用户名或密码错误");
            if (!JWTUtil.verify(token, username, ManagerHelper.PasswordSecret))
                throw new AuthenticationException("token校验不通过");
        }
        else{
            user = ssoUserService.getUserByUserNameInter(username);
            if (user == null)
                throw new AuthenticationException("用户名或密码错误");

            if (!JWTUtil.verify(token, username, user.getPassword()))
                throw new AuthenticationException("token校验不通过");
        }

        return new SimpleAuthenticationInfo(token, token, "shiro_realm");
    }
}
