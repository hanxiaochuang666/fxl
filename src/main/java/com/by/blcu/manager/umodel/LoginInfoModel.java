package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 前端用户登录类
 */
@Data
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
}