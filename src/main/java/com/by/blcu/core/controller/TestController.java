package com.by.blcu.core.controller;

import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.utils.GetUserInfoUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author MrBird
 */
@RestController
@CheckToken
@RequestMapping("test")
public class TestController {

    /**
     * 需要登录才能访问
     */
    @GetMapping("/1")
    public Object test1(HttpServletRequest re) {
        StringBuffer sb = new StringBuffer("登录用户信息：         ");
        sb.append(" userId:"+re.getAttribute("userId") +"       ");
        sb.append(" username:"+re.getAttribute("username")+"    ");
        sb.append(" orgCode:"+re.getAttribute("orgCode")+"      ");
        sb.append(" orgType:"+re.getAttribute("orgType"));

        return sb.toString();
    }

    /**
     * 需要 admin 角色才能访问
     */
    @GetMapping("/2")
    @RequiresRoles("admin")
    public String test2() {
        return "success";
    }

    /**
     * 需要 "user:add" 权限才能访问
     */
    @GetMapping("/3")
    @RequiresPermissions("user:add")
    public String test3() {
        return "success";
    }

}
