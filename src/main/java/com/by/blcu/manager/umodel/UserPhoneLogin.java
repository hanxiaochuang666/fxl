package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 手机短信登录
 */
@Data
@ApiModel(description= "手机短信登录")
public class UserPhoneLogin {

    /**
     *手机号
     */
    @ApiModelProperty(value = "手机号")
    private String account;

    /**
     *图片验证码
     */
    @ApiModelProperty(value = "图片验证码")
    private String verifyCodeImg;
    /**
     *手机短信验证码
     */
    @ApiModelProperty(value = "手机短信验证码")
    private String verifyCodePhone;

    @ApiModelProperty(value = "机构号")
    private String orgCode;
}
