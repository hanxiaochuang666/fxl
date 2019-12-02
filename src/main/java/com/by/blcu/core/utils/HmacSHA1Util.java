package com.by.blcu.core.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Component
public class HmacSHA1Util {

    public static String HmacSHA1Encrypt(String encryptText,String key) throws Exception {

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1"));
        byte[] signData = mac.doFinal(encryptText.getBytes("UTF-8"));

        String signature = Base64.encodeBase64String(signData);
        signature = signature.replaceAll("\r", "");
        signature = signature.replaceAll("\n", "");
        //完成 Mac 操作
        return signature;
    }
}
