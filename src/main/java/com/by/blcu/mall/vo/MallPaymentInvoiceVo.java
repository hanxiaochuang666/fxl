package com.by.blcu.mall.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@ToString
public class MallPaymentInvoiceVo implements Serializable {
    private static final long serialVersionUID = -4477329314760874197L;
    /**
     *VARCHAR
     *
     */
    private String piId;

    /**
     *INTEGER
     *订单ID
     */
    private String orderId;

    /**
     *VARCHAR
     *邮寄地址
     */
    private String address;

    /**
     *VARCHAR
     *邮政编码
     */
    private String zipCode;

    /**
     *INTEGER
     *发票抬头（0 个人，1单位）
     */
    private Integer invoice;

    /**
     *VARCHAR
     *公司名称
     */
    private String companyName;

    /**
     *VARCHAR
     *纳税人识别号
     */
    private String taxpayerNumber;

    /**
     *VARCHAR
     *发票类型
     */
    private String type;

    /**
     *VARCHAR
     *注册地址
     */
    private String registeredAddress;

    /**
     *VARCHAR
     *注册电话
     */
    private String registeredPhone;

    /**
     *VARCHAR
     *开户行
     */
    private String bank;

    /**
     *VARCHAR
     *公司账号
     */
    private String companyAccount;

    /**
     *VARCHAR
     *备用
     */
    private String bak;

    /**
     *TIMESTAMP
     *创建时间
     */
    private Date createTime;

    /**
     *VARCHAR
     *创建人
     */
    private String createUser;

    /**
     *TIMESTAMP
     *
     */
    private Date updateTime;

    /**
     *VARCHAR
     *更新人
     */
    private String updateUser;

    /**
     *VARCHAR
     *收获人
     */
    private String receiver;

    /**
     *
     *手机号
     */
    private String telNum;

    /**
     *
     *支付方式
     */
    private Integer payType;


}
