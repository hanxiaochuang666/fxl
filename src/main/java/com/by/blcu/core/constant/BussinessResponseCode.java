package com.by.blcu.core.constant;

public enum BussinessResponseCode {

    //for Captchacode service
    AUTH_CAPTCHA_PATIENT_WAITING(100,"auth.captcha.patient.waiting"),
    AUTH_CAPTCHA_EMPTY(101,"auth.captcha.empty"),
    AUTH_CAPTCHA_CODE_NOTEXIST(102,"auth.captcha.code.notexist"),
    AUTH_CAPTCHA_CODE_EXPIRED(103,"auth.captcha.code.expired"),
    AUTH_CAPTCHA_CODE_NOT_EXPIRE(104,"auth.captcha.code.notexpire"),
    AUTH_CAPTCHA_IDENTITY_EXIST(105,"auth.captcha.identity.exist"),
    AUTH_CAPTCHA_IDENTITY_NOT_EXIST(106,"auth.captcha.identity.not.exist"),
    AUTH_CAPTCHA_PHONE_NUMBER_INVALID(107,"auth.captcha.phone.number.invalid"),
    AUTH_CAPTCHA_SEND_FAIL(108,"auth.captcha.send.fail"),
    AUTH_CAPTCHA_VALID_OK(110,"auth.captcha.valid.ok"),
    AUTH_CAPTCHA_VALID_FAIL(111,"auth.captcha.valid.fail"),
    //AUTH_CAPTCHA_TIMES_NOENOUGH(110,"auth.captcha.times.noenough"),

    ;

    public int code;
    public String info;

    BussinessResponseCode(int code, String info) {
        this.code = code;
        this.info = info;
    }

}
