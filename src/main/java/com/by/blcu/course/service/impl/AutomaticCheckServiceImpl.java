package com.by.blcu.course.service.impl;

import com.alibaba.fastjson.JSON;
import com.by.blcu.core.contentSecurity.ContentSecurityImpl;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.DateUtils;
import com.by.blcu.core.utils.HttpReqUtil;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.dao.IAutomaticCheckDao;
import com.by.blcu.course.dao.ICourseDao;
import com.by.blcu.course.dto.*;
import com.by.blcu.course.model.AliCheckStatusEnum;
import com.by.blcu.course.model.CheckPassModel;
import com.by.blcu.course.model.CourseCheckQueryModel;
import com.by.blcu.course.model.CourseCommitCheck;
import com.by.blcu.course.service.IAutomaticCheckService;
import com.by.blcu.course.service.ICatalogService;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.course.service.ICourseModelTypeService;
import com.by.blcu.mall.model.File;
import com.by.blcu.mall.service.CourseCategoryInfoService;
import com.by.blcu.mall.service.FileService;
import com.by.blcu.mall.service.RedisService;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.service.SsoUserService;
import com.by.blcu.manager.umodel.UserSearchModel;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.dto.VideoInfo;
import com.by.blcu.resource.model.TestPaperCheckModel;
import com.by.blcu.resource.service.IResourcesService;
import com.by.blcu.resource.service.ITestPaperService;
import com.by.blcu.resource.service.IVideoInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Service("automaticCheckService")
@Slf4j
public class AutomaticCheckServiceImpl extends BaseServiceImpl implements IAutomaticCheckService {
    @Autowired
    private IAutomaticCheckDao automaticCheckDao;


    @Resource(name = "contentSecurity")
    private ContentSecurityImpl contentSecurityImpl;

    @Resource(name="courseDetailService")
    private ICourseDetailService courseDetailService;

    @Resource(name = "courseModelTypeService")
    private ICourseModelTypeService courseModelTypeService;

    @Resource(name = "resourcesService")
    private IResourcesService resourcesService;

    @Resource(name="videoInfoService")
    private IVideoInfoService videoInfoService;

    @Resource(name="testPaperService")
    private ITestPaperService testPaperService;

    @Autowired
    private ICatalogService catalogService;

    @Resource
    private FileService fileService;

    @Resource
    private RedisService redisService;

    @Resource
    private ICourseDao courseDao;

    @Value("${alibaba.replaceFromUrl}")
    private String replaceFromUrl;
    @Value("${alibaba.replaceUrl}")
    private String replaceUrl;

    @Value("${open.getVideoUrl}")
    private String getVideoUrl;

    @Value("${open.videoOutTime}")
    private int videoOutTime;

    @Value("${open.appkey}")
    private String appkey;

    @Value("${open.appsecret}")
    private String appsecret;


    @Resource
    private SsoUserService ssoUserService;

    @Resource
    private CourseCategoryInfoService courseCategoryInfoService;



    @Override
    protected IBaseDao getDao() {
        return this.automaticCheckDao;
    }

