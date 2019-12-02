package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 后台角色查询类
 */
@Data
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
}