package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

/**
 * 机构表（入驻的机构或教师个人）
 */
@Data
@Table(name = "manager_organization")
@ApiModel(description= "机构表（入驻的机构或教师个人）")
public class ManagerOrganization {
    /**
     *机构表d
     */
    @Id
    @Column(name = "organization_id")
    @ApiModelProperty(value = "机构表Id")
    private String organizationId;

    /**
     *申请表Id
     */
    @Column(name = "apply_id")
    @ApiModelProperty(value = "申请表Id")
    private String applyId;

    /**
     *主营类目
     */
    @Column(name = "cc_id")
    @ApiModelProperty(value = "主营类目")
    private String ccId;

    /**
     *机构名称
     */
    @Column(name = "organization_name")
    @ApiModelProperty(value = "机构名称")
    private String organizationName;

    /**
     *入驻者组织编码
     */
    @Column(name = "org_code")
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
    @Column(name = "user_name")
    @ApiModelProperty(value = "负责人账号")
    private String userName;

    /**
     *负责人真实姓名
     */
    @Column(name = "real_name")
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
     *机构Logo图标
     */
    @Column(name = "organization_logo")
    @ApiModelProperty(value = "机构Logo图标")
    private String organizationLogo;

    /**
     *机构网站图标
     */
    @Column(name = "web_pic")
    @ApiModelProperty(value = "首页封面")
    private String webPic;

    /**
     *状态（1. 正常 ，2 停用）
     */
    @ApiModelProperty(value = "状态（1. 正常 ，2 停用）")
    private Integer status;

    /**
     * 审核状态（0 未审核，1 审核成功，2 审核失败）
     */
    @Column(name = "verify_status")
    @ApiModelProperty(value = "审核状态（0 未审核，1 审核成功，2 审核失败）")
    private Integer verifyStatus;

    /**
     * 审核时间
     */
    @Column(name = "verify_time")
    @ApiModelProperty(value = "审核时间")
    private Date verifyTime;

    /**
     * 审核备注
     */
    @Column(name = "verify_note")
    @ApiModelProperty(value = "审核备注")
    private String verifyNote;

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
    private Date modifyTime;

    /**
     *修改人
     */
    @Column(name = "modify_by")
    @ApiModelProperty(value = "修改人")
    private String modifyBy;
}