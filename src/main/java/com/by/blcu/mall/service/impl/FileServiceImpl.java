package com.by.blcu.mall.service.impl;

import com.by.blcu.core.aop.CourseCheck;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.core.utils.*;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.mall.dao.FileMapper;
import com.by.blcu.mall.model.File;
import com.by.blcu.mall.model.ResourceFile;
import com.by.blcu.mall.service.FileService;
import com.by.blcu.mall.vo.CourseCategoryCommodityVo;
import com.by.blcu.mall.vo.VideoInfoVo;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.dto.VideoInfo;
import com.by.blcu.resource.model.FileViewModel;
import com.by.blcu.resource.model.ResourceTypeEnum;
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

/**
* @Description: FileService接口实现类
* @author 李程
* @date 2019/08/21 19:19
*/
@Service
@Slf4j
public class FileServiceImpl extends AbstractService<File> implements FileService {

    public static final String VIDEO_OK="OK";
    public static final String VIDEO_READY="READY";

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

    @Resource
    private FileMapper fileMapper;
    @Resource
    private IResourcesService resourcesService;
    @Resource
    private ICourseDetailService courseDetailService;

    @Resource
    private IVideoInfoService videoInfoService;

    @Override
    public <T> long selectCount(Map<String, Object> map) throws Exception{
        return fileMapper.selectResourcesCount(map);
    }

    @Override
    public <T> List<ResourceFile> selectList(Map<String, Object> map) throws Exception{
        return fileMapper.selectResourcesList(map);
    }

    @Override
    @Transactional
    @CourseCheck
    public Integer insertResourceFile(File file, FileViewModel fileViewModel, HttpServletRequest request, CourseCheckModel courseCheckModel) throws Exception{
        Integer userId = Integer.valueOf(request.getAttribute("userId").toString());
        String orgCode = request.getAttribute("orgCode").toString();
        CourseDetail courseDetail = new CourseDetail();
        //0.判断是否为目录下资源 是否已关联资源
        if(!"0".equals(fileViewModel.getCatalogId())){
            Map<String, Object> detailParam = MapUtils.initMap("catalogId", fileViewModel.getCatalogId());
            List<CourseDetail> details = courseDetailService.selectList(detailParam);
            if(details != null && details.size() > 0){
                //原目录包含资源 则移除资源
                courseDetail = details.get(0);
                if(0==courseCheckModel.getIsUpper())
                    resourcesService.removeByResourceType(courseDetail.getResourcesId());
            }
        }

        //1.创建资源表信息 检索是否存在对应resource
        Resources resource = new Resources();
        resource.setTitle(fileViewModel.getFileName());
        if(!"0".equals(fileViewModel.getCatalogId())) {
            //课程下资料
            resource.setType(ResourceTypeEnum.DOC.getTypeCode());
        }else {
            //目录下文档
            resource.setType(ResourceTypeEnum.DATA.getTypeCode());
        }
        Date date = new Date();
        resource.setOrgCode(orgCode);//后期 org_code 待调整
        resource.setContent(file.getFileId());//关联试卷ID
        resource.setCreateUser(userId);
        resource.setCreayeTime(date);
        resource.setUpdateUser(userId);
        resource.setUpdateTime(date);
        resource.setCheckStatus(0);
        Integer state = resourcesService.insertSelective(resource);
        if(state < 0) return state;

        //2.创建课程详情信息
        courseDetail.setCourseId(Integer.valueOf(fileViewModel.getCourseId()));
        courseDetail.setModelType(Integer.valueOf(fileViewModel.getModelType()));

        if(StringUtils.isEmpty(fileViewModel.getCatalogId())) {
            courseDetail.setCatalogId(0);//表示非绑定在目录上的资源
        }else{
            courseDetail.setCatalogId(Integer.valueOf(fileViewModel.getCatalogId()));
        }
        courseDetail.setResourcesId(resource.getResourcesId());
        courseDetail.setCreateTime(new Date());
        courseDetail.setCreateUser(userId);

        if(StringUtils.isEmpty(courseDetail.getCourseDetailId())){
            courseDetailService.insertSelective(courseDetail);
        }else {
            courseDetailService.updateByPrimaryKeySelective(courseDetail);
        }
        //3.创建文件表信息
        file.setBak2(String.valueOf(resource.getResourcesId()));
        file.setBak3(fileViewModel.getCatalogId());
        state = fileMapper.insert(file);
        return state;
    }

