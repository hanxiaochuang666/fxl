package com.by.blcu.manager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.*;

/**
 * 后台用户表
 */
@Data
@Table(name = "manager_account")
@ApiModel(description= "后台用户表")
public class ManagerAccount {
    /**
     *后台用户表Id
     */
    @Id
    @Column(name = "account_id")
    @ApiModelProperty(value = "后台用户表Id")
    private String accountId;

    /**
     *用户表Id
     */
    @Column(name = "user_id")
    @ApiModelProperty(value = "用户Id")
    private String userId;

    /**
     *用户名
     */
    @Column(name = "user_name")
    @ApiModelProperty(value = "用户名")
    private String userName;

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


}