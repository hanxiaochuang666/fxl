package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 前端用户登录类
 */
@ApiModel(description= "用户")
public class LoginInfoModel {
    /**
     *用户Id
     */
    @ApiModelProperty(value = "用户Id")
    private String userId;
    /**
     *用户账号
     */
    @ApiModelProperty(value = "用户账号")
    private String userName;
    /**
     *token
     */
    @ApiModelProperty(value = "token")
    private String token;
    /**
     *token过期时间
     */
    @ApiModelProperty(value = "token过期时间")
    private String exipreTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getExipreTime() {
        return exipreTime;
    }

    public void setExipreTime(String exipreTime) {
        this.exipreTime = exipreTime == null ? null : exipreTime.trim();
    }
}