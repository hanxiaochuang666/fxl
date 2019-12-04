package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 普通用户登录
 */
@ApiModel(description= "普通用户登录")
public class UserCommonLogin {
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
    /**
     * 机构号
     */
    @ApiModelProperty(value = "机构号")
    private String orgCode;

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

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }
}
