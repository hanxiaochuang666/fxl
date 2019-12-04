package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import javax.persistence.*;

/**
 * 教师入驻申请表
 */
@Table(name = "manager_teacher_apply")
@ApiModel(description= "教师申请表")
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
     * 获取教师入驻申请表Id
     *
     * @return teacher_apply_id - 教师入驻申请表Id
     */
    public String getTeacherApplyId() {
        return teacherApplyId;
    }

    /**
     * 设置教师入驻申请表Id
     *
     * @param teacherApplyId 教师入驻申请表Id
     */
    public void setTeacherApplyId(String teacherApplyId) {
        this.teacherApplyId = teacherApplyId == null ? null : teacherApplyId.trim();
    }

    /**
     * 获取真实姓名
     *
     * @return real_name - 真实姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置真实姓名
     *
     * @param realName 真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    /**
     * 获取头像图片
     *
     * @return head_pic - 头像图片
     */
    public String getHeadPic() {
        return headPic;
    }

    /**
     * 设置头像图片
     *
     * @param headPic 头像图片
     */
    public void setHeadPic(String headPic) {
        this.headPic = headPic == null ? null : headPic.trim();
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
     * 获取QQ
     *
     * @return q_q - QQ
     */
    public String getqQ() {
        return qQ;
    }

    /**
     * 设置QQ
     *
     * @param qQ QQ
     */
    public void setqQ(String qQ) {
        this.qQ = qQ == null ? null : qQ.trim();
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
     * 获取个人简介
     *
     * @return personal_profile - 个人简介
     */
    public String getPersonalProfile() {
        return personalProfile;
    }

    /**
     * 设置个人简介
     *
     * @param personalProfile 个人简介
     */
    public void setPersonalProfile(String personalProfile) {
        this.personalProfile = personalProfile == null ? null : personalProfile.trim();
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
     * 获取身份证
     *
     * @return identification - 身份证
     */
    public String getIdentification() {
        return identification;
    }

    /**
     * 设置身份证
     *
     * @param identification 身份证
     */
    public void setIdentification(String identification) {
        this.identification = identification == null ? null : identification.trim();
    }

    /**
     * 获取手持身份证正面照
     *
     * @return id_front_pic - 手持身份证正面照
     */
    public String getIdFrontPic() {
        return idFrontPic;
    }

    /**
     * 设置手持身份证正面照
     *
     * @param idFrontPic 手持身份证正面照
     */
    public void setIdFrontPic(String idFrontPic) {
        this.idFrontPic = idFrontPic == null ? null : idFrontPic.trim();
    }

    /**
     * 获取手持身份证背面照
     *
     * @return id_back_pic - 手持身份证背面照
     */
    public String getIdBackPic() {
        return idBackPic;
    }

    /**
     * 设置手持身份证背面照
     *
     * @param idBackPic 手持身份证背面照
     */
    public void setIdBackPic(String idBackPic) {
        this.idBackPic = idBackPic == null ? null : idBackPic.trim();
    }

    /**
     * 获取教师资格证
     *
     * @return teacher_certificate_pic - 教师资格证
     */
    public String getTeacherCertificatePic() {
        return teacherCertificatePic;
    }

    /**
     * 设置教师资格证
     *
     * @param teacherCertificatePic 教师资格证
     */
    public void setTeacherCertificatePic(String teacherCertificatePic) {
        this.teacherCertificatePic = teacherCertificatePic == null ? null : teacherCertificatePic.trim();
    }

    /**
     * 获取学历证
     *
     * @return educationr_certificate_pic - 学历证
     */
    public String getEducationrCertificatePic() {
        return educationrCertificatePic;
    }

    /**
     * 设置学历证
     *
     * @param educationrCertificatePic 学历证
     */
    public void setEducationrCertificatePic(String educationrCertificatePic) {
        this.educationrCertificatePic = educationrCertificatePic == null ? null : educationrCertificatePic.trim();
    }

    /**
     * 获取专业证书
     *
     * @return professionalr_certificate_pic - 专业证书
     */
    public String getProfessionalrCertificatePic() {
        return professionalrCertificatePic;
    }

    /**
     * 设置专业证书
     *
     * @param professionalrCertificatePic 专业证书
     */
    public void setProfessionalrCertificatePic(String professionalrCertificatePic) {
        this.professionalrCertificatePic = professionalrCertificatePic == null ? null : professionalrCertificatePic.trim();
    }

    /**
     * 获取其他
     *
     * @return otherr_certificate_pic - 其他
     */
    public String getOtherrCertificatePic() {
        return otherrCertificatePic;
    }

    /**
     * 设置其他
     *
     * @param otherrCertificatePic 其他
     */
    public void setOtherrCertificatePic(String otherrCertificatePic) {
        this.otherrCertificatePic = otherrCertificatePic == null ? null : otherrCertificatePic.trim();
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