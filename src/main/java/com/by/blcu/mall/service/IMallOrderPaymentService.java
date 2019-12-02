package com.by.blcu.mall.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.mall.model.MallOrderInfo;
import com.by.blcu.mall.model.MallPayLogInfo;
import com.by.blcu.mall.model.payment.PayFormInfo;
import com.by.blcu.mall.vo.MallPaymentInvoiceVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public interface IMallOrderPaymentService extends IBaseService {

    /**
     * 根据支付渠道初始化
     * @param payChannel
     * @param paymentInvoiceVo
     * @param request
     * @return
     * @throws Exception
     */
    RetResult initiatePayment(String payChannel, MallPaymentInvoiceVo paymentInvoiceVo, HttpServletRequest request) throws Exception;

    /**
     * pos机参数初始化
     * @param orderNo
     * @return
     * @throws Exception
     */
    String initPos(String orderNo,Double amount) throws Exception;

    /**
     *  发起校园支付
     * @param order 订单信息
     * @param request
     * @return
     * @throws Exception
     */
    PayFormInfo campusPay(MallOrderInfo order, HttpServletRequest request) throws Exception;


    /**
     * 验证支付回调信息
     * @param orderDate   交易时间
     * @param orderNo     订单号
     * @param amount      订单金额
     * @param jylsh       支付平台流水号
     * @param tranStat    订单支付状态 1成功 2失败 3可疑
     * @param return_type 1同步通知 2异步通知
     * @param sign        签名
     * @return
     * @throws Exception
     */
    Boolean verificationSign(String orderDate, String orderNo, String amount, String jylsh,
                             String tranStat, String return_type, String sign ) throws Exception;

    /**
     * 同步学生购买课程资源
     * @param order
     * @return
     * @throws Exception
     */
    Boolean syncCourseResources(MallOrderInfo order) throws Exception;

    Boolean insertPaylog(MallPayLogInfo mallPayLogInfo);

    /**
     * 同步学生购买课程资源 商品再次上架使用
     * @param orderId
     * @param userName
     * @return
     * @throws Exception
     */
    Boolean syncCourseResourcesForShelf(String orderId, String userName) throws Exception;

    void queryCourseIdByOrderId(String orderId, Set courseIdSet) throws Exception;
}