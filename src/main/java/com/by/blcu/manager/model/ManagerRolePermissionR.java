package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import javax.persistence.*;

/**
 * 角色权限表
 */
@Table(name = "manager_role_permission_r")
@ApiModel(description= "角色权限表")
public class ManagerRolePermissionR {
    /**
     *角色权限表Id
     */
    @Id
    @Column(name = "role_permission_r_id")
    @ApiModelProperty(value = "角色权限表Id")
    private String rolePermissionRId;

    /**
     *角色ID
     */
    @Column(name = "role_id")
    @ApiModelProperty(value = "角色ID")
    private String roleId;

    /**
     *菜单ID
     */
    @Column(name = "permission_id")
    @ApiModelProperty(value = "菜单ID")
    private String permissionId;

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
     * 获取角色权限表Id
     *
     * @return role_permission_r_id - 角色权限表Id
     */
    public String getRolePermissionRId() {
        return rolePermissionRId;
    }

    /**
     * 设置角色权限表Id
     *
     * @param rolePermissionRId 角色权限表Id
     */
    public void setRolePermissionRId(String rolePermissionRId) {
        this.rolePermissionRId = rolePermissionRId == null ? null : rolePermissionRId.trim();
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
     * 获取菜单ID
     *
     * @return permission_id - 菜单ID
     */
    public String getPermissionId() {
        return permissionId;
    }

    /**
     * 设置菜单ID
     *
     * @param permissionId 菜单ID
     */
    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId == null ? null : permissionId.trim();
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