    @Override
    public RetResult commitCheck(int courseId) throws Exception{
        Course courseTemp = courseDao.selectByPrimaryKey(courseId);
        if(courseTemp==null){
            log.info("课程id为"+courseId+"的课程不存在");
            return RetResponse.makeErrRsp("课程id为"+courseId+"的课程不存在");
        }
        if(courseTemp.getStatus()>=1){
            log.info("课程id为"+courseId+"的课程已进入审核阶段");
            return RetResponse.makeErrRsp("课程id为"+courseId+"的课程已进入审核阶段");
        }
        //1.自动审核图片
        List<String> imageLst=new ArrayList<>();
        Condition condition=new Condition(File.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("commodityId",courseId+"");
        criteria.andEqualTo("fileType","pic");
        List<File> filesImage = fileService.selectByCondition(condition);
        Map<String,File> fileMap=new HashMap<>();
        for (File file : filesImage) {
            if(!StringUtils.isEmpty(file)
                    && !StringUtils.isEmpty(file.getFilePath())) {
                if(null==file || StringUtils.isEmpty(file.getCheckId())) {
                    String filePath = file.getFilePath();
                    //String s = filePath.replaceFirst(replaceFromUrl, replaceUrl);
                    fileMap.put(filePath, file);
                    imageLst.add(filePath);
                }
            }
        }
        //2.自动审核文档
        Map<String, Object> initMap = MapUtils.initMap("courseId", courseId);
        Map<String,Resources> resourcesMap=new HashMap<>();
        Map<String,Resources> videoResourcesMap=new HashMap<>();
        List<String> fileLst=new ArrayList<>();
        List<String> videoLst=new ArrayList<>();
        Map<Integer,String> resourcesTextMap=new HashMap<>();
        Map<Integer,String> catalogTextMap=new HashMap<>();
        Map<Integer,List<String>> testPaperVoiceMap=new HashMap<>();
        Map<String, Object> stringObjectMap = MapUtils.initMap("courseId", courseId);
        stringObjectMap.put("status",1);
        //课程目录map
        List<Catalog> catalogs = catalogService.selectList(stringObjectMap);
        for (Catalog catalog : catalogs) {
            Integer catalogId = catalog.getCatalogId();
            String name = catalog.getName();
            String checkId = catalog.getCheckId();
            if(StringUtils.isEmpty(checkId)
                    || (null!=catalog.getCheckTime()
                    && catalog.getUpdateTime().after(catalog.getCheckTime())))
                catalogTextMap.put(catalogId, name);
        }
        List<CourseDetail> categoryLst = courseDetailService.selectList(initMap);
        if(null!=categoryLst && categoryLst.size()>0) {
            for (CourseDetail courseDetail : categoryLst) {
                Integer resourcesId = courseDetail.getResourcesId();
                if(null!=resourcesId && resourcesId.intValue()>0){
                    Resources resources = resourcesService.selectByPrimaryKey(resourcesId);
                    if(null!=resources){
                        if(null!=resources.getCheckId() &&
                                (null!=resources.getCheckTime() && null!=resources.getUpdateTime()
                                        && resources.getUpdateTime().before(resources.getCheckTime())))
                            continue;
                        //提取文档
                        if(6==resources.getType()|| 8==resources.getType()) {
                            File file = fileService.selectById(resources.getContent());
                            if(null==file || StringUtils.isEmpty(file.getFilePath()))
                                continue;
                            //String s = file.getFilePath().replaceFirst(replaceFromUrl, replaceUrl);
                            resourcesMap.put(file.getFilePath(), resources);
                            fileLst.add(file.getFilePath());
                            continue;
                        }
                        //提取视频
                        if(2==resources.getType()){
                            String content = resources.getContent();
                            if(StringUtils.isEmpty(content))
                                continue;
                            VideoInfo videoInfo = videoInfoService.selectByPrimaryKey(Integer.valueOf(content));
                            if(!StringUtils.isEmpty(videoInfo)
                                    && !"ACCA".equals(videoInfo.getBucketName())
                                    && !StringUtils.isEmpty(videoInfo.getUrl())){
                                String fileId = videoInfo.getFileId();
                                if(!StringUtils.isEmpty(fileId)) {
                                    if (!redisService.hasKey(fileId)) {
                                        String resUrl = getVideoUrl.replaceAll("\\{fileId\\}", fileId);
                                        log.info("获取录播视频url:" + resUrl);
                                        HttpResponse httpResponse = HttpReqUtil.getOpentReq(resUrl + "?orderType=ASC&delayMinute=" + videoOutTime, appkey, appsecret);
                                        Map<String, Object> responsemap = HttpReqUtil.parseHttpResponse(httpResponse);
                                        Map<String, Object> payload = (Map<String, Object>) responsemap.get("payload");
                                        String url = payload.get("url").toString();
                                        log.info("获取到的播放地址:" + url);
                                        videoLst.add(url);
                                        videoResourcesMap.put(url, resources);
                                    }else {
                                        videoLst.add(videoInfo.getUrl());
                                        videoResourcesMap.put(videoInfo.getUrl(), resources);
                                    }
                                }
                            }
                            continue;
                        }
                        //提取试卷文本
                        if(resources.getType()<=1){
                            String content = resources.getContent();
                            if(StringUtils.isEmpty(content))
                                continue;
                            List<String> voiceLst=new ArrayList<>();
                            Integer contentInt=0;
                            try{
                                contentInt = Integer.valueOf(content);
                            }catch (Exception e){
                                log.info("无效的试卷id"+contentInt);
                                return RetResponse.makeErrRsp("无效的试卷id"+contentInt);
                            }
                            TestPaperCheckModel checkInfo = testPaperService.getCheckInfo(contentInt, voiceLst);
                            //提取试卷中的音频
                            if(voiceLst.size()>0)
                                testPaperVoiceMap.put(resources.getResourcesId(),voiceLst);
                            if(null!=checkInfo)
                                resourcesTextMap.put(resources.getResourcesId(), JSON.toJSONString(checkInfo));
                            continue;
                        }
                        //提取文本
                        if(resources.getType()==7){
                            String content = resources.getContent();
                            if(!StringUtils.isEmpty(content))
                                resourcesTextMap.put(resources.getResourcesId(), JSON.toJSONString(content));
                            continue;
                        }
                        //直播课程审核默认通过
                        if(resources.getType()==3){
                            resources.setCheckStatus(2);
                            resources.setCheckTime(new Date());
                            resourcesService.updateByPrimaryKeySelective(resources);
                        }
                    }
                }
            }
        }
        List<AutomaticCheck> resultLst=new ArrayList<>();
        //图片审核
        if(imageLst.size()>0) {
            List<AutomaticCheck> automaticCheckList = contentSecurityImpl.ImageAsyncCheck(imageLst);
            if (null == automaticCheckList) {
                log.info("图片自动审核失败");
                return RetResponse.makeErrRsp("图片自动审核失败");
            }
            resultLst.addAll(automaticCheckList);
        }
        //文档审核
        if(fileLst.size()>0) {
            List<AutomaticCheck> automaticCheckList1 = contentSecurityImpl.FileAsyncCheck(fileLst);
            resultLst.addAll(automaticCheckList1);
        }
        //视频审核
        if(videoLst.size()>0) {
            List<AutomaticCheck> automaticCheckList2 = contentSecurityImpl.VideoAsyncCheck(videoLst);
            resultLst.addAll(automaticCheckList2);
        }
        //4.音频文件自动审核
        List<String> strings=null;
        for(Map.Entry<Integer,List<String>> entry:testPaperVoiceMap.entrySet()){
            Integer key = entry.getKey();
            List<String> value = entry.getValue();
            if(null!=value&& value.size()>0) {
                for(int i=0;i<value.size();i+=5) {
                    if(i+5>value.size()){
                        strings = value.subList(i, value.size());
                    }else {
                        strings = value.subList(i, i + 5);
                    }
                    List<AutomaticCheck> automaticCheckList3 = contentSecurityImpl.VoiceAsyncCheck(strings);
                    for (AutomaticCheck automaticCheck : automaticCheckList3) {
                        automaticCheck.setBak1(key.toString());
                    }
                    resultLst.addAll(automaticCheckList3);
                    //延时2秒
                    Thread.sleep(2000);
                }
            }
        }
        //5.文本文件自动审核
        //5.1目录审核
        if(!catalogTextMap.isEmpty()) {
            List<AutomaticCheck> automaticCheckList5 = contentSecurityImpl.TextCheck(catalogTextMap);
            for (AutomaticCheck automaticCheck : automaticCheckList5) {
                automaticCheck.setBak3("catalog");
            }
            resultLst.addAll(automaticCheckList5);
        }
        //5.2试卷及文本文档资源审核
        if(!resourcesTextMap.isEmpty()) {
            List<AutomaticCheck> automaticCheckList4 = contentSecurityImpl.TextCheck(resourcesTextMap);
            for (AutomaticCheck automaticCheck : automaticCheckList4) {
                automaticCheck.setBak3("resources");
            }
            resultLst.addAll(automaticCheckList4);
        }
        return SyncAutomaticCheck(resultLst,fileMap,resourcesMap,videoResourcesMap,courseId);
    }

    @Override
    @Transactional
    public RetResult SyncAutomaticCheck(List<AutomaticCheck> automaticCheckList,Map<String,File> fileMap,
                                        Map<String,Resources> resourcesMap,Map<String,Resources> videoResourcesMap,int courseId){
        Date date=new Date();
        for (AutomaticCheck automaticCheck : automaticCheckList) {
            if (0 == automaticCheck.getCheckType()) {
                //图片同步
                File file = fileMap.get(automaticCheck.getContext());
                if (null != file) {
                    file.setCheckId(automaticCheck.getAutomaticCheckId());
                    file.setCheckStatus(0);
                    fileService.update(file);
                }
            }else if (4 == automaticCheck.getCheckType()) {
                //文档同步
                Resources resources = resourcesMap.get(automaticCheck.getContext());
                resources.setCheckId(automaticCheck.getAutomaticCheckId());
                resources.setCheckStatus(0);
                resourcesService.updateByPrimaryKeySelective(resources);
            }else if (2 == automaticCheck.getCheckType()) {
                //视频同步
                Resources resources = videoResourcesMap.get(automaticCheck.getContext());
                resources.setCheckId(automaticCheck.getAutomaticCheckId());
                resources.setCheckStatus(0);
                resourcesService.updateByPrimaryKeySelective(resources);
            }else if(3 == automaticCheck.getCheckType()){
                //目录同步
                if(!StringUtils.isEmpty(automaticCheck.getBak3())){
                    if("catalog".equals(automaticCheck.getBak3())){
                        Catalog catalog = new Catalog();
                        catalog.setCatalogId(Integer.valueOf(automaticCheck.getBak2()));
                        catalog.setCheckId(automaticCheck.getAutomaticCheckId());
                        catalog.setCheckTime(date);
                        String antispam = automaticCheck.getAntispam();
                        if (AliCheckStatusEnum.NORMAL.getStatus().equals(antispam)) {
                            catalog.setCheckStatus(2);
                        } else {
                            String descByStatus = AliCheckStatusEnum.getDescByStatus(antispam);
                            catalog.setCheckStatus(1);
                        }
                        catalogService.updateByPrimaryKeySelective(catalog);
                    }else if("resources".equals(automaticCheck.getBak3())){
                            String bak2 = automaticCheck.getBak2();
                            if(null!=bak2) {
                                //需要递归查询状态来判断resources的状态
                                Integer integer = Integer.valueOf(bak2);
                                Resources resources = new Resources();
                                resources.setResourcesId(integer);
                                String antispam = automaticCheck.getAntispam();
                                if (!StringUtils.isEmpty(automaticCheck.getBak1())) {
                                    if (AliCheckStatusEnum.NORMAL.getStatus().equals(antispam))
                                        resources.setCheckStatus(getStatus(automaticCheck.getBak1(), automaticCheckList));
                                    else
                                        resources.setCheckStatus(1);
                                } else {
                                    if (AliCheckStatusEnum.NORMAL.getStatus().equals(antispam)) {
                                        resources.setCheckStatus(2);
                                    } else {
                                        String descByStatus = AliCheckStatusEnum.getDescByStatus(antispam);
                                        automaticCheck.setAntispamDetail(descByStatus);
                                        resources.setCheckStatus(1);
                                    }
                                }
                                resources.setCheckTime(date);
                                resources.setCheckId(automaticCheck.getAutomaticCheckId());
                                resourcesService.updateByPrimaryKeySelective(resources);
                            }
                    }
                    automaticCheck.setBak3(null);
                }
            }
            automaticCheckDao.insertSelective(automaticCheck);
        }
        Course course = new Course();
        course.setCourseId(courseId);
        course.setStatus(1);
        courseDao.updateByPrimaryKeySelective(course);
        return RetResponse.makeOKRsp();
    }

    int getStatus(String bak1,List<AutomaticCheck> automaticCheckList){
        for (AutomaticCheck automaticCheck : automaticCheckList) {
            if(automaticCheck.getAutomaticCheckId().equals(bak1)){
                String antispam = automaticCheck.getAntispam();
                if (!AliCheckStatusEnum.NORMAL.getStatus().equals(antispam)) {
                    return 1;
                }else {
                    return getStatus(automaticCheck.getBak1(),automaticCheckList);
                }
            }
        }
        return 2;
    }
    @Override
    @Transactional
    public boolean checkCallBack(String checksum, String content) {
        return contentSecurityImpl.checkCallBack(checksum,content);
    }

    @Override
    @Transactional(rollbackFor=ServiceException.class)
    public RetResult checkPassAll(int courseId,int userId) {
        //目录检查
        Map<String, Object> initMap = MapUtils.initMap("courseId", courseId);
        initMap.put("status",1);
        Date date = new Date();
        List<Catalog> catalogList = catalogService.selectList(initMap);
        for (Catalog catalog : catalogList) {
                Catalog catalog1 = new Catalog();
                catalog1.setCheckStatus(4);
                catalog1.setCheckUser(userId);
                catalog1.setCheckTime(date);
                catalog1.setCatalogId(catalog.getCatalogId());
                catalogService.updateByPrimaryKeySelective(catalog1);
        }
        //resources检查
        List<CourseDetail> objects = courseDetailService.selectList(initMap);
        List<Integer> resourcesLst=new ArrayList<>();
        for (CourseDetail object : objects) {
            Integer resourcesId = object.getResourcesId();
            if(StringUtils.isEmpty(resourcesId))
                continue;
            resourcesLst.add(resourcesId);
        }
        Map<String, Object> entityKeyValues = MapUtils.initMap("entityKeyValues",resourcesLst);
        List<Resources> resLst = resourcesService.selectList(entityKeyValues);
        Set<Integer> resourcesSet=new HashSet<>();
        for (Resources resources : resLst) {
                //获取机审的资源id
                if(resources.getCheckStatus()<=2)
                    resourcesSet.add(resources.getResourcesId());
                Resources resources1 = new Resources();
                resources1.setCheckStatus(4);
                resources1.setCheckUser(userId);
                resources1.setCheckTime(date);
                resources1.setResourcesId(resources.getResourcesId());
                resourcesService.updateByPrimaryKeySelective(resources1);
        }
        //同步试卷资源
        resourcesService.syncStudentResources(courseId,resourcesSet);
        //file表检查
        Condition condition=new Condition(File.class);
        condition.createCriteria().andEqualTo("commodityId",courseId+"");
        List<File> fileList = fileService.selectByCondition(condition);
        for (File file : fileList) {
                File file1 = new File();
                file1.setCheckStatus(4);
                file1.setFileId(file.getFileId());
                file1.setCheckTime(date);
                file1.setCheckUser(userId);
                fileService.update(file1);
        }
        //设置状态为人工审核通过
        Course course = new Course();
        course.setCourseId(courseId);
        course.setStatus(2);
        course.setExamineUser(userId);
        course.setExamineTime(date);
        courseDao.updateByPrimaryKeySelective(course);
        return RetResponse.makeOKRsp();
    }

    @Override
    public RetResult checkCommit(CourseCommitCheck courseCommitCheck, int userId) {
        //目录检查
        boolean isPass=true;
        int courseId=courseCommitCheck.getCourseId();
        Map<String, Object> initMap = MapUtils.initMap("courseId", courseId);
        initMap.put("status",1);
        Date date = new Date();
        List<Catalog> catalogList = catalogService.selectList(initMap);
        for (Catalog catalog : catalogList) {
            Catalog catalog1 = new Catalog();
            if(null==catalog.getCheckStatus()||0==catalog.getCheckStatus())
                throw new ServiceException("该课程有未通过审核的内容，请仔细核查。");
            else if(1==catalog.getCheckStatus()){
                isPass=false;
                catalog1.setCheckStatus(3);
            }
            else if(2==catalog.getCheckStatus()){
                catalog1.setCheckStatus(4);
            }
            catalog1.setCheckUser(userId);
            catalog1.setCheckTime(date);
            catalog1.setCatalogId(catalog.getCatalogId());
            catalogService.updateByPrimaryKeySelective(catalog1);
        }
        //resources检查
        List<CourseDetail> objects = courseDetailService.selectList(initMap);
        Set<Integer> resourcesLst=new HashSet<>();
        for (CourseDetail object : objects) {
            Integer resourcesId = object.getResourcesId();
            if(StringUtils.isEmpty(resourcesId))
                continue;
            resourcesLst.add(resourcesId);
        }
        Map<String, Object> entityKeyValues = MapUtils.initMap("entityKeyValues",resourcesLst);
        List<Resources> resLst = resourcesService.selectList(entityKeyValues);
        Set<Integer> passTestPaper=new HashSet<>();
        for (Resources resources : resLst) {
            Resources resources1 = new Resources();
            if(null==resources.getCheckStatus())
                throw new ServiceException("该课程有未通过审核的内容，请仔细核查。");
            else if(resources.getCheckStatus()==0&&resources.getType()==3){
                resources1.setCheckStatus(4);
            }
            else if(resources.getCheckStatus()==1){
                resources1.setCheckStatus(3);
                isPass=false;
            }
            else if(resources.getCheckStatus()==2){
                passTestPaper.add(resources.getResourcesId());
                resources1.setCheckStatus(4);
            }
            resources1.setCheckUser(userId);
            resources1.setCheckTime(date);
            resources1.setResourcesId(resources.getResourcesId());
            resourcesService.updateByPrimaryKeySelective(resources1);
        }
        //同步学生资源资源
        resourcesService.syncStudentResources(courseId,passTestPaper);
        //file表检查
        Condition condition=new Condition(File.class);
        condition.createCriteria().andEqualTo("commodityId",courseId+"");
        List<File> fileList = fileService.selectByCondition(condition);
        for (File file : fileList) {
            File file1 = new File();
            if(null==file.getCheckStatus() || file.getCheckStatus()==0)
                throw new ServiceException("该课程有未通过审核的内容，请仔细核查。");
            else if(file.getCheckStatus()==1){
                file1.setCheckStatus(3);
                isPass=false;
            }
            else if(file.getCheckStatus()==2){
                file1.setCheckStatus(4);
            }
            file1.setFileId(file.getFileId());
            file1.setCheckTime(date);
            file1.setCheckUser(userId);
            fileService.update(file1);
        }
        //设置状态为人工审核通过
        Course course = new Course();
        course.setCourseId(courseId);
        if(isPass) {
            course.setStatus(2);
        }
        else
            course.setStatus(3);
        course.setExamineUser(userId);
        course.setExamineTime(date);
        course.setCommitTime(date);
        course.setExamineContext(courseCommitCheck.getExamineContext());
        courseDao.updateByPrimaryKeySelective(course);
        return RetResponse.makeOKRsp();
    }

    /*private RetResult syncCheckStatus(List<Catalog> catalogList,List<Resources> resLst,List<File> fileList,int optType,String msg){
        switch (optType){
            case 1:
                for (Catalog catalog : catalogList) {
                    ca
                }
                break;
            case 2:
                break;
            default:
        }
    }*/
    @Override
    public RetResult checkPass(CheckPassModel checkPassModel) {
        Integer resourceType = checkPassModel.getResourceType();
        Integer resourceId = checkPassModel.getResourceId();
        if(resourceType==0){
            Catalog catalog = new Catalog();
            catalog.setCatalogId(resourceId);
            catalog.setCheckStatus(checkPassModel.getCheckStatus()==3?1:2);
            catalogService.updateByPrimaryKeySelective(catalog);
        }else if(resourceType==1){
            Resources resources = new Resources();
            resources.setResourcesId(resourceId);
            resources.setCheckStatus(checkPassModel.getCheckStatus()==3?1:2);
            resourcesService.updateByPrimaryKeySelective(resources);
        }
        return RetResponse.makeOKRsp();
    }

    @Override
    public RetResult selectList(CourseCheckQueryModel courseCheckQueryModel) throws Exception{
        List<Integer> userIdLst=new ArrayList<>();
        HashMap<Integer, String> integerStringHashMap = new HashMap<>();
        if(!StringUtils.isEmpty(courseCheckQueryModel.getUserName())) {
            UserSearchModel search = new UserSearchModel();
            search.setUserName(courseCheckQueryModel.getUserName());
            List<SsoUser> ssoUsers = ssoUserService.selectListAnd(search);
            if(null!=ssoUsers && ssoUsers.size()>0){
                for (SsoUser ssoUser : ssoUsers) {
                    userIdLst.add(ssoUser.getId());
                    integerStringHashMap.put(ssoUser.getId(),ssoUser.getUserName());
                }
            }
        }
        Map<String, Object> initMap = MapUtils.initMap();
        String categoryOne = courseCheckQueryModel.getCategoryOne();
        if(!StringUtils.isEmpty(categoryOne)){
            initMap.put("categoryOne",categoryOne);
        }
        Integer status = courseCheckQueryModel.getStatus();
        if(null!=status && status>=1 && status<=3 ){
            initMap.put("status",status);
        }else {
            initMap.put("minStatus",1);
        }
        String name = courseCheckQueryModel.getName();
        if(!StringUtils.isEmpty(name)){
            initMap.put("name",name);
        }
        if(userIdLst.size()>0){
            initMap.put("createUsers",userIdLst);
        }
        if(!StringUtils.isEmpty(courseCheckQueryModel.getPage()) && !StringUtils.isEmpty(courseCheckQueryModel.getLimit())){
            int page = courseCheckQueryModel.getPage();
            int pagesize=courseCheckQueryModel.getLimit();
            int __currentIndex__=(page-1)*pagesize;
            initMap.put("__currentIndex__",__currentIndex__);
            initMap.put("__pageSize__",pagesize);
        }
        long total = courseDao.selectCount(initMap);
        List<Course> objects = courseDao.selectList(initMap);
        List<Map<String,Object>> resList=new ArrayList<>();
        for (Course object : objects) {
            HashMap<String, Object> stringObjectHashMap = new HashMap<>();
            stringObjectHashMap.put("name",object.getName());
            if(object.getCreateUser()!=null) {
                SsoUser userByIdInter = ssoUserService.getUserByIdInter(object.getCreateUser());
                if(null!=userByIdInter) {
                    stringObjectHashMap.put("userName", userByIdInter.getUserName());
                }else {
                    stringObjectHashMap.put("userName", "");
                }
            }else {
                stringObjectHashMap.put("userName", "");
            }
            String s = courseCategoryInfoService.selectCcNameByCcId(object.getCategoryTwo());
            stringObjectHashMap.put("ccName",StringUtils.isEmpty(s)?"":s);
            stringObjectHashMap.put("status",object.getStatus());
            String s1 = DateUtils.date2String(object.getCommitTime(), DateUtils.TIME_FORMAT);
            stringObjectHashMap.put("commitTime",StringUtils.isEmpty(s1)?"":s1);
            stringObjectHashMap.put("courseId",object.getCourseId());
            resList.add(stringObjectHashMap);
        }
        return RetResponse.makeRsp(resList,total);
    }


}