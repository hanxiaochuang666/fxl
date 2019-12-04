package com.by.blcu.mall.vo;

import com.by.blcu.mall.model.MallPaymentInvoice;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class MallOrderInfoVo implements Serializable {
    private static final long serialVersionUID = -7448627747231817950L;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 用户ID
     */
    private String userName;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 支付日期
     */
    private Date paymentTime;

    /**
     * 机构
     */
    private String org;

    /**
     * 机构名字
     */
    private String orgName;

    /**
     * 订单状态（删除：0，未删除：1）
     */
    private Integer orderStatus;

    /**
     * 是否需要开发票（否：0，是：1）
     */
    private Integer isBill;

    /**
     * 是否已经开发票（否：0，是：1）
     */
    private Integer isdrawBill;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 支付状态(未支付：0，已支付：1)
     */
    private Integer paymentStatus;

    /**
     * 总金额
     */
    private Double amount;

    /**
     * 收货人
     */
    private String receiver;

    /**
     * 手机号
     */
    private String telNum;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 原价
     */
    private Double costPrice;

    /**
     * 汇款人
     */
    private String remittance;

    /**
     * 汇款截图
     */
    private String screenshot;

    /**
     * 汇款截图展示
     */
    private String screenshotView;

    private List<MallCommodityOrderVo> MallCommodityOrderVoList;

    private MallPaymentInvoice mallPaymentInvoice;

    /**
     * 开始日期
     */
    private Date createTimeStart;

    /**
     * 结束日期
     */
    private Date createTimeEnd;

}
