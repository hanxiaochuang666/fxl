package com.by.blcu.core.dataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据源切换管理类
 */
public class DynamicDataSourceContextHolder {
    //线程局部变量，用来存储当前请求对应的数据库链接信息
    private static final ThreadLocal<String> contextHolder=new ThreadLocal<>();
    //所有的数据库链接信息
    public static List<String> dataSourceIds = new ArrayList<>();

    public static String getContextHolder() {
        return contextHolder.get();
    }

    public static void setContextHolder(String contextHolderOld){
        contextHolder.set(contextHolderOld);
    }

    public static void clearContextHolder(){
        contextHolder.remove();
    }

    public static boolean isContain(String dataSource){
        return  dataSourceIds.contains(dataSource);
    }
}
