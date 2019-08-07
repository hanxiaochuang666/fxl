package com.by.blcu.core.configurer;

import com.by.blcu.core.utils.GetUserInfoUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@Aspect
@Slf4j
public class UserInfoConfigurer {
    @Pointcut("@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)")
    public void pointcut() {
    }
    @Around("pointcut() && (@within(com.by.blcu.core.aop.CheckToken) || @annotation(com.by.blcu.core.aop.CheckToken))")
    public Object simpleAop(final ProceedingJoinPoint joinPoint) throws Throwable {

        try {
            // 调用原有的方法
            Long befor = System.currentTimeMillis();
            Object o = joinPoint.proceed();
            Long after = System.currentTimeMillis();
            log.info("调用方法结束===================共耗时：" + (after - befor) + "毫秒");
            log.info("方法返回：return:====================" + o);
            return o;
        } catch (Throwable throwable) {
            throw throwable;
        }
    }

    @Before("@within(com.by.blcu.core.aop.CheckToken) || @annotation(com.by.blcu.core.aop.CheckToken)")
    public void doBefore(JoinPoint joinPoint) throws Exception {
        log.info("进入方法>>>>>>>" + getFunctionName(joinPoint));
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        String url = request.getRequestURL().toString();

        //拦截器中获取 userID
        int userId = GetUserInfoUtils.getUserIdByRequest(request);
        request.setAttribute("userId",userId);

        log.info("请求URL:【{}】,\n请求参数:【{}】", url, args);
        Long befor = System.currentTimeMillis();
        log.info("方法开始时间：【{}】", befor);
    }

    private String getFunctionName(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getName();
//        CheckToken annotation = signature.getMethod().getAnnotation(CheckToken.class);

    }
}
