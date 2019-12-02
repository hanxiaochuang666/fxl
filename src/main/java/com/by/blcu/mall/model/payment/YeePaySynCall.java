package com.by.blcu.mall.model.payment;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 易宝支付成功通知参数
 */
@Data
@ToString
public class YeePaySynCall{

    //商户编号
    private String p1_MerId = "";
    //业务类型
    private String r0_Cmd = "";
    //支付结果 (1代表成功)
    private String r1_Code = "";
    //易宝交易流水号
    private String r2_TrxId = "";
    //支付金额
    private String r3_Amt = "";

    //交易币种
    private String r4_Cur = "";
    //商品名称
    private String r5_Pid = "";
    //商户订单号
    private String r6_Order = "";
    //易宝支付会员 ID
    private String r7_Uid = "";
    //商户拓展信息
    private String r8_MP = "";
    //通知类型 (1浏览器同步 2服务器异步)
    private String r9_BType = "";

    //支付通道编码              --不参与 hmac 校验
    private String rb_BankId;
    //银行订单号                --不参与 hmac 校验
    private String ro_BankOrderId;
    //支付成功实践              --不参与 hmac 校验
    private String rp_PayDate;
    //神州行充值卡号            --不参与 hmac 校验
    private String rq_CardNo;
    //通知时间                 --不参与 hmac 校验
    private String ru_Trxtime;
    //用户手续费               --不参与 hmac 校验
    private String rq_SourceFee;
    //商户手续费               --不参与 hmac 校验
    private String rq_TargetFee;

// -------------- 签名认证 ------------------

    //安全签名数据
    private String hmac_safe;
    //签名数据
    private String hmac;




}
