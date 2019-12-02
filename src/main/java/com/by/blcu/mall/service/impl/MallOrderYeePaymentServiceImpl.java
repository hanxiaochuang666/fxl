package com.by.blcu.mall.service.impl;

import com.by.blcu.core.configurer.PaymentConfig;
import com.by.blcu.core.constant.Constants;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.*;
import com.by.blcu.mall.dao.IMallOrderPaymentDao;
import com.by.blcu.mall.model.MallOrderInfo;
import com.by.blcu.mall.model.MallOrderPayment;
import com.by.blcu.mall.model.MallPayLogInfo;
import com.by.blcu.mall.model.payment.YeePayFormInfo;
import com.by.blcu.mall.model.payment.YeePayOrderReq;
import com.by.blcu.mall.model.payment.YeePaySynCall;
import com.by.blcu.mall.service.IMallOrderPaymentService;
import com.by.blcu.mall.service.MallOrderInfoService;
import com.by.blcu.mall.service.MallOrderYeePaymentService;
import com.by.blcu.mall.service.MallPayLogInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MallOrderYeePaymentServiceImpl implements MallOrderYeePaymentService {

    @Resource
    private IMallOrderPaymentDao mallOrderPaymentDao;
    @Resource
    private MallOrderInfoService mallOrderInfoService;
    @Resource
    private PaymentConfig paymentConfig;
    @Resource
    private IMallOrderPaymentService mallOrderPaymentService;
    @Resource
    private MallPayLogInfoService mallPayLogInfoService;

    @Override
    @Transactional
    public YeePayFormInfo yeePay(MallOrderInfo order, HttpServletRequest request) throws Exception {
        String username = (String) request.getAttribute("username");
        String orderNo = order.getOrderNo();
        Double amount = order.getAmount();

        //0.查看是否存在支付中记录表信息
        Map param = MapUtils.initMap("orderNo", orderNo);
        param.put("_sort_line", "create_time");//按照创建时间 默认倒序
        List<MallOrderPayment> list = mallOrderPaymentDao.selectList(param);
        if (list == null || list.size() == 0) {
            //1.创建支付订单
            MallOrderPayment orderPayment = new MallOrderPayment();
            orderPayment.setPid(ApplicationUtils.getUUID());
            orderPayment.setOrderNo(orderNo);
            orderPayment.setAmount(new BigDecimal(amount));
            orderPayment.setCurrency("CNY");
            orderPayment.setPayStatus(String.valueOf(Constants.READY_TO_PAY));//支付中
            orderPayment.setPayType("YEE_PAY");//根据传入记录
            orderPayment.setCreateTime(new Date());
            orderPayment.setCreateUser(username);
            Integer state = mallOrderPaymentDao.insert(orderPayment);
            if (state != 1)
                throw new ServiceException("易宝支付信息提交失败！订单号：" + orderNo);
        } else {
            MallOrderPayment orderPayment = list.get(0);
            if (String.valueOf(Constants.READY_TO_PAY).equals(orderPayment.getPayStatus())) {
                log.info("订单号：" + orderNo + " 继续发起易宝支付！");
            } else {
                throw new ServiceException("订单支付状态有误！订单号：" + orderNo);
            }
        }

        //2.更新订单信息
        mallOrderInfoService.update(order);

        //3.发起易宝支付
        YeePayFormInfo yeePayFormInfo = new YeePayFormInfo();
        yeePayFormInfo.setAction(paymentConfig.getRequestURL());
        yeePayFormInfo.setP1_MerId(ApplicationUtils.decryptToken(paymentConfig.getP1_MerId()));
        yeePayFormInfo.setP2_Order(orderNo);
        yeePayFormInfo.setP3_Amt(String.format("%.2f", amount));

        yeePayFormInfo.setP6_Pcat("Online Education");
        yeePayFormInfo.setP8_Url(paymentConfig.getP8_Url());
        yeePayFormInfo.setPb_ServerNotifyUrl(paymentConfig.getPb_ServerNotifyUrl());

        String key = ApplicationUtils.decryptToken(paymentConfig.getKeyValue());
        yeePayFormInfo.setHmac_safe(getHmac_safe(yeePayFormInfo, key));
        yeePayFormInfo.setHmac(getHmac(yeePayFormInfo, key));

        return yeePayFormInfo;
    }

    @Override
    @Transactional
    public Boolean verificationYeePay(YeePaySynCall yeePaySynCall) throws Exception {
        String backType = ("1".equals(yeePaySynCall.getR9_BType())) ? "浏览器重定向" : "服务器点对点通讯";
        //0.记录回调日志到数据库
        MallPayLogInfo payBackLog = new MallPayLogInfo();
        payBackLog.setPayResponId(ApplicationUtils.getUUID());
        payBackLog.setOrderNo(yeePaySynCall.getR6_Order());
        payBackLog.setAmount(yeePaySynCall.getR3_Amt());
        payBackLog.setPayWay("YEE_PAY");
        payBackLog.setMac(yeePaySynCall.toString());
        payBackLog.setCreateDate(new Date());
        mallPayLogInfoService.insert(payBackLog);

        //1.验证签名
        String key = ApplicationUtils.decryptToken(paymentConfig.getKeyValue());
        String checkHmac_safe = getHmac_safe(yeePaySynCall, key);
        String checkHmac = getHmac(yeePaySynCall, key);
        if (!yeePaySynCall.getHmac_safe().equals(checkHmac_safe) || !yeePaySynCall.getHmac().equals(checkHmac)) {
            payBackLog.setMessage("易宝支付【" + backType + "】回调，签名验证失败！订单号：" + yeePaySynCall.getR6_Order());//回调信息描述
            mallPayLogInfoService.update(payBackLog);
            return false;
        } else {
            String orderNo = yeePaySynCall.getR6_Order();
            String resultCode = yeePaySynCall.getR1_Code();
            String amount = yeePaySynCall.getR3_Amt();

            //2.根据订单号获取订单
            MallOrderInfo order = mallOrderInfoService.selectBy("orderNo", orderNo);
            if (order == null) {
                throw new ServiceException("回传订单号不存在！订单号：" + yeePaySynCall.getR6_Order() + " 回调时间：" + DateUtils.formatFullTime(LocalDateTime.now()));
            }
            if (order.getPaymentStatus() == 1) {
                log.info("订单号：" + order.getOrderNo() + " 易宝支付成功！接收【" + backType + "】易宝支付请求成功");
                payBackLog.setMessage("易宝支付【" + backType + "】回调成功，订单已支付成功！订单号：" + orderNo+"，付款金额："+amount+" 元");//回调信息描述
                mallPayLogInfoService.update(payBackLog);
                return true;
            }

            //3.更新订单支付状态
            if ("1".equals(resultCode)) {
                payBackLog.setMessage("易宝支付【" + backType + "】回调成功并更新订单，订单支付成功！订单号：" + orderNo+"，付款金额："+amount+" 元");//回调信息描述
                mallPayLogInfoService.update(payBackLog);
                return updateOrderPaymentStatus(order, true, yeePaySynCall.getR2_TrxId());
            } else {
                payBackLog.setMessage("易宝支付【" + backType + "】回调成功并更新订单，订单支付失败！订单号：" + orderNo+"，付款金额："+amount+" 元");//回调信息描述
                mallPayLogInfoService.update(payBackLog);
                return updateOrderPaymentStatus(order, false, yeePaySynCall.getR2_TrxId());
            }

        }
    }

    @Override
    @Transactional
    public Boolean queryPayResult(MallOrderInfo order) throws Exception {
        String orderNo = order.getOrderNo();
        YeePayOrderReq orderReq = new YeePayOrderReq();
        Map<String, String> resultMap = new HashMap();//存放回显结果串

        //0.判断传入订单信息支付状态正常
        order = mallOrderInfoService.selectBy("orderNo", orderNo);
        if (order == null) {
            throw new ServiceException("该订单号不存在！订单号：" + orderNo);
        }

        if (order.getPaymentStatus() == 1) {
            return true;
        }

        //1.准备请求参数
        orderReq.setP1_MerId(ApplicationUtils.decryptToken(paymentConfig.getP1_MerId()));
        orderReq.setP2_Order(orderNo);
        String key = ApplicationUtils.decryptToken(paymentConfig.getKeyValue());
        orderReq.setHmac_safe(getHmac_safe(orderReq, key));
        orderReq.setHmac(getHmac(orderReq, key));

        //2.服务器点对点调用
        HttpResponse response = HttpReqUtil.getPayResponse(paymentConfig.getQueryURL(), getYeeParam(orderReq));
        int stateCode = response.getStatusLine().getStatusCode();
        String result = "";
        if (stateCode == 200) {
            result = EntityUtils.toString(response.getEntity());

            log.info("易宝支付订单查询结果：" + result);
            String[] payResult = result.split("\n");
            for (String payString : payResult) {
                String[] payArr = payString.split("=");
                if (payArr.length > 1) {
                    resultMap.put(payArr[0], payArr[1]);
                }
            }

            //3.判断支付结果
            String r1_Code = resultMap.get("r1_Code");//支付状态，1查询正常，50订单不存在
            String r2_TrxId = resultMap.get("r2_TrxId");//易宝流水号
            String rb_PayStatus = resultMap.get("rb_PayStatus");//INIT未支付；CANCELED已取消；SUCCESS已支付
            if ("1".equals(r1_Code)) {

                //4.更新相关订单支付状态
                if ("SUCCESS".equals(rb_PayStatus)) {

                    //记录查询结果
                    MallPayLogInfo payBackLog = new MallPayLogInfo();
                    payBackLog.setPayResponId(ApplicationUtils.getUUID());
                    payBackLog.setOrderNo(orderNo);
                    payBackLog.setAmount(resultMap.get("r3_Amt"));
                    payBackLog.setPayWay("YEE_PAY");
                    payBackLog.setMac(result);
                    payBackLog.setMessage("易宝支付查询支付结果，订单支付成功！订单号："+orderNo+"，付款金额："+resultMap.get("r3_Amt")+" 元");
                    payBackLog.setCreateDate(new Date());
                    mallPayLogInfoService.insert(payBackLog);

                    return updateOrderPaymentStatus(order, true, r2_TrxId);
                } else {
                    log.info("当前订单尚未支付成功，支付状态：" + rb_PayStatus + "，订单号：" + orderNo);
                    return updateOrderPaymentStatus(order, false, r2_TrxId);
                }
            } else {
                log.info("该订单不存在！订单号：" + orderNo);
                throw new ServiceException("该订单不存在！订单号：" + orderNo);
            }
        } else {
            result = EntityUtils.toString(response.getEntity());
            log.error("查询易宝支付订单失败，订单号：" + orderNo + "返回信息：" + result);
            throw new ServiceException("查询易宝支付订单失败！订单号：" + orderNo);
        }
    }

    @Override
    @Transactional
    public Boolean updateOrderPaymentStatus(MallOrderInfo order, Boolean payFlag, String serialNumber) throws Exception {
        String orderNo = order.getOrderNo();

        //1.获取支付流水信息
        Map param = MapUtils.initMap("orderNo", orderNo);
        param.put("_sort_line", "create_time");//按照创建时间 默认倒序
        List<MallOrderPayment> list = mallOrderPaymentDao.selectList(param);
        if (list == null || list.size() == 0) {
            log.info("不存在订单待支付信息！订单号：" + orderNo);
            throw new ServiceException("不存在订单待支付信息！订单号：" + orderNo);
        }

        //2.验证订单支付结果
        MallOrderPayment orderPayment = list.get(0);
        if (payFlag) {

            //3.更新订单流水状态，订单状态，同步课程信息
            if (!"1".equals(orderPayment.getPayStatus())) {
                orderPayment.setBillNo(serialNumber);
                orderPayment.setPayStatus("1");//0未支付，1支付成功，2支付中，3支付失败
                orderPayment.setUpdateTime(new Date());
                mallOrderPaymentDao.updateByPrimaryKey(orderPayment);
                order.setPaymentStatus(Constants.PAY_SUCCESS);//支付成功 更新订单状态
                mallOrderInfoService.update(order);

                Boolean flag = mallOrderPaymentService.syncCourseResources(order);
                if (flag) {
                    log.info("订单号：" + order.getOrderNo() + " 易宝支付-课程资源同步成功！");
                } else {
                    log.info("订单号：" + order.getOrderNo() + " 易宝支付-课程资源同步失败！");
                }
            }

        } else {
            //支付失败
            /*order.setPaymentStatus(Constants.PAY_FAILED);//支付失败
            mallOrderInfoService.update(order);*/
            orderPayment.setRemarks("易宝支付失败订单，流水号："+serialNumber);
            orderPayment.setPayStatus("3");
            mallOrderPaymentDao.updateByPrimaryKeySelective(orderPayment);
            log.info("订单号：" + orderNo + "，易宝支付结果失败 ");
            return false;
        }
        return true;
    }


    private static String getHmac_safe(Object yeePayObject, String key) {
        String[] strArr = MapAndObjectUtils.getHmac(yeePayObject);
        return YeePayUtils.getHmac_safe(strArr, key);
    }

    private static String getHmac(Object yeePayObject, String key) {
        String[] hmacArr = MapAndObjectUtils.getHmac(yeePayObject);
        return YeePayUtils.getHmac(hmacArr, key);
    }

    //获取 param 方法类
    private String getYeeParam(Object obj) {
        StringBuffer str = new StringBuffer();
        if (obj == null) {
            return null;
        }
        try {
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                if (declaredField.get(obj) == null) continue;
                if (StringUtils.isBlank(declaredField.get(obj).toString())) {
                    continue;
                } else {
                    str.append(declaredField.getName() + "=");
                    str.append(declaredField.get(obj).toString());
                    str.append("&");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (str.length() > 1) {
            str.delete(str.length() - 1, str.length());
        }
        return str.toString();
    }

    public static void main(String[] args) {

        String p1_MerId = "";
        String keyValue = "";

        String encrypt_p1 = ApplicationUtils.encryptToken(p1_MerId);
        String encrypt_keyValue = ApplicationUtils.encryptToken(keyValue);

        System.out.println("商户编号加密后：" + encrypt_p1);
        System.out.println("商户密钥加密后：" + encrypt_keyValue);

        System.out.println("编号解密：" + ApplicationUtils.decryptToken(encrypt_p1));
        System.out.println("密钥解密：" + ApplicationUtils.decryptToken(encrypt_keyValue));
    }
}
