package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

/**
 * 后台用户查询类
 */
@Data
@ApiModel(description= "后台用户查询类")
public class AccountSearchModel extends ManagerPagerModel {

    /**
     *后台用户表Id
     */
    @ApiModelProperty(value = "后台用户表Id")
    private String accountId;

    /**
     *统一用户表Id
     */
    @ApiModelProperty(value = "统一用户表Id")
    private String userId;

    /**
     *账号
     */
    @ApiModelProperty(value = "账号")
    private String userName;

    /**
     *真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     *手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

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
}
