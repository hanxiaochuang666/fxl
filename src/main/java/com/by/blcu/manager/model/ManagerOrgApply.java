package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import javax.persistence.*;

/**
 * 机构入驻申请表
 */
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
     *审核时间
     */
    @Column(name = "verify_time")
    @ApiModelProperty(value = "审核时间")
    private Date verifyTime;

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

    /**
     * 获取机构入驻申请表Id
     *
     * @return org_apply_id - 机构入驻申请表Id
     */
    public String getOrgApplyId() {
        return orgApplyId;
    }

    /**
     * 设置机构入驻申请表Id
     *
     * @param orgApplyId 机构入驻申请表Id
     */
    public void setOrgApplyId(String orgApplyId) {
        this.orgApplyId = orgApplyId == null ? null : orgApplyId.trim();
    }

    /**
     * 获取申请人账号
     *
     * @return user_name - 申请人账号
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置申请人账号
     *
     * @param userName 申请人账号
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 获取申请人姓名
     *
     * @return apply_name - 申请人姓名
     */
    public String getApplyName() {
        return applyName;
    }

    /**
     * 设置申请人姓名
     *
     * @param applyName 申请人姓名
     */
    public void setApplyName(String applyName) {
        this.applyName = applyName == null ? null : applyName.trim();
    }

    /**
     * 获取手机
     *
     * @return phone - 手机
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机
     *
     * @param phone 手机
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取Email
     *
     * @return email - Email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置Email
     *
     * @param email Email
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取机构图标
     *
     * @return organization_pic - 机构图标
     */
    public String getOrganizationPic() {
        return organizationPic;
    }

    /**
     * 设置机构图标
     *
     * @param organizationPic 机构图标
     */
    public void setOrganizationPic(String organizationPic) {
        this.organizationPic = organizationPic == null ? null : organizationPic.trim();
    }

    /**
     * 获取机构名称
     *
     * @return organization_name - 机构名称
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * 设置机构名称
     *
     * @param organizationName 机构名称
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName == null ? null : organizationName.trim();
    }

    /**
     * 获取机构简介
     *
     * @return organization_description - 机构简介
     */
    public String getOrganizationDescription() {
        return organizationDescription;
    }

    /**
     * 设置机构简介
     *
     * @param organizationDescription 机构简介
     */
    public void setOrganizationDescription(String organizationDescription) {
        this.organizationDescription = organizationDescription == null ? null : organizationDescription.trim();
    }

    /**
     * 获取一句话介绍
     *
     * @return organization_brief - 一句话介绍
     */
    public String getOrganizationBrief() {
        return organizationBrief;
    }

    /**
     * 设置一句话介绍
     *
     * @param organizationBrief 一句话介绍
     */
    public void setOrganizationBrief(String organizationBrief) {
        this.organizationBrief = organizationBrief == null ? null : organizationBrief.trim();
    }

    /**
     * 获取主营类目
     *
     * @return main_category - 主营类目
     */
    public Integer getMainCategory() {
        return mainCategory;
    }

    /**
     * 设置主营类目
     *
     * @param mainCategory 主营类目
     */
    public void setMainCategory(Integer mainCategory) {
        this.mainCategory = mainCategory;
    }

    /**
     * 获取承诺书图片
     *
     * @return commitment_pic - 承诺书图片
     */
    public String getCommitmentPic() {
        return commitmentPic;
    }

    /**
     * 设置承诺书图片
     *
     * @param commitmentPic 承诺书图片
     */
    public void setCommitmentPic(String commitmentPic) {
        this.commitmentPic = commitmentPic == null ? null : commitmentPic.trim();
    }

    /**
     * 获取机构性质（1 有营业执照；2 没有营业执照）
     *
     * @return organization_nature - 机构性质（1 有营业执照；2 没有营业执照）
     */
    public Integer getOrganizationNature() {
        return organizationNature;
    }

    /**
     * 设置机构性质（1 有营业执照；2 没有营业执照）
     *
     * @param organizationNature 机构性质（1 有营业执照；2 没有营业执照）
     */
    public void setOrganizationNature(Integer organizationNature) {
        this.organizationNature = organizationNature;
    }

    /**
     * 获取证件有效期（1 长期有效；2 固定时限）
     *
     * @return license_term - 证件有效期（1 长期有效；2 固定时限）
     */
    public Integer getLicenseTerm() {
        return licenseTerm;
    }

    /**
     * 设置证件有效期（1 长期有效；2 固定时限）
     *
     * @param licenseTerm 证件有效期（1 长期有效；2 固定时限）
     */
    public void setLicenseTerm(Integer licenseTerm) {
        this.licenseTerm = licenseTerm;
    }

    /**
     * 获取证件有效期至
     *
     * @return license_term_time - 证件有效期至
     */
    public Date getLicenseTermTime() {
        return licenseTermTime;
    }

    /**
     * 设置证件有效期至
     *
     * @param licenseTermTime 证件有效期至
     */
    public void setLicenseTermTime(Date licenseTermTime) {
        this.licenseTermTime = licenseTermTime;
    }

    /**
     * 获取法人身份证正面照
     *
     * @return legal_front_pic - 法人身份证正面照
     */
    public String getLegalFrontPic() {
        return legalFrontPic;
    }

    /**
     * 设置法人身份证正面照
     *
     * @param legalFrontPic 法人身份证正面照
     */
    public void setLegalFrontPic(String legalFrontPic) {
        this.legalFrontPic = legalFrontPic == null ? null : legalFrontPic.trim();
    }

    /**
     * 获取法人身份证背面照
     *
     * @return legal_back_pic - 法人身份证背面照
     */
    public String getLegalBackPic() {
        return legalBackPic;
    }

    /**
     * 设置法人身份证背面照
     *
     * @param legalBackPic 法人身份证背面照
     */
    public void setLegalBackPic(String legalBackPic) {
        this.legalBackPic = legalBackPic == null ? null : legalBackPic.trim();
    }

    /**
     * 获取审核状态（0 未审核，1 审核成功，2 审核失败）
     *
     * @return verify_status - 审核状态（0 未审核，1 审核成功，2 审核失败）
     */
    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    /**
     * 设置审核状态（0 未审核，1 审核成功，2 审核失败）
     *
     * @param verifyStatus 审核状态（0 未审核，1 审核成功，2 审核失败）
     */
    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    /**
     * 获取审核时间
     *
     * @return verify_time - 审核时间
     */
    public Date getVerifyTime() {
        return verifyTime;
    }

    /**
     * 设置审核时间
     *
     * @param verifyTime 审核时间
     */
    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }

    /**
     * 获取审核备注
     *
     * @return verify_note - 审核备注
     */
    public String getVerifyNote() {
        return verifyNote;
    }

    /**
     * 设置审核备注
     *
     * @param verifyNote 审核备注
     */
    public void setVerifyNote(String verifyNote) {
        this.verifyNote = verifyNote == null ? null : verifyNote.trim();
    }

    /**
     * 获取备注
     *
     * @return note - 备注
     */
    public String getNote() {
        return note;
    }

    /**
     * 设置备注
     *
     * @param note 备注
     */
    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    /**
     * 获取添加时间
     *
     * @return create_time - 添加时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置添加时间
     *
     * @param createTime 添加时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取添加人
     *
     * @return create_by - 添加人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置添加人
     *
     * @param createBy 添加人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取修改人
     *
     * @return modify_by - 修改人
     */
    public String getModifyBy() {
        return modifyBy;
    }

    /**
     * 设置修改人
     *
     * @param modifyBy 修改人
     */
    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
    }
}