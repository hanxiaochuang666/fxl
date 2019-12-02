package com.by.blcu.resource.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.aop.CourseCheck;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.HttpReqUtil;
import com.by.blcu.core.utils.MapAndObjectUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.course.service.ICourseService;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.resource.dao.IVideoInfoDao;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.dto.VideoInfo;
import com.by.blcu.resource.model.ResourceTypeEnum;
import com.by.blcu.resource.model.VideoInfoVO;
import com.by.blcu.resource.service.IResourcesService;
import com.by.blcu.resource.service.IVideoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("videoInfoService")
@CheckToken
@Slf4j
public class VideoInfoServiceImpl extends BaseServiceImpl implements IVideoInfoService {
    public static final String VIDEO_OK="OK";
    public static final String VIDEO_READY="READY";
    @Resource
    private IVideoInfoDao videoInfoDao;

    @Resource(name="resourcesService")
    private IResourcesService resourcesService;

    @Resource
    private ICourseDetailService courseDetailService;

    @Resource
    private ICourseService courseService;

    @Override
    protected IBaseDao getDao() {
        return this.videoInfoDao;
    }

    @Value("${open.getVideoUrl}")
    private String getVideoUrl;

    @Value("${open.appkey}")
    private String appkey;

    @Value("${open.appsecret}")
    private String appsecret;

    @Value("${open.videoOutTime}")
    private int videoOutTime;

    @Override
    @Transactional
    public void deleteVideoById(int videoInfoId) {
        getDao().deleteByPrimaryKey(videoInfoId);
        Map<String, Object> initMap = MapUtils.initMap("content", videoInfoId + "");
        resourcesService.deleteByParams(initMap);
    }

    @Override
    @Transactional
    public Map<String,String> saveVideoPlayUrl(Integer videoInfoId, String videoName,int userId,Integer bak2,Integer duration) throws Exception{
        VideoInfo videoInfo = videoInfoDao.selectByPrimaryKey(videoInfoId);
        String fileid = videoInfo.getFileId();
        String resUrl=getVideoUrl.replaceAll("\\{fileId\\}",fileid);
        log.info("获取录播视频url:"+resUrl);
        HttpResponse httpResponse = HttpReqUtil.getOpentReq(resUrl + "?orderType=ASC&delayMinute="+videoOutTime,appkey,appsecret);
        Map<String, Object> responsemap = HttpReqUtil.parseHttpResponse(httpResponse);
        Map<String, Object> payload = (Map<String, Object>)responsemap.get("payload");
        String url = payload.get("url").toString();
        log.info("获取到的播放地址:"+url);
        Date date = new Date();
        VideoInfo videoInfo1=new VideoInfo();
        videoInfo1.setVideoInfoId(videoInfo.getVideoInfoId());
        videoInfo1.setVideoName(videoName);
        videoInfo1.setUrl(url);
        videoInfo1.setUpdateUser(userId);
        videoInfo1.setUpdateTime(date);
        videoInfo1.setBak1(VIDEO_OK);
        videoInfo1.setBak2(bak2.toString());
        videoInfo1.setBak3("course");
        videoInfo1.setDuration(duration.longValue());
        getDao().updateByPrimaryKeySelective(videoInfo1);
        //同步到resources表
        Resources resources = new Resources();
        resources.setContent(videoInfo1.getVideoInfoId()+"");
        resources.setType(2);
        resources.setCreateUser(userId);
        resources.setCreayeTime(date);
        resources.setUpdateUser(userId);
        resources.setUpdateTime(date);
        resources.setCheckStatus(0);
        resourcesService.insertSelective(resources);
        Map<String, String> stringObjectMap = new HashMap<>();
        stringObjectMap.put(fileid,url);
        return stringObjectMap;
    }

    @Override
    public RetResult<VideoInfo> getAuthorization(String extendName,int userId,String videoName) throws Exception{
        HttpResponse httpResponse=HttpReqUtil.openObjectReq(extendName,appkey,appsecret);
        Map<String, Object> responsemap = HttpReqUtil.parseHttpResponse(httpResponse);
        Map<String, Object> payload = (Map<String, Object>)responsemap.get("payload");
        VideoInfo videoInfo = MapAndObjectUtils.MapToObject(payload, VideoInfo.class);
        videoInfo.setKeyWord(payload.get("key").toString());
        videoInfo.setCreateUser(userId);
        videoInfo.setCreateTime(new Date());
        videoInfo.setBak1(VIDEO_READY);
        videoInfo.setVideoName(videoName);
        videoInfoDao.insertSelective(videoInfo);
        return RetResponse.makeOKRsp(videoInfo);
    }

    @Override
    @Transactional
    @CourseCheck
    public RetResult editVideoInfo(VideoInfo videoInfo,CourseCheckModel courseCheckModel)throws Exception {
        Map<String, Object> content = MapUtils.initMap("content", videoInfo.getVideoInfoId() + "");
        content.put("type",2);
        List<Resources> objectList = resourcesService.selectList(content);
        if(null==objectList || objectList.size()<=0){
            log.info("录播视频无资源描述信息,视频id"+videoInfo.getVideoInfoId());
            return RetResponse.makeErrRsp("录播视频无资源描述信息,视频id"+videoInfo.getVideoInfoId());
        }
        Resources resources = new Resources();
        resources.setResourcesId(objectList.get(0).getResourcesId());
        resources.setUpdateUser(videoInfo.getUpdateUser());
        resources.setUpdateTime(videoInfo.getUpdateTime());
        getDao().updateByPrimaryKeySelective(videoInfo);
        return RetResponse.makeOKRsp();
    }

