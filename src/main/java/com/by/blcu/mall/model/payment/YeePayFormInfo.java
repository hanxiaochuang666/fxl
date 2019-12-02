package com.by.blcu.mall.model.payment;

import com.by.blcu.core.utils.MapAndObjectUtils;
import com.by.blcu.core.utils.YeePayUtils;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class YeePayFormInfo {

    //请求Action
    private String action;

    //业务类型
    private String p0_Cmd = "Buy";
    //商户编号
    private String p1_MerId = "";
    //商户订单号
    private String p2_Order = "";
    //支付金额 大于等于 0.01
    private String p3_Amt = "";
    //支付金额
    private String p4_Cur = "CNY";
    //商品名称 中文需要转码 GBK GB2312
    private String p5_Pid = "";

    //商品种类
    private String p6_Pcat = "";
    //商品描述
    private String p7_Pdesc = "";
    //回调地址 （同步回调页面）
    private String p8_Url = "";
    //送货地址
    private String p9_SAF = "";
    //商户扩展信息
    private String pa_MP = "";

    //服务器通知地址 （异步回调地址）
    private String pb_ServerNotifyUrl = "";
    //支付通道编码 (不填写则跳转易宝网关 填写则跳转银行支付页面)
    private String pd_FrpId = "";
    //订单有效期 默认7 最多180
    private String pm_Period = "180";
    //订单有效期单位 默认 day
    private String pn_Unit = "day";
    //应答机制
    private String pr_NeedResponse = "1";

//---------------- 网银一键（填写后可以直接跳转网银的网关） -----------------

    //考生/用户姓名
    private String pt_UserName = "";
    //身份证号
    private String pt_PostalCode = "";
    //地区
    private String pt_Address = "";
    //报考序号/银行卡号
    private String pt_TeleNo = "";
    //手机号
    private String pt_Mobile = "";

    //邮件地址
    private String pt_Email = "";
    //用户表示
    private String pt_LeaveMessage = "";


    //安全签名数据
    private String hmac_safe;
    //签名数据
    private String hmac;



}
