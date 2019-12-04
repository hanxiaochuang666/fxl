package com.by.blcu.manager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.*;

/**
 * 角色表
 */
@Table(name = "manager_role")
@ApiModel(description= "角色表")
public class ManagerRole {
    /**
     *角色表Id
     */
    @Id
    @Column(name = "role_id")
    @ApiModelProperty(value = "角色表Id")
    private String roleId;

    /**
     *角色名称
     */
    @Column(name = "role_name")
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
    @Column(name = "is_system")
    @ApiModelProperty(value = "是否系统级别")
    private Boolean isSystem;

    /**
     *所属组织编码
     */
    @Column(name = "org_code")
    @ApiModelProperty(value = "所属组织编码")
    private String orgCode;

    /**
     *角色描述
     */
    @ApiModelProperty(value = "角色描述")
    private String description;

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
     * 获取角色表Id
     *
     * @return role_id - 角色表Id
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * 设置角色表Id
     *
     * @param roleId 角色表Id
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
    }

    /**
     * 获取角色名称
     *
     * @return role_name - 角色名称
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 设置角色名称
     *
     * @param roleName 角色名称
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * 获取角色状态（1 正常 ，2 停用）
     *
     * @return status - 角色状态（1 正常 ，2 停用）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置角色状态（1 正常 ，2 停用）
     *
     * @param status 角色状态（1 正常 ，2 停用）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取是否系统级别
     *
     * @return is_system - 是否系统级别
     */
    public Boolean getIsSystem() {
        return isSystem;
    }

    /**
     * 设置是否系统级别
     *
     * @param isSystem 是否系统级别
     */
    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
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
     * 获取角色描述
     *
     * @return description - 角色描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置角色描述
     *
     * @param description 角色描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
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