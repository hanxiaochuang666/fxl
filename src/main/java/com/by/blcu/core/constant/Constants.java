package com.by.blcu.core.constant;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    /**
     *购物车
     */
    public static final String BUYER_CART = "BUYER_CART";

    /**
     * 支付状态
     */
    public static final int READY_TO_PAY = 0; //待支付
    public static final int PAY_SUCCESS = 1;  //支付成功
//    public static final int PAYING = 2;       //支付中
//    public static final int PAY_FAILED = 3;   //支付失败
    /**
     * 支付渠道
     */
    public static final String YEE_PAY = "1";         //易宝支付
    public static final String CAMPUS_PAY = "2";      //校园支付
    public static final String MONEY_TRANSFER = "3";  //汇款
    public static final String POS = "4";             //现场

    /**
     * 试题选项字母候选
     */
    public static final String WORLD_KEY = "ABCDEFGHIGKLMNOPQRSTUVWXYZ";

    public static final Map<Integer,String> WORLD_MAP = new HashMap<>();
    static {
        WORLD_MAP.put(0,"A");
        WORLD_MAP.put(1,"B");
        WORLD_MAP.put(2,"C");
        WORLD_MAP.put(3,"D");
        WORLD_MAP.put(4,"E");
        WORLD_MAP.put(5,"F");
        WORLD_MAP.put(6,"G");
        WORLD_MAP.put(7,"H");
        WORLD_MAP.put(8,"I");
        WORLD_MAP.put(9,"J");
        WORLD_MAP.put(10,"K");
        WORLD_MAP.put(11,"L");
        WORLD_MAP.put(12,"M");
        WORLD_MAP.put(13,"N");
        WORLD_MAP.put(14,"O");
        WORLD_MAP.put(15,"P");
        WORLD_MAP.put(16,"Q");
        WORLD_MAP.put(17,"R");
        WORLD_MAP.put(18,"S");
        WORLD_MAP.put(19,"T");
        WORLD_MAP.put(20,"U");
        WORLD_MAP.put(21,"V");
        WORLD_MAP.put(22,"W");
        WORLD_MAP.put(23,"X");
        WORLD_MAP.put(24,"Y");
        WORLD_MAP.put(25,"Z");
    }

}
