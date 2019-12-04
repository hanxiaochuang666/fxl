package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户注册类
 */
@ApiModel(description= "用户注册类")
public class UserPhoneRegistModel {

    /**
     *手机号/邮箱/账号
     */
    @ApiModelProperty(value = "手机号/邮箱/账号")
    private String account;

    /**
     *验证码
     */
    @ApiModelProperty(value = "验证码")
    private String verifyCode;
    /**
     *密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode == null ? null : verifyCode.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }
}