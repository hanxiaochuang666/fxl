package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 后台角色查询类
 */
@ApiModel(description= "后台角色查询类")
public class RoleSearchModel  extends ManagerPagerModel {
    /**
     *角色表Id
     */
    @ApiModelProperty(value = "角色表Id")
    private String roleId;

    /**
     *角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    /**
     *角色状态（1 正常 ，2 停用）
     */
    @ApiModelProperty(value = "角色状态（1 正常 ，2 停用）")
    private Integer status;
    /**
     *是否系统级别
     */
    @ApiModelProperty(value = "是否系统级别")
    private Boolean isSystem;

    /**
     *所属组织编码
     */
    @ApiModelProperty(value = "所属组织编码")
    private String orgCode;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean isSystem) {
        isSystem = isSystem;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }
}