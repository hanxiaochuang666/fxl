package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import javax.persistence.*;

@Data
@ApiModel(description= "用户")
@Table(name = "sso_user")
public class SsoUser {

    /**
     * 自增id
     */
    @Id
    @ApiModelProperty(value = "自增id")
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
    @ApiModelProperty(value = "账号")
    @Column(name = "user_name")
    private String userName;

    /**
     *密码
     */
    @Column(name = "password")
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
     * QQ号
     */
    @ApiModelProperty(value = "QQ号")
    private  String qq;

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

}