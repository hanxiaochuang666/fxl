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


    /**
     * 将日期对象转换为yyyy-MM-dd HH:mm:ss格式字符串
     *
     * @param date 时间对象
     * @return yyyy-MM-dd HH:mm:ss格式字符串
     */
    public static String toStringYmdHmsWthH(Date date) {
        return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
    }

    /**
     * 将日期对象转换为yyyy-MM-dd HH:mm:ss SSS格式字符串
     *
     * @param date 时间对象
     * @return yyyy-MM-dd HH:mm:ss SSS格式字符串
     */
    public static String toStringYmdHmsWthHS(Date date) {
        return (new SimpleDateFormat("yyyyMMddHHmmssSSS")).format(date);
    }

    /**
     * 将yyyy-MM-dd HH:mm:ss格式字符串转换为日期对象
     *
     * @param dateStr yyyy-MM-dd HH:mm:ss格式字符串
     * @return 日期对象
     */
    public static Date toDateYmdHmsWthH(String dateStr) {
        try {
            return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("yyyy-MM-dd HH:mm:ss 格式不对", e);
        }
    }

    /**
     * 将日期对象转换为yyyy-MM-dd格式字符串
     *
     * @param date 时间对象
     * @return yyyy-MM-dd格式字符串
     */
    public static String toStringYmdWthH(Date date) {
        return (new SimpleDateFormat("yyyy-MM-dd")).format(date);
    }

    /**
     * 将日期对象转换为yyyy/MM/dd格式字符串
     *
     * @param date 时间对象
     * @return yyyy/MM/dd格式字符串
     */
    public static String toStringYmdWthB(Date date) {
        return (new SimpleDateFormat("yyyy/MM/dd")).format(date);
    }


    /**
     * 将yyyy-MM-dd格式字符串转换为日期对象
     *
     * @param dateStr yyyy-MM-dd格式字符串
     * @return 日期对象
     */
    public static Date toDateYmdWthH(String dateStr) {
        try {
            return (new SimpleDateFormat("yyyy-MM-dd")).parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("格式不对 yyyy-MM-dd",e);
        }
    }

    /**
     * 将日期对象转换为yyyyMMddHHmmss格式字符串
     *
     * @param date 时间对象
     * @return yyyyMMddHHmmss格式字符串
     */
    public static String toStringYmdHms(Date date) {
        return (new SimpleDateFormat("yyyyMMddHHmmss")).format(date);
    }

    /**
     * 将yyyyMMddHHmmss格式字符串转换为日期对象
     *
     * @param dateStr yyyyMMddHHmmss格式字符串
     * @return 日期对象
     */
    public static Date toDateYmdHms(String dateStr) {
        try {
            SimpleDateFormat dfm = new SimpleDateFormat("yyyyMMddHHmmss");
            return dfm.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("格式不对 yyyyMMddHHmmss",e);
        }
    }

    /**
     * 将日期对象转换为yyyyMMdd格式字符串
     *
     * @param date 时间对象
     * @return yyyyMMdd格式字符串
     */
    public static String toStringYmd(Date date) {
        return (new SimpleDateFormat("yyyyMMdd")).format(date);
    }

    /**
     * 将yyyy-MM-dd HH24:mm:ss格式字符串转换为日期对象
     *
     * @param dateStr yyyy-MM-dd HH24:mm:ss格式字符串
     * @return 日期对象
     * @deprecated 请使用toDateYmdHmsWthH方法
     */
    @Deprecated
    public static Date toDateYmdH24msWthH(String dateStr) {
        return toDateYmdHmsWthH(dateStr);
    }

    /**
     * 比较两个日期的大小
     * <p/>
     * <pre>
     * 1、日期参数为空表示无穷小
     * </pre>
     *
     * @param inDate1 第一个日期参数
     * @param inDate2 第二个日期参数
     * @return 处理结果 0:相等, -1:inDate1<inDate2, 1:inDate1>inDate2
     * @throws
     */
    public static int dateCompare(Date inDate1, Date inDate2) {
        return dateCompare(inDate1, inDate2, 13);
    }

    /**
     * 比较日期大小
     *
     * @param inDate1 日期1
     * @param inDate2 日期2
     * @param unit    比较精度 年：1 ;月：2; 周：3; 日：5; 时：10; 分：12;秒：13
     * @return 处理结果 0:相等, -1:inDate1<inDate2, 1:inDate1>inDate2
     */
    public static int dateCompare(Date inDate1, Date inDate2, int unit) {
        // 字符空验证
        if (inDate1 == null && inDate2 == null) {
            // 两个日期都为空返回相等
            return 0;
        } else if (inDate1 == null) {
            // 日期1为空，日期2不为空返回-1
            return -1;
        } else if (inDate2 == null) {
            // 日期1不为空，日期为空返回2
            return 1;
        }
        return DateUtils.truncate(inDate1, unit).compareTo(DateUtils.truncate(inDate2, unit));
    }

    /**
     * 按照精度要求对日期进行舍取
     * <p/>
     * <pre>
     * 例如:保留到日期的年truncate("20120511154440", DateUtil.YEAE);返回：20120101000000<br/>
     * </pre>
     *
     * @param inDate 输入日期
     * @param unit   单位 年：1 ;月：2;日：5; 时：10; 分：12;秒：13
     * @return 处理后的日期
     */
    public static Date truncate(Date inDate, int unit) {
        return org.apache.commons.lang3.time.DateUtils.truncate(inDate, unit);
    }

    /**
     * 计算两个日期时间相差几天,几小时,几分钟
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param format 时间格式
     */
    private static String differTimes (String startTime, String endTime, String format) throws ParseException {
        //按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat(format);
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数long diff;try {
        //获得两个时间的毫秒时间差异
        long diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
        long day = diff / nd;//计算差多少天
        long hour = diff % nd / nh;//计算差多少小时
        long min = diff % nd % nh / nm;//计算差多少分钟
        long sec = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
        //System.out.println("时间相差："+day+"天"+hour+"小时"+min+"分钟"+sec+"秒。");
        String outDate = "";
        if (day != 0) {
            outDate = day + "天";
        } else if (day == 0 && hour != 0) {
            outDate = hour + "小时";
        } else if (day == 0 && hour == 0) {
            outDate = min + "分钟" + sec + "秒";
        } else if (day == 0 && hour == 0 && min == 0) {
            outDate = sec + "秒";
        }
        return outDate;
    }

    /**
     * 计算两个日期时间相差几天,几小时,几分钟
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param format 时间格式
     */
    public static String differTime (String startTime, String endTime, String format) throws ParseException {
        String differTime = differTimes(startTime, endTime, format);
        return differTime;
    }

    /**
     * 调整时间, 按照需要调整的单位调整时间
     * <p/>
     * <pre>
     * 例如:保留到日期的年change("20120511154440", 1, 2);返回：20140511154440<br/>
     * 例如:获取当前日期3天后的日期 change(now(),5,3)
     * </pre>
     *
     * @param inDate 传入日志
     * @param unit   调整单位 年：1 ;月：2; 周：3; 日：5; 时：10; 分：12;秒：13
     * @param amount 调整数量
     * @return 调整后的日期
     */
    public static Date change(Date inDate, int unit, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inDate);
        calendar.add(unit, amount);
        return calendar.getTime();
    }
}
