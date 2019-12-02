package com.by.blcu.manager.common;

import com.by.blcu.core.utils.ApplicationUtils;

/**
 * 用户帮助类
 */
public class UserHelper {
    /**
     * 用户Id 32位
     */
    public static String UserId="12345678912345678912345678912345";
    /**
     * 用户账号
     */
    public static String UserName="admin";

    public static String DefaultPassword="123456";
    /**
     * 用户账号
     * @return
     */
    public static String generatorUserName(String phone){
        return ApplicationUtils.getUUID().substring(0,8);
    }




}
