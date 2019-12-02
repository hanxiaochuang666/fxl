package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

/**
 * 教师入驻申请表
 */
@Data
@Table(name = "manager_teacher_apply")
@ApiModel(description= "用户")
public class ManagerTeacherApply {
    /**
     *教师入驻申请表Id
     */
    @Id
    @Column(name = "teacher_apply_id")
    @ApiModelProperty(value = "教师入驻申请表Id")
    private String teacherApplyId;

    /**
     *真实姓名
     */
    @Column(name = "real_name")
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     *头像图片
     */
    @Column(name = "head_pic")
    @ApiModelProperty(value = "头像图片")
    private String headPic;

    /**
     *主营类目
     */
    @Column(name = "main_category")
    @ApiModelProperty(value = "主营类目")
    private Integer mainCategory;

    /**
     *QQ
     */
    @Column(name = "q_q")
    @ApiModelProperty(value = "QQ")
    private String qQ;

    /**
     *Email
     */
    @ApiModelProperty(value = "Email")
    private String email;

    /**
     *个人简介
     */
    @Column(name = "personal_profile")
    @ApiModelProperty(value = "个人简介")
    private String personalProfile;

    /**
     *手机
     */
    @ApiModelProperty(value = "手机")
    private String phone;

    /**
     *承诺书图片
     */
    @Column(name = "commitment_pic")
    @ApiModelProperty(value = "承诺书图片")
    private String commitmentPic;

    /**
     *身份证
     */
    @ApiModelProperty(value = "身份证")
    private String identification;

    /**
     *手持身份证正面照
     */
    @Column(name = "id_front_pic")
    @ApiModelProperty(value = "手持身份证正面照")
    private String idFrontPic;

    /**
     *手持身份证背面照
     */
    @Column(name = "id_back_pic")
    @ApiModelProperty(value = "手持身份证背面照")
    private String idBackPic;

    /**
     *教师资格证
     */
    @Column(name = "teacher_certificate_pic")
    @ApiModelProperty(value = "教师资格证")
    private String teacherCertificatePic;

    /**
     *学历证
     */
    @Column(name = "educationr_certificate_pic")
    @ApiModelProperty(value = "学历证")
    private String educationrCertificatePic;

    /**
     *专业证书
     */
    @Column(name = "professionalr_certificate_pic")
    @ApiModelProperty(value = "专业证书")
    private String professionalrCertificatePic;

    /**
     *其他
     */
    @Column(name = "otherr_certificate_pic")
    @ApiModelProperty(value = "其他")
    private String otherrCertificatePic;

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