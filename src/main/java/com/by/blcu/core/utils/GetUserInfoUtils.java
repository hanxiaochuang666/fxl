package com.by.blcu.core.utils;

import com.by.blcu.core.authentication.JWTFilter;
import com.by.blcu.core.authentication.JWTUtil;

import javax.servlet.http.HttpServletRequest;

public class GetUserInfoUtils {
    private GetUserInfoUtils(){}

    public static int getUserIdByRequest(HttpServletRequest httpServletRequest){
        //从 header 中获取 token
        String token = httpServletRequest.getHeader(JWTFilter.TOKEN);
        //根据 token 获取 username
        String username = JWTUtil.getUsername(ApplicationUtils.decryptToken(token));

        //需要一个根据 username 获取 Id 的方法，可以先查 redis
        String userId = "";

        if(StringUtils.isEmpty(userId))
            return -1;
        return Integer.valueOf(userId);
    }
}
