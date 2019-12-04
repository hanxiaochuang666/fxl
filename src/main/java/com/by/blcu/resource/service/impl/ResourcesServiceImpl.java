package com.by.blcu.resource.service.impl;

import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.aop.CourseCheck;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.*;
import com.by.blcu.course.dao.ICatalogDao;
import com.by.blcu.course.dao.ICourseDetailDao;
import com.by.blcu.course.dto.Catalog;
import com.by.blcu.course.dto.Course;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.service.ICourseService;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.mall.model.CommodityInfo;
import com.by.blcu.mall.model.File;
import com.by.blcu.mall.service.CommodityInfoService;
import com.by.blcu.mall.service.FileService;
import com.by.blcu.mall.service.MallOrderInfoService;
import com.by.blcu.mall.service.RedisService;
import com.by.blcu.mall.vo.MallCommodityOrderVo;
import com.by.blcu.mall.vo.MallOrderInfoVo;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.service.SsoUserService;
import com.by.blcu.resource.dao.*;
import com.by.blcu.resource.dto.*;
import com.by.blcu.resource.model.FileViewModel;
import com.by.blcu.resource.model.LiveDetailInfo;
import com.by.blcu.resource.model.ResourceTypeEnum;
import com.by.blcu.resource.model.ResourcesViewModel;
import com.by.blcu.resource.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service("resourcesService")
@CheckToken
@Slf4j
public class ResourcesServiceImpl extends BaseServiceImpl implements IResourcesService {
    @Resource
    private IResourcesDao resourcesDao;

    @Resource
    private ICourseService courseService;

    @Resource
    private ICourseDetailDao courseDetailDao;

    @Resource
    private ICatalogDao catalogDao;

    @Resource
    private CommodityInfoService commodityInfoService;

    @Resource
    private FileService fileService;

    @Resource
    private FastDFSClientWrapper dfsClient;

    @Resource
    private ILiveTelecastDao liveTelecastDao;

    @Resource
    private ILiveService liveService;

    @Resource
    private ITestPaperService testPaperService;

    @Resource
    private ITestResultDao testResultDao;

    @Resource
    private ITestResultDetailDao testResultDetailDao;

    @Resource
    private IVideoInfoService videoInfoService;
    
    @Resource
    private SsoUserService ssoUserService;

    @Resource(name="learnActiveService")
    private ILearnActiveService learnActiveService;

    @Autowired
    private MallOrderInfoService orderInfoService;

    @Resource
    private IDiscussDao discussDao;

    @Resource
    private RedisService redisService;

    @Value("${open.videoOutTime}")
    private int videoOutTime;

    @Value("${open.getVideoUrl}")
    private String getVideoUrl;

    @Value("${open.appkey}")
    private String appkey;

    @Value("${open.appsecret}")
    private String appsecret;

    @Override
    protected IBaseDao getDao() {
        return this.resourcesDao;
    }

    @Override
    public boolean isUsedResources(Integer contentId, Integer type, Integer minType, Integer maxType) {
        if(StringUtils.isEmpty(contentId))
            throw new ServiceException("参数错误contentId");
        if(!StringUtils.isEmpty(type)&&(type<0 ||type>8))
            throw new ServiceException("参数错误type");
        if(!StringUtils.isEmpty(maxType) && (maxType>8||maxType<0)){
            throw new ServiceException("参数错误maxType");
        }
        if(!StringUtils.isEmpty(minType) && (minType>8||minType<0)){
            throw new ServiceException("参数错误minType");
        }
        if(minType!=null && maxType!=null && maxType.intValue()<minType.intValue())
            throw new ServiceException("参数错误minType & maxType");
        if((!StringUtils.isEmpty(type)&&(type>=4&&type<=8))
                || (!StringUtils.isEmpty(minType)&&minType>=4&&minType<=8)
                || (!StringUtils.isEmpty(maxType)&&maxType>=4&&maxType<=8)) {
            Map<String, Object> objectMap1 = MapUtils.initMap("resourcesId",contentId);
            long count=courseDetailDao.selectCount(objectMap1);
            if(count>0)
                return true;
        }
        Map<String, Object> objectMap = MapUtils.initMap("content", contentId + "");
        if(!StringUtils.isEmpty(type))
            objectMap.put("type",type);
        if(!StringUtils.isEmpty(minType))
            objectMap.put("minType",minType);
        if(!StringUtils.isEmpty(maxType))
            objectMap.put("maxType",maxType);
        List<Resources> objects = getDao().selectList(objectMap);
        if(objects.size()>0){
            Integer resourcesId = objects.get(0).getResourcesId();
            objectMap.clear();
            objectMap.put("resourcesId",resourcesId.intValue());
            long count=courseDetailDao.selectCount(objectMap);
            if(count>0)
                return true;
        }
        return false;
    }

