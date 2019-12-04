package com.by.blcu.mall.service.impl;

import com.by.blcu.core.configurer.PaymentConfig;
import com.by.blcu.core.constant.Constants;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.DateUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.service.ICourseService;
import com.by.blcu.mall.dao.IMallOrderPaymentDao;
import com.by.blcu.mall.model.MallOrderInfo;
import com.by.blcu.mall.model.MallOrderPayment;
import com.by.blcu.mall.model.MallPayLogInfo;
import com.by.blcu.mall.model.payment.PayFormInfo;
import com.by.blcu.mall.service.IMallOrderPaymentService;
import com.by.blcu.mall.service.MallOrderInfoService;
import com.by.blcu.mall.service.MallOrderYeePaymentService;
import com.by.blcu.mall.service.MallPayLogInfoService;
import com.by.blcu.mall.vo.CommodityInfoFileVo;
import com.by.blcu.mall.vo.MallCommodityOrderVo;
import com.by.blcu.mall.vo.MallOrderInfoVo;
import com.by.blcu.mall.vo.MallPaymentInvoiceVo;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.service.SsoUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service("mallOrderPaymentService")
public class MallOrderPaymentServiceImpl extends BaseServiceImpl implements IMallOrderPaymentService {

    @Resource
    private IMallOrderPaymentDao mallOrderPaymentDao;
    @Resource
    private MallOrderInfoService mallOrderInfoService;
    @Resource
    private SsoUserService ssoUserService;
    @Resource
    private ICourseService courseService;
    @Resource
    private PaymentConfig paymentConfig;
    @Resource
    private MallOrderYeePaymentService yeePaymentService;
    @Resource
    private MallPayLogInfoService mallPayLogInfoService;
    @Resource
    private IMallOrderPaymentService mallOrderPaymentService;

    @Override
    protected IBaseDao getDao() {
        return this.mallOrderPaymentDao;
    }

    @Value("${pos.communication}")
    private String communication;

    @Value("${pos.payDirect}")
    private String payDirect;

    @Value("${pos.merchantId}")
    private String merchantId;

    @Value("${pos.networkId}")
    private String networkId;

    @Value("${pos.terminalId}")
    private String terminalId;

    @Value("${pos.yibaoorder}")
    private String yibaoorder;

    @Value("${pos.metadata}")
    private String metadata;

    @Value("${pos.additionalData}")
    private String additionalData;

