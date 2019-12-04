package com.by.blcu.manager.umodel;

import com.by.blcu.manager.common.StringHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description= "找回密码-重置")
public class FindPasswordReset {
    @ApiModelProperty(value = "用户账号/手机号/邮箱")
    private String account;

    @ApiModelProperty(value = "新密码")
    private String newPassword;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword == null ? null : newPassword.trim();
    }
}
