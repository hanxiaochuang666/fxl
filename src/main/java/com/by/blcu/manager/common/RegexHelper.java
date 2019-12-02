package com.by.blcu.manager.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证类
 */
public final class RegexHelper {
    private static final String regexPhone = "^\\d{11}$";
    private static final String regexEmail="^\\w+@[a-z0-9]+\\.[a-z]{2,4}$";
    private static final String regexPassword = "^(?![^A-Za-z]+$)(?![^0-9]+$)[\\da-zA-Z]{6,12}$";

    /**
     * 检查手机号
     *
     * @param phone
     * @return
     */
    public static Boolean CheckPhone(String phone) {
        Pattern p = Pattern.compile(regexPhone);
        Matcher m = p.matcher(phone);
        boolean isMatch = m.matches();
        return isMatch;
    }
    /**
     * 检查邮箱
     *
     * @param email
     * @return
     */
    public static Boolean CheckEmail(String email) {
        Pattern p = Pattern.compile(regexEmail);
        Matcher m = p.matcher(email);
        boolean isMatch = m.matches();
        return isMatch;
    }

    /**
     * 检查密码 6~16位字符
     * @param password
     * @return
     */
    public static Boolean CheckPassword(String password){
        Pattern p = Pattern.compile(regexPassword);
        Matcher m = p.matcher(password);
        boolean isMatch = m.matches();
        return isMatch;
    }
}