    @Override
    public RetResult initiatePayment(String payChannel, MallPaymentInvoiceVo paymentInvoiceVo, HttpServletRequest request) throws Exception {
        Object payResult = null;
        String paymentDescription = "";

        if (StringUtils.isBlank(paymentInvoiceVo.getOrderId())) {
            throw new ServiceException("订单ID不能为空！");
        }

        //1.判断订单状态 支付中状态可以重复发起
        MallOrderInfo order = mallOrderInfoService.selectById(paymentInvoiceVo.getOrderId());
        if (order != null) {
            if (order.getPaymentStatus() == Constants.READY_TO_PAY) {
                try {
                    order.setPayType(Integer.valueOf(payChannel));
                    if (Constants.CAMPUS_PAY.equals(payChannel)) {
                        paymentDescription = "校园支付";
                        log.info("订单号：" + order.getOrderNo() + " 发起" + paymentDescription);
                        payResult = campusPay(order, request);
                    } else if (Constants.YEE_PAY.equals(payChannel)) {
                        paymentDescription = "易宝支付";
                        log.info("订单号：" + order.getOrderNo() + " 发起" + paymentDescription);
                        payResult = yeePaymentService.yeePay(order, request);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    throw new ServiceException("发起" + paymentDescription + "失败！订单号：" + order.getOrderNo());
                }
            } else {
                String orderStatus = "";
                if (order.getPaymentStatus() == 1) orderStatus = "[支付成功]";
                if (order.getPaymentStatus() == 3) orderStatus = "[支付失败]";
                log.info("该订单状态为" + orderStatus + "非待支付，订单号：" + order.getOrderNo());
                throw new ServiceException("该订单状态为" + orderStatus + "非待支付，订单号：" + order.getOrderNo());
            }
        } else {
            log.info("该订单不存在，订单号：" + order.getOrderNo());
            throw new ServiceException("该订单不存在，订单号：" + order.getOrderNo());
        }

        return RetResponse.makeOKRsp(payResult);
    }

    @Override
    public String initPos(String orderNo, Double amount) throws Exception {
        StringBuffer bf = new StringBuffer();
        bf.append(StringUtils.formatStr(communication, 100));
        bf.append(payDirect);
        bf.append(StringUtils.formatStr(merchantId, 11));
        bf.append(StringUtils.formatStr(networkId, 15));
        bf.append(StringUtils.formatStr(terminalId, 8));
        bf.append(StringUtils.formatStr(orderNo, 40));
        bf.append(StringUtils.formatStr(yibaoorder, 30));
        String s = String.valueOf(amount * 100);
        bf.append(StringUtils.addZeroForNum(s.substring(0, s.indexOf(".")), 12));
        bf.append(StringUtils.formatStr(metadata, 40));
        bf.append(StringUtils.formatStr(additionalData, 80));
        return bf.toString();
    }

    @Override
    @Transactional
    public PayFormInfo campusPay(MallOrderInfo order, HttpServletRequest request) throws Exception {
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
            orderPayment.setPayType("CAMPUS_PAY");//根据传入记录
            orderPayment.setCreateTime(new Date());
            orderPayment.setCreateUser(username);
            Integer state = mallOrderPaymentDao.insert(orderPayment);
            if (state != 1)
                throw new ServiceException("校园支付信息提交失败！订单号：" + orderNo);
        } else {
            MallOrderPayment orderPayment = list.get(0);
            if (String.valueOf(Constants.READY_TO_PAY).equals(orderPayment.getPayStatus())) {
                log.info("订单号：" + orderNo + "，继续发起校园支付！");
            } else {
                throw new ServiceException("订单支付状态有误！订单号：" + orderNo);
            }
        }

        //2.更新订单信息
        Integer insert = mallOrderInfoService.update(order);

        //3.发起支付
        PayFormInfo payForm = new PayFormInfo();
        payForm.setAction(paymentConfig.getUrl());
        payForm.setOrderDate(DateUtils.formatFullTime(LocalDateTime.now()));
        payForm.setOrderNo(orderNo);
        payForm.setAmount(String.format("%.2f", amount));
        payForm.setXmpch(ApplicationUtils.decryptToken(paymentConfig.getXmpch()));
        payForm.setReturn_url(paymentConfig.getReturnUrl());
        payForm.setNotify_url(paymentConfig.getNotifyUrl());
        generatePaySign(payForm);
        return payForm;
    }

