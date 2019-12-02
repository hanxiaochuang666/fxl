package com.by.blcu.core.contentSecurity;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.by.blcu.course.dto.AutomaticCheck;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class AbstractContentSecurity {

    public static String accessKeyId;

    public static String accessKeySecret;

    public static String regionId;

    public static String connectTimeout;

    public static String readTimeout;

    public static String callbackUrl;

    public static String uid;

    public static String interval;


    public static IAcsClient client=null;

    //三个检测维度
    public static final List<String> scenesLst=Arrays.asList("porn", "terrorism", "ad");


    @Value("${alibaba.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        AbstractContentSecurity.accessKeyId = accessKeyId;
    }

    @Value("${alibaba.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        AbstractContentSecurity.accessKeySecret = accessKeySecret;
    }

    @Value("${alibaba.regionId}")
    public void setRegionId(String regionId) {
        AbstractContentSecurity.regionId = regionId;
    }

    @Value("${alibaba.connectTimeout}")
    public void setConnectTimeout(String connectTimeout){
        AbstractContentSecurity.connectTimeout=connectTimeout;
    }

    @Value("${alibaba.readTimeout}")
    public void setReadTimeout(String readTimeout){
        AbstractContentSecurity.readTimeout=readTimeout;
    }

    @Value("${alibaba.callbackUrl}")
    public void setCallbackUrl(String callbackUrl){
        AbstractContentSecurity.callbackUrl=callbackUrl;
    }

    @Value("${alibaba.uid}")
    public void setUid(String uid){
        AbstractContentSecurity.uid=uid;
    }

    @Value("${alibaba.interval}")
    public void setInterval(String interval){
        AbstractContentSecurity.interval=interval;
    }

    protected IAcsClient getClient()throws Exception{
        if(null==client) {
            IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint(regionId, regionId, "Green", getDomain());
            client = new DefaultAcsClient(profile);
        }
        return client;
    }

    private  String getDomain(){
        if("cn-shanghai".equals(regionId)){
            return "green.cn-shanghai.aliyuncs.com";
        }

        if ("cn-beijing".equals(regionId)) {
            return "green.cn-beijing.aliyuncs.com";
        }

        if ("ap-southeast-1".equals(regionId)) {
            return "green.ap-southeast-1.aliyuncs.com";
        }

        if ("us-west-1".equals(regionId)) {
            return "green.us-west-1.aliyuncs.com";
        }

        return "green.cn-shanghai.aliyuncs.com";
    }

    /**
     * 图片异步检测请求
     * @param imageLst
     * @return
     */
    abstract List<AutomaticCheck> ImageAsyncCheck(List<String> imageLst)throws Exception;

    /**
     * 视频异步检测
     * @param videoLst
     * @return
     */
    abstract List<AutomaticCheck> VideoAsyncCheck(List<String> videoLst)throws Exception;

    /**
     * 文件异步检测，最多支持五个文件同时检测
     * @param fileLst
     * @return
     */
    abstract List<AutomaticCheck>  FileAsyncCheck(List<String> fileLst)throws Exception;

    /**
     * 音频异步检测
     * @param
     * @return
     *
     * 检测任务列表，JSON数组中的每个元素是一个结构体，最多支持100个元素。每个元素的具体机构见voice结构体参数说明。
     * 语音文件限制说明
     * 支持的语音文件大小 < 100 M
     * 支持的语音文件时长 < 30分钟
     * 支持的音频文件格式：.mp3、.wav、.aac、.wma、.ogg、.m4a、.amr
     * 支持的视频文件格式：.avi、.flv、.mp4、.mpg、.asf、.wmv、.mov、.rmvb、.rm
     */
    abstract List<AutomaticCheck> VoiceAsyncCheck(List<String> voiceLst)throws Exception;

    /**
     * 文本同步检测
     * @param textMap  資源ID  和待檢測文本
     * @return
     * 文本检测任务列表，包含一个或多个元素。每个元素是个结构体，最多可添加100个元素，即最多对100段文本进行检测。每个元素的具体结构描述见task。
     * 待检测文本，最长10,000个字符。
     */
    abstract List<AutomaticCheck>  TextCheck(Map<Integer,String> textMap)throws Exception;

    /**
     * 异步检测回调
     * @param checksum
     * @param content
     */
    abstract boolean checkCallBack(String checksum,String content);
}
