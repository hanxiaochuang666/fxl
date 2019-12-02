package com.by.blcu.core.dataSource;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DynamicDataSourceInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String serverName = request.getServerName();
        String[] split = serverName.split("\\.");
        if(split.length<=1)
            //使用默认的数据源
            DynamicDataSourceContextHolder.setContextHolder("dataSource");
        else {
            String s = split[1];
            //不存在时，使用默认的数据源
            if(!DynamicDataSourceContextHolder.isContain(s)){
                DynamicDataSourceContextHolder.setContextHolder("dataSource");
            }else {
                DynamicDataSourceContextHolder.setContextHolder(s);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        DynamicDataSourceContextHolder.clearContextHolder();
    }
}
