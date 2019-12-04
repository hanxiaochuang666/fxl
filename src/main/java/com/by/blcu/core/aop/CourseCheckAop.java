package com.by.blcu.core.aop;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.dto.Catalog;
import com.by.blcu.course.dto.Course;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.model.CatalogModel;
import com.by.blcu.course.model.KnowledgePointNode;
import com.by.blcu.course.model.TaskViewModel;
import com.by.blcu.course.service.ICatalogService;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.course.service.ICourseService;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.course.service.courseCheck.ICourseChangeCheck;
import com.by.blcu.mall.model.CommodityInfo;
import com.by.blcu.mall.model.File;
import com.by.blcu.mall.service.CommodityInfoService;
import com.by.blcu.mall.service.MallOrderInfoService;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.service.SsoUserService;
import com.by.blcu.resource.dto.*;
import com.by.blcu.resource.model.FileViewModel;
import com.by.blcu.resource.model.QuestionModel;
import com.by.blcu.resource.model.VideoInfoVO;
import com.by.blcu.resource.service.ILearnActiveService;
import com.by.blcu.resource.service.IQuestionService;
import com.by.blcu.resource.service.IResourcesService;
import com.by.blcu.resource.service.ITestPaperQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.*;

@Component
@Aspect
@Slf4j
public class CourseCheckAop {

    public static boolean isCheck;

    @Value("${course.check}")
    public  void setIsCheck(boolean isCheck) {
        CourseCheckAop.isCheck = isCheck;
    }
    @Resource(name = "courseChangeCheck")
    private ICourseChangeCheck courseChangeCheck;

    @Resource
    private CommodityInfoService commodityInfoService;

    @Autowired
    private ICatalogService catalogService;

    @Resource
    private ICourseService courseService;

    @Resource
    private IResourcesService resourcesService;

    @Resource
    private ICourseDetailService courseDetailService;

    @Autowired
    private MallOrderInfoService orderInfoService;

    @Resource
    private SsoUserService ssoUserService;

    @Autowired
    private IQuestionService questionService;

    @Resource(name="learnActiveService")
    private ILearnActiveService learnActiveService;

    @Resource(name="testPaperQuestionService")
    private ITestPaperQuestionService testPaperQuestionService;

    @Pointcut("@annotation(com.by.blcu.core.aop.CourseCheck)")
    private void activeMethod(){

    }

