package com.by.blcu.manager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.*;

/**
 * 权限表
 */
@Table(name = "manager_permission")
@ApiModel(description= "权限表")
@Data
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
     *所属父ID(顶级为0)
     */
    @Column(name = "class_layer")
    @ApiModelProperty(value = "分类深度")
    private Integer classLayer;

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
     * 权限标识
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
     *修改人
     */
    @Column(name = "modify_by")
    @ApiModelProperty(value = "修改人")
    private String modifyBy;


}