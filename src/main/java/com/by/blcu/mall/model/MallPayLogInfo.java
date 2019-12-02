package com.by.blcu.mall.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "mall_pay_log_info")
public class MallPayLogInfo implements Serializable {
    private static final long serialVersionUID = -7554482088262174772L;
    /**
     * 返回值ID
     */
    @Id
    @Column(name = "pay_respon_id")
    private String payResponId;

    /**
     * 商户编号
     */
    @Column(name = "merchant_id")
    private String merchantId;

    /**
     * 网点编号
     */
    @Column(name = "network_id")
    private String networkId;

    /**
     * 终端编号
     */
    @Column(name = "terminal_id")
    private String terminalId;

    /**
     * 订单号
     */
    @Column(name = "order_no")
    private String orderNo;

    /**
     * 易宝订单
     */
    private String yibaoorder;

    /**
     * 金额
     */
    private String amount;

    /**
     * 返回码(成功：00，动态库失败：FF，pos机失败：其他)
     */
    @Column(name = "return_code")
    private String returnCode;

    /**
     * 卡号
     */
    @Column(name = "card_no")
    private String cardNo;

    /**
     * 交易日期
     */
    @Column(name = "pay_date")
    private String payDate;

    /**
     * 交易时间
     */
    @Column(name = "pay_time")
    private String payTime;

    /**
     * 流水号
     */
    private String trace;

    /**
     * 参考号
     */
    @Column(name = "reference_no")
    private String referenceNo;

    /**
     * 支付渠道
     */
    @Column(name = "pay_way")
    private String payWay;

    /**
     * 原数据
     */
    @Column(name = "send_data")
    private String sendData;

    /**
     * 附加数据
     */
    @Column(name = "add_data")
    private String addData;

    /**
     * 回调信息描述
     */
    private String message;

    @Column(name = "create_date")
    private Date createDate;

    private String bak1;

    private String bak2;

    private String bak3;

    private String bak4;

    private String bak5;

    /**
     * 退货信息
     */
    private String refundinf;

    /**
     * 回调请求串
     */
    private String mac;

    /**
     * 获取返回值ID
     *
     * @return pay_respon_id - 返回值ID
     */
    public String getPayResponId() {
        return payResponId;
    }

    /**
     * 设置返回值ID
     *
     * @param payResponId 返回值ID
     */
    public void setPayResponId(String payResponId) {
        this.payResponId = payResponId == null ? null : payResponId.trim();
    }

    /**
     * 获取商户编号
     *
     * @return merchant_id - 商户编号
     */
    public String getMerchantId() {
        return merchantId;
    }

