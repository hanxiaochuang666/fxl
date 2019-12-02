package com.by.blcu.core.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class StringUtils {


    /**
     * @Author 焦冬冬
     * @Description 
     * @Date 11:22 2019/3/21
     * @Param [object]
     * @return boolean
     **/
    public static boolean isEmpty(Object object){
        if(null==object)
            return true;
        if(object instanceof String &&"".equals(object))
            return true;

        return false;
    }
    public static int  getActiveLength(String str){
        try {
            String s = new String(str.getBytes("GB2312"), "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.length();
    }

    /**
     * @Author 焦冬冬
     * @Description 切割字符串转换为list
     * @Date 17:38 2019/3/26
     * @Param 
     * @return 
     **/
    public static List<String> str2List(String str,String sp){
        if(StringUtils.isEmpty(str))
            return null;
        List<String> wordLst=new ArrayList<>();
        if(StringUtils.isEmpty(str)) {
            wordLst.add(str);
            return wordLst;
        }
        if(str.indexOf(sp)==-1){
            wordLst.add(str);
        }else {
            String[] split = str.split(sp);
            for (String s : split) {
                wordLst.add(s);
            }
        }
        return wordLst;
    }

    public static boolean isBlank(String str) {
        int strLen;
        if(str != null && (strLen = str.length()) != 0) {
            for(int i=0; i<strLen; ++i) {
                if(!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static int getLength(String value){
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < value.length(); i++) {
            /* 获取一个字符 */
            String temp = value.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }
    public static String getSubString(String str, int length) {
        int count = 0;
        int offset = 0;
        char[] c = str.toCharArray();
        int size = StringUtils.getLength(str);
        if(size >= length){
            for (int i = 0; i < c.length; i++) {
                if (c[i] > 256) {
                    offset = 2;
                    count += 2;
                } else {
                    offset = 1;
                    count++;
                }
                if (count == length) {
                    return str.substring(0, i + 1);
                }
                if ((count == length + 1 && offset == 2)) {
                    return str.substring(0, i);
                }
            }
        }else{
            return str;
        }
        return "";
    }

    /**
     * @param target       需要转化的对象
     * @param defaultValue 缺省值
     * @return 转化结果:如果为null，则返回缺省值，否则，返回toString()
     * @throws
     * @author liyx
     */
    public static String obj2Str(Object target, String defaultValue) {
        String value = defaultValue;
        if (target != null) {
            value = String.valueOf(target);
        }
        return value;

    }

    /**
     * @param target 需要转化的对象
     * @return 转化结果 :如果为null，空字符串，不为空则toString()
     * @throws
     * @author liyx
     */
    public static String obj2Str(Object target) {
        return obj2Str(target, "");
    }

    /**
     * 生成指定长度字符串，不足位右补空格
     * @param str
     * @param length
     * @return
     */
    public static String formatStr(String str, int length) {
        int strLen;
        if (str == null) {
            strLen = 0;
        }else{
            strLen= str.length();
        }

        if (strLen == length) {
            return str;
        } else if (strLen < length) {
            int temp = length - strLen;
            String tem = "";
            for (int i = 0; i < temp; i++) {
                tem = tem + " ";
            }
            return str + tem;
        }else{
            return str.substring(0,length);
        }
    }

    public static String addZeroForNum(String str, int strLength) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                sb.append("0").append(str);// 左补0
                // sb.append(str).append("0");//右补0
                str = sb.toString();
                strLen = str.length();
            }
        }

        return str;
    }

    /**
     * list去重
     * @param list
     * @author licheng
     */
    public static List removeDuplicate(List list) {
        HashSet h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    public static void main(String[] args){
        String s = "123456789";
        String a = s.substring(0,2);
        String b = s.substring(2,4);
        System.out.println(a +"--------------" + b);
    }

}