    @Override
    public void syncResources(Integer contentId, Integer type, Integer minType, Integer maxType) {
        if(StringUtils.isEmpty(contentId))
            throw new ServiceException("参数错误contentId");
        if(!StringUtils.isEmpty(type)&&(type<0 ||type>8))
            throw new ServiceException("参数错误type");
        if(!StringUtils.isEmpty(maxType) && (maxType>8||maxType<0)){
            throw new ServiceException("参数错误maxType");
        }
        if(!StringUtils.isEmpty(minType) && (minType>8||minType<0)){
            throw new ServiceException("参数错误minType");
        }
        if(minType!=null && maxType!=null && maxType.intValue()<minType.intValue()) {
            throw new ServiceException("参数错误minType & maxType");
        }
        Map<String, Object> objectMap = MapUtils.initMap("content", contentId + "");
        if(!StringUtils.isEmpty(type))
            objectMap.put("type",type);
        if(!StringUtils.isEmpty(minType))
            objectMap.put("minType",minType);
        if(!StringUtils.isEmpty(maxType))
            objectMap.put("maxType",maxType);
        List<Resources> objects = getDao().selectList(objectMap);
        Date date = new Date();
        if(null!=objects && objects.size()>0){
            for (Resources object : objects) {
                Resources resource = new Resources();
                resource.setResourcesId(object.getResourcesId());
                resource.setUpdateTime(date);
                getDao().updateByPrimaryKeySelective(resource);
                objectMap.clear();
                objectMap.put("resourcesId",object.getResourcesId());
                List<CourseDetail> objects1 = courseDetailDao.selectList(objectMap);
                if(null!=objects1&&objects1.size()>0) {
                    for (CourseDetail courseDetail : objects1) {
                        Integer courseId = courseDetail.getCourseId();
                        Course course = new Course();
                        course.setCourseId(courseId);
                        course.setStatus(0);
                        courseService.updateByPrimaryKeySelective(course);
                    }
                }
            }
        }

    }