    /**
     * 设置商户编号
     *
     * @param merchantId 商户编号
     */
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId == null ? null : merchantId.trim();
    }

    /**
     * 获取网点编号
     *
     * @return network_id - 网点编号
     */
    public String getNetworkId() {
        return networkId;
    }

    /**
     * 设置网点编号
     *
     * @param networkId 网点编号
     */
    public void setNetworkId(String networkId) {
        this.networkId = networkId == null ? null : networkId.trim();
    }

    /**
     * 获取终端编号
     *
     * @return terminal_id - 终端编号
     */
    public String getTerminalId() {
        return terminalId;
    }

    /**
     * 设置终端编号
     *
     * @param terminalId 终端编号
     */
    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId == null ? null : terminalId.trim();
    }

    /**
     * 获取订单号
     *
     * @return order_no - 订单号
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * 设置订单号
     *
     * @param orderNo 订单号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    /**
     * 获取易宝订单
     *
     * @return yibaoorder - 易宝订单
     */
    public String getYibaoorder() {
        return yibaoorder;
    }

    /**
     * 设置易宝订单
     *
     * @param yibaoorder 易宝订单
     */
    public void setYibaoorder(String yibaoorder) {
        this.yibaoorder = yibaoorder == null ? null : yibaoorder.trim();
    }

    /**
     * 获取金额
     *
     * @return amount - 金额
     */
    public String getAmount() {
        return amount;
    }

    /**
     * 设置金额
     *
     * @param amount 金额
     */
    public void setAmount(String amount) {
        this.amount = amount == null ? null : amount.trim();
    }

    /**
     * 获取返回码(成功：00，动态库失败：FF，pos机失败：其他)
     *
     * @return return_code - 返回码(成功：00，动态库失败：FF，pos机失败：其他)
     */
    public String getReturnCode() {
        return returnCode;
    }

    /**
     * 设置返回码(成功：00，动态库失败：FF，pos机失败：其他)
     *
     * @param returnCode 返回码(成功：00，动态库失败：FF，pos机失败：其他)
     */
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode == null ? null : returnCode.trim();
    }

    /**
     * 获取卡号
     *
     * @return card_no - 卡号
     */
    public String getCardNo() {
        return cardNo;
    }

    /**
     * 设置卡号
     *
     * @param cardNo 卡号
     */
    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    /**
     * 获取交易日期
     *
     * @return pay_date - 交易日期
     */
    public String getPayDate() {
        return payDate;
    }

    /**
     * 设置交易日期
     *
     * @param payDate 交易日期
     */
    public void setPayDate(String payDate) {
        this.payDate = payDate == null ? null : payDate.trim();
    }

    /**
     * 获取交易时间
     *
     * @return pay_time - 交易时间
     */
    public String getPayTime() {
        return payTime;
    }

    /**
     * 设置交易时间
     *
     * @param payTime 交易时间
     */
    public void setPayTime(String payTime) {
        this.payTime = payTime == null ? null : payTime.trim();
    }

    /**
     * 获取流水号
     *
     * @return trace - 流水号
     */
    public String getTrace() {
        return trace;
    }

    /**
     * 设置流水号
     *
     * @param trace 流水号
     */
    public void setTrace(String trace) {
        this.trace = trace == null ? null : trace.trim();
    }

    /**
     * 获取参考号
     *
     * @return reference_no - 参考号
     */
    public String getReferenceNo() {
        return referenceNo;
    }

    /**
     * 设置参考号
     *
     * @param referenceNo 参考号
     */
    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo == null ? null : referenceNo.trim();
    }

    /**
     * 获取支付渠道
     *
     * @return pay_way - 支付渠道
     */
    public String getPayWay() {
        return payWay;
    }

    /**
     * 设置支付渠道
     *
     * @param payWay 支付渠道
     */
    public void setPayWay(String payWay) {
        this.payWay = payWay == null ? null : payWay.trim();
    }

    /**
     * 获取原数据
     *
     * @return send_data - 原数据
     */
    public String getSendData() {
        return sendData;
    }

    /**
     * 设置原数据
     *
     * @param sendData 原数据
     */
    public void setSendData(String sendData) {
        this.sendData = sendData == null ? null : sendData.trim();
    }

    /**
     * 获取附加数据
     *
     * @return add_data - 附加数据
     */
    public String getAddData() {
        return addData;
    }

    /**
     * 设置附加数据
     *
     * @param addData 附加数据
     */
    public void setAddData(String addData) {
        this.addData = addData == null ? null : addData.trim();
    }

    /**
     * 获取回调信息描述
     *
     * @return message - 回调信息描述
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置回调信息描述
     *
     * @param message 回调信息描述
     */
    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    /**
     * @return create_date
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return bak1
     */
    public String getBak1() {
        return bak1;
    }

    /**
     * @param bak1
     */
    public void setBak1(String bak1) {
        this.bak1 = bak1 == null ? null : bak1.trim();
    }

    /**
     * @return bak2
     */
    public String getBak2() {
        return bak2;
    }

    /**
     * @param bak2
     */
    public void setBak2(String bak2) {
        this.bak2 = bak2 == null ? null : bak2.trim();
    }

    /**
     * @return bak3
     */
    public String getBak3() {
        return bak3;
    }

    /**
     * @param bak3
     */
    public void setBak3(String bak3) {
        this.bak3 = bak3 == null ? null : bak3.trim();
    }

    /**
     * @return bak4
     */
    public String getBak4() {
        return bak4;
    }

    /**
     * @param bak4
     */
    public void setBak4(String bak4) {
        this.bak4 = bak4 == null ? null : bak4.trim();
    }

    /**
     * @return bak5
     */
    public String getBak5() {
        return bak5;
    }

    /**
     * @param bak5
     */
    public void setBak5(String bak5) {
        this.bak5 = bak5 == null ? null : bak5.trim();
    }

    /**
     * 获取退货信息
     *
     * @return refundinf - 退货信息
     */
    public String getRefundinf() {
        return refundinf;
    }

    /**
     * 设置退货信息
     *
     * @param refundinf 退货信息
     */
    public void setRefundinf(String refundinf) {
        this.refundinf = refundinf == null ? null : refundinf.trim();
    }

    /**
     * 获取回调请求串
     *
     * @return mac - 回调请求串
     */
    public String getMac() {
        return mac;
    }

    /**
     * 设置回调请求串
     *
     * @param mac 回调请求串
     */
    public void setMac(String mac) {
        this.mac = mac == null ? null : mac.trim();
    }
}