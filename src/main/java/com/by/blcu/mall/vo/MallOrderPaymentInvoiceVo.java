package com.by.blcu.mall.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ToString
public class MallOrderPaymentInvoiceVo implements Serializable {
    private static final long serialVersionUID = -4477329314760874197L;
    /**
     * 商品id
     */
    private String commodityId;

    /**
     * 商品id
     */
    private List<ChildCommoditySort> commodityList;

    /**
     * 支付方式
     */
    @NotNull(message="支付方式不能为空")
    private Integer payType;
    /**
     * 汇款人
     */
    private String remittance;
    /**
     * 汇款截图ID
     */
    private String screenshot;

    /**
     * 用户名
     */
    private String userName;
    /**
     *INTEGER
     *订单ID
     */
    private String orderId;

    /**
     *INTEGER
     *订单ID
     */
    private String piId;

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
    @NotBlank(message="姓名不能为空")
    @Max(value=10)
    private String receiver;

    /**
     *
     *手机号
     */
    @NotBlank(message="手机号不能为空")
    private String telNum;

    /**
     *
     *手机号
     */
    private Integer isBill;

}
