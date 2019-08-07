package com.by.blcu.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
    private DateUtils() {}
    public static final String DATE_FORMAT="yyyy-MM-dd";
    public static final String TIME_FORMAT="yyyy-MM-dd HH:mm:ss";
    public static final String FULL_TIME_PATTERN = "yyyyMMddHHmmss";

    public static Calendar calendar = new GregorianCalendar();
    /**
     * @Author 焦冬冬
     * @Description 时间计算器
     * @Date 17:02 2019/3/25
     * @Param 
     * @return 
     **/
    public static Date compute(Date date,int field, int amount){
        calendar.setTime(date);
        calendar.add(field,amount);
        return calendar.getTime();
    }
    
    /**
     * @Author 焦冬冬
     * @Description 字符串转日期
     * @Date 10:20 2019/3/26
     * @Param 
     * @return 
     **/
    public static Date string2Date(String dateStr,String format)throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dateStr);
    }
    
    /**
     * @Author 焦冬冬
     * @Description 日期转字符串
     * @Date 9:32 2019/3/27
     * @Param 
     * @return 
     **/
    public static String date2String(Date date,String format)throws ParseException{
        if(null==date)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 获取当前日期
     * @return
     */
    public static Date now(){
        return new Date();
    }

    /**
     * @Description 获取精确时间
     * @param localDateTime
     * @return
     */
    public static String formatFullTime(LocalDateTime localDateTime) {
        return formatFullTime(localDateTime, FULL_TIME_PATTERN);
    }
    public static String formatFullTime(LocalDateTime localDateTime, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(dateTimeFormatter);
    }
}
