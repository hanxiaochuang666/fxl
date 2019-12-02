package com.by.blcu.resource.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.*;
import com.by.blcu.course.dao.ICourseDetailDao;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.mall.model.File;
import com.by.blcu.resource.dao.ILiveTelecastDao;
import com.by.blcu.resource.dao.IResourcesDao;
import com.by.blcu.resource.dto.LiveTelecast;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.model.*;
import com.by.blcu.resource.service.ILiveService;
import com.by.blcu.resource.service.IResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

@Service
@Slf4j
public class LiveServiceImpl implements ILiveService {

    @Value("${live.appId}")
    private String appId;

    @Value("${live.appKey}")
    private String appKey;

    @Value("${live.encryptKey}")
    private String encryptKey;

    @Value("${live.createRoom}")
    private String createRoom;

    @Value("${live.updateRoom}")
    private String updateRoom;

    @Value("${live.searchRoom}")
    private String searchRoom;

    @Value("${live.roomList}")
    private String roomList;

    @Value("${live.getRoomQas}")
    private String getRoomQas;

    @Value("${live.playbackList}")
    private String playbackList;

    @Value("${live.getQrCode}")
    private String getQrCode;

    @Value("${live.closeRoom}")
    private String closeRoom;

    @Autowired
    private ILiveTelecastDao liveTelecastDao;

    @Autowired
    private IResourcesDao resourcesDao;

    @Autowired
    private ICourseDetailDao detailDao;

    @Resource
    private IResourcesService resourcesService;

    @Override
    @Transactional
    public LiveRetModel createRoom(LiveModel liveModel,HttpServletRequest request) throws Exception {

        // 当前目录是否已关联资源 存在则移除
        Map<String, Object> detailParam = MapUtils.initMap("catalogId", liveModel.getCatalogId());
        List<CourseDetail> courseDetails = detailDao.selectList(detailParam);
        if(!CommonUtils.listIsEmptyOrNull(courseDetails)){
            for(CourseDetail courseDetail : courseDetails){
                resourcesService.removeByResourceType(courseDetail.getResourcesId());
                detailDao.deleteByPrimaryKey(courseDetail.getCourseDetailId());
            }
        }

        Integer userId = (Integer) request.getAttribute("userId");
        Date date = DateUtils.now();
        // 首先调用奥鹏的创建直播间接口
        liveModel.setAppId(appId);
        liveModel.setAppKey(appKey);
        String timestamp = null;
        try {
            timestamp = DateUtils.date2String(date, "yyyy-MM-dd'T'HH:mm:ss'Z'");
            log.info("创建直播间日期时间：" + timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String random = ApplicationUtils.getNumStringRandom(6);
        liveModel.setSignature(getSignature(timestamp, random));
        liveModel.setSignatureNonce(random);
        liveModel.setTimestamp(timestamp);

        JSONObject requestBody = JSONObject.parseObject(JSONObject.toJSONString(liveModel));
        requestBody.remove("catalogId");
        requestBody.remove("roomId");
        requestBody.remove("courseId");
        ResponseEntity<JSONObject> response;
        try {
            response = RestTemplateUtils.post(createRoom, getRequestEntity(requestBody), JSONObject.class);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        Map retMap = JSONUtil.parseJSON2Map(Objects.requireNonNull(response.getBody()));
        if (!"1".equals(retMap.get("status"))) {
            throw new ServiceException((String) retMap.get("message"));
        }
        Map<String, Object> payload = (Map<String, Object>) retMap.get("payload");
        log.info("创建直播间接口返回参数:" + payload.toString());

        // 存直播返回的信息
        LiveTelecast live = new LiveTelecast();
        live.setAssUrl(String.valueOf(payload.get("assistantAutoLoginUrl")));
        live.setTecUrl(String.valueOf(payload.get("teacherAutoLoginUrl")));
        live.setStuUrl(String.valueOf(payload.get("studentAutoLoginUrl")));
        live.setRomeId(String.valueOf(payload.get("roomId")));
        live.setStartTime(DateUtils.string2Date(liveModel.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
        live.setEndTime(DateUtils.string2Date(liveModel.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
        live.setDes(liveModel.getRoomDesc());
        live.setName(liveModel.getRoomName());
        live.setStatus(1);
        live.setCreateUser(userId);
        live.setCreateTime(date);
        liveTelecastDao.insertSelective(live);

        // 存资源表
        Resources resources = new Resources();
        resources.setCreateUser(userId);
        resources.setCreayeTime(date);
        resources.setContent(String.valueOf(payload.get("roomId")));
        resources.setType(3);
        resources.setUpdateTime(date);
        resources.setUpdateUser(userId);
        resources.setCheckStatus(0);
        resourcesDao.insertSelective(resources);

        // 存资源课程关系表
        List<CourseDetail> details = detailDao.selectList(MapUtils.initMap("catalogId",liveModel.getCatalogId()));
        if(!CommonUtils.listIsEmptyOrNull(details)){
            // 如果有了就更新，没有就添加
            CourseDetail detail = details.get(0);
            detail.setModelType(1);
            detail.setResourcesId(resources.getResourcesId());
            detailDao.updateByPrimaryKeySelective(detail);
        }else{
            CourseDetail detail = new CourseDetail();
            detail.setCatalogId(Integer.valueOf(liveModel.getCatalogId()));
            detail.setCourseId(Integer.valueOf(liveModel.getCourseId()));
            detail.setCreateUser(userId);
            detail.setCreateTime(date);
            detail.setResourcesId(resources.getResourcesId());
            detail.setModelType(1);//默认目录
            detailDao.insertSelective(detail);
        }

        LiveRetModel liveRetModel = new LiveRetModel();
        liveRetModel.setTecUrl(live.getTecUrl());//回显教师直播间地址
        liveRetModel.setCatalogId(liveModel.getCatalogId());
        liveRetModel.setCourseId(liveModel.getCourseId());
        liveRetModel.setContent(StringUtils.obj2Str(payload.get("roomId")));
        liveRetModel.setResourceId(""+resources.getResourcesId());
        liveRetModel.setType("3");
        liveRetModel.setLiveStartTime(live.getStartTime());
        liveRetModel.setLiveEndTime(live.getEndTime());
        if(1 == DateUtils.dateCompare(live.getStartTime(),DateUtils.now())){// 表示直播未开始
            liveRetModel.setLiveResult("（直播未开始）");
            liveRetModel.setLiveStatus(0);
        }else if(-1 == DateUtils.dateCompare(live.getStartTime(),DateUtils.now())){
            if(1 == DateUtils.dateCompare(live.getEndTime(),DateUtils.now())){
                liveRetModel.setLiveResult("（正在直播）");
                liveRetModel.setLiveStatus(1);
            }else if(-1 == DateUtils.dateCompare(live.getEndTime(),DateUtils.now())){
                liveRetModel.setLiveResult("（直播已结束）");
                liveRetModel.setLiveStatus(2);
            }
        }

        return liveRetModel;

    }

    @Override
    public String getPlaybackUrl(String roomId, HttpServletRequest request) throws Exception {

        Date date = DateUtils.now();
        // 先查是否存在
        Map paraMap = new HashMap();
        paraMap.put("romeId", roomId);
        List<LiveTelecast> list = liveTelecastDao.selectList(paraMap);
        if (CommonUtils.listIsEmptyOrNull(list)) {
            throw new ServiceException("未查询到直播间信息！");
        }
        LiveTelecast liveTelecast = list.get(0);
        if (!StringUtils.isEmpty(liveTelecast.getPlaybackUrl())) {
            return liveTelecast.getPlaybackUrl();
        }

        LiveBaseModel model = new LiveBaseModel();
        model.setAppId(appId);
        model.setAppKey(appKey);
        String timestamp = null;
        try {
            timestamp = DateUtils.date2String(date, "yyyy-MM-dd'T'HH:mm:ss'Z'");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String random = ApplicationUtils.getNumStringRandom(6);
        model.setSignature(getSignature(timestamp, random));
        model.setSignatureNonce(random);
        model.setTimestamp(timestamp);
        JSONObject requestBody = JSONObject.parseObject(JSONObject.toJSONString(model));
        requestBody.put("viewername", liveTelecast.getCreateUser());
        requestBody.put("roomId", roomId);
        ResponseEntity<JSONObject> response;
        try {
            response = RestTemplateUtils.post(playbackList, getRequestEntity(requestBody), JSONObject.class);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        Map retMap = JSONUtil.parseJSON2Map(Objects.requireNonNull(response.getBody()));
        if (!"1".equals(retMap.get("status"))) {
            throw new ServiceException((String) retMap.get("message"));
        }
        Map<String, Object> payload = (Map<String, Object>) retMap.get("payload");
        log.info("返回的payload:" + payload.toString());
        String playbackUrl = (String) payload.get("playbackUrl");
        // 更新进直播的表
        liveTelecast.setPlaybackUrl(playbackUrl);
        liveTelecastDao.updateByPrimaryKeySelective(liveTelecast);
        return playbackUrl;
    }

    @Override
    public LiveRetModel updateRoom(LiveModel liveModel, HttpServletRequest request) throws Exception {

        String roomId = liveModel.getRoomId();
        if (StringUtils.isEmpty(roomId)) {
            throw new ServiceException("roomId必传！");
        }
        Map paraMap = new HashMap();
        paraMap.put("romeId", roomId);
        List<LiveTelecast> list = liveTelecastDao.selectList(paraMap);
        if (CommonUtils.listIsEmptyOrNull(list)) {
            throw new ServiceException("未查询到直播间信息！");
        }
        Date date = DateUtils.now();
        liveModel.setAppId(appId);
        liveModel.setAppKey(appKey);
        String timestamp = null;
        try {
            timestamp = DateUtils.date2String(date, "yyyy-MM-dd'T'HH:mm:ss'Z'");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String random = ApplicationUtils.getNumStringRandom(6);
        liveModel.setSignature(getSignature(timestamp, random));
        liveModel.setSignatureNonce(random);
        liveModel.setTimestamp(timestamp);

        JSONObject requestBody = JSONObject.parseObject(JSONObject.toJSONString(liveModel));
        requestBody.remove("catalogId");
        requestBody.remove("courseId");
        ResponseEntity<JSONObject> response;
        try {
            response = RestTemplateUtils.post(updateRoom, getRequestEntity(requestBody), JSONObject.class);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        Map retMap = JSONUtil.parseJSON2Map(Objects.requireNonNull(response.getBody()));
        if (!"1".equals(retMap.get("status"))) {
            throw new ServiceException((String) retMap.get("message"));
        }
        log.info("修改直播间信息返回的参数:" + retMap);

        LiveTelecast live = list.get(0);
        live.setRomeId(roomId);
        live.setStartTime(DateUtils.string2Date(liveModel.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
        live.setEndTime(DateUtils.string2Date(liveModel.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
        live.setDes(liveModel.getRoomDesc());
        live.setName(liveModel.getRoomName());
        liveTelecastDao.updateByPrimaryKeySelective(live);

        Integer userId = (Integer) request.getAttribute("userId");
        List<CourseDetail> details = detailDao.selectList(MapUtils.initMap("catalogId",liveModel.getCatalogId()));
        if(!CommonUtils.listIsEmptyOrNull(details)){
            Resources resources = resourcesDao.selectByPrimaryKey(details.get(0).getResourcesId());
            resources.setUpdateUser(userId);
            resources.setUpdateTime(DateUtils.now());
            resourcesDao.updateByPrimaryKeySelective(resources);
        }
        LiveRetModel liveRetModel = new LiveRetModel();
        liveRetModel.setTecUrl(live.getTecUrl());//回显教师直播间地址
        liveRetModel.setCatalogId(liveModel.getCatalogId());
        liveRetModel.setCourseId(liveModel.getCourseId());
        liveRetModel.setContent(live.getRomeId());
        liveRetModel.setResourceId(""+details.get(0).getResourcesId());
        liveRetModel.setType("3");
        liveRetModel.setLiveStartTime(live.getStartTime());
        liveRetModel.setLiveEndTime(live.getEndTime());
        if(1 == DateUtils.dateCompare(live.getStartTime(),DateUtils.now())){// 表示直播未开始
            liveRetModel.setLiveResult("（直播未开始）");
            liveRetModel.setLiveStatus(0);
        }else if(-1 == DateUtils.dateCompare(live.getStartTime(),DateUtils.now())){
            if(1 == DateUtils.dateCompare(live.getEndTime(),DateUtils.now())){
                liveRetModel.setLiveResult("（正在直播）");
                liveRetModel.setLiveStatus(1);
            }else if(-1 == DateUtils.dateCompare(live.getEndTime(),DateUtils.now())){
                liveRetModel.setLiveResult("（直播已结束）");
                liveRetModel.setLiveStatus(2);
            }
        }
        return liveRetModel;
    }

    @Override
    public LiveDetailInfo searchRoom(String roomId, HttpServletRequest request) throws Exception {

        LiveModel liveModel = new LiveModel();
        Map paraMap = new HashMap();
        paraMap.put("romeId", roomId);
        List<LiveTelecast> list = liveTelecastDao.selectList(paraMap);
        if (CommonUtils.listIsEmptyOrNull(list)) {
            throw new ServiceException("未查询到直播间信息！");
        }
        Date date = DateUtils.now();
        liveModel.setAppId(appId);
        liveModel.setAppKey(appKey);
        liveModel.setRoomId(roomId);
        String timestamp = null;
        try {
            timestamp = DateUtils.date2String(date, "yyyy-MM-dd'T'HH:mm:ss'Z'");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String random = ApplicationUtils.getNumStringRandom(6);
        liveModel.setSignature(getSignature(timestamp, random));
        liveModel.setSignatureNonce(random);
        liveModel.setTimestamp(timestamp);

        JSONObject requestBody = JSONObject.parseObject(JSONObject.toJSONString(liveModel));
        requestBody.remove("catalogId");
        requestBody.remove("courseId");
        ResponseEntity<JSONObject> response;
        try {
            response = RestTemplateUtils.post(searchRoom, getRequestEntity(requestBody), JSONObject.class);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        Map retMap = JSONUtil.parseJSON2Map(Objects.requireNonNull(response.getBody()));
        if (!"1".equals(retMap.get("status"))) {
            throw new ServiceException((String) retMap.get("message"));
        }
        Map<String, Object> payload = (Map<String, Object>) retMap.get("payload");
        log.info("查询直播间详情返回的payload【{}】", payload.toString());
        LiveDetailInfo detailInfo = new LiveDetailInfo();
        detailInfo.setName((String) payload.get("name"));
        detailInfo.setAssistantAutoLoginUrl((String) payload.get("assistantAutoLoginUrl"));
        detailInfo.setAssistantPass((String) payload.get("assistantPass"));
        detailInfo.setDesc((String) payload.get("desc"));
        detailInfo.setEndTime((String) payload.get("endTime"));
        detailInfo.setPublisherPass((String) payload.get("publisherPass"));
        detailInfo.setPublishUrl((String) payload.get("publishUrl"));
        detailInfo.setRoomid((String) payload.get("roomid"));
        detailInfo.setStartTime((String) payload.get("startTime"));
        detailInfo.setStatus("" + payload.get("status"));
        detailInfo.setStudentAutoLoginUrl((String) payload.get("studentAutoLoginUrl"));
        detailInfo.setTeacherAutoLoginUrl((String) payload.get("teacherAutoLoginUrl"));
        detailInfo.setId("" + payload.get("id"));
        return detailInfo;
    }

    private String getSignature(String timestamp, String random) throws Exception {
        /**
         * 签名结果串拼接 signature
         * 规则：appId&appKey&timestamp&signatureNonce 拼接后用HMAC-SHA1加密
         * 加密秘钥：encryptKey 默认分配
         * appId&appKey 默认分配
         * signatureNonce：6位随机数
         * timestamp格式：日期格式按照ISO8601标准表示，并需要使用 UTC 时间。
         * 格式为：YYYY-MM-DDThh:mm:ssZ 例如，2019-05-26T12:00:00Z（为北京时间 2019 年 5 月 26 日 12 点 0 分 0 秒）
         */
        StringBuilder signature = new StringBuilder();
        signature = signature.append(appId).append("&").append(appKey).append("&").append(timestamp).append("&").append(random);
        String signatureStr = HmacSHA1Util.HmacSHA1Encrypt(signature.toString(), encryptKey);
        log.info("请求timestamp:【{}】,\n请求random:【{}】,\n签名结果串拼接结果:【{}】", timestamp, random, signatureStr);
        return signatureStr;
    }

    @Override
    public String getRoomImg(String roomId, HttpServletRequest request) throws Exception {

        Date date = DateUtils.now();
        // 先查是否存在
        Map paraMap = new HashMap();
        paraMap.put("romeId", roomId);
        List<LiveTelecast> list = liveTelecastDao.selectList(paraMap);
        if (CommonUtils.listIsEmptyOrNull(list)) {
            throw new ServiceException("未查询到直播间信息！");
        }
        LiveTelecast liveTelecast = list.get(0);
        LiveBaseModel model = new LiveBaseModel();
        model.setAppId(appId);
        model.setAppKey(appKey);
        String timestamp = null;
        try {
            timestamp = DateUtils.date2String(date, "yyyy-MM-dd'T'HH:mm:ss'Z'");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String random = ApplicationUtils.getNumStringRandom(6);
        model.setSignature(getSignature(timestamp, random));
        model.setSignatureNonce(random);
        model.setTimestamp(timestamp);
        JSONObject requestBody = JSONObject.parseObject(JSONObject.toJSONString(model));
        requestBody.put("viewername", liveTelecast.getCreateUser());
        requestBody.put("roomId", roomId);
        ResponseEntity<byte[]> response;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            response = RestTemplateUtils.get(getQrCode, headers,byte[].class,requestBody);
            log.info("调用查询二维码接口成功!");
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        return JSONObject.toJSONString(response.getBody());
    }

    @Override
    public String closeRoom(String roomId) throws Exception {
        Date date = DateUtils.now();
        // 先查是否存在
        Map paraMap = new HashMap();
        paraMap.put("romeId", roomId);
        List<LiveTelecast> list = liveTelecastDao.selectList(paraMap);
        if (CommonUtils.listIsEmptyOrNull(list)) {
            throw new ServiceException("未查询到直播间信息！");
        }
        LiveTelecast liveTelecast = list.get(0);
        LiveBaseModel model = new LiveBaseModel();
        model.setAppId(appId);
        model.setAppKey(appKey);
        String timestamp = null;
        try {
            timestamp = DateUtils.date2String(date, "yyyy-MM-dd'T'HH:mm:ss'Z'");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String random = ApplicationUtils.getNumStringRandom(6);
        model.setSignature(getSignature(timestamp, random));
        model.setSignatureNonce(random);
        model.setTimestamp(timestamp);
        JSONObject requestBody = JSONObject.parseObject(JSONObject.toJSONString(model));
        requestBody.put("viewername", liveTelecast.getCreateUser());
        requestBody.put("roomId", roomId);
        ResponseEntity<JSONObject> response;
        try {
            response = RestTemplateUtils.post(closeRoom, getRequestEntity(requestBody), JSONObject.class);
        } catch (Throwable e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        Map retMap = JSONUtil.parseJSON2Map(Objects.requireNonNull(response.getBody()));
        if (!"1".equals(retMap.get("status"))) {
            throw new ServiceException((String) retMap.get("message"));
        }
        return (String) retMap.get("message");
    }

    private HttpEntity<MultiValueMap<String, Object>> getRequestEntity(JSONObject jsonObject) {


        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        jsonObject.keySet().forEach(key -> postParameters.add(key, jsonObject.get(key)));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        return new HttpEntity<>(postParameters, headers);
    }
}