    @Before("activeMethod()")
    public void before(JoinPoint joinPoint)throws Exception {
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();
        CourseCheck annotation = method.getAnnotation(CourseCheck.class);
        String methodName = method.getName();
        //boolean check = annotation.isCheck();
        Object[] args = joinPoint.getArgs();
        if(args.length<2){
            log.info("参数未满足最小长度");
            return;
        }
        List<Object> objects = Arrays.asList(args);
        CourseCheckModel courseCheckModel=null;
        for (Object arg : objects) {
            if(arg instanceof CourseCheckModel){
                courseCheckModel=new CourseCheckModel();
                break;
            }
        }
        if(null==courseCheckModel){
            log.info("未添加课程资源修改的功能参数");
            return;
        }
        courseCheckModel.setIsUpper(0);
        if(!CourseCheckAop.isCheck) {
            putAragsValue(args,courseCheckModel);
            return;
        }
        switch (methodName){
            case "addKnowledgePoints":
                CatalogModel temp=(CatalogModel)objects.get(0);;
                if (null != temp) {
                    int checkFlagByCourseId = isCheckFlagByCourseId(temp.getCourseId());
                    courseCheckModel.setCourseId(temp.getCourseId());
                    if (-1 == checkFlagByCourseId)
                        throw new ServiceException("课程信息不合法，请重新修改");
                    else if (0 == checkFlagByCourseId) {
                        //课程上架的编辑，需要实时审核
                        courseCheckModel.setIsUpper(1);
                        courseChangeCheck.addKnowledgePointsCheck(temp, courseCheckModel);
                    }else if(2==checkFlagByCourseId){
                        //回退审核状态
                        Course course = new Course();
                        course.setCourseId(temp.getCourseId());
                        course.setStatus(0);
                        courseService.updateByPrimaryKeySelective(course);
                    }else if(1==checkFlagByCourseId){
                        courseCheckModel.setSyncFlag(1);
                    }
                }
                break;
            case "editKnowledgePoints":
                int pointDetailId=(int)objects.get(0);
                String name=(String) objects.get(1);
                Catalog catalog = catalogService.selectByPrimaryKey(pointDetailId);
                if(null==catalog)
                    throw new ServiceException("课程目录不存在");
                int checkFlagByCourseId = isCheckFlagByCourseId(catalog.getCourseId());
                courseCheckModel.setCourseId(catalog.getCourseId());
                if (-1 == checkFlagByCourseId)
                    throw new ServiceException("课程信息不合法，请重新修改");
                else if (0 == checkFlagByCourseId) {
                    courseCheckModel.setIsUpper(1);
                    courseChangeCheck.editKnowledgePointsCheck(pointDetailId,name,courseCheckModel);
                }else if(2==checkFlagByCourseId){
                    //回退审核状态
                    catalogService.resetCatalogCheckStatus(pointDetailId);
                }else if(1==checkFlagByCourseId){
                    courseCheckModel.setSyncFlag(1);
                }
                break;
            case "addTask":
                TaskViewModel taskViewModel=(TaskViewModel)objects.get(0);
                int checkFlagByCourseId1 = isCheckFlagByCourseId(taskViewModel.getCourseId());
                courseCheckModel.setCourseId(taskViewModel.getCourseId());
                if(-1==checkFlagByCourseId1)
                    throw new ServiceException("课程信息不合法，请重新修改");
                else if (0 == checkFlagByCourseId1) {
                    courseCheckModel.setIsUpper(1);
                    //试卷中，只做文本的相关审核
                    if(null!=taskViewModel.getResourcesId()){
                        //修改试卷名称,可以只审核名称
                        courseChangeCheck.optTestPaperCheck(taskViewModel.getTaskName(),null,courseCheckModel);
                    }else {
                        List<Integer> testPaperLst=new ArrayList<>();
                        if (taskViewModel.getTestPaperId().contains(",")) {
                            String[] split = taskViewModel.getTestPaperId().split(",");
                            for (String s : split) {
                                testPaperLst.add(Integer.valueOf(s));
                            }
                        } else {
                            testPaperLst.add(Integer.valueOf(taskViewModel.getTestPaperId()));
                        }
                        courseCheckModel.setChangeType(1);
                        courseCheckModel.setCourseId(taskViewModel.getCourseId());
                        courseCheckModel.setContentType(1);
                        courseChangeCheck.optTestPaperCheck(null,testPaperLst,courseCheckModel);
                    }
                }else if(2==checkFlagByCourseId1){
                    //回退审核状态
                    if(null!=taskViewModel.getResourcesId())
                        resourcesService.resetResourcesCheckStatus(taskViewModel.getResourcesId());
                    else {
                        List<String> testPapers = new ArrayList<String>();
                        if (taskViewModel.getTestPaperId().contains(",")) {
                            testPapers = Arrays.asList(taskViewModel.getTestPaperId().split(","));
                        } else {
                            testPapers.add(taskViewModel.getTestPaperId());
                        }
                        Map<String, Object> maxType = MapUtils.initMap("maxType", 1);
                        for (String testPaperId : testPapers) {
                            maxType.put("content", testPaperId);
                            List<Resources> resources = resourcesService.selectList(maxType);
                            if(null!=resources && resources.size()>0){
                                resourcesService.resetResourcesCheckStatus(resources.get(0).getResourcesId());
                            }
                        }
                    }
                    Integer courseId = taskViewModel.getCourseId();
                    if(null!=courseId && courseId.intValue()>0){
                        Course course = new Course();
                        course.setCourseId(courseId);
                        course.setStatus(0);
                        courseService.updateByPrimaryKeySelective(course);
                    }
                }else if(1==checkFlagByCourseId1){
                    courseCheckModel.setSyncFlag(1);
                }
                break;
            case "addVideoInfo":
                VideoInfoVO videoInfoVO=(VideoInfoVO)objects.get(0);
                int checkFlagByCourseId2 = isCheckFlagByCourseId(videoInfoVO.getCourseId());
                courseCheckModel.setCourseId(videoInfoVO.getCourseId());
                if(-1==checkFlagByCourseId2)
                    throw new ServiceException("课程信息不合法，请重新修改");
                else if(0==checkFlagByCourseId2){
                    //上架处理
                    courseCheckModel.setIsUpper(1);
                    courseChangeCheck.videoCheck(videoInfoVO,courseCheckModel);
                }else if(2==checkFlagByCourseId2){
                    //已审核状态回退（）
                    Integer videoInfoId = videoInfoVO.getVideoInfoId();
                    Map<String, Object> initMap = MapUtils.initMap("type", 2);
                    initMap.put("content", String.valueOf(videoInfoId));
                    List<Resources> resourcesLst = resourcesService.selectList(initMap);
                    Course course = courseService.selectByPrimaryKey(videoInfoVO.getCourseId());
                    if(null!=resourcesLst&& resourcesLst.size()>0){
                        Resources resources = resourcesLst.get(0);
                        Course course1 = new Course();
                        if(resources.getCheckStatus()==0){
                            if(null!=course){
                                course1.setCourseId(course.getCourseId());
                                course1.setStatus(0);
                            }
                        }else if(1==resources.getCheckStatus() || 3==resources.getCheckStatus()){
                            if(null!=course){
                                course1.setCourseId(course.getCourseId());
                                course1.setStatus(3);
                            }
                        }
                        courseService.updateByPrimaryKeySelective(course1);
                    }
                }else if(1==checkFlagByCourseId2){
                    courseCheckModel.setSyncFlag(1);
                }
                break;
            case "saveRichText":
                FileViewModel fileViewModel=(FileViewModel)objects.get(0);
                if(null!=fileViewModel){
                    int checkFlagSaveRichText = isCheckFlagByCourseId(Integer.valueOf(fileViewModel.getCourseId()));
                    courseCheckModel.setCourseId(Integer.valueOf(fileViewModel.getCourseId()));
                    if(-1==checkFlagSaveRichText)
                        throw new ServiceException("课程信息不合法，请重新修改");
                    else if (0 == checkFlagSaveRichText) {
                        //已上架
                        courseCheckModel.setIsUpper(1);
                        courseChangeCheck.saveRichTextCheck(fileViewModel,courseCheckModel);
                    }else if(2==checkFlagSaveRichText){
                        //已审核
                        String catalogId = fileViewModel.getCatalogId();
                        String courseId = fileViewModel.getCourseId();
                        Map<String, Object> catalogIdMap= MapUtils.initMap("catalogId", Integer.valueOf(catalogId));
                        catalogIdMap.put("courseId",Integer.valueOf(courseId));
                        List<CourseDetail> objects1 = courseDetailService.selectList(catalogIdMap);
                        if(null!=objects1 && objects1.size()>0){
                            CourseDetail courseDetail = objects1.get(0);
                            if(!StringUtils.isEmpty(fileViewModel.getResourcesId())){
                                Integer integer = Integer.valueOf(fileViewModel.getResourcesId());
                                if(integer.intValue()==courseDetail.getResourcesId().intValue()){
                                    Resources resources = new Resources();
                                    resources.setResourcesId(integer);
                                    resources.setCheckStatus(0);
                                    resourcesService.updateByPrimaryKeySelective(resources);
                                    Course course =  new Course();
                                    course.setCourseId(course.getCourseId());
                                    course.setStatus(0);
                                    courseService.updateByPrimaryKeySelective(course);
                                }
                            }
                        }
                    }else if(1==checkFlagSaveRichText){
                        courseCheckModel.setSyncFlag(1);
                    }
                }
                break;
            case "insertResourceFile":
                File file=(File)objects.get(0);
                FileViewModel fileViewModels=(FileViewModel)objects.get(1);
                if(null!=file && null!=fileViewModels){
                    String courseId = fileViewModels.getCourseId();
                    int checkFlagSaveRichText = isCheckFlagByCourseId(Integer.valueOf(courseId));
                    courseCheckModel.setCourseId(Integer.valueOf(Integer.valueOf(courseId)));
                    if(-1==checkFlagSaveRichText)
                        throw new ServiceException("课程信息不合法，请重新修改");
                    else if(0 == checkFlagSaveRichText){
                        //已上架
                        courseCheckModel.setIsUpper(1);
                        courseChangeCheck.fileCheck(file,fileViewModels,courseCheckModel);
                    }else if(2==checkFlagSaveRichText){
                        //已审核,因每次的资源文档都是新上传的，没有关联的说法，所以不存在Resources表的update操作，所以不需要同步审核状态
                    }else if(1==checkFlagSaveRichText){
                        courseCheckModel.setSyncFlag(1);
                    }
                }
                break;
            case "editTestPaper":
                TestPaper testPaper=(TestPaper)objects.get(0);
                Integer testPaperId = testPaper.getTestPaperId();
                Map<String, Object> initMap = MapUtils.initMap("maxType", 1);
                initMap.put("content",testPaperId+"");
                List<Resources> resourcesList = resourcesService.selectList(initMap);
                if(null==resourcesList||resourcesList.size()<=0){
                    log.info("试卷无资源描述信息，testPaperId:"+testPaper.getTestPaperId());
                    throw new ServiceException("试卷无资源描述信息，testPaperId:"+testPaper.getTestPaperId());
                }
                Map<String, Object> objectMap = MapUtils.initMap("resourcesId", resourcesList.get(0).getResourcesId());
                List<CourseDetail> courseDetailList = courseDetailService.selectList(objectMap);
                if(null!=courseDetailList &&courseDetailList.size()>0){
                    CourseDetail courseDetail = courseDetailList.get(0);
                    int checkFlagByCourseId3 = isCheckFlagByCourseId(courseDetail.getCourseId());
                    courseCheckModel.setCourseId(courseDetail.getCourseId());
                    if(-1==checkFlagByCourseId3)
                        throw new ServiceException("课程信息不合法，请重新修改");
                    else if(0==checkFlagByCourseId3){
                        //已上架
                        courseCheckModel.setIsUpper(1);
                        courseCheckModel.setContentType(1);
                        courseChangeCheck.optTestPaperCheck(testPaper.getName(),null,courseCheckModel);
                    }else if(2==checkFlagByCourseId3){
                        //已审核，实现程序已做处理
                    }else if(1==checkFlagByCourseId3){
                        courseCheckModel.setSyncFlag(1);
                    }
                }
                break;
            case "saveTestPaperQuestion":
                List<TestPaperQuestion> testPaperQuestionLst=(List<TestPaperQuestion>)objects.get(0);
                TestPaperQuestion testPaperQuestion = testPaperQuestionLst.get(0);
                Integer testPagerId2 = testPaperQuestion.getTestPagerId();
                Map<String, Object> initMap1 = MapUtils.initMap("maxType", 1);
                initMap1.put("content",testPagerId2+"");
                List<Resources> resourcesList1 = resourcesService.selectList(initMap1);
                if(null==resourcesList1||resourcesList1.size()<=0){
                    log.info("试卷无资源描述信息，testPaperId:"+testPagerId2);
                    throw new ServiceException("试卷无资源描述信息，testPaperId:"+testPagerId2);
                }
                Map<String, Object> objectMap1 = MapUtils.initMap("resourcesId", resourcesList1.get(0).getResourcesId());
                List<CourseDetail> courseDetailLists = courseDetailService.selectList(objectMap1);
                if(null!=courseDetailLists &&courseDetailLists.size()>0){
                    CourseDetail courseDetail = courseDetailLists.get(0);
                    int checkFlagByCourseId3 = isCheckFlagByCourseId(courseDetail.getCourseId());
                    courseCheckModel.setCourseId(courseDetail.getCourseId());
                    if(-1==checkFlagByCourseId3)
                        throw new ServiceException("课程信息不合法，请重新修改");
                    else if(0==checkFlagByCourseId3){
                        //已上架
                        courseCheckModel.setIsUpper(1);
                        courseCheckModel.setChangeType(1);
                        courseCheckModel.setCourseId(courseDetail.getCourseId());
                        courseCheckModel.setContent(testPagerId2+"");
                        courseCheckModel.setContentType(1);
                        courseChangeCheck.saveTestPaperQuestionCheck(testPaperQuestionLst,courseCheckModel);
                    }else if(2==checkFlagByCourseId3){
                        //已审核，主体程序已做处理
                    }else if(1==checkFlagByCourseId3){
                        courseCheckModel.setSyncFlag(1);
                    }
                }
                break;
            case "editQuestion":
                QuestionModel questionModel=(QuestionModel) objects.get(0);
                Question question = questionService.selectByPrimaryKey(questionModel.getQuestionId());
                if(null!=question){
                    List<TestPaperQuestion> testPaperQuestionLsts = testPaperQuestionService.selectList(MapUtils.initMap("questionId", questionModel.getQuestionId()));
                    if(null!=testPaperQuestionLsts && testPaperQuestionLsts.size()>0) {
                        StringBuilder temps=new StringBuilder();
                        for (TestPaperQuestion testPaperQuestions : testPaperQuestionLsts) {
                            temps.append(testPaperQuestions.getTestPagerId()).append(",");
                        }
                        int checkFlagByCourseId4 = isCheckFlagByCourseId(question.getCourseId());
                        courseCheckModel.setCourseId(question.getCourseId());
                        if (-1 == checkFlagByCourseId4)
                            throw new ServiceException("课程信息不合法，请重新修改");
                        else if (0 == checkFlagByCourseId4) {
                            String toString = temps.toString();
                            courseCheckModel.setCourseId(question.getCourseId());
                            courseCheckModel.setIsUpper(1);
                            courseCheckModel.setChangeType(1);
                            courseCheckModel.setContentType(1);
                            courseCheckModel.setContent(toString.substring(0,toString.length()-1));
                            courseChangeCheck.editQuestionCheck(questionModel,courseCheckModel);
                        } else if (2 == checkFlagByCourseId4) {
                            //已审核，主体程序已处理
                        }else if(1==checkFlagByCourseId4){
                            courseCheckModel.setSyncFlag(1);
                        }
                    }
                }
                break;
            case "editVideoInfo":
                VideoInfo videoInfo =(VideoInfo)objects.get(0);
                Integer videoInfoId = videoInfo.getVideoInfoId();
                if(null!=videoInfoId && videoInfoId.intValue()>0) {
                    Map<String, Object> content = MapUtils.initMap("content", videoInfo.getVideoInfoId() + "");
                    content.put("type", 2);
                    List<Resources> editVideoInfoList = resourcesService.selectList(content);
                    if(null==editVideoInfoList || editVideoInfoList.size()<=0)
                        throw new ServiceException("录播视频无资源描述信息,视频id"+videoInfo.getVideoInfoId());
                    Map<String, Object> editVideoInfoMap = MapUtils.initMap("resourcesId", editVideoInfoList.get(0).getResourcesId());
                    List<CourseDetail> editVideoInfoCourseDetailLists = courseDetailService.selectList(editVideoInfoMap);
                    if(null!=editVideoInfoCourseDetailLists && editVideoInfoCourseDetailLists.size()>0){
                        CourseDetail courseDetail = editVideoInfoCourseDetailLists.get(0);
                        int checkFlagByCourseId4 = isCheckFlagByCourseId(courseDetail.getCourseId());
                        courseCheckModel.setCourseId(courseDetail.getCourseId());
                        if(-1==checkFlagByCourseId4)
                            throw new ServiceException("课程信息不合法，请重新修改");
                        else if(0==checkFlagByCourseId4){
                            courseCheckModel.setIsUpper(1);
                            courseChangeCheck.editVideoInfoCheck(videoInfo,courseCheckModel);
                        }else if(2==checkFlagByCourseId4){
                            //课程已审核的，回退到课程未审核状态
                            Course course = new Course();
                            course.setCourseId(courseDetail.getCourseId());
                            course.setStatus(0);
                            courseService.updateByPrimaryKeySelective(course);
                        }else if(1==checkFlagByCourseId4){
                            courseCheckModel.setSyncFlag(1);
                        }
                    }
                }
                break;
            case "saveAccaVideo":
                JSONObject jsonObject=(JSONObject)objects.get(0);
                if(jsonObject.containsKey("videoInfoId")){
                    Map<String, Object> content = MapUtils.initMap("content", jsonObject.getString("videoInfoId"));
                    content.put("type", 2);
                    List<Resources> editVideoInfoList = resourcesService.selectList(content);
                    if(null==editVideoInfoList || editVideoInfoList.size()<=0)
                        throw new ServiceException("ACCA录播视频无资源描述信息,视频id"+jsonObject.getInteger("videoInfoId"));
                    Map<String, Object> editVideoInfoMap = MapUtils.initMap("resourcesId", editVideoInfoList.get(0).getResourcesId());
                    List<CourseDetail> editVideoInfoCourseDetailLists = courseDetailService.selectList(editVideoInfoMap);
                    if(null!=editVideoInfoCourseDetailLists && editVideoInfoCourseDetailLists.size()>0){
                        CourseDetail courseDetail = editVideoInfoCourseDetailLists.get(0);
                        int checkFlagByCourseId4 = isCheckFlagByCourseId(courseDetail.getCourseId());
                        courseCheckModel.setCourseId(courseDetail.getCourseId());
                        if(-1==checkFlagByCourseId4)
                            throw new ServiceException("课程信息不合法，请重新修改");
                        else if(0==checkFlagByCourseId4){
                            courseCheckModel.setIsUpper(1);
                            VideoInfo videoInfoii=new VideoInfo();
                            videoInfoii.setVideoName(jsonObject.getString("videoName"));
                            videoInfoii.setVideoInfoId(-1);
                            courseChangeCheck.editVideoInfoCheck(videoInfoii,courseCheckModel);
                        }else if(2==checkFlagByCourseId4){
                            //课程已审核的，回退到课程未审核状态
                            Course course = new Course();
                            course.setCourseId(courseDetail.getCourseId());
                            course.setStatus(0);
                            courseService.updateByPrimaryKeySelective(course);
                        }else if(1==checkFlagByCourseId4){
                            courseCheckModel.setSyncFlag(1);
                        }
                    }
                }
                break;
            default:
                courseCheckModel=null;
        }
        putAragsValue(args,courseCheckModel);
    }

