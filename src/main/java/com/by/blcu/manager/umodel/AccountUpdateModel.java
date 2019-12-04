package com.by.blcu.manager.umodel;

import com.by.blcu.manager.model.ManagerAccount;
import com.by.blcu.manager.model.ManagerAccountExtend;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@ApiModel(description= "后台用户")
public class AccountUpdateModel extends ManagerAccount {

    /**
     *账号
     */
    @ApiModelProperty(value = "账号")
    private String userName;

    /**
     *密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     *密码
     */
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     *手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     *机构名称
     */
    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "用户扩展信息")
    private ManagerAccountExtend accountExtend;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName == null ? null : orgName.trim();
    }

    public ManagerAccountExtend getAccountExtend() {
        return accountExtend;
    }

    public void setAccountExtend(ManagerAccountExtend accountExtend) {
        this.accountExtend = accountExtend;
    }
}
