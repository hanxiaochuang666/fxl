package com.by.blcu.manager.controller;

import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.manager.common.ManagerHelper;
import com.by.blcu.manager.common.RedisHelper;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.common.UserSessionHelper;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;



/**
 * @Description: ManagerBase类
 * @author 耿鹤闯
 * @date 2019/09/02 14:37
 */
@RestController
@CheckToken
public class ManagerBase {

    @Resource
    private RedisHelper redisHelper;

    //登录用户账号
    public String userNameBse;
    //登录用户Id
    public String userIdBase;
    //组织编号
    public String orgCodeBase;
    //组织类型
    public String orgTypeBase;
    //request
    public HttpServletRequest requestBase;
    //登录信息类
    public UserSessionHelper userSessionHelper;

     @ModelAttribute
      protected void initBinder(HttpServletRequest req) throws Exception {
        requestBase =req;
        if(requestBase.getAttribute("username")!=null){
            userNameBse = requestBase.getAttribute("username").toString();
        }
        if(requestBase.getAttribute("userId")!=null){
            userIdBase = requestBase.getAttribute("userId").toString();
        }
        if(requestBase.getAttribute("orgCode")!=null){
            orgCodeBase = requestBase.getAttribute("orgCode").toString();
        }
        else{
            orgCodeBase=ManagerHelper.OrgCode;
        }
        if(requestBase.getAttribute("orgType")!=null){
            orgTypeBase = requestBase.getAttribute("orgType").toString();
        }
        else{
            orgTypeBase=ManagerHelper.OrgType;
        }

        if(!StringHelper.IsNullOrWhiteSpace(userNameBse)){
            userSessionHelper = redisHelper.getUserSession(userNameBse);
            if(userSessionHelper==null){
                userSessionHelper =new UserSessionHelper();
                userSessionHelper.setUserName(userNameBse);
                userSessionHelper.setUserId(userIdBase);
                userSessionHelper.setOrgCode(orgCodeBase);
                userSessionHelper.setOrgType(orgTypeBase);
                userSessionHelper.setIp(getIpAddr());
            }
            else{
                userSessionHelper.setOrgCode(orgCodeBase);
                userSessionHelper.setOrgType(orgTypeBase);
            }
        }
        else{
            userNameBse="guest";
            userIdBase="-1";
            orgCodeBase=ManagerHelper.OrgCode;;
            orgTypeBase=ManagerHelper.OrgType;
            userSessionHelper =new UserSessionHelper();
            userSessionHelper.setUserName(userNameBse);
            userSessionHelper.setUserId(userIdBase);
            userSessionHelper.setOrgCode(orgCodeBase);
            userSessionHelper.setOrgType(orgTypeBase);
            userSessionHelper.setIp(getIpAddr());
        }
     }

    public String getIpAddr() {
        String ip = requestBase.getHeader("x-forwarded-for");
        System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if( ip.indexOf(",")!=-1 ){
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = requestBase.getHeader("Proxy-Client-IP");
            System.out.println("Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = requestBase.getHeader("WL-Proxy-Client-IP");
            System.out.println("WL-Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = requestBase.getHeader("HTTP_CLIENT_IP");
            System.out.println("HTTP_CLIENT_IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = requestBase.getHeader("HTTP_X_FORWARDED_FOR");
            System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = requestBase.getHeader("X-Real-IP");
            System.out.println("X-Real-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = requestBase.getRemoteAddr();
            System.out.println("getRemoteAddr ip: " + ip);
        }
        System.out.println("获取客户端ip: " + ip);
        return ip;
    }
}
