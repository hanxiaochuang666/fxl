package com.by.blcu.manager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.*;

/**
 * 后台用户表
 */
@Table(name = "manager_account")
@ApiModel(description= "后台用户表")
public class ManagerAccount {
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
    @ApiModelProperty(value = "用户Id")
    private String userId;

    /**
     *用户名
     */
    @Column(name = "user_name")
    @ApiModelProperty(value = "用户名")
    private String userName;

    /**
     *用户类型( 1本部人员；2.教师；3第三方机构，4租户)
     */
    @ApiModelProperty(value = "用户类型( 1本部人员；2.教师；3第三方机构，4租户)")
    private Integer type;

    /**
     *所属组织编码
     */
    @Column(name = "org_code")
    @ApiModelProperty(value = "所属组织编码")
    private String orgCode;

    /**
     *用户状态（1. 正常 ，2 停用）
     */
    @ApiModelProperty(value = "用户状态（1. 正常 ，2 停用）")
    private Integer status;

    /**
     *是否是管理者
     */
    @Column(name = "is_manager")
    @ApiModelProperty(value = "是否是管理者")
    private Boolean isManager;

    /**
     *排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     *是否删除
     */
    @Column(name = "is_deleted")
    @ApiModelProperty(value = "是否删除")
    private Boolean isDeleted;

    /**
     *添加时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value = "添加时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
     * 获取用户名
     *
     * @return user_name - 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 获取用户类型( 1本部人员；2.教师；3第三方机构，4租户)
     *
     * @return type - 用户类型( 1本部人员；2.教师；3第三方机构，4租户)
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置用户类型( 1本部人员；2.教师；3第三方机构，4租户)
     *
     * @param type 用户类型( 1本部人员；2.教师；3第三方机构，4租户)
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取所属组织编码
     *
     * @return org_code - 所属组织编码
     */
    public String getOrgCode() {
        return orgCode;
    }

    /**
     * 设置所属组织编码
     *
     * @param orgCode 所属组织编码
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    /**
     * 获取用户状态（1. 正常 ，2 停用）
     *
     * @return status - 用户状态（1. 正常 ，2 停用）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置用户状态（1. 正常 ，2 停用）
     *
     * @param status 用户状态（1. 正常 ，2 停用）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取是否是管理者
     *
     * @return is_manager - 是否是管理者
     */
    public Boolean getIsManager() {
        return isManager;
    }

    /**
     * 设置是否是管理者
     *
     * @param isManager 是否是管理者
     */
    public void setIsManager(Boolean isManager) {
        this.isManager = isManager;
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取是否删除
     *
     * @return is_deleted - 是否删除
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否删除
     *
     * @param isDeleted 是否删除
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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