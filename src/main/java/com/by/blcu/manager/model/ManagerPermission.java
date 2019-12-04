package com.by.blcu.manager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.*;

/**
 * 权限表
 */
@Table(name = "manager_permission")
@ApiModel(description= "权限表")
public class ManagerPermission {
    /**
     *表Id
     */
    @Id
    @Column(name = "permission_id")
    @ApiModelProperty(value = "表Id")
    private String permissionId;

    /**
     *菜单名称
     */
    @Column(name = "menu_name")
    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    /**
     *菜单编号
     */
    @Column(name = "menu_code")
    @ApiModelProperty(value = "菜单编号")
    private String menuCode;

    /**
     *所属父ID(顶级为0)
     */
    @Column(name = "parent_id")
    @ApiModelProperty(value = "所属父ID(顶级为0)")
    private String parentId;

    /**
     *修改人
     */
    @Column(name = "modify_by")
    @ApiModelProperty(value = "修改人")
    private String modifyBy;

    /**
     *类型（1菜单 2按扭，3API）
     */
    @ApiModelProperty(value = "类型（1菜单 2按扭，3API）")
    private Integer type;

    /**
     *是否系统级别
     */
    @Column(name = "is_system")
    @ApiModelProperty(value = "是否系统级别")
    private Boolean isSystem;

    /**
     *是否可见
     */
    @Column(name = "is_display")
    @ApiModelProperty(value = "是否可见")
    private Boolean isDisplay;

    /**
     *地址
     */
    @ApiModelProperty(value = "地址")
    private String url;

    /**
     *权限标识
     */
    @ApiModelProperty(value = "权限标识")
    private String perms;

    /**
     *菜单图标（只存在于一级菜单）
     */
    @ApiModelProperty(value = "菜单图标（只存在于一级菜单）")
    private String icon;

    /**
     *菜单状态（1 正常 ，2 停用）
     */
    @ApiModelProperty(value = "菜单状态（1 正常 ，2 停用）")
    private Integer status;

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
     *备注
     */
    @ApiModelProperty(value = "备注")
    private String note;

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
     *分类深度
     */
    @Column(name = "class_layer")
    @ApiModelProperty(value = "分类深度")
    private Integer classLayer;

    /**
     * 获取表Id
     *
     * @return permission_id - 表Id
     */
    public String getPermissionId() {
        return permissionId;
    }

    /**
     * 设置表Id
     *
     * @param permissionId 表Id
     */
    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId == null ? null : permissionId.trim();
    }

    /**
     * 获取菜单名称
     *
     * @return menu_name - 菜单名称
     */
    public String getMenuName() {
        return menuName;
    }

    /**
     * 设置菜单名称
     *
     * @param menuName 菜单名称
     */
    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    /**
     * 获取菜单编号
     *
     * @return menu_code - 菜单编号
     */
    public String getMenuCode() {
        return menuCode;
    }

    /**
     * 设置菜单编号
     *
     * @param menuCode 菜单编号
     */
    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode == null ? null : menuCode.trim();
    }

    /**
     * 获取所属父ID(顶级为0)
     *
     * @return parent_id - 所属父ID(顶级为0)
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * 设置所属父ID(顶级为0)
     *
     * @param parentId 所属父ID(顶级为0)
     */
    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
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

    /**
     * 获取类型（1菜单 2按扭，3API）
     *
     * @return type - 类型（1菜单 2按扭，3API）
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型（1菜单 2按扭，3API）
     *
     * @param type 类型（1菜单 2按扭，3API）
     */
    public void setType(Integer type) {
        this.type = type;
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
     * 获取是否可见
     *
     * @return is_display - 是否可见
     */
    public Boolean getIsDisplay() {
        return isDisplay;
    }

    /**
     * 设置是否可见
     *
     * @param isDisplay 是否可见
     */
    public void setIsDisplay(Boolean isDisplay) {
        this.isDisplay = isDisplay;
    }

    /**
     * 获取地址
     *
     * @return url - 地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置地址
     *
     * @param url 地址
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 获取权限标识
     *
     * @return perms - 权限标识
     */
    public String getPerms() {
        return perms;
    }

    /**
     * 设置权限标识
     *
     * @param perms 权限标识
     */
    public void setPerms(String perms) {
        this.perms = perms == null ? null : perms.trim();
    }

    /**
     * 获取菜单图标（只存在于一级菜单）
     *
     * @return icon - 菜单图标（只存在于一级菜单）
     */
    public String getIcon() {
        return icon;
    }

    /**
     * 设置菜单图标（只存在于一级菜单）
     *
     * @param icon 菜单图标（只存在于一级菜单）
     */
    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    /**
     * 获取菜单状态（1 正常 ，2 停用）
     *
     * @return status - 菜单状态（1 正常 ，2 停用）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置菜单状态（1 正常 ，2 停用）
     *
     * @param status 菜单状态（1 正常 ，2 停用）
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * 获取备注
     *
     * @return note - 备注
     */
    public String getNote() {
        return note;
    }

    /**
     * 设置备注
     *
     * @param note 备注
     */
    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
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
     * 获取分类深度
     *
     * @return class_layer - 分类深度
     */
    public Integer getClassLayer() {
        return classLayer;
    }

    /**
     * 设置分类深度
     *
     * @param classLayer 分类深度
     */
    public void setClassLayer(Integer classLayer) {
        this.classLayer = classLayer;
    }
}