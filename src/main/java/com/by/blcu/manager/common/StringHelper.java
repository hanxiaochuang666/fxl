package com.by.blcu.manager.common;

import java.util.List;
import java.util.Set;

import static com.by.blcu.core.baseservice.captcode.CaptchaCodeService.intNumber;

/**
 * 字符串帮助类
 */
public class StringHelper{

    /**
     * 数组序列化
     * @param list
     * @param separator
     * @return
     */
    public static String listToString(List list, char separator) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i)).append(separator);
        }
        return sb.toString().substring(0, sb.toString().length() - 1);
    }

    /**
     * 字符串非空判断
     * @param object
     * @return
     */
    public static boolean isEmpty(Object object){
        if(null==object)
            return true;
        return object instanceof String && "".equals(object);

    }
    /**
     * 字符串非空判断
     * @param param
     * @return
     */
    public static boolean IsNullOrWhiteSpace(String param){
        return param == null || param.trim().length() == 0;
    }

    /**
     * 整型判断非空或非0
     * @param param
     * @return
     */
    public static boolean IsNullOrZero(Integer param){
        return param == null || param == 0;
    }

    /**
     * 判断列表是否为空
     * @param param
     * @return
     */
    public static <T> boolean IsNullOrEmpty(List<T> param){
        if( param!=null && !param.isEmpty()){
            return false;
        }
        return true;
    }

    /**
     * 判断列表是否为空
     * @param param
     * @return
     */
    public static <T> boolean IsNullOrEmpty(Set<T> param){
        if( param!=null && !param.isEmpty()){
            return false;
        }
        return true;
    }

    /**
     * 判断bool型
     * @param param
     * @return
     */
    public static boolean IsNullOrFalse(Boolean param){
        if( param!=null && param==true){
            return false;
        }
        return true;
    }

    /**
     * 生成随机数
     * @param codeLength
     * @return
     */
    public static String generateCode(Integer codeLength){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<codeLength;i++){
            sb.append(intNumber(0,9));
        }
        return sb.toString();
    }
    private static int intNumber(int start, int end) {
        int delta = end - start;
        if (delta < 0) delta = 0 - delta;
        return (int)(delta * Math.random() + start);
    }

    public final static Integer OrgCodeLenth=8;
}
