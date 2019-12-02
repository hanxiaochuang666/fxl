package com.by.blcu.core.configurer;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 支付相关配置
 */
@Component
@Data
public class PaymentConfig {

    /**
     * 校园支付
     */
    @Value("${payment.xmpch}")
    private String xmpch;

    @Value("${payment.key}")
    private String key;

    @Value("${payment.return.url}")
    private String returnUrl;

    @Value("${payment.notify.url}")
    private String notifyUrl;

    @Value("${payment.url}")
    private String url;

    /**
     * 易宝银联支付
     */
    @Value("${yeepay.p1_MerId}")
    private String p1_MerId;

    @Value("${yeepay.keyValue}")
    private String keyValue;

    @Value("${yeepay.requestURL}")
    private String requestURL;

    @Value("${yeepay.p8_Url}")
    private String p8_Url;

    @Value("${yeepay.pb_ServerNotifyUrl}")
    private String pb_ServerNotifyUrl;

    @Value("${yeepay.queryURL}")
    private String queryURL;

    @Value("${yeepay.refundURL}")
    private String refundURL;

    @Value("${yeepay.refundQueryURL}")
    private String refundQueryURL;

    @Value("${yeepay.cancelOrderURL}")
    private String cancelOrderURL;


}
