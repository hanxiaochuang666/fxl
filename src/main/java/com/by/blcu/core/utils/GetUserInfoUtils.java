package com.by.blcu.core.utils;

import com.auth0.jwt.interfaces.Claim;
import com.by.blcu.core.authentication.JWTFilter;
import com.by.blcu.core.authentication.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class GetUserInfoUtils {
    private GetUserInfoUtils() {
    }

    public static int getUserIdByRequest(HttpServletRequest httpServletRequest) {
        //从 header 中获取 token
        String token = httpServletRequest.getHeader(JWTFilter.TOKEN);
        if ("null".equals(token) || StringUtils.isEmpty(token)) {
            return -1;
        }
        Map<String, Claim> userMap = JWTUtil.getUserInfo(ApplicationUtils.decryptToken(token));
        if (userMap == null || (userMap != null && !userMap.containsKey("userId"))) {
            return -1;
        }
        Integer userId = userMap.get("userId").asInt();
        return userId;
    }

    public static String getUserNameByToken(HttpServletRequest httpServletRequest) {
        //从 header 中获取 token
        String token = httpServletRequest.getHeader(JWTFilter.TOKEN);
        if ("null".equals(token) || StringUtils.isEmpty(token)) {
            return "";
        }
        //根据 token 获取 username
        Map<String, Claim> userMap = JWTUtil.getUserInfo(ApplicationUtils.decryptToken(token));
        if (userMap == null || (userMap != null && !userMap.containsKey("username"))) {
            return "";
        }
        return userMap.get("username").asString();
    }

    public static String getOrgCodeByToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(JWTFilter.TOKEN);
        //判定token是否有效
        if ("null".equals(token) || StringUtils.isBlank(token)) {
            return "eblcu";
        }
        Map<String, Claim> userMap = JWTUtil.getUserInfo(ApplicationUtils.decryptToken(token));
        if (userMap == null || (userMap != null && !userMap.containsKey("orgCode"))) {
           return "eblcu";
        }
        return userMap.get("orgCode").asString();
    }

    public static String getOrgTypeByToken(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(JWTFilter.TOKEN);
        //判定token是否有效
        if ("null".equals(token) || StringUtils.isBlank(token)) {
            return "1";
        }
        Map<String, Claim> userMap = JWTUtil.getUserInfo(ApplicationUtils.decryptToken(token));
        if (userMap == null || (userMap != null && !userMap.containsKey("type"))) {
            return "1";
        }
        return userMap.get("type").asString();
    }
}
