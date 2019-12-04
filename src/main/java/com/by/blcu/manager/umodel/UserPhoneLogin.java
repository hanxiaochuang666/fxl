package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 手机短信登录
 */
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getVerifyCodeImg() {
        return verifyCodeImg;
    }

    public void setVerifyCodeImg(String verifyCodeImg) {
        this.verifyCodeImg = verifyCodeImg == null ? null : verifyCodeImg.trim();
    }

    public String getVerifyCodePhone() {
        return verifyCodePhone;
    }

    public void setVerifyCodePhone(String verifyCodePhone) {
        this.verifyCodePhone = verifyCodePhone == null ? null : verifyCodePhone.trim();
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }
}
