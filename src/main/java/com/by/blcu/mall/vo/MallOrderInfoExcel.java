package com.by.blcu.mall.vo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "mall_order_info")
public class MallOrderInfoExcel implements Serializable {
    private static final long serialVersionUID = 5148941495324152480L;
    /**
     * 订单id
     */
    @Id
    @Column(name = "order_id")
    private String orderId;

    /**
     * 用户ID
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 创建日期
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 机构
     */
    private String org;

    /**
     * 订单状态（删除：0，未删除：1）
     */
    @Column(name = "order_status")
    private Integer orderStatus;

    /**
     * 是否需要开发票（否：0，是：1）
     */
    @Column(name = "is_bill")
    private Integer isBill;

    /**
     * 是否已经开发票（否：0，是：1）
     */
    @Column(name = "isdraw_bill")
    private Integer isdrawBill;

    /**
     * 订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 支付状态(未支付：0，已支付：1)
     */
    @Column(name = "payment_status")
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
    @Column(name = "tel_num")
    private String telNum;

    /**
     * 支付方式
     */
    @Column(name = "pay_type")
    private Integer payType;

    /**
     * 原价
     */
    @Column(name = "cost_price")
    private Double costPrice;

    /**
     * 开始日期
     */
    private String excelCreateTimeStart;

    /**
     * 结束日期
     */
    private String excelCreateTimeEnd;


}