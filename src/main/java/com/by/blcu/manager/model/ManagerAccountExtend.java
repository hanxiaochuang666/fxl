package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import javax.persistence.*;

/**
 * 用户扩展信息
 */
@Table(name = "manager_account_extend")
@ApiModel(description= "用户扩展信息")
public class ManagerAccountExtend {
    /**
     *后台用户表Id
     */
    @Id
    @Column(name = "account_id")
    @ApiModelProperty(value = "后台用户表Id")
    private String accountId;

    /**
     *用户表Id
     */
    @Column(name = "user_id")
    @ApiModelProperty(value = "用户表Id")
    private String userId;

    /**
     *用户账号
     */
    @Column(name = "user_name")
    @ApiModelProperty(value = "用户账号")
    private String userName;

    /**
     *教师简介
     */
    @ApiModelProperty(value = "教师简介")
    private String description;

    /**
     *讲授课程
     */
    @Column(name = "teach_course")
    @ApiModelProperty(value = "讲授课程")
    private String teachCourse;

    /**
     *自定义属性(JSON)
     */
    @ApiModelProperty(value = "自定义属性(JSON)")
    private String attribute;

    /**
     *添加时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    /**
     *添加人
     */
    @Column(name = "create_by")
    @ApiModelProperty(value = "添加人")
    private String createBy;

    /**
     *修改时间
     */
    @Column(name = "modify_time")
    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    /**
     *修改人
     */
    @Column(name = "modify_by")
    @ApiModelProperty(value = "修改人")
    private String modifyBy;

    /**
     * 获取后台用户表Id
     *
     * @return account_id - 后台用户表Id
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * 设置后台用户表Id
     *
     * @param accountId 后台用户表Id
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
    }

    /**
     * 获取用户表Id
     *
     * @return user_id - 用户表Id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户表Id
     *
     * @param userId 用户表Id
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取用户账号
     *
     * @return user_name - 用户账号
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户账号
     *
     * @param userName 用户账号
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 获取教师简介
     *
     * @return description - 教师简介
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置教师简介
     *
     * @param description 教师简介
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 获取讲授课程
     *
     * @return teach_course - 讲授课程
     */
    public String getTeachCourse() {
        return teachCourse;
    }

    /**
     * 设置讲授课程
     *
     * @param teachCourse 讲授课程
     */
    public void setTeachCourse(String teachCourse) {
        this.teachCourse = teachCourse == null ? null : teachCourse.trim();
    }

    /**
     * 获取自定义属性(JSON)
     *
     * @return attribute - 自定义属性(JSON)
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * 设置自定义属性(JSON)
     *
     * @param attribute 自定义属性(JSON)
     */
    public void setAttribute(String attribute) {
        this.attribute = attribute == null ? null : attribute.trim();
    }

    /**
     * 获取添加时间
     *
     * @return create_time - 添加时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置添加时间
     *
     * @param createTime 添加时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取添加人
     *
     * @return create_by - 添加人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置添加人
     *
     * @param createBy 添加人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取修改人
     *
     * @return modify_by - 修改人
     */
    public String getModifyBy() {
        return modifyBy;
    }

    /**
     * 设置修改人
     *
     * @param modifyBy 修改人
     */
    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
    }
}