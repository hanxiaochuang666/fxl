package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sso_user")
@ApiModel(description= "用户")
public class SsoUser {
    /**
     *自增Id
     */
    @Id
    @ApiModelProperty(value = "自增Id")
    private Integer id;

    /**
     *统一用户表Id
     */
    @Column(name = "user_id")
    @ApiModelProperty(value = "统一用户表Id")
    private String userId;

    /**
     *账号
     */
    @Column(name = "user_name")
    @ApiModelProperty(value = "账号")
    private String userName;

    /**
     *密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     *真实姓名
     */
    @Column(name = "real_name")
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     *用户状态（1 正常，2停用，3锁定）
     */
    @ApiModelProperty(value = "用户状态（1 正常，2停用，3锁定）")
    private Integer status;

    /**
     *头像地址
     */
    @Column(name = "header_url")
    @ApiModelProperty(value = "头像地址")
    private String headerUrl;

    /**
     *昵称
     */
    @Column(name = "nick_name")
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     *性别(0 未知；1女；2男)
     */
    @ApiModelProperty(value = "性别(0 未知；1女；2男)")
    private Integer sex;

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
     *QQ号
     */
    @ApiModelProperty(value = "QQ号")
    private String qq;

    /**
     *绑定QQ
     */
    @Column(name = "q_q_open_id")
    @ApiModelProperty(value = "绑定QQ")
    private String qQOpenId;

    /**
     *绑定微信
     */
    @Column(name = "wechat_open_id")
    @ApiModelProperty(value = "绑定微信")
    private String wechatOpenId;

    /**
     *学历
     */
    @ApiModelProperty(value = "学历")
    private String education;

    /**
     *所在省
     */
    @ApiModelProperty(value = "所在省")
    private String province;

    /**
     *所在市
     */
    @ApiModelProperty(value = "所在市")
    private String city;

    /**
     *收货人姓名
     */
    @Column(name = "consignee_name")
    @ApiModelProperty(value = "收货人姓名")
    private String consigneeName;

    /**
     *收货人手机号
     */
    @Column(name = "consignee_phone")
    @ApiModelProperty(value = "收货人手机号")
    private String consigneePhone;

    /**
     *收货人地址（省）
     */
    @Column(name = "consignee_province")
    @ApiModelProperty(value = "收货人地址（省）")
    private String consigneeProvince;

    /**
     *收货人地址（市）
     */
    @Column(name = "consignee_city")
    @ApiModelProperty(value = "收货人地址（市）")
    private String consigneeCity;

    /**
     *收货人地址（区）
     */
    @Column(name = "consignee_district")
    @ApiModelProperty(value = "收货人地址（区）")
    private String consigneeDistrict;

    /**
     *收货人地址（详细）
     */
    @Column(name = "consignee_address")
    @ApiModelProperty(value = "收货人地址（详细）")
    private String consigneeAddress;

    /**
     *排序
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
     * 获取自增Id
     *
     * @return id - 自增Id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置自增Id
     *
     * @param id 自增Id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取统一用户表Id
     *
     * @return user_id - 统一用户表Id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置统一用户表Id
     *
     * @param userId 统一用户表Id
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取账号
     *
     * @return user_name - 账号
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置账号
     *
     * @param userName 账号
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
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
     * 获取用户状态（1 正常，2停用，3锁定）
     *
     * @return status - 用户状态（1 正常，2停用，3锁定）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置用户状态（1 正常，2停用，3锁定）
     *
     * @param status 用户状态（1 正常，2停用，3锁定）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取头像地址
     *
     * @return header_url - 头像地址
     */
    public String getHeaderUrl() {
        return headerUrl;
    }

    /**
     * 设置头像地址
     *
     * @param headerUrl 头像地址
     */
    public void setHeaderUrl(String headerUrl) {
        this.headerUrl = headerUrl == null ? null : headerUrl.trim();
    }

