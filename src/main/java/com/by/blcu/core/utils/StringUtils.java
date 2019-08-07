package com.by.blcu.core.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

    public static void main(String[] args) {
        String temp="你好，今日头条|你好普法节目";
        String sp="\\|";
        String[] split = temp.split(sp);
        for (String s : split) {
            System.out.println(s);
        }

    }
}
