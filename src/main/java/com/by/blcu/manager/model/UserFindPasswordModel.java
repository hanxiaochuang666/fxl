package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户找回密码类
 */
@Data
@ApiModel(description= "用户找回密码类")
public class UserFindPasswordModel {
    /**
     *手机号/邮箱/账号
     */
    @ApiModelProperty(value = "手机号/邮箱/账号")
    private String account;

    @ApiModelProperty(value = "类型：1 手机号，2 邮箱，3 账号")
    private Integer type;

    /**
     *验证码
     */
    @ApiModelProperty(value = "图片验证码")
    private String verifyCodeImg;
    /**
     *密码
     */
    @ApiModelProperty(value = "邮件/短信验证码")
    private String verifyCode;
}