    @Override
    @Transactional
    public Boolean verificationSign(String orderDate, String orderNo, String amount, String jylsh, String tranStat, String return_type, String sign) throws Exception {
        String payChannel = ("1".equals(return_type)) ? "同步" : "异步";
        //0.记录回调日志到数据库
        MallPayLogInfo payBackLog = new MallPayLogInfo();
        payBackLog.setPayResponId(ApplicationUtils.getUUID());
        payBackLog.setOrderNo(orderNo);
        payBackLog.setAmount(amount);
        payBackLog.setPayWay("CAMPUS_PAY");
        payBackLog.setMac(payBackString(orderDate, orderNo, amount, jylsh, tranStat, return_type, sign));
        payBackLog.setCreateDate(new Date());
        mallPayLogInfoService.insert(payBackLog);

        //1.验证签名
        String checkSign = generateCallbackSign(orderDate, orderNo, amount, jylsh, tranStat, return_type);
        if (StringUtils.isBlank(sign) || !sign.equals(checkSign)) {
            payBackLog.setMessage("校园支付" + payChannel + "回调，签名验证失败！订单号：" + orderNo);//回调信息描述
            mallPayLogInfoService.update(payBackLog);
            return false;
        } else {
            //2.根据订单号获取订单
            MallOrderInfo order = mallOrderInfoService.selectBy("orderNo", orderNo);

            if (order == null) {
                throw new ServiceException("回传订单号不存在！订单号：" + orderNo + " 回调时间：" + DateUtils.formatFullTime(LocalDateTime.now()));
            }

            if (order.getPaymentStatus() == 1) {
                log.info("订单号：" + order.getOrderNo() + " 校园支付成功！接收" + payChannel + "支付请求成功");
                payBackLog.setMessage("校园支付" + payChannel + "回调成功，订单已支付成功！订单号：" + order.getOrderNo() + "，付款金额：" + amount + " 元");
                mallPayLogInfoService.update(payBackLog);
                return true;
            }
            if (order.getPaymentStatus() == 3) {
                log.info("订单号：" + order.getOrderNo() + " 校园支付失败！接收" + payChannel + "请求成功");
                payBackLog.setMessage("校园支付" + payChannel + "回调成功，订单已支付失败！订单号：" + order.getOrderNo() + "，付款金额：" + amount + " 元");
                mallPayLogInfoService.update(payBackLog);
                return true;
            }

            //3.获取支付流水信息
            Map param = MapUtils.initMap("orderNo", orderNo);
            param.put("_sort_line", "create_time");//按照创建时间 默认倒序
            List<MallOrderPayment> list = mallOrderPaymentDao.selectList(param);
            if (list == null || list.size() == 0) {
                log.info("不存在订单待支付信息！订单号：" + orderNo);
                throw new ServiceException("不存在订单待支付信息！订单号：" + orderNo);
            }

            //4.验证支付结果 1成功 2失败 3可疑
            MallOrderPayment orderPayment = list.get(0);
            if ("1".equals(tranStat)) {

                //5.更新支付订单流水，更新订单状态，更新关联 mall_order_info 表信息（幂等性判定）
                if ("0".equals(orderPayment.getPayStatus())) {
                    orderPayment.setBillNo(jylsh);
                    orderPayment.setPayStatus("1");//0未支付，1支付成功，2支付中，3支付失败
                    orderPayment.setUpdateTime(new Date());
                    mallOrderPaymentDao.updateByPrimaryKey(orderPayment);

                    order.setPaymentStatus(Constants.PAY_SUCCESS);//支付成功 更新订单状态
                    order.setPaymentTime(new Date());
                    mallOrderInfoService.update(order);
                    //同步课程资料
                    Boolean flag = syncCourseResources(order);
                    if (flag) {
                        log.info("订单号：" + order.getOrderNo() + " 课程资源同步成功！");
                    } else {
                        log.info("订单号：" + order.getOrderNo() + " 课程资源同步失败！");
                    }

                    payBackLog.setMessage("校园支付" + payChannel + "回调成功并更新订单，订单支付成功！订单号：" + order.getOrderNo() + "，付款金额：" + amount + " 元");//回调信息描述
                    mallPayLogInfoService.update(payBackLog);
                }

            } else if ("2".equals(tranStat)) {

                /*order.setPaymentStatus(Constants.PAY_FAILED);//支付失败
                mallOrderInfoService.update(order);*/
                orderPayment.setRemarks("支付失败订单，流水号：" + jylsh);
                orderPayment.setPayStatus("3");
                mallOrderPaymentDao.updateByPrimaryKeySelective(orderPayment);
                log.info("订单号：" + orderNo + "，支付时间：" + orderDate + "，" + payChannel + "接收校园支付结果失败 ");
                payBackLog.setMessage("校园支付" + payChannel + "回调成功并更新订单，订单支付失败！订单号：" + order.getOrderNo() + "，付款金额：" + amount + " 元");//回调信息描述
                mallPayLogInfoService.update(payBackLog);
                return false;

            } else {

                /*order.setPaymentStatus(Constants.PAY_FAILED);//支付失败 支付可疑
                mallOrderInfoService.update(order);*/
                orderPayment.setPayStatus("3");
                orderPayment.setRemarks("可疑支付订单，流水号：" + jylsh);
                mallOrderPaymentDao.updateByPrimaryKeySelective(orderPayment);
                log.info("订单号：" + orderNo + "，支付时间：" + orderDate + "，" + payChannel + "接收校园支付结果失败");
                payBackLog.setMessage("校园支付" + payChannel + "回调成功并更新订单，订单支付失败（可疑订单），订单号：" + order.getOrderNo() + "，付款金额：" + amount + " 元");//回调信息描述
                mallPayLogInfoService.update(payBackLog);
                return false;

            }
        }

        return true;
    }

