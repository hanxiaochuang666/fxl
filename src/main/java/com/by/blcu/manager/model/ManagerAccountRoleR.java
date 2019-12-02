package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import javax.persistence.*;

/**
 * 账号角色关联表
 */
@Table(name = "manager_account_role_r")
@ApiModel(description= "账号角色关联表")
public class ManagerAccountRoleR {
    /**
     *账号角色关联表Id
     */
    @Id
    @Column(name = "account_role_r_id")
    @ApiModelProperty(value = "账号角色关联表Id")
    private String accountRoleRId;

    /**
     *角色ID
     */
    @Column(name = "role_id")
    @ApiModelProperty(value = "角色ID")
    private String roleId;

    /**
     *用户ID
     */
    @Column(name = "account_id")
    @ApiModelProperty(value = "用户ID")
    private String accountId;

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
     * 获取账号角色关联表Id
     *
     * @return account_role_r_id - 账号角色关联表Id
     */
    public String getAccountRoleRId() {
        return accountRoleRId;
    }

    /**
     * 设置账号角色关联表Id
     *
     * @param accountRoleRId 账号角色关联表Id
     */
    public void setAccountRoleRId(String accountRoleRId) {
        this.accountRoleRId = accountRoleRId == null ? null : accountRoleRId.trim();
    }

    /**
     * 获取角色ID
     *
     * @return role_id - 角色ID
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * 设置角色ID
     *
     * @param roleId 角色ID
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    /**
     * 获取用户ID
     *
     * @return account_id - 用户ID
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * 设置用户ID
     *
     * @param accountId 用户ID
     */
    public void setAccountId(String accountId) {
        this.accountId = accountId == null ? null : accountId.trim();
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