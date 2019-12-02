package com.by.blcu.manager.common;

import java.util.List;

/**
 * Mapper文件帮助类
 */
public class MapperHelper {

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
    public static boolean IsNullOrEmpty(List<Object> param){
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
     * 判断bool型
     * @param param
     * @return
     */
    public static boolean IsNull(Boolean param){
        if( param!=null){
            return false;
        }
        return true;
    }
}
