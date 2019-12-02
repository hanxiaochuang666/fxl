package com.by.blcu.mall.model.payment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class PayFormInfo implements Serializable{

    private static final long serialVersionUID = 2787244319287980592L;

    //跳转Action
    private String action;

    //交易时间 yyyyMMddHHmmss
    private String orderDate;

    //订单号 最大20位
    private String orderNo;

    //订单金额 0.01~100000000.00
    private String amount;

    //支付平台批次号
    private String xmpch;

    //同步通知路径
    private String return_url;

    //异步通知路径
    private String notify_url;

    //签名
    private String sign;


}
