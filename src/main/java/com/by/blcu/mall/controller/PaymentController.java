package com.by.blcu.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.constant.Constants;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.mall.model.MallOrderInfo;
import com.by.blcu.mall.model.MallPayLogInfo;
import com.by.blcu.mall.model.payment.PaySynCall;
import com.by.blcu.mall.model.payment.YeePaySynCall;
import com.by.blcu.mall.service.IMallOrderPaymentService;
import com.by.blcu.mall.service.MallOrderYeePaymentService;
import com.by.blcu.mall.service.MallOrderInfoService;
import com.by.blcu.mall.vo.MallOrderInfoVo;
import com.by.blcu.mall.vo.MallPaymentInvoiceVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("/payment")
@Api(tags = "校园支付API", description = "包含接口：\n" +
        "1、订单校园支付\n" +
        "2、订单易宝支付\n" +
        "3、校园支付异步回调\n" +
        "4、校园支付同步回调")
public class PaymentController {

    private String message;

    @Resource
    private IMallOrderPaymentService mallOrderPaymentService;

    @Resource
    private MallOrderYeePaymentService mallOrderYeePaymentService;

    @Resource
    private MallOrderInfoService mallOrderInfoService;

    @PostMapping("/campusPay")
    @ApiOperation(value = "校园支付", notes = "根据订单金额创建发票发起-校园支付")
    RetResult campusPay(@RequestBody MallPaymentInvoiceVo paymentInvoiceVo, HttpServletRequest request) throws Exception {
        return mallOrderPaymentService.initiatePayment(Constants.CAMPUS_PAY, paymentInvoiceVo, request);
    }

    @PostMapping("/yeePay")
    @ApiOperation(value = "易宝支付", notes = "根据订单金额创建发票发起-易宝支付")
    RetResult yeePay(@RequestBody MallPaymentInvoiceVo paymentInvoiceVo, HttpServletRequest request) throws Exception {
        return mallOrderPaymentService.initiatePayment(Constants.YEE_PAY, paymentInvoiceVo, request);
    }

    @PostMapping("/posPay")
    @ApiOperation(value = "pos支付")
    RetResult posPay(@RequestBody JSONObject obj) throws Exception {
        if(obj.containsKey("orderId") && !StringUtils.isBlank(obj.getString("orderId"))){
            String orderId = obj.getString("orderId");
            List<MallOrderInfoVo> mallOrderInfoVos = mallOrderInfoService.selectMallOrderInfoVoListByOrderId(orderId);
            String orderNo = mallOrderInfoVos.get(0).getOrderNo();
            Double amount = mallOrderInfoVos.get(0).getAmount();
            if(amount == 0){
                return RetResponse.makeErrRsp("金额为0，无需支付");
            }
            String initPos = mallOrderPaymentService.initPos(orderNo,amount);
            return RetResponse.makeOKRsp(initPos);
        }else{
            return RetResponse.makeErrRsp("未生成订单号！");
        }
    }

    @PostMapping("/posPayReturn")
    @ApiOperation(value = "pos支付")
    RetResult posPayReturn(@RequestBody JSONObject obj) throws Exception {
        if(obj.containsKey("data") && !StringUtils.isBlank(obj.getString("data"))){
            String data = obj.getString("data");
            MallPayLogInfo mallPayLogInfo = new MallPayLogInfo();
            mallPayLogInfo.setPayResponId(ApplicationUtils.getUUID());
            mallPayLogInfo.setMerchantId(data.substring(0,11));
            mallPayLogInfo.setNetworkId(data.substring(11,26));
            mallPayLogInfo.setTerminalId(data.substring(26,34));
            mallPayLogInfo.setOrderNo(data.substring(34,74));
            mallPayLogInfo.setYibaoorder(data.substring(74,104));
            mallPayLogInfo.setAmount(data.substring(104,116));
            mallPayLogInfo.setReturnCode(data.substring(116,118));
            mallPayLogInfo.setCardNo(data.substring(118,137));
            mallPayLogInfo.setPayDate(data.substring(137,145));
            mallPayLogInfo.setPayTime(data.substring(145,151));
            mallPayLogInfo.setTrace(data.substring(151,157));
            mallPayLogInfo.setReferenceNo(data.substring(157,169));
            mallPayLogInfo.setPayWay(data.substring(169,170));
            mallPayLogInfo.setSendData(data.substring(170,210));
            mallPayLogInfo.setAddData(data.substring(210,290));
            Boolean aBoolean = mallOrderPaymentService.insertPaylog(mallPayLogInfo);
            if(aBoolean){
                return RetResponse.makeOKRsp(1);
            }
            return RetResponse.makeErrRsp("订单号为：" + mallPayLogInfo.getOrderNo() + "交易失败");
        }else{
            return RetResponse.makeErrRsp("没有数据！");
        }
    }