    /**
     * 返回 0：已上架 1：课程未审核 2课程审核过
     * @param courseId
     * @return
     */
    private int isCheckFlagByCourseId(int courseId){
        boolean isUp=false;
        List<CommodityInfo> commodityInfos = commodityInfoService.selectByCourseId(courseId+"");
        if(null!=commodityInfos && commodityInfos.size()>0) {
            for (CommodityInfo cd : commodityInfos) {
                if(1 == cd.getCommodityStatus()) { //上架了
                    isUp = true;
                    break;
                }
            }
        }
        if(isUp)
            return 0;
        else{
            Course course = courseService.selectByPrimaryKey(courseId);
            if(null!=course){
                if(course.getStatus()==0)
                    return 1;
                else
                    return 2;
            }
            else
                return -1;
        }
    }

    private void putAragsValue(Object[] args,CourseCheckModel courseCheckModel){
        for (Object arg : args) {
            if(arg instanceof CourseCheckModel){
                ((CourseCheckModel) arg).setChangeType(courseCheckModel.getChangeType());
                ((CourseCheckModel) arg).setContent(courseCheckModel.getContent());
                ((CourseCheckModel) arg).setSyncFlag(courseCheckModel.getSyncFlag());
                ((CourseCheckModel) arg).setCatalogId(courseCheckModel.getCatalogId());
                ((CourseCheckModel) arg).setContentId(courseCheckModel.getContentId());
                ((CourseCheckModel) arg).setCourseId(courseCheckModel.getCourseId());
                ((CourseCheckModel) arg).setCourseDetailId(courseCheckModel.getCourseDetailId());
                ((CourseCheckModel) arg).setContentType(courseCheckModel.getContentType());
                ((CourseCheckModel) arg).setIsUpper(courseCheckModel.getIsUpper());
                break;
            }
        }
    }
    //AfterReturning 方法执行正常时的执行，after都会执行
    @AfterReturning("activeMethod()")
    public void afterReturning(JoinPoint joinPoint) {
        if(!CourseCheckAop.isCheck)
            return;
        Object[] args = joinPoint.getArgs();
        List<Object> objects = Arrays.asList(args);
        CourseCheckModel courseCheckModel=null;
        for (Object arg : objects) {
            if(arg instanceof CourseCheckModel){
                courseCheckModel=(CourseCheckModel)arg;
                break;
            }
        }
        Integer isUp=courseCheckModel.getIsUpper();
        if(null==isUp || isUp.intValue()==0)
            return;
        Integer syncFlag = courseCheckModel.getSyncFlag();
        Integer courseId = courseCheckModel.getCourseId();
        Set<String> userNameSet = orderInfoService.selectUserListByCourseId(courseId.toString());
        if(null!=userNameSet && !userNameSet.isEmpty()){
            KnowledgePointNode knowledgePoints = catalogService.getKnowledgePoints(courseId,null);
            Map<Integer, Integer> catalogSortMap = getCatalogSortMap(knowledgePoints);
            for (String s : userNameSet) {
                Set<Integer> studentSet=new HashSet<>();
                Date date = new Date();
                Map<String,Integer> nameIdMap=new HashMap<>();
                SsoUser userByUserNameInter = ssoUserService.getUserByUserNameInter(s);
                if(!StringUtils.isEmpty(userByUserNameInter)&&userByUserNameInter.getId()!=null) {
                    studentSet.add(userByUserNameInter.getId());
                    nameIdMap.put(s,userByUserNameInter.getId());
                }
                Map<String, Object> objectMap = MapUtils.initMap("courseId", courseId);
                List<CourseDetail> selectList = courseDetailService.selectList(objectMap);
                if(null!=selectList && selectList.size()>0){
                    for (CourseDetail courseDetail : selectList) {
                        if(0 == courseDetail.getCatalogId()){
                            continue;
                        }
                        for (Integer integer : studentSet) {
                            LearnActive learnActive = new LearnActive();
                            learnActive.setCourseId(courseDetail.getCourseId());
                            learnActive.setStudentId(integer);
                            learnActive.setCourseDetailId(courseDetail.getCourseDetailId());
                            learnActive.setLearnFlag(0);
                            learnActive.setSort(catalogSortMap.get(courseDetail.getCatalogId()));
                            learnActiveService.insertNoExists(learnActive);
                        }
                    }
                }
            }
        }


        if(null==syncFlag || 0!=syncFlag)
            return;
        Integer contentType = courseCheckModel.getContentType();
        if(null!=contentType){
            if(contentType.intValue()==1){
                //作业测试同步
                String content = courseCheckModel.getContent();
                Set<Integer> passTestPaper=new HashSet<>();
                Map<String, Object> maxType = MapUtils.initMap("maxType", 1);
                if (content.contains(",")) {
                    String[] split = content.split(",");
                    for (String s : split) {
                        maxType.put("content",s);
                        List<Resources> objects1 = resourcesService.selectList(maxType);
                        if(null!=objects1 && objects1.size()>0) {
                            passTestPaper.add(objects1.get(0).getResourcesId());
                        }
                    }
                }else {
                    maxType.put("content",content);
                    List<Resources> objects1 = resourcesService.selectList(maxType);
                    if(null!=objects1 && objects1.size()>0) {
                        passTestPaper.add(objects1.get(0).getResourcesId());
                    }
                }

                //同步学生资源资源
                resourcesService.syncStudentResources(courseCheckModel.getCourseId(),passTestPaper);
            }
        }
    }

    private Map<Integer, Integer> getCatalogSortMap(KnowledgePointNode knowledgePoints) {
        List<Integer> CatalogLst = new ArrayList<>();
        treeToLst(knowledgePoints.getNodes(), CatalogLst);
        Map<Integer, Integer> resMap = new HashMap<>();
        int i = 0;
        for (Integer integer : CatalogLst) {
            resMap.put(integer, ++i);
        }
        return resMap;
    }
    private void treeToLst(List<KnowledgePointNode> nodes, List<Integer> CatalogLst) {
        for (KnowledgePointNode node : nodes) {
            if (node.getNodes() != null && node.getNodes().size() > 0) {
                treeToLst(node.getNodes(), CatalogLst);
            } else {
                CatalogLst.add(node.getId());
            }
        }
    }

}

