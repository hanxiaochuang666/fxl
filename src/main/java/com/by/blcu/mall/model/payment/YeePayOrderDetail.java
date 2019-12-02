package com.by.blcu.mall.model.payment;

import com.by.blcu.core.utils.MapAndObjectUtils;
import com.by.blcu.core.utils.YeePayUtils;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class YeePayOrderDetail {

    //业务类型
    private String r0_Cmd = "QueryOrdDetail";
    //查询结果（1：查询正常；50：订单不存在.）
    private String r1_Code;
    //易宝交易流水号
    private String r2_TrxId;
    //支付金额
    private String r3_Amt;
    //交易币种
    private String r4_Cur;
    //商品名称
    private String r5_Pid;
    //商户订单号
    private String r6_Order;
    //商户拓展信息
    private String r8_MP;
    //退款请求号
    private String rw_RefundRequestID;
    //订单创建时间
    private String rx_CreateTime;
    //订单成功时间
    private String ry_FinshTime;
    //退款请求金额
    private String rz_RefundAmount;
    //订单支付状态
    private String rb_PayStatus;
    //已退款次数
    private String rc_RefundCount;
    //已退款金额
    private String rd_RefundAmt;

    private String hmac_safe;
    private String hmac;



}
