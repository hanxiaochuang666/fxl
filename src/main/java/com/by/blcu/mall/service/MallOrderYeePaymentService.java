package com.by.blcu.mall.service;

import com.by.blcu.mall.model.MallOrderInfo;
import com.by.blcu.mall.model.payment.YeePayFormInfo;
import com.by.blcu.mall.model.payment.YeePayOrderReq;
import com.by.blcu.mall.model.payment.YeePaySynCall;

import javax.servlet.http.HttpServletRequest;

public interface MallOrderYeePaymentService {

    /**
     * 发起易宝支付
     * @param order
     * @param request
     * @return
     * @throws Exception
     */
    YeePayFormInfo yeePay(MallOrderInfo order, HttpServletRequest request) throws Exception;

    /**
     * 验证易宝异步回调
     * @param yeePaySynCall
     * @return
     * @throws Exception
     */
    Boolean verificationYeePay(YeePaySynCall yeePaySynCall) throws Exception;

    /**
     * 查询易宝订单支付状态
     * @param order
     * @return
     * @throws Exception
     */
    Boolean queryPayResult(MallOrderInfo order) throws Exception;

    /**
     * 判断订单状态并更新
     * @param order
     * @param payFlag
     * @param serialNumber
     * @return
     * @throws Exception
     */
    Boolean updateOrderPaymentStatus(MallOrderInfo order, Boolean payFlag, String serialNumber) throws Exception;

}