    @Override
    public File selectByFileId(String fileId) {
        return fileMapper.selectByFileId(fileId);
    }

    @Override
    public VideoInfoVo getAuthorization(String extendName, int userId, String videoName,String appkey,String appsecret) throws Exception {
        HttpResponse httpResponse=HttpReqUtil.openObjectReq(extendName,appkey,appsecret);
        Map<String, Object> responsemap = HttpReqUtil.parseHttpResponse(httpResponse);
        Map<String, Object> payload = (Map<String, Object>)responsemap.get("payload");
        VideoInfoVo videoInfoVo = MapAndObjectUtils.MapToObject(payload, VideoInfoVo.class);
        videoInfoVo.setKeyWord(payload.get("key").toString());
        videoInfoVo.setCreateUser(userId);
        videoInfoVo.setCreateTime(new Date());
        videoInfoVo.setBak1(VIDEO_READY);
        videoInfoVo.setBak3("mall");
        videoInfoVo.setVideoName(videoName);
        videoInfoService.insertSelective(videoInfoVo);
        return videoInfoVo;
    }

    @Override
    @Transactional
    public VideoInfoVo saveVideoPlayUrl(Integer videoInfoId, String videoName,int userId,String size,Integer duration) throws Exception{
        VideoInfo videoInfo = videoInfoService.selectByPrimaryKey(videoInfoId);
        VideoInfoVo videoInfoVo = MapAndObjectUtils.ObjectClone(videoInfo, VideoInfoVo.class);
        String fileid = videoInfoVo.getFileId();
        String resUrl=getVideoUrl.replaceAll("\\{fileId\\}",fileid);
        log.info("获取录播视频url:"+resUrl);
        HttpResponse httpResponse = HttpReqUtil.getOpentReq(resUrl + "?orderType=ASC&delayMinute="+videoOutTime,appkey,appsecret);
        Map<String, Object> responsemap = HttpReqUtil.parseHttpResponse(httpResponse);
        Map<String, Object> payload = (Map<String, Object>)responsemap.get("payload");
        String url = payload.get("url").toString();
        log.info("获取到的播放地址:"+url);
        Date date = new Date();
        VideoInfoVo videoInfoVo1=new VideoInfoVo();
        videoInfoVo1.setVideoInfoId(videoInfoVo.getVideoInfoId());
        videoInfoVo1.setVideoName(videoName);
        videoInfoVo1.setUrl(url);
        videoInfoVo1.setUpdateUser(userId);
        videoInfoVo1.setUpdateTime(date);
        videoInfoVo1.setBak1(VIDEO_OK);
        videoInfoVo1.setBak2(size.toString());
        videoInfoVo1.setDuration(duration.longValue());
        videoInfoService.updateByPrimaryKeySelective(videoInfoVo1);
        VideoInfo videoInfoView = videoInfoService.selectByPrimaryKey(videoInfoId);
        VideoInfoVo videoInfoVoView = MapAndObjectUtils.ObjectClone(videoInfoView, VideoInfoVo.class);
        //同步到File表
        File file = selectByFileId(videoInfoVo1.getVideoInfoId() + "");
        if(null == file){
            File fileSave = new File();
            fileSave.setFileName(videoInfoVo1.getVideoName() + "." + videoInfoVo.getExtensionName());
            fileSave.setFileSize(videoInfoVo1.getBak2());
            fileSave.setFileTime(new Date());
            fileSave.setIsdelete(1);
            fileSave.setIsvalidity(1);
            fileSave.setFileId(videoInfoVo1.getVideoInfoId()+"");
            Integer state = insert(fileSave);
        }
        return videoInfoVoView;
    }

}