    @Override
    @Transactional
    @CourseCheck
    public Integer addVideoInfo(VideoInfoVO videoInfoVO, HttpServletRequest request, CourseCheckModel courseCheckModel) throws Exception{
        Integer userId = Integer.valueOf(request.getAttribute("userId").toString());
        Integer courseId = videoInfoVO.getCourseId();
        Resources resourceNew = null;//传入新资源

        Map<String, Object> param = MapUtils.initMap("content", String.valueOf(videoInfoVO.getVideoInfoId()));
        List<Resources> resources = resourcesService.selectList(param);
        if(resources != null && resources.size() > 0){
            //1.获取关联Resource
            for(Resources r : resources){
                if(ResourceTypeEnum.VIDEO.getTypeCode().intValue() == r.getType().intValue()){
                    resourceNew = r;
                    if(!StringUtils.isBlank(videoInfoVO.getTitle())){
                        resourceNew.setTitle(videoInfoVO.getTitle());
                    }
                }
            }

            //2.查询目录关联 新增或更新
            Map<String, Object> detailParam = MapUtils.initMap("catalogId", videoInfoVO.getCatalogId());
            List<CourseDetail> details = courseDetailService.selectList(detailParam);
            if(details != null && details.size() > 0){
                CourseDetail detail = details.get(0);
                if(resourceNew.getResourcesId().intValue() != detail.getResourcesId().intValue()){

                    //根据类型移除无关资源
                    if(0==courseCheckModel.getIsUpper())
                        resourcesService.removeByResourceType(detail.getResourcesId());
                    detail.setModelType(1);
                    detail.setResourcesId(resourceNew.getResourcesId());
                    detail.setUpdateTime(new Date());
                    detail.setUpdateUser(userId);
                    courseDetailService.updateByPrimaryKeySelective(detail);
                }
                //移除多余 CourseDetail
                for(int i=1;i<details.size();i++){
                    courseDetailService.deleteByPrimaryKey(details.get(i).getCourseDetailId());
                }
            }else {
                CourseDetail courseDetail = new CourseDetail();
                courseDetail.setModelType(1);
                courseDetail.setCourseId(videoInfoVO.getCourseId());
                courseDetail.setModelType(videoInfoVO.getModelType());
                courseDetail.setCatalogId(videoInfoVO.getCatalogId());
                courseDetail.setResourcesId(resourceNew.getResourcesId());
                courseDetail.setCreateTime(new Date());
                courseDetail.setCreateUser(userId);
                int insertState = courseDetailService.insertSelective(courseDetail);
            }
            resourcesService.updateByPrimaryKeySelective(resourceNew);
            //courseService.changeCourseStatus(courseId, userId);
            return resourceNew.getResourcesId();
        }else{
            throw new ServiceException("视频缺失关联资源，视频ID：" + videoInfoVO.getVideoInfoId());
        }
    }

    @Override
    public RetResult saveAccaVideo(JSONObject obj,CourseCheckModel courseCheckModel)throws Exception {
        String videoName = obj.getString("videoName");
        String url = obj.getString("url");
        Integer userId = obj.getInteger("userId");
        Date date=new Date();
        if(obj.containsKey("videoInfoId")){
            if(obj.getInteger("videoInfoId").intValue()<=0){
                log.info("ACCA课程id违列");
                return RetResponse.makeErrRsp("ACCA课程id违列");
            }
            VideoInfo videoInfo = getDao().selectByPrimaryKey(obj.getInteger("videoInfoId"));
            if(null==videoInfo|| !"ACCA".equals(videoInfo.getBucketName())){
                log.info("请求数据错误:"+obj.getInteger("videoInfoId"));
                return RetResponse.makeErrRsp("请求数据错误:"+obj.getInteger("videoInfoId"));
            }
            VideoInfo videoInfo1 = new VideoInfo();
            videoInfo1.setVideoInfoId(videoInfo.getVideoInfoId());
            videoInfo1.setUrl(url);
            videoInfo1.setVideoName(videoName);
            videoInfo1.setBak1(VideoInfoServiceImpl.VIDEO_OK);
            videoInfo1.setBak3("course");
            videoInfo1.setCreateUser(userId);
            getDao().updateByPrimaryKeySelective(videoInfo1);
        }else {
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setBak1(VideoInfoServiceImpl.VIDEO_OK);
            videoInfo.setBak3("course");
            videoInfo.setVideoName(videoName);
            videoInfo.setUrl(url);
            videoInfo.setBucketName("ACCA");
            videoInfo.setCreateUser(userId);
            getDao().insertSelective(videoInfo);
            //同步到resources表
            Resources resources = new Resources();
            resources.setContent(videoInfo.getVideoInfoId()+"");
            resources.setType(2);
            resources.setCreateUser(userId);
            resources.setCreayeTime(date);
            resources.setUpdateUser(userId);
            resources.setUpdateTime(date);
            resources.setCheckStatus(2);
            resourcesService.insertSelective(resources);
        }
        return RetResponse.makeOKRsp();
    }

}