    @RequestMapping(value = "/callbackAsynchronous", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "异步接收校园支付结果", notes = "接收异步查询结果")
    String paymentCallBack(String orderDate, String orderNo, String amount, String jylsh,
                           String tranStat, String return_type, String sign) throws Exception {
        try {
            Boolean payResult = mallOrderPaymentService.verificationSign(orderDate, orderNo, amount, jylsh, tranStat, return_type, sign);
            if (!payResult) {
                log.info("异步校园支付结果请求，订单号：" + orderNo + "，请求响应失败");
                return "failed";
            }
        } catch (Exception e) {
            message = "订单号：" + orderNo + " 校园支付异步通知接收异常！";
            log.error(message, e);
            return message;
        }
        return "success";
    }

    @RequestMapping(value = "/callback", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "同步接收校园支付结果", notes = "接收同步查询结果")
    String paymentCallBackSyn(@RequestBody PaySynCall paySynCall) throws Exception {
        try {
            log.info("校园支付同步请求：" + paySynCall.toString());
            Boolean payResult = mallOrderPaymentService.verificationSign(paySynCall.getOrderDate(), paySynCall.getOrderNo(), paySynCall.getAmount(), paySynCall.getJylsh()
                    , paySynCall.getTranStat(), paySynCall.getReturn_type(), paySynCall.getSign());

            if (!payResult) {
                log.info("校园支付同步请求：" + paySynCall.toString() + "，请求响应失败");
                return "failed";
            }
        } catch (Exception e) {
            message = "订单号：" + paySynCall.getOrderNo() + " 校园支付同步通知接收异常！";
            log.error(message, e);
            return message;
        }
        return "success";
    }

    @RequestMapping(value = "/YeePaySuccess", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation(value = "易宝支付回调", notes = "易宝支付异步回调")
    String yeePaymentCallBack(String p1_MerId, String r0_Cmd, String r1_Code, String r2_TrxId, String r3_Amt,
                              String r4_Cur, String r5_Pid, String r6_Order, String r7_Uid, String r8_MP, String r9_BType, String rb_BankId,
                              String ro_BankOrderId, String rp_PayDate, String rq_CardNo, String ru_Trxtime, String rq_SourceFee,
                              String rq_TargetFee, String hmac_safe, String hmac) throws Exception {

        try {
            YeePaySynCall yeePaySynCall = new YeePaySynCall();
            yeePaySynCall.setP1_MerId(p1_MerId);
            yeePaySynCall.setR0_Cmd(r0_Cmd);
            yeePaySynCall.setR1_Code(r1_Code);
            yeePaySynCall.setR2_TrxId(r2_TrxId);
            yeePaySynCall.setR3_Amt(r3_Amt);
            yeePaySynCall.setR4_Cur(r4_Cur);
            yeePaySynCall.setR5_Pid(r5_Pid);
            yeePaySynCall.setR6_Order(r6_Order);
            yeePaySynCall.setR7_Uid(r7_Uid);
            yeePaySynCall.setR8_MP(r8_MP);
            yeePaySynCall.setR9_BType(r9_BType);
            yeePaySynCall.setRb_BankId(rb_BankId);
            yeePaySynCall.setRo_BankOrderId(ro_BankOrderId);
            yeePaySynCall.setRp_PayDate(rp_PayDate);
            yeePaySynCall.setRq_CardNo(rq_CardNo);
            yeePaySynCall.setRu_Trxtime(ru_Trxtime);
            yeePaySynCall.setRq_SourceFee(rq_SourceFee);
            yeePaySynCall.setRq_TargetFee(rq_TargetFee);
            yeePaySynCall.setHmac_safe(hmac_safe);
            yeePaySynCall.setHmac(hmac);
            log.info("易宝支付异步回调请求：" + yeePaySynCall.toString());
            Boolean payResult = mallOrderYeePaymentService.verificationYeePay(yeePaySynCall);

            if (!payResult) {
                log.info("易宝支付异步回调请求：" + yeePaySynCall.toString() + "，请求响应失败");
                return "failed";
            }
        } catch (Exception e) {
            message = "订单号：" + r6_Order + " 易宝支付异步通知接收异常！";
            log.error(message, e);
            return message;
        }
        return "SUCCESS";
    }

    @PostMapping("/queryYeePay")
    @ApiOperation(value = "易宝支付结果", notes = "根据订单号查看易宝支付结果")
    RetResult queryYeePayResult(@RequestBody MallOrderInfo order) throws Exception {
        //主动请求支付结果
        return RetResponse.makeOKRsp(mallOrderYeePaymentService.queryPayResult(order));
    }

    @PostMapping("/syn")
    RetResult syn(@RequestBody MallOrderInfo order) throws Exception {
        //手动同步课程资源
        return RetResponse.makeOKRsp(mallOrderPaymentService.syncCourseResources(order));
    }


}