    @Override
    @Transactional
    public Boolean syncCourseResources(MallOrderInfo order) throws Exception {
        List<Integer> courseIdList = new ArrayList<>();
        Set<Integer> courseIdSet = new HashSet<>();
        SsoUser ssoUser = null;

        //1.根据订单ID 获取订单详情
        queryCourseIdByOrderId(order.getOrderId(), courseIdSet);

        //2.根据 username 获取 userId
        if (courseIdSet.size() > 0) {
            ssoUser = ssoUserService.getUserByUserNameInter(order.getUserName());
            courseIdSet.forEach(courseId -> courseIdList.add(courseId));
        } else {
            log.info("该订单下无对应课程，订单ID：" + order.getOrderId());
            return true;
        }

        //3.调用课程同步服务 为学生同步课程资源
        RetResult retResult = courseService.syncCourseResources(courseIdList, ssoUser.getId());
        if (retResult.getCode() == 200){
            return true;
        }else{
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean syncCourseResourcesForShelf(String orderId, String userName) throws Exception {
        List<Integer> courseIdList = new ArrayList<>();
        Set<Integer> courseIdSet = new HashSet<>();
        SsoUser ssoUser = null;

        //1.根据订单ID 获取订单详情
        queryCourseIdByOrderId(orderId, courseIdSet);

        //2.根据 username 获取 userId
        if (courseIdSet.size() > 0) {
            ssoUser = ssoUserService.getUserByUserNameInter(userName);
            courseIdSet.forEach(courseId -> courseIdList.add(courseId));
        } else {
            log.info("该订单下无对应课程，订单ID：" + orderId);
            return true;
        }

        //3.调用课程同步服务 为学生同步课程资源
        RetResult retResult = courseService.syncCourseResources(courseIdList, ssoUser.getId());
        if (retResult.getCode() == 200){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void queryCourseIdByOrderId(String orderId, Set courseIdSet) throws Exception {

        List<MallOrderInfoVo> orderInfoVos = mallOrderInfoService.selectMallOrderInfoVoListByOrderId(orderId);
        if (orderInfoVos != null && orderInfoVos.size() > 0) {
            List<MallCommodityOrderVo> commodityOrderVoList = orderInfoVos.get(0).getMallCommodityOrderVoList();

            //1.获取订单下关联商品
            if (commodityOrderVoList != null && commodityOrderVoList.size() > 0) {
                for (MallCommodityOrderVo commodityOrderVo : commodityOrderVoList) {
                    if (commodityOrderVo.getCommodityInfoFileVo() != null) {
                        CommodityInfoFileVo commodity = commodityOrderVo.getCommodityInfoFileVo();

                        //2.判断是否为套餐商品 0课程 1套餐
                        if (commodity.getCourseType().intValue() == 0) {
                            if (StringUtils.isBlank(commodity.getCourseId())) {
                                throw new ServiceException("商品ID：" + commodityOrderVo.getCommodityId() + "，缺失关联课程！");
                            }
                            Integer courseId = Integer.valueOf(commodity.getCourseId());
                            courseIdSet.add(courseId);

                        } else if (commodity.getCourseType().intValue() == 1) {
                            if (commodity.getCommodityInfoFileVoList() != null && commodity.getCommodityInfoFileVoList().size() > 0) {
                                //3.获取商品套餐子商品
                                for (CommodityInfoFileVo c : commodity.getCommodityInfoFileVoList()) {
                                    if (StringUtils.isBlank(c.getCourseId())) {
                                        throw new ServiceException("商品ID：" + c.getCommodityId() + "，缺失关联课程！");
                                    }
                                    Integer courseId = Integer.valueOf(c.getCourseId());
                                    courseIdSet.add(courseId);
                                }

                            }

                        }

                    }
                }
            }
        } else {
            throw new ServiceException("订单ID：" + orderId + "，当前订单，关联商品信息不存在！");
        }

    }

    @Override
    @Transactional
    public Boolean insertPaylog(MallPayLogInfo mallPayLogInfo) {
        String returnCode = mallPayLogInfo.getReturnCode();
        String orderNo = mallPayLogInfo.getOrderNo();
        if (!StringUtils.isBlank(orderNo)) {
            if (!StringUtils.isBlank(returnCode) && "00".equals(returnCode)) {
                //根据订单号获取订单
                MallOrderInfo order = mallOrderInfoService.selectBy("orderNo", orderNo);
                if (null != order) {
                    order.setPaymentStatus(Constants.PAY_SUCCESS);//支付成功 更新订单状态
                    order.setPaymentTime(new Date());
                    Integer state = mallOrderInfoService.update(order);
                    Integer insert = mallPayLogInfoService.insert(mallPayLogInfo);
                    try {
                        Boolean aBoolean = mallOrderPaymentService.syncCourseResourcesForShelf(order.getOrderId(), order.getUserName());
                        if (!aBoolean) {
                            throw new ServiceException("同步课程失败！");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (state != 1 || insert != 1) {
                        return false;
                    }
                    return true;
                } else {
                    throw new ServiceException("不存在订单待支付信息！订单号：" + orderNo);
                }
            } else {
                return false;
            }
        }
        return false;
    }


    /**
     * 生成请求支付签名
     *
     * @param form
     * @return
     */
    private String generatePaySign(PayFormInfo form) {
        StringBuffer str = new StringBuffer();
        str.append("orderDate=" + form.getOrderDate() + "&");
        str.append("orderNo=" + form.getOrderNo() + "&");
        str.append("amount=" + form.getAmount() + "&");
        str.append("xmpch=" + form.getXmpch() + "&");
        str.append("return_url=" + form.getReturn_url() + "&");
        if (StringUtils.isBlank(form.getSign())) {
            str.append("notify_url=" + form.getNotify_url() + ApplicationUtils.decryptToken(paymentConfig.getKey()));
            form.setSign(ApplicationUtils.md5Hex(str.toString()));
            return form.getSign();
        } else {
            str.append("notify_url=" + form.getNotify_url());
            str.append("&sign=" + form.getSign());
        }
        return str.toString();
    }

    private String generateCallbackSign(String orderDate, String orderNo, String amount, String jylsh, String tranStat, String return_type) {
        StringBuffer result = new StringBuffer();
        result.append("orderDate=" + orderDate + "&");
        result.append("orderNo=" + orderNo + "&");
        result.append("amount=" + amount + "&");
        result.append("jylsh=" + jylsh + "&");
        result.append("tranStat=" + tranStat + "&");
        result.append("return_type=" + return_type + ApplicationUtils.decryptToken(paymentConfig.getKey()));
        log.info("校园支付回调中，待认证签名串：" + result.toString() + "，获取时间：" + LocalDateTime.now());
        return ApplicationUtils.md5Hex(result.toString());
    }

    private String payBackString(String orderDate, String orderNo, String amount, String jylsh, String tranStat, String return_type, String sign) {
        StringBuffer str = new StringBuffer();
        str.append("orderDate=" + orderDate + "，");
        str.append("orderNo=" + orderNo + "，");
        str.append("amount=" + amount + "，");
        str.append("jylsh=" + jylsh + "，");
        str.append("tranStat=" + tranStat + "，");
        str.append("return_type=" + return_type + "，");
        str.append("sign=" + sign);
        return str.toString();
    }

}