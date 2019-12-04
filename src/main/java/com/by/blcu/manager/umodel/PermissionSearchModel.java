package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 *  权限查询类
 */
@ApiModel(description= "用户")
public class PermissionSearchModel {

    /**
     *权限表Id
     */
    @ApiModelProperty(value = "权限表Id")
    private String permissionId;

    /**
     *菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    /**
     *菜单编号
     */
    @ApiModelProperty(value = "菜单编号")
    private String menuCode;


    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId == null ? null : permissionId.trim();
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode == null ? null : menuCode.trim();
    }
}