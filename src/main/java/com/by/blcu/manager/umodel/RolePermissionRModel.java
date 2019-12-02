package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import java.util.List;

/**
 * 权限角色类
 */
@Data
@ApiModel(description= "权限角色类")
public class RolePermissionRModel {

    /**
     * 所属组织编码
     */
    @ApiModelProperty(value = "所属组织编码")
    private String orgCode;

    /**
     * 角色表Id
     */
    @ApiModelProperty(value = "角色表Id")
    private String roleId;

    /**
     * 权限Id列表
     */
    @ApiModelProperty(value = "权限Id列表")
    private List<String> permissionIdList;
}
