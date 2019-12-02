package com.by.blcu.core.utils;

import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.universal.IBaseService;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
    private CommonUtils(){}
    public static boolean listIsEmptyOrNull(List list) {
        if (null == list)
            return true;
        return list.size() <= 0;
    }

    /**
     * 查询分页参数整理
     * @param param
     */
    public static void queryParamOpt(Map<String,Object> param){
        if(!StringUtils.isEmpty(param.get("page")) && !StringUtils.isEmpty(param.get("limit"))){
            int page = Integer.valueOf(param.get("page").toString()).intValue();
            int pagesize=Integer.valueOf(param.get("limit").toString()).intValue();
            int __currentIndex__=(page-1)*pagesize;
            param.put("__currentIndex__",__currentIndex__);
            param.put("__pageSize__",pagesize);
        }
    }

    /**
     * 判断是否是自己的资源1
     * @param userId
     * @param primaryKey
     * @param baseService
     * @return
     * @throws Exception
     */
    public static boolean isOperation(int userId, int primaryKey,IBaseService baseService)throws Exception{
        return isOperateChild(userId, baseService.selectByPrimaryKey(primaryKey));
    }

    /**
     * 判断是否是自己的资源2
     * @param userId
     * @param primaryKey
     * @param basedao
     * @return
     * @throws Exception
     */
    public static boolean isOperationDao(int userId, int primaryKey,IBaseDao basedao)throws Exception{
        return isOperateChild(userId, basedao.selectByPrimaryKey(primaryKey));
    }

    private static boolean isOperateChild(int userId, Object o) throws Exception {
        if(o==null)
            return false;
        Map<String, Object> stringObjectMap = MapAndObjectUtils.ObjectToMap(o);
        if(stringObjectMap.containsKey("createUser")){
            String createUser = stringObjectMap.get("createUser").toString();
            int intValue = Integer.valueOf(createUser).intValue();
            if(intValue!=userId)
                return false;
        }
        return true;
    }

    public static String ListToStr(List<Integer> lst,String splt){
        StringBuilder temp=new StringBuilder();
        int size = lst.size();
        int i=0;
        for (Integer integer : lst) {
            temp.append(integer);
            if(++i<size) {
                temp.append(splt);
            }
        }
        return temp.toString();
    }

    //从html中提取纯文本
    public static String Html2Text(String inputString) {
        String htmlStr = inputString; // 含html标签的字符串
        String textStr = "";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;
        try {
            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
            String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); // 过滤script标签
            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); // 过滤style标签
            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); // 过滤html标签
            textStr = htmlStr;
        } catch (Exception e) {System.err.println("Html2Text: " + e.getMessage()); }
        //剔除空格行
        textStr=textStr.replaceAll("[ ]+", " ");
        textStr=textStr.replaceAll("(?m)^\\s*$(\\n|\\r\\n)", "");
        return textStr;// 返回文本字符串
    }

    /**
     * 得到网页中图片的地址
     * @param htmlStr 字符串
     */
    public static Set<String> getImgStr(String htmlStr) {
        Set<String> pics = new HashSet<String>();
        String img = "";
        Pattern p_image;
        Matcher m_image;
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            // 得到<img />数据
            img = m_image.group();
            // 匹配<img>中的src数据
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }

    /**
     * list去重
     * @param stringList 需要去重的list对象
     * @return 返回去重后的list对象
     */
    public static List<String> removeStringListDupli(List<String> stringList) {
        Set<String> set = new LinkedHashSet<>(stringList);
        return new ArrayList<>(set);
    }

    /**
     * 判断是否是带子题，目前包含：完形填空题，阅读理解题，配对题，综合题
     * @param code
     * @return
     */
    public static boolean hasChildQuestion(String code){

        return "WANXINGTIANKONG".equals(code) || "PEIDUI".equals(code) || "YUEDULIJIE".equals(code) || "ZONGHE".equals(code) ;
    }

    /***
     * 去除String数组中的空值
     */
    public static String[] removeEmptyArray(String[] strArray) {
        List<String> strList= Arrays.asList(strArray);
        List<String> strListNew=new ArrayList<>();
        for (int i = 0; i <strList.size(); i++) {
            if (strList.get(i)!=null&&!strList.get(i).equals("")){
                strListNew.add(strList.get(i));
            }
        }
        return strListNew.toArray(new String[strListNew.size()]);
    }

}
