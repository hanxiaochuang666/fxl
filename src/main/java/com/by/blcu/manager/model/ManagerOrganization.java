package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import javax.persistence.*;

/**
 * 机构表（入驻的机构或教师个人）
 */
@Table(name = "manager_organization")
@ApiModel(description= "机构表（入驻的机构或教师个人）")
public class ManagerOrganization {
    /**
     *机构表d
     */
    @Id
    @Column(name = "organization_id")
    @ApiModelProperty(value = "机构表d")
    private String organizationId;

    /**
     *申请表Id
     */
    @Column(name = "apply_id")
    @ApiModelProperty(value = "申请表Id")
    private String applyId;

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
     *状态（1. 正常 ，2 停用）
     */
    @ApiModelProperty(value = "状态（1. 正常 ，2 停用）")
    private Integer status;

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
    @ApiModelProperty(value = "机构网站图标")
    private String webPic;

    /**
     *主营类目
     */
    @Column(name = "cc_id")
    @ApiModelProperty(value = "主营类目")
    private String ccId;

    /**
     * 获取机构表d
     *
     * @return organization_id - 机构表d
     */
    public String getOrganizationId() {
        return organizationId;
    }

    /**
     * 设置机构表d
     *
     * @param organizationId 机构表d
     */
    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId == null ? null : organizationId.trim();
    }

    /**
     * 获取申请表Id
     *
     * @return apply_id - 申请表Id
     */
    public String getApplyId() {
        return applyId;
    }

    /**
     * 设置申请表Id
     *
     * @param applyId 申请表Id
     */
    public void setApplyId(String applyId) {
        this.applyId = applyId == null ? null : applyId.trim();
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
     * 获取入驻者组织编码
     *
     * @return org_code - 入驻者组织编码
     */
    public String getOrgCode() {
        return orgCode;
    }

    /**
     * 设置入驻者组织编码
     *
     * @param orgCode 入驻者组织编码
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    /**
     * 获取用户类型( 1.本部；2.教师；3第三方机构；4租户)
     *
     * @return type - 用户类型( 1.本部；2.教师；3第三方机构；4租户)
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置用户类型( 1.本部；2.教师；3第三方机构；4租户)
     *
     * @param type 用户类型( 1.本部；2.教师；3第三方机构；4租户)
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取负责人账号
     *
     * @return user_name - 负责人账号
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置负责人账号
     *
     * @param userName 负责人账号
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 获取负责人真实姓名
     *
     * @return real_name - 负责人真实姓名
     */
    public String getRealName() {
        return realName;
    }

    /**
     * 设置负责人真实姓名
     *
     * @param realName 负责人真实姓名
     */
    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    /**
     * 获取手机号
     *
     * @return phone - 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号
     *
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 获取状态（1. 正常 ，2 停用）
     *
     * @return status - 状态（1. 正常 ，2 停用）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态（1. 正常 ，2 停用）
     *
     * @param status 状态（1. 正常 ，2 停用）
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * 获取是否删除
     *
     * @return is_deleted - 是否删除
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否删除
     *
     * @param isDeleted 是否删除
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
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

    /**
     * 获取机构Logo图标
     *
     * @return organization_logo - 机构Logo图标
     */
    public String getOrganizationLogo() {
        return organizationLogo;
    }

    /**
     * 设置机构Logo图标
     *
     * @param organizationLogo 机构Logo图标
     */
    public void setOrganizationLogo(String organizationLogo) {
        this.organizationLogo = organizationLogo == null ? null : organizationLogo.trim();
    }

    /**
     * 获取机构网站图标
     *
     * @return web_pic - 机构网站图标
     */
    public String getWebPic() {
        return webPic;
    }

    /**
     * 设置机构网站图标
     *
     * @param webPic 机构网站图标
     */
    public void setWebPic(String webPic) {
        this.webPic = webPic == null ? null : webPic.trim();
    }

    /**
     * 获取主营类目
     *
     * @return cc_id - 主营类目
     */
    public String getCcId() {
        return ccId;
    }

    /**
     * 设置主营类目
     *
     * @param ccId 主营类目
     */
    public void setCcId(String ccId) {
        this.ccId = ccId == null ? null : ccId.trim();
    }
}