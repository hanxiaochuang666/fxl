package com.by.blcu.mall.model.payment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class PaySynCall implements Serializable {

    private static final long serialVersionUID = -7980777577530422977L;

    //交易时间 yyyyMMddHHmmss
    private String orderDate;

    //订单号 最大20位
    private String orderNo;

    //订单金额 0.01~100000000.00
    private String amount;

    //支付交易流水号 String(30)
    private String jylsh;

    //订单支付状态 1成功 2失败 3可疑
    private String tranStat;

    //通知类型 1同步 2异步
    private String return_type;

    //订单签名数据
    private String sign;

    //平台交易流水号（校园平台）
    private String tradeNo;

    //平台支付方式（WX：微信支付）
    private String payMethod;

}