    /**
     * 获取昵称
     *
     * @return nick_name - 昵称
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * 设置昵称
     *
     * @param nickName 昵称
     */
    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    /**
     * 获取性别(0 未知；1女；2男)
     *
     * @return sex - 性别(0 未知；1女；2男)
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * 设置性别(0 未知；1女；2男)
     *
     * @param sex 性别(0 未知；1女；2男)
     */
    public void setSex(Integer sex) {
        this.sex = sex;
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
     * 获取QQ号
     *
     * @return qq - QQ号
     */
    public String getQq() {
        return qq;
    }

    /**
     * 设置QQ号
     *
     * @param qq QQ号
     */
    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    /**
     * 获取绑定QQ
     *
     * @return q_q_open_id - 绑定QQ
     */
    public String getQQOpenId() {
        return qQOpenId;
    }

    /**
     * 设置绑定QQ
     *
     * @param qQOpenId 绑定QQ
     */
    public void setQQOpenId(String qQOpenId) {
        this.qQOpenId = qQOpenId == null ? null : qQOpenId.trim();
    }

    /**
     * 获取绑定微信
     *
     * @return wechat_open_id - 绑定微信
     */
    public String getWechatOpenId() {
        return wechatOpenId;
    }

    /**
     * 设置绑定微信
     *
     * @param wechatOpenId 绑定微信
     */
    public void setWechatOpenId(String wechatOpenId) {
        this.wechatOpenId = wechatOpenId == null ? null : wechatOpenId.trim();
    }

    /**
     * 获取学历
     *
     * @return education - 学历
     */
    public String getEducation() {
        return education;
    }

    /**
     * 设置学历
     *
     * @param education 学历
     */
    public void setEducation(String education) {
        this.education = education == null ? null : education.trim();
    }

    /**
     * 获取所在省
     *
     * @return province - 所在省
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置所在省
     *
     * @param province 所在省
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * 获取所在市
     *
     * @return city - 所在市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置所在市
     *
     * @param city 所在市
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * 获取收货人姓名
     *
     * @return consignee_name - 收货人姓名
     */
    public String getConsigneeName() {
        return consigneeName;
    }

    /**
     * 设置收货人姓名
     *
     * @param consigneeName 收货人姓名
     */
    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName == null ? null : consigneeName.trim();
    }

    /**
     * 获取收货人手机号
     *
     * @return consignee_phone - 收货人手机号
     */
    public String getConsigneePhone() {
        return consigneePhone;
    }

    /**
     * 设置收货人手机号
     *
     * @param consigneePhone 收货人手机号
     */
    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone == null ? null : consigneePhone.trim();
    }

    /**
     * 获取收货人地址（省）
     *
     * @return consignee_province - 收货人地址（省）
     */
    public String getConsigneeProvince() {
        return consigneeProvince;
    }

    /**
     * 设置收货人地址（省）
     *
     * @param consigneeProvince 收货人地址（省）
     */
    public void setConsigneeProvince(String consigneeProvince) {
        this.consigneeProvince = consigneeProvince == null ? null : consigneeProvince.trim();
    }

    /**
     * 获取收货人地址（市）
     *
     * @return consignee_city - 收货人地址（市）
     */
    public String getConsigneeCity() {
        return consigneeCity;
    }

    /**
     * 设置收货人地址（市）
     *
     * @param consigneeCity 收货人地址（市）
     */
    public void setConsigneeCity(String consigneeCity) {
        this.consigneeCity = consigneeCity == null ? null : consigneeCity.trim();
    }

    /**
     * 获取收货人地址（区）
     *
     * @return consignee_district - 收货人地址（区）
     */
    public String getConsigneeDistrict() {
        return consigneeDistrict;
    }

    /**
     * 设置收货人地址（区）
     *
     * @param consigneeDistrict 收货人地址（区）
     */
    public void setConsigneeDistrict(String consigneeDistrict) {
        this.consigneeDistrict = consigneeDistrict == null ? null : consigneeDistrict.trim();
    }

    /**
     * 获取收货人地址（详细）
     *
     * @return consignee_address - 收货人地址（详细）
     */
    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    /**
     * 设置收货人地址（详细）
     *
     * @param consigneeAddress 收货人地址（详细）
     */
    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress == null ? null : consigneeAddress.trim();
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
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
}