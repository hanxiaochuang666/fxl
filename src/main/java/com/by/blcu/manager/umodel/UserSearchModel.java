package com.by.blcu.manager.umodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

/**
 * 用户查询类
 */
@ApiModel(description= "用户查询类")
public class UserSearchModel extends ManagerPagerModel {
    /**
     *统一用户表Id
     */
    @ApiModelProperty(value = "统一用户表Id")
    private String userId;

    /**
     *账号
     */
    @ApiModelProperty(value = "账号")
    private String userName;


    /**
     *真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     *用户状态（1 正常，2停用，3锁定）
     */
    @ApiModelProperty(value = "用户状态（1 正常，2停用，3锁定）")
    private Integer status;

    /**
     *手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     *邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     *添加时间-开始
     */
    @ApiModelProperty(value = "添加时间-开始")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeBegin;

    /**
     *添加时间-结束
     */

    @ApiModelProperty(value = "添加时间-结束")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;

    @ApiModelProperty(value = "用户Id列表")
    private List<String> userIdList;

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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }
}