    @Override
    public boolean isUsedMall(Integer contentId, Integer type, Integer minType, Integer maxType) {
        if(StringUtils.isEmpty(contentId))
            throw new ServiceException("参数错误contentId");
        if(!StringUtils.isEmpty(type)&&(type<0 ||type>8))
            throw new ServiceException("参数错误type");
        if(!StringUtils.isEmpty(maxType) && (maxType>8||maxType<0)){
            throw new ServiceException("参数错误maxType");
        }
        if(!StringUtils.isEmpty(minType) && (minType>8||minType<0)){
            throw new ServiceException("参数错误minType");
        }
        if(minType!=null && maxType!=null && maxType.intValue()<minType.intValue())
            throw new ServiceException("参数错误minType & maxType");
        if((!StringUtils.isEmpty(type)&&(type>=4&&type<=8))
                || (!StringUtils.isEmpty(minType)&&minType>=4&&minType<=8)
                || (!StringUtils.isEmpty(maxType)&&maxType>=4&&maxType<=8)) {
            Map<String, Object> objectMap1 = MapUtils.initMap("resourcesId",contentId);
            long count=courseDetailDao.selectCount(objectMap1);
            if(count>0)
                return true;
        }
        Map<String, Object> objectMap = MapUtils.initMap("content", contentId + "");
        if(!StringUtils.isEmpty(type))
            objectMap.put("type",type);
        if(!StringUtils.isEmpty(minType))
            objectMap.put("minType",minType);
        if(!StringUtils.isEmpty(maxType))
            objectMap.put("maxType",maxType);
        List<Resources> objects = getDao().selectList(objectMap);
        if(objects.size()>0){
            Integer resourcesId = objects.get(0).getResourcesId();
            objectMap.clear();
            objectMap.put("resourcesId",resourcesId.intValue());
            List<CourseDetail> courseLst = courseDetailDao.selectList(objectMap);
            if(courseLst.size()<=0)
                return false;
            for (CourseDetail course : courseLst) {
                List<CommodityInfo> commodityInfos = commodityInfoService.selectByCourseId(course.getCourseId().toString());
                if(null!=commodityInfos && commodityInfos.size()>0) {
                    for (CommodityInfo cd : commodityInfos) {
                        if(1 == cd.getCommodityStatus()) //上架了
                            return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    @Transactional
    @CourseCheck
    public Integer saveRichText(FileViewModel fileViewModel, HttpServletRequest request, CourseCheckModel courseCheckModel) throws Exception {
        Integer userId = Integer.valueOf(request.getAttribute("userId").toString());
        String orgCode = request.getAttribute("orgCode").toString();
        Resources resource = new Resources();
        String catalogName = "";

        //1.验证目录有效性
        if(!"0".equals(fileViewModel.getCatalogId())){
            Catalog catalog = catalogDao.selectByPrimaryKey(Integer.valueOf(fileViewModel.getCatalogId()));
            if(catalog != null){
                catalogName = catalog.getName();
                resource.setTitle(catalogName);
            }else{
                throw new ServiceException("该目录不存在，目录ID：" + catalog.getCatalogId());
            }
        }

        if(StringUtils.isBlank(fileViewModel.getResourcesId())){
            //2.当前目录是否已关联资源 存在则更新
            CourseDetail courseDetail = null;
            Map<String, Object> detailParam = MapUtils.initMap("catalogId", fileViewModel.getCatalogId());
            List<CourseDetail> details = courseDetailDao.selectList(detailParam);
            if(details != null && details.size() > 0){
                courseDetail = details.get(0);
            }

            //3.资源不存在，创建资源
            createResource(userId, resource, fileViewModel, orgCode);
            resourcesDao.insertSelective(resource);

            //4.创建课程详情 关联目录
            if(courseDetail != null){
                removeByResourceType(courseDetail.getResourcesId());
                createDetail(userId, resource, courseDetail, fileViewModel);
                courseDetailDao.updateByPrimaryKeySelective(courseDetail);
            }else{
                CourseDetail detail = new CourseDetail();
                createDetail(userId, resource, detail, fileViewModel);
                courseDetailDao.insert(detail);
            }

        }else{
            //5.当前目录是否已关联资源
            Map<String, Object> detailParam = MapUtils.initMap("catalogId", fileViewModel.getCatalogId());
            List<CourseDetail> details = courseDetailDao.selectList(detailParam);
            if(details != null && details.size() > 0){
                CourseDetail courseDetail = details.get(0);
                if(!fileViewModel.getResourcesId().equals(String.valueOf(courseDetail.getResourcesId()))){
                    //移除之前挂载资源
                    removeByResourceType(courseDetail.getResourcesId());
                    //创建资源
                    createResource(userId, resource, fileViewModel, orgCode);
                    resourcesDao.insertSelective(resource);
                }else{
                    //6.资源存在，更新资源
                    Map param = MapUtils.initMap("resourcesId",Integer.valueOf(fileViewModel.getResourcesId()));
                    List<Resources> resourcesList = selectList(param);
                    if(resourcesList != null && resourcesList.size() > 0) {
                        resource = resourcesList.get(0);
                        if(resource.getType().intValue() != ResourceTypeEnum.TEXT.getTypeCode().intValue()){
                            //类型不一致则创建
                            createResource(userId, resource, fileViewModel, orgCode);
                            resourcesDao.insertSelective(resource);
                        }else {
                            //类型一致则更新
                            resource.setContent(fileViewModel.getRichText());
                            resource.setUpdateUser(userId);
                            resource.setUpdateTime(new Date());
                            resource.setCheckStatus(0);
                            resourcesDao.updateByPrimaryKey(resource);
                        }
                    }
                }

                //更新关联 course_detail 表信息
                courseDetail.setResourcesId(Integer.valueOf(resource.getResourcesId()));
                courseDetailDao.updateByPrimaryKey(courseDetail);
            }else{
                //目录未关联资源
                createResource(userId, resource, fileViewModel, orgCode);
                resourcesDao.insertSelective(resource);

                CourseDetail courseDetailTemp = new CourseDetail();
                createDetail(userId, resource, courseDetailTemp, fileViewModel);
                courseDetailDao.insert(courseDetailTemp);
            }

        }

        //7.去除无效 File 并更新 File 关联 resourcesId
        Set<String> images = CommonUtils.getImgStr(fileViewModel.getRichText());//获取富文本中img
        if(images.size() != 0){
            //8.富文本中包含图片(bak3->catalogId)
            List<File> fileList = fileService.selectListBy("bak3", fileViewModel.getCatalogId());
            if(fileList != null && fileList.size() > 0){
                for(File file : fileList){
                    //9.移除不存在于富文本的文件 更新资源ID至bak2
                    if(images.contains(file.getFilePath())){
                        file.setBak2(String.valueOf(resource.getResourcesId()));
                        fileService.update(file);
                    }else{
                        //移除多余 File
                        dfsClient.deleteFile(file.getFilePath());
                        fileService.deleteById(file.getFileId());
                    }
                }

            }
        }
        return resource.getResourcesId();
    }

    private void createResource(Integer userId, Resources resources, FileViewModel fileViewModel, String orgCode){
        //创建关联资源
        resources.setResourcesId(null);
        resources.setType(ResourceTypeEnum.TEXT.getTypeCode());//文本信息
        resources.setOrgCode(orgCode);
        resources.setContent(fileViewModel.getRichText());
        resources.setCreateUser(userId);
        resources.setCreayeTime(new Date());
        resources.setCheckStatus(0);//初始化审核状态
    }

    private void createDetail(Integer userId, Resources resources, CourseDetail courseDetail, FileViewModel fileViewModel){
        //创建课程详情
        courseDetail.setCourseId(Integer.valueOf(fileViewModel.getCourseId()));
        courseDetail.setCatalogId(Integer.valueOf(fileViewModel.getCatalogId()));
        courseDetail.setResourcesId(resources.getResourcesId());
        courseDetail.setModelType(1);//默认目录
        courseDetail.setCreateTime(new Date());
        courseDetail.setCreateUser(userId);
    }

    @Override
    @Transactional
    public boolean removeByResourceType(Integer resourcesId) throws Exception {
        //0.获取资源
        Resources resource = resourcesDao.selectByPrimaryKey(resourcesId);
        if(resource == null)
            return true;

        //1.富文本资源/目录下文档
        if(resource.getType() == ResourceTypeEnum.TEXT.getTypeCode() || resource.getType() == ResourceTypeEnum.DOC.getTypeCode()){
            //1.1 删除 Resources 表数据
            Integer deleteState = resourcesDao.deleteByPrimaryKey(resourcesId);
            //1.2 根据 resourcesId 获取文件列表
            List<File> fileList = fileService.selectListBy("bak2", String.valueOf(resource.getResourcesId()));
            if(fileList != null && fileList.size() > 0){
                for(File file : fileList){
                    //1.3循环删除
                    try {
                        dfsClient.deleteFile(file.getFilePath());
                        fileService.deleteById(file.getFileId());
                    } catch (Exception e) {
                        log.error("删除文档资源异常！FileId："+file.getFileId());
                    }
                }
            }
            return true;

        }else if(resource.getType() == ResourceTypeEnum.LIVE.getTypeCode()){
            //2.直播资源
            // 2.1 调用奥鹏接口关闭直播间
            List<LiveTelecast> list = liveTelecastDao.selectList(MapUtils.initMap("roomId",resource.getContent()));
            if(!CommonUtils.listIsEmptyOrNull(list)){
                LiveTelecast liveTelecast = list.get(0);
                if(DateUtils.dateCompare(DateUtils.now(),liveTelecast.getEndTime()) < 1){
                    liveService.closeRoom(resource.getContent());
                }
                // 2.2 删除 Resources 表数据
                Integer deleteState = resourcesDao.deleteByPrimaryKey(resourcesId);
                // 2.3 删除 liveTelecast 表数据
                liveTelecastDao.deleteByPrimaryKey(liveTelecast.getLiveTelecasId());
            }

            return true;
        }else if(resource.getType() == ResourceTypeEnum.DISCUSS.getTypeCode()){
            Map<String, Object> map = MapUtils.initMap("resourceId", resourcesId);
            // 删除所有回复
            discussDao.deleteByParams(map);
            // 删除讨论标题
            resourcesDao.deleteByPrimaryKey(resourcesId);
            return true;
        }


        return false;
    }

    @Override
    public boolean getResourcesByCatalogId(ResourcesViewModel resourcesViewModel, int userId) throws Exception {
        //1.判断目录有效性
        CourseDetail courseDetail = null;
        Map<String, Object> detailParam = MapUtils.initMap("catalogId", resourcesViewModel.getCatalogId());
        List<CourseDetail> details = courseDetailDao.selectList(detailParam);
        if (details != null && details.size() > 0) {
            courseDetail = details.get(0);
            if(userId != courseDetail.getCreateUser()){
                log.info("访问目录下资源被拒绝，详情ID：" + courseDetail.getCourseDetailId() +
                        "，请求用户ID：" + userId + "创建该资源用户ID："+courseDetail.getCreateUser());
                //throw new ServiceException("当前资源非本用户创建，不可获取！");
            }
        } else {
            throw new ServiceException("该目录下不存在资源，目录ID：" + resourcesViewModel.getCatalogId());
        }

        //2.获取关联资源信息
        Map param = MapUtils.initMap("resourcesId", courseDetail.getResourcesId());
        List<Resources> resourcesList = selectList(param);
        if (resourcesList != null && resourcesList.size() > 0) {
            Resources resources = resourcesList.get(0);
            resourcesViewModel.setResourcesId(String.valueOf(resources.getResourcesId()));
            resourcesViewModel.setType(String.valueOf(resources.getType()));
            resourcesViewModel.setContent(resources.getContent());

            if(resources.getType().intValue() == ResourceTypeEnum.TEST.getTypeCode().intValue() || resources.getType().intValue() == ResourceTypeEnum.TASK.getTypeCode().intValue()){
                //1.作业测试回显
                TestPaper testPaper = testPaperService.selectByPrimaryKey(Integer.valueOf(resources.getContent()));
                if(testPaper != null){
                    // 根据作业id查询作业状态
                    List<TestResult> testResults = testResultDao.selectTestList(MapUtils.initMap("testPaperId",testPaper.getTestPaperId()));
                    if(!CommonUtils.listIsEmptyOrNull(testResults)){
                        testPaper.setStatus(testResults.get(0).getStatus());
                    }else{
                        testPaper.setStatus(0);
                    }
                    resourcesViewModel.setData(testPaper);
                }
            }else if(resources.getType().intValue() == ResourceTypeEnum.VIDEO.getTypeCode().intValue()){
                //2.在线录播视频学习
                VideoInfo videoInfo = videoInfoService.selectByPrimaryKey(Integer.valueOf(resources.getContent()));
                if(!"ACCA".equals(videoInfo.getBucketName())) {
                    String fileId = videoInfo.getFileId();
                    if (!redisService.hasKey(fileId)) {
                        String resUrl = getVideoUrl.replaceAll("\\{fileId\\}", fileId);
                        log.info("获取录播视频url:" + resUrl);
                        HttpResponse httpResponse = HttpReqUtil.getOpentReq(resUrl + "?orderType=ASC&delayMinute=" + videoOutTime, appkey, appsecret);
                        Map<String, Object> responsemap = HttpReqUtil.parseHttpResponse(httpResponse);
                        Map<String, Object> payload = (Map<String, Object>) responsemap.get("payload");
                        String url = payload.get("url").toString();
                        log.info("获取到的播放地址:" + url);
                        videoInfo.setUrl(url);
                        redisService.setWithExpire(fileId, url, videoOutTime * 60);
                        videoInfoService.updateByPrimaryKeySelective(videoInfo);
                    } else {
                        String url = redisService.get(fileId);
                        videoInfo.setUrl(url);
                    }
                }
                resourcesViewModel.setData(videoInfo);
            }else if(resources.getType().intValue() == ResourceTypeEnum.LIVE.getTypeCode().intValue()){
                //3.直播
                Map liveParam = MapUtils.initMap("romeId", resources.getContent());
                List<LiveTelecast> list = liveTelecastDao.selectList(liveParam);
                if(list != null && list.size() > 0){
                    resourcesViewModel.setData(list.get(0));
                }
            }else if(resources.getType().intValue() == ResourceTypeEnum.DOC.getTypeCode().intValue()){
                //4.文档
                File file = fileService.selectByFileId(resources.getContent());
                resourcesViewModel.setData(file);
            }else if(resources.getType().intValue() == ResourceTypeEnum.DISCUSS.getTypeCode().intValue()){
                //5.讨论
                resourcesViewModel.setData(resources);
            }
            return true;
        }else{
            throw new ServiceException("该目录下关联资源不存在，资源ID：" + courseDetail.getResourcesId());
        }

    }

    @Override
    @Transactional
    public boolean delResourcesByCatalogId(ResourcesViewModel resourcesViewModel, int userId) throws Exception {
        boolean flag = false;
        //1.获取目录下详情
        Map<String, Object> detailParam = MapUtils.initMap("catalogId", resourcesViewModel.getCatalogId());
        List<CourseDetail> details = courseDetailDao.selectList(detailParam);
        if (details == null || details.size() < 1) {
            throw new ServiceException("该目录下不存在资源，目录ID：" + resourcesViewModel.getCatalogId());
        }

        //2.移除课程资源详情
        for(CourseDetail courseDetail : details){
            if(userId != courseDetail.getCreateUser()){
                log.info("删除目录下资源被拒绝，详情ID：" + courseDetail.getCourseDetailId() +
                        "，请求用户ID：" + userId + "创建该资源用户ID："+courseDetail.getCreateUser());
                throw new ServiceException("当前资源非本用户创建，不可删除！");
            }
            flag = removeByResourceType(courseDetail.getResourcesId());
            Integer state = courseDetailDao.deleteByPrimaryKey(courseDetail.getCourseDetailId());
            syncDeleteStudentResource(courseDetail.getCourseId(),courseDetail.getCourseDetailId() );
        }

        return flag;
    }

    @Override
    public void syncStudentResources(Integer courseId, Set<Integer> resourcesSet) {
        //1.根据课程id查询购买该课程的用户信息集合，返回Set<String>,通过调用李程的接口获取
        Set<String> userNameSet = orderInfoService.selectUserListByCourseId(courseId.toString());
        if(null == userNameSet || userNameSet.isEmpty())
            return;
        //2.根据userName查询userId的集合
        Set<Integer> studentSet=new HashSet<>();
        Date date = new Date();
        Map<String,Integer> nameIdMap=new HashMap<>();
        for (String s : userNameSet) {
            SsoUser userByUserNameInter = ssoUserService.getUserByUserNameInter(s);
            if(!StringUtils.isEmpty(userByUserNameInter)&&userByUserNameInter.getId()!=null) {
                studentSet.add(userByUserNameInter.getId());
                nameIdMap.put(s,userByUserNameInter.getId());
            }
        }
        //3.查询试卷id集合
        Set<Integer> testPaperSet=new HashSet<>();
        Map<String, Object> initMap = MapUtils.initMap("courseId", courseId);
        Set<CourseDetail> courseDetailSet=new HashSet<>();
        for (Integer resoucesId : resourcesSet) {
            Resources resources = resourcesDao.selectByPrimaryKey(resoucesId);
            //提取试卷
            if(null!=resources && resources.getType().intValue()<=1){
                String content = resources.getContent();
                if(!StringUtils.isEmpty(content)){
                    Integer integer = Integer.valueOf(content);
                    TestPaper testPaper = testPaperService.selectByPrimaryKey(integer);
                    if(null!=testPaper)
                        testPaperSet.add(testPaper.getTestPaperId());
                }
            }
            //提取课程详情记录
            initMap.put("resourcesId",resources.getResourcesId());
            List<CourseDetail> objects = courseDetailDao.selectList(initMap);
            if(null!=objects&&objects.size()>0){
                for (CourseDetail object : objects) {
                    if(object.getCatalogId()!=0)
                        courseDetailSet.add(object);
                }
            }
        }
        initMap.clear();
        initMap.put("courseId", courseId);
        //4.分发新的试卷
        for (Integer integer : studentSet) {
            Map<String, Object> map = MapUtils.initMap("studentId", integer);
            initMap.put("studentId", integer);
            for (Integer testPaper : testPaperSet) {
                map.put("testPaperId",testPaper);
                List<TestResult> list = testResultDao.selectList(map);
                LearnActive learnActive = new LearnActive();
                if(null!=list && list.size()>0){
                    TestResult testResult = list.get(0);
                    if(testResult.getStatus()>=2&&testResult.getStatus()<=3){
                        TestResult testResult1 = new TestResult();
                        testResult1.setStatus(1);
                        testResult1.setTestResultId(testResult.getTestResultId());
                        testResult1.setEndTime(null);
                        testResult1.setUseTime(null);
                        testResult1.setObjectiveScore(0);
                        testResult1.setTotalScore(0);
                        testResultDao.updateByPrimaryKeySelective(testResult1);
                    }
                }else {
                    TestResult testResult = new TestResult();
                    testResult.setTestPaperId(testPaper);
                    testResult.setStudentId(integer);
                    testResult.setStatus(0);
                    testResultDao.insertSelective(testResult);
                }
            }
        }
        //5.去除学生无关的试卷信息
        for (String s : userNameSet) {
            List<MallOrderInfoVo> orderInfoVos = orderInfoService.selectMallOrderInfoVoByUserName(s);
            List<MallCommodityOrderVo> mallCommodityOrderVos = new ArrayList<>();
            orderInfoVos.forEach(o->mallCommodityOrderVos.addAll(o.getMallCommodityOrderVoList()));
            Set<Integer> courseIdSet=new HashSet<>();
            for (MallCommodityOrderVo mallCommodityOrderVo : mallCommodityOrderVos) {
                String courseId1 = mallCommodityOrderVo.getCommodityInfoFileVo().getCourseId();
                if(!StringUtils.isEmpty(courseId1)&& Integer.valueOf(courseId1).intValue()>0){
                    courseIdSet.add(Integer.valueOf(courseId1));
                }
            }
            Set<Integer> testPaperIdsByCourseIds = getTestPaperIdsByCourseIds(courseIdSet);
            if(null!=testPaperIdsByCourseIds && testPaperIdsByCourseIds.size()>0) {
                Map<String, Object> notTestPaperIds = MapUtils.initMap("notTestPaperIds", testPaperIdsByCourseIds);
                notTestPaperIds.put("studentId", nameIdMap.get(s));
                List<TestResult> objects1 = testResultDao.selectList(notTestPaperIds);
                if (null != objects1 && objects1.size() > 0) {
                    Integer testResultId = objects1.get(0).getTestResultId();
                    testResultDetailDao.deleteByParams(MapUtils.initMap("testResultId", testResultId));
                    testResultDao.deleteByPrimaryKey(testResultId);
                }
            }
        }
        //6.新增资源的学习行为同步
        initMap.clear();
        initMap.put("courseId", courseId);
        for (CourseDetail courseDetail : courseDetailSet) {
            initMap.put("courseDetailId",courseDetail.getCourseDetailId());
            List<LearnActive> objectList = learnActiveService.selectList(initMap);
            if(null==objectList || objectList.size()==0){
                for (Integer integer : studentSet) {
                    LearnActive learnActive = new LearnActive();
                    learnActive.setCourseDetailId(courseDetail.getCourseDetailId());
                    learnActive.setStudentId(integer);
                    learnActive.setCourseId(courseId);
                    learnActive.setLearnFlag(0);
                    learnActiveService.insertNoExists(learnActive);
                }
            }

        }
    }

    @Override
    public void resetResourcesCheckStatus(Integer res) {
        Resources resource = getDao().selectByPrimaryKey(res);
        Resources resources = new Resources();
        resources.setResourcesId(resource.getResourcesId());
        resources.setCheckStatus(0);
        List<CourseDetail> courseDetailList = courseDetailDao.selectList(MapUtils.initMap("resourcesId", resource.getResourcesId()));
        for (CourseDetail courseDetail : courseDetailList) {
            Integer courseId = courseDetail.getCourseId();
            Course course = new Course();
            course.setCourseId(courseId);
            course.setStatus(0);
            courseService.updateByPrimaryKeySelective(course);
        }
    }


    /**
     * 根据课程id查询其所有的试卷id
     * @param courseIdSet
     * @return
     */
    Set<Integer> getTestPaperIdsByCourseIds(Set<Integer> courseIdSet){
        Map<String, Object> minTypes = MapUtils.initMap("maxType",1);
        Set<Integer> testPaperSet=new HashSet<>();
        for (Integer integer : courseIdSet) {
            List<CourseDetail> courseDetails = courseDetailDao.selectList(MapUtils.initMap("courseId", integer));
            if(null!=courseDetails && courseDetails.size()>0){
                Set<Integer> resourcesSet=new HashSet<>();
                for (CourseDetail courseDetail : courseDetails) {
                    Integer resourcesId = courseDetail.getResourcesId();
                    if(null!=resourcesId && resourcesId.intValue()>0)
                        resourcesSet.add(resourcesId);
                }
                if(resourcesSet.size()>0) {
                    minTypes.put("entityKeyValues", resourcesSet);
                    List<Resources> objects = resourcesDao.selectList(minTypes);
                    if (null != objects && objects.size() > 0) {
                        List<Integer> testPaperIds = new ArrayList<>();
                        for (Resources object : objects) {
                            String content = object.getContent();
                            if (!StringUtils.isEmpty(content)) {
                                testPaperIds.add(Integer.valueOf(content));
                            }
                        }
                        if(testPaperIds.size()>0) {
                            List<TestPaper> testPapers = testPaperService.selectList(MapUtils.initMap("entityKeyValues", testPaperIds));
                            for (TestPaper testPaper : testPapers) {
                                testPaperSet.add(testPaper.getTestPaperId());
                            }
                        }
                    }
                }
            }
        }
        return testPaperSet;
    }

    /**
     * 删除资源时同步删除学生学习进度
     */
    public void syncDeleteStudentResource(Integer courseId ,Integer courseDetailId) throws ServiceException{
        //1.根据课程id查询购买该课程的用户信息集合，返回Set<String>,通过调用李程的接口获取
        Set<String> userNameSet = orderInfoService.selectUserListByCourseId(courseId.toString());
        if(null== userNameSet || userNameSet.isEmpty())
            return;
        //2.根据userName查询userId的集合
        Set<Integer> studentSet=new HashSet<>();
        for (String s : userNameSet) {
            SsoUser userByUserNameInter = ssoUserService.getUserByUserNameInter(s);
            if(!StringUtils.isEmpty(userByUserNameInter)&&userByUserNameInter.getId()!=null) {
                studentSet.add(userByUserNameInter.getId());
            }
        }

        Map<String,Object> pMap = new HashMap<>();
        pMap.put("courseDetailId", courseDetailId);
        for (Integer studentId : studentSet) {
            pMap.put("studentId", studentId);
            List<LearnActive> list = learnActiveService.selectList(pMap);
            if (!CommonUtils.listIsEmptyOrNull(list)){
                list.forEach(l->learnActiveService.deleteByPrimaryKey(l.getLearnActiveId()));
            }
        }

    }

}