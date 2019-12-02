package com.by.blcu.manager.common;

import com.by.blcu.core.domain.User;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.MD5Util;
import com.by.blcu.manager.model.SsoUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 管理员静态类
 */
public final class ManagerHelper {
    public static String UserName="admin";
    public static String Password="admin@123456";
    public static String PasswordSecret= MD5Util.encrypt(UserName, Password);
    public static String UserId="12345678912345678912345678912345";
    public static Boolean isEnable=true;
    public static String OrgCode="eblcu";
    public static String OrgType="1";
    public static SsoUser ssoUser =new SsoUser();
    public static List<String> roleList=new ArrayList<>();

    static  {
        //用户信息
        ssoUser.setId(-9999);
        ssoUser.setUserId(UserId);
        ssoUser.setUserName(UserName);
        ssoUser.setPassword("");
        ssoUser.setRealName("管理员");
        ssoUser.setStatus(1);
        ssoUser.setHeaderUrl("");
        ssoUser.setNickName("昵称");
        ssoUser.setSex(2);
        ssoUser.setPhone("19999999999");
        ssoUser.setEmail("admin@admin.admin");
        ssoUser.setQq("1234567");
        ssoUser.setEducation("博士后");
        ssoUser.setProvince("北京");
        ssoUser.setCity("北京");
        ssoUser.setIsDeleted(false);
        //角色
        roleList.add("admin");
        //权限

    }


}
