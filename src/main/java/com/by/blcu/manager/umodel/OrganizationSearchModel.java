package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 *机构查询类
 */
@ApiModel(description= "机构查询类")
@Data
public class OrganizationSearchModel extends ManagerPagerModel{
    /**
     *机构表d
     */
    @ApiModelProperty(value = "机构表d")
    private String organizationId;

    /**
     *机构名称
     */
    @ApiModelProperty(value = "机构名称")
    private String organizationName;

    /**
     *入驻者组织编码
     */
    @ApiModelProperty(value = "入驻者组织编码")
    private String orgCode;

    /**
     *用户类型( 1.本部；2.教师；3第三方机构；4租户)
     */
    @ApiModelProperty(value = "用户类型( 1.本部；2.教师；3第三方机构；4租户)")
    private Integer type;

    /**
     *负责人账号
     */
    @ApiModelProperty(value = "负责人账号")
    private String userName;

    /**
     *负责人真实姓名
     */
    @ApiModelProperty(value = "负责人真实姓名")
    private String realName;

    /**
     *手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     *邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String email;

    /**
     *状态（1. 正常 ，2 停用）
     */
    @ApiModelProperty(value = "状态（1. 正常 ，2 停用）")
    private Integer status;

    /**
     * 审核状态（0 未审核，1 审核成功，2 审核失败）
     */
    @ApiModelProperty(value = "审核状态（0 未审核，1 审核成功，2 审核失败）")
    private Integer verifyStatus;

}
