package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

/**
 * 用户角色类
 */
@Data
@ApiModel(description= "用户角色类")
public class AccountRoleRModel {

    /**
     * 所属组织编码
     */
    @ApiModelProperty(value = "所属组织编码")
    private String orgCode;

    /**
     * 后台用户表Id
     */
    @ApiModelProperty(value = "后台用户表Id")
    private String accountId;

    /**
     * 用户表Id
     */
    @ApiModelProperty(value = "用户表Id")
    private String userId;

    /**
     * 角色Id列表
     */
    @ApiModelProperty(value = "角色Id列表")
    private List<String> roleIdList;

}

