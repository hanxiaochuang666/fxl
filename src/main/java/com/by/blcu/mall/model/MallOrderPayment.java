package com.by.blcu.mall.model;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ToString
public class MallOrderPayment {
    /**
	 *VARCHAR
	 *支付ID
	 */
    private String pid;

    /**
	 *VARCHAR
	 *订单号
	 */
    private String orderNo;

    /**
	 *VARCHAR
	 *支付流水号
	 */
    private String billNo;

    /**
	 *DECIMAL
	 *金额
	 */
    private BigDecimal amount;

    /**
	 *VARCHAR
	 *币种
	 */
    private String currency;

    /**
	 *VARCHAR
	 *支付状态（0支付中，1支付成功，2支付失败）
	 */
    private String payStatus;

    /**
	 *VARCHAR
	 *支付方式（CAMPUS_PAY，YIBAO，CASH，POS）
	 */
    private String payType;

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
	 *备注字段
	 */
    private String remarks;

    /**
	 *VARCHAR
	 *备用
	 */
    private String bak;

	/**
	 * VARCHAR
	 * 汇款人
	 */
    private String remittance;
	/**
	 * VARCHAR
	 * 汇款截图
	 */
	private String screenshot;

}