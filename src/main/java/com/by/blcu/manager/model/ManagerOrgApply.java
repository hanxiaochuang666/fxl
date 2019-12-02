package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

/**
 * 机构入驻申请表
 */
@Data
@Table(name = "manager_org_apply")
@ApiModel(description= "机构入驻申请表")
public class ManagerOrgApply {
    /**
     *机构入驻申请表Id
     */
    @Id
    @Column(name = "org_apply_id")
    @ApiModelProperty(value = "机构入驻申请表Id")
    private String orgApplyId;

    /**
     *申请人账号
     */
    @Column(name = "user_name")
    @ApiModelProperty(value = "申请人账号")
    private String userName;

    /**
     *申请人姓名
     */
    @Column(name = "apply_name")
    @ApiModelProperty(value = "申请人姓名")
    private String applyName;

    /**
     *手机
     */
    @ApiModelProperty(value = "手机")
    private String phone;

    /**
     *Email
     */
    @ApiModelProperty(value = "Email")
    private String email;

    /**
     *机构图标
     */
    @Column(name = "organization_pic")
    @ApiModelProperty(value = "机构图标")
    private String organizationPic;

    /**
     *机构名称
     */
    @Column(name = "organization_name")
    @ApiModelProperty(value = "机构名称")
    private String organizationName;

    /**
     *机构简介
     */
    @Column(name = "organization_description")
    @ApiModelProperty(value = "机构简介")
    private String organizationDescription;

    /**
     *一句话介绍
     */
    @Column(name = "organization_brief")
    @ApiModelProperty(value = "一句话介绍")
    private String organizationBrief;

    /**
     *主营类目
     */
    @Column(name = "main_category")
    @ApiModelProperty(value = "主营类目")
    private Integer mainCategory;

    /**
     *承诺书图片
     */
    @Column(name = "commitment_pic")
    @ApiModelProperty(value = "承诺书图片")
    private String commitmentPic;

    /**
     *机构性质（1 有营业执照；2 没有营业执照）
     */
    @Column(name = "organization_nature")
    @ApiModelProperty(value = "机构性质（1 有营业执照；2 没有营业执照）")
    private Integer organizationNature;

    /**
     *营业执照/教育资质证书
     */
    @Column(name = "license_pic")
    @ApiModelProperty(value = "营业执照/教育资质证书")
    private String licensePic;

    /**
     *营业执照注册号/证书编号
     */
    @ApiModelProperty(value = "营业执照注册号/证书编号")
    private String number;

    /**
     *证件有效期（1 长期有效；2 固定时限）
     */
    @Column(name = "license_term")
    @ApiModelProperty(value = "证件有效期（1 长期有效；2 固定时限）")
    private Integer licenseTerm;

    /**
     *证件有效期至
     */
    @Column(name = "license_term_time")
    @ApiModelProperty(value = "证件有效期至")
    private Date licenseTermTime;

    /**
     *法人身份证正面照
     */
    @Column(name = "legal_front_pic")
    @ApiModelProperty(value = "法人身份证正面照")
    private String legalFrontPic;

    /**
     *法人身份证背面照
     */
    @Column(name = "legal_back_pic")
    @ApiModelProperty(value = "法人身份证背面照")
    private String legalBackPic;

    /**
     *审核状态（0 未审核，1 审核成功，2 审核失败）
     */
    @Column(name = "verify_status")
    @ApiModelProperty(value = "审核状态（0 未审核，1 审核成功，2 审核失败）")
    private Integer verifyStatus;

    /**
     * 审核时间
     */
    @Column(name = "verify_time")
    @ApiModelProperty(value = "审核时间")
    private Integer verifyTime;

    /**
     *审核备注
     */
    @Column(name = "verify_note")
    @ApiModelProperty(value = "审核备注")
    private String verifyNote;

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