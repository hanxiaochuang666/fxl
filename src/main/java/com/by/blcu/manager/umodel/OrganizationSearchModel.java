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

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId == null ? null : organizationId.trim();
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName == null ? null : organizationName.trim();
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }
}
