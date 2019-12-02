package com.by.blcu.manager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.*;

/**
 * 角色表
 */
@Table(name = "manager_role")
@ApiModel(description= "角色表")
@Data
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
     * 排序
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