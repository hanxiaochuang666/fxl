package com.by.blcu.resource.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.*;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.mall.service.RedisService;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.service.SsoUserService;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.dto.VideoInfo;
import com.by.blcu.resource.model.*;
import com.by.blcu.resource.service.IResourcesService;
import com.by.blcu.resource.service.IVideoInfoService;
import com.by.blcu.resource.service.impl.VideoInfoServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/videoInfo")
@Api(tags = "视频管理API",description = "包含接口：\n" +
        "1、视频列表查询\n" +
        "2、根据视频id获取视频播放地址\n" +
        "3、视频编辑\n" +
        "4、根据主键删除视频\n" +
        "5、视频上传成功回调\n" +
        "6、获取授权\n" +
        "7、视频编辑\n" +
        "8、目录下添加视频\n" +
        "9、目录下视频信息查看\n")
@CheckToken
@Slf4j
public class VideoInfoController {
    @Resource(name = "videoInfoService")
    private IVideoInfoService videoInfoService;

    @Resource(name="resourcesService")
    private IResourcesService resourcesService;

    @Resource
    private SsoUserService ssoUserService;

    @Resource
    private RedisService redisService;

    @Value("${open.authorizationUrl}")
    private String authorizationUrl;

    @Value("${open.videoOutTime}")
    private int videoOutTime;

    @Value("${open.getVideoUrl}")
    private String getVideoUrl;

    @Value("${open.appkey}")
    private String appkey;

    @Value("${open.appsecret}")
    private String appsecret;

    @RequestMapping(value = "/list", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "视频列表查询",notes = "视频列表查询",httpMethod = "POST")
    public RetResult<List<VideoInfoListModel>> list(HttpServletRequest httpServletRequest, @RequestBody VideoInfoReqModel videoInfoReqModel)throws Exception{
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        Map<String, Object> param = MapAndObjectUtils.ObjectToMap2(videoInfoReqModel);
        CommonUtils.queryParamOpt(param);
        param.put("createUser",userId);
        param.put("bak1", VideoInfoServiceImpl.VIDEO_OK);
        param.put("bak3","course");
        param.put("_sort_line","video_info_id");
        log.info("list请求数据:"+param.toString());
        long count = videoInfoService.selectCount(param);
        if(count<=0)
            return RetResponse.makeRsp(null,count);
        List<VideoInfo> list = videoInfoService.selectList(param);
        List<VideoInfoListModel> resList=new ArrayList<>();
        for (VideoInfo videoInfo : list) {
            VideoInfoListModel videoInfoListModel = new VideoInfoListModel();
            videoInfoListModel.setVideoInfoId(videoInfo.getVideoInfoId());
            videoInfoListModel.setBucketName(videoInfo.getBucketName());
            videoInfoListModel.setVideoName(videoInfo.getVideoName());
            if(!StringUtils.isEmpty(videoInfo.getBak2()))
                videoInfoListModel.setSize(Integer.valueOf(videoInfo.getBak2()));
            else
                videoInfoListModel.setSize(0);
            if("ACCA".equals(videoInfo.getBucketName())){
                videoInfoListModel.setUrl(videoInfo.getUrl());
            }else {
                videoInfoListModel.setUrl("/videoInfo/getPlayUrl");
            }
            videoInfoListModel.setDuration(videoInfo.getDuration().intValue());
            //根据userId查询用户名称
            SsoUser userByUserIdInter = ssoUserService.getUserByIdInter(userId);
            videoInfoListModel.setCreateUserName(userByUserIdInter.getUserName());
            Date createTime = videoInfo.getCreateTime();
            String date2String = DateUtils.date2String(createTime, DateUtils.TIME_FORMAT);
            videoInfoListModel.setCreateTimeStr(date2String);
            resList.add(videoInfoListModel);
        }
        return RetResponse.makeRsp(resList,count);
    }

    @RequestMapping(value = "/saveAccaVideo", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "保存Acca视频",notes = "保存Acca视频",httpMethod = "POST")
    public RetResult saveAccaVideo(HttpServletRequest httpServletRequest,@RequestBody JSONObject obj)throws Exception{
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        if (!obj.containsKey("videoName") || StringUtils.isEmpty(obj.getString("videoName"))) {
            log.info("ACCA课程名称不能为空");
            return RetResponse.makeErrRsp("ACCA课程名称不能为空");
        }
        if (!obj.containsKey("url") || StringUtils.isEmpty(obj.getString("url"))) {
            log.info("ACCA课程播放地址不能为空");
            return RetResponse.makeErrRsp("ACCA课程播放地址不能为空");
        }
        obj.put("userId",userId);
        return videoInfoService.saveAccaVideo(obj,new CourseCheckModel());
    }

    @RequestMapping(value = "/getPlayUrl", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "根据视频id获取视频播放地址",notes = "根据视频id获取视频播放地址",httpMethod = "POST")
    public RetResult getPlayUrl(@RequestBody JSONObject obj)throws Exception {
        if (!obj.containsKey("videoInfoId") || obj.getInteger("videoInfoId").intValue() <= 0) {
            log.info("videoInfoId 错误");
            return RetResponse.makeErrRsp("videoInfoId 错误");
        }
        VideoInfo videoInfo = videoInfoService.selectByPrimaryKey(obj.getInteger("videoInfoId"));
        if (null == videoInfo) {
            log.info("请求数据错误:" + obj.getInteger("videoInfoId"));
            return RetResponse.makeErrRsp("请求数据错误:" + obj.getInteger("videoInfoId"));
        }
        String fileId = videoInfo.getFileId();
        String bucketName = videoInfo.getBucketName();
        String url = null;
        Map<String, Object> url1 = MapUtils.initMap();
        if (bucketName.equals("ACCA")) {
            url = videoInfo.getUrl();
            url1.put("url", url);
            url1.put("bucketName","ACCA");
        } else {
            if(StringUtils.isEmpty(videoInfo.getFileId())){
                log.info("请求数据错误:" + obj.getInteger("videoInfoId"));
                return RetResponse.makeErrRsp("请求数据错误:" + obj.getInteger("videoInfoId"));
            }
            if (!redisService.hasKey(fileId)) {
                String resUrl = getVideoUrl.replaceAll("\\{fileId\\}", fileId);
                log.info("获取录播视频url:" + resUrl);
                HttpResponse httpResponse = HttpReqUtil.getOpentReq(resUrl + "?orderType=ASC&delayMinute=" + videoOutTime, appkey, appsecret);
                Map<String, Object> responsemap = HttpReqUtil.parseHttpResponse(httpResponse);
                if(!responsemap.containsKey("payload")){
                    log.info("获取播放地址失败，请求URL:"+resUrl + "?orderType=ASC&delayMinute=" + videoOutTime);
                    return RetResponse.makeErrRsp("获取播放地址失败，请求URL:"+resUrl + "?orderType=ASC&delayMinute=" + videoOutTime);
                }

                Map<String, Object> payload = (Map<String, Object>) responsemap.get("payload");
                url = payload.get("url").toString();
                log.info("获取到的播放地址:" + url);
                videoInfo.setUrl(url);
                redisService.setWithExpire(fileId, url, videoOutTime * 60);
                videoInfoService.updateByPrimaryKeySelective(videoInfo);
                url1.put("url", url);
                url1.put("bucketName","openbj");
            } else {
                url = redisService.get(fileId);
                url1.put("url", url);
                url1.put("bucketName","openbj");
            }
        }


        return RetResponse.makeOKRsp(url1);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "视频编辑",notes = "视频编辑",httpMethod = "POST")
    public RetResult edit(HttpServletRequest httpServletRequest,@Valid @RequestBody SaveVideoPlayModel saveVideoPlayModel)throws Exception{
        //, @ApiParam(value = "视频主键id")@RequestParam Integer videoInfoId, @ApiParam(value = "视频名称")@RequestParam(required = false) String videoInfoNam
        Integer videoInfoId=saveVideoPlayModel.getVideoInfoId();
        String  videoInfoName=saveVideoPlayModel.getVideoInfoName();
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        boolean operation = CommonUtils.isOperation(userId, videoInfoId, videoInfoService);
        if(!operation){
            log.info("自己只能操作自己的视频");
            return RetResponse.makeRsp(RetCode.SUCCESS.code,"自己只能操作自己的视频");
        }

        if(!StringUtils.isEmpty(videoInfoName)){
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setVideoInfoId(videoInfoId);
            videoInfo.setVideoName(videoInfoName);
            videoInfo.setUpdateTime(new Date());
            videoInfo.setUpdateUser(userId);
            return videoInfoService.editVideoInfo(videoInfo,new CourseCheckModel());
        }
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "根据主键删除视频",notes = "根据主键删除视频",httpMethod = "DELETE")
    public RetResult deleteById(HttpServletRequest httpServletRequest,@ApiParam(value = "视频主键id") @RequestParam Integer videoInfoId)throws Exception{
        if(null==videoInfoId)
            return RetResponse.makeOKRsp();
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        boolean operation = CommonUtils.isOperation(userId, videoInfoId, videoInfoService);
        if(!operation){
            log.info("自己只能操作自己的视频");
            return RetResponse.makeRsp(RetCode.SUCCESS.code,"自己只能操作自己的视频");
        }
        //查询该视频是否正在关联中
        boolean usedResources = resourcesService.isUsedResources(videoInfoId, 2, null, null);
        if(usedResources){
            log.info("视频id为"+videoInfoId+"的视频正在被课程使用，不能删除");
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"视频id为"+videoInfoId+"的视频正在被课程使用，不能删除");
        }
        //删除逻辑
        videoInfoService.deleteVideoById(videoInfoId);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/saveVideoPlayUrl", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "视频上传成功回调",notes = "视频上传成功回调",httpMethod = "POST")
    public RetResult saveVideoPlayUrl(HttpServletRequest httpServletRequest,@Valid @RequestBody SaveVideoPlayModel saveVideoPlayModel)throws Exception{
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        if(!StringUtils.isEmpty(saveVideoPlayModel.getVideoInfoId())
                &&!StringUtils.isEmpty(saveVideoPlayModel.getVideoInfoName())){
            if(StringUtils.isEmpty(saveVideoPlayModel.getBak2()))
                saveVideoPlayModel.setBak2(0);
            Map<String, String> stringIntegerMap = videoInfoService.saveVideoPlayUrl(saveVideoPlayModel.getVideoInfoId(), saveVideoPlayModel.getVideoInfoName(), userId, saveVideoPlayModel.getBak2(), saveVideoPlayModel.getDuration());
            if(null!=stringIntegerMap) {
                for (Map.Entry<String, String> entry : stringIntegerMap.entrySet()) {
                    String mapKey = entry.getKey();
                    String mapValue = entry.getValue();
                    redisService.setWithExpire(mapKey, mapValue, videoOutTime * 60);
                }
            }
        }
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/getAuthorization", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "获取授权",notes = "获取授权",httpMethod = "GET")
    public RetResult<VideoInfo> getAuthorization(HttpServletRequest httpServletRequest,@RequestBody GetAuthorModel getAuthorModel)throws Exception{
        String extendName = getAuthorModel.getExtendName();
        String videoName=getAuthorModel.getVideoName();
        if(StringUtils.isEmpty(extendName))
            extendName=authorizationUrl+"?extendName=mp4&storeType=VE";
        else
            extendName=authorizationUrl+"?extendName="+extendName+"&storeType=VE";
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        if(StringUtils.isEmpty(videoName)){
            log.info("视频名称不能为空");
            return RetResponse.makeErrRsp("视频名称不能为空");
        }
        Map<String, Object> stringObjectMap = MapUtils.initMap("createUser",userId);
        stringObjectMap.put("videoName",videoName);
        long count = videoInfoService.selectCount(stringObjectMap);
        if(count>0){
            log.info("已存在视频名称为:"+videoName+"的视频");
            return RetResponse.makeErrRsp("已存在视频名称为:"+videoName+"的视频");
        }
        return  videoInfoService.getAuthorization(extendName,userId,videoName);
    }

    @PostMapping("/add")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "新增目录下录播视频")
    public RetResult addVideoInfo(@Valid @RequestBody VideoInfoVO videoInfoVO, HttpServletRequest request){
        try {
            //新增目录下视频录播资源
            Integer rid = videoInfoService.addVideoInfo(videoInfoVO, request,new CourseCheckModel());
            Map map = MapUtils.initMap("resourcesId",rid);
            map.put("content", videoInfoVO.getVideoInfoId());
            return RetResponse.makeOKRsp(map);
        } catch (Exception e) {
            log.error("新增目录视频失败", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @PostMapping("/get")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "目录下视频信息查看")
    public RetResult getVideoInfoByResourcesId(@RequestBody VideoInfoVO videoInfoVO) throws Exception{
        Resources resources = null;
        VideoInfo videoInfo = null;
        Map param = MapUtils.initMap("resourcesId", videoInfoVO.getResourcesId());
        List<Resources> resourcesList = resourcesService.selectList(param);
        if(resourcesList != null && resourcesList.size() > 0){
            resources = resourcesList.get(0);
            if(org.apache.commons.lang3.StringUtils.isNumeric(resources.getContent())){
                Map videoParam = MapUtils.initMap("videoInfoId", Integer.valueOf(resources.getContent()));
                List<VideoInfo> videoInfos = videoInfoService.selectList(videoParam);
                if(videoInfos != null && videoInfos.size() > 0){
                    videoInfo = videoInfos.get(0);
                }else{
                    throw new ServiceException("资源下录播视频不存在，视频ID：" + videoInfoVO.getResourcesId());
                }
            }

        }else{
            throw new ServiceException("该资源不存在，资源ID：" + videoInfoVO.getResourcesId());
        }

        return RetResponse.makeOKRsp(videoInfo);
    }


}
