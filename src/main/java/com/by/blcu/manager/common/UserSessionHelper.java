package com.by.blcu.manager.common;

import lombok.Data;

import java.util.Date;

/**
 *  登录后用户信息类
 */
@Data
public class UserSessionHelper {
    //用户账号
    private String userName;
    //用户Id
    private String userId;
    //机构编码
    private String orgCode;
    //机构类型
    private String orgType;
    //ip地址
    private String ip;
    //登录时间
    private Date loginTime;
}
