package com.by.blcu.resource.service.impl;

import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.*;
import com.by.blcu.course.dao.ICourseDao;
import com.by.blcu.course.dto.Course;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.mall.service.CommodityInfoService;
import com.by.blcu.mall.service.MallOrderInfoService;
import com.by.blcu.mall.vo.MallCommodityOrderVo;
import com.by.blcu.mall.vo.MallOrderInfoVo;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.service.SsoUserService;
import com.by.blcu.resource.dao.ITestPaperDao;
import com.by.blcu.resource.dao.ITestResultDao;
import com.by.blcu.resource.dao.ITestResultDetailDao;
import com.by.blcu.resource.dto.*;
import com.by.blcu.resource.model.StudentCourseInfoModel;
import com.by.blcu.resource.model.StudentCourseModel;
import com.by.blcu.resource.service.ILearnActiveService;
import com.by.blcu.resource.service.IResourcesService;
import com.by.blcu.resource.service.ITestPaperQuestionService;
import com.by.blcu.resource.service.ITestResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Service("testResultService")
@Slf4j
public class TestResultServiceImpl extends BaseServiceImpl implements ITestResultService {
    @Autowired
    private ITestResultDao testResultDao;

    @Autowired
    private ITestPaperDao testPaperDao;

    @Autowired
    private ICourseDao courseDao;

    @Autowired
    private ITestResultDetailDao testResultDetailDao;

    @Resource
    private CommodityInfoService commodityInfoService;

    @Resource
    private SsoUserService ssoUserService;

    @Resource(name = "courseDetailService")
    private ICourseDetailService courseDetailService;

    @Resource(name="resourcesService")
    private IResourcesService resourcesService;

    @Autowired
    private MallOrderInfoService orderInfoService;

    @Autowired
    private FastDFSClientWrapper dfsClient;

    @Resource(name = "testPaperQuestionService")
    private ITestPaperQuestionService testPaperQuestionService;

    @Resource(name="learnActiveService")
    private ILearnActiveService learnActiveService;

    @Override
    protected IBaseDao getDao() {
        return this.testResultDao;
    }

    @Override
    @Transactional
    public RetResult syncTestPaper(Integer courseId, Integer studentId) {
        Map<String, Object> initMap = MapUtils.initMap("courseId", courseId);
        List<CourseDetail> objects = courseDetailService.selectList(initMap);
        if(null==objects||objects.size()<=0)
            return RetResponse.makeOKRsp();
        Set<Integer> resourcesLst=new HashSet<>();
        Map<Integer,Integer> courseDetailResMap=new HashMap<>();
        Set<Integer> testPagerSet=new HashSet<>();
        for (CourseDetail object : objects) {
            Integer resourcesId = object.getResourcesId();
            if(StringUtils.isEmpty(resourcesId))
                continue;
            resourcesLst.add(resourcesId);
            courseDetailResMap.put(resourcesId,object.getCourseDetailId());
        }
        Map<String, Object> entityKeyValues = MapUtils.initMap("entityKeyValues",resourcesLst);
        List<Resources> resLst = resourcesService.selectList(entityKeyValues);
        Map<String, Object> initMap1 = MapUtils.initMap("studentId",studentId);
        for (Resources resources : resLst) {
            if(resources.getType().intValue()<=1){
                String content = resources.getContent();
                if(!StringUtils.isEmpty(content)){
                    TestPaper testPaper = testPaperDao.selectByPrimaryKey(Integer.valueOf(content));
                    if(null!=testPaper){
                        testPagerSet.add(testPaper.getTestPaperId());
                        initMap1.put("testPaperId",testPaper.getTestPaperId());
                        List<TestResult> testResultLst = testResultDao.selectList(initMap1);
                        if(null!=testResultLst&&testResultLst.size()>0){
                        }else {
                            TestResult testResult=new TestResult();
                            testResult.setStudentId(studentId);
                            testResult.setTestPaperId(testPaper.getTestPaperId());
                            testResult.setStatus(0);
                            testResultDao.insert(testResult);
                        }
                    }
                }
            }
        }
        return RetResponse.makeOKRsp(testPagerSet);
    }

    @Override
    @Transactional
    public RetResult addTestCallBack(Integer courseId, Integer testPaperId, Integer oldTestPaperId,List<Integer> studentList) {
        TestPaper testPaper = testPaperDao.selectByPrimaryKey(testPaperId);
        if(null==testPaper){
            log.info("试卷不存在:(试卷id"+testPaperId+")");
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"试卷不存在:(试卷id"+testPaperId+")");
        }
        for (Integer student : studentList) {
            TestResult testResult=new TestResult();
            testResult.setStudentId(student);
            testResult.setTestPaperId(testPaperId);
            testResult.setStatus(0);
            testResultDao.insertNoExists(testResult);
        }
        if(null==oldTestPaperId)
            return RetResponse.makeOKRsp();
        TestPaper testPaper1  = testPaperDao.selectByPrimaryKey(oldTestPaperId);
        if(null==testPaper1)
            return RetResponse.makeOKRsp();
        Map<String, Object> initMap = MapUtils.initMap("testPaperId", oldTestPaperId);
        testResultDao.deleteByParams(initMap);
        return RetResponse.makeOKRsp();
    }


    @Override
    public List<StudentCourseModel> getStudentCourseLst(Integer useType, int studentId, String studentName, Integer courseId,Boolean isMyCourse) throws Exception {
        List<Integer> coueseIds=new ArrayList<>();
        if(courseId==null) {
            List<MallOrderInfoVo> orderInfoVos = orderInfoService.selectMallOrderInfoVoByUserName(studentName);
            List<MallCommodityOrderVo> mallCommodityOrderVos = new ArrayList<>();
            orderInfoVos.forEach(o -> mallCommodityOrderVos.addAll(o.getMallCommodityOrderVoList()));
            for (MallCommodityOrderVo mallCommodityOrderVo : mallCommodityOrderVos) {
                String courseId1 = mallCommodityOrderVo.getCommodityInfoFileVo().getCourseId();
                if (!StringUtils.isEmpty(courseId1))
                    coueseIds.add(Integer.valueOf(courseId1));
            }
        }else {
            coueseIds.add(Integer.valueOf(courseId));
        }
        if(coueseIds.size()<=0){
            log.info("学生没有购买课程");
            throw new ServiceException("学生没有购买课程");
        }
        List<Integer> resourcesLst=new ArrayList<>();
        for (Integer coueseId : coueseIds) {
            Map<String,Object> objectMap = MapUtils.initMap("courseId", coueseId);
            if(isMyCourse){
                objectMap.put("catalogId", "0");
            }
            List<CourseDetail> objectList = courseDetailService.selectList(objectMap);
            for (CourseDetail courseDetail : objectList) {
                if(!StringUtils.isEmpty(courseDetail.getResourcesId())) {
                    resourcesLst.add(courseDetail.getResourcesId());
                }
            }

        }
        Map<String, Object> initMap1 = MapUtils.initMap("type", useType);
        if(resourcesLst.size()>0) {
            initMap1.put("entityKeyValues", resourcesLst);
        }else {
            resourcesLst.add(-1);
            initMap1.put("entityKeyValues", resourcesLst);
        }
        Map<String, Object> initMap = MapUtils.initMap("studentId", studentId);
        List<Resources> objects = resourcesService.selectList(initMap1);
        List<TestResult> testResults=new ArrayList<>();
        for (Resources object : objects) {
            String content = object.getContent();
            if(StringUtils.isEmpty(content)){
               continue;
            }
            Integer testPaperId = Integer.valueOf(content);
            initMap.put("testPaperId",testPaperId);
            List<TestResult> testResults1 = testResultDao.selectList(initMap);
            testResults.addAll(testResults1);
        }
        Map<Integer,List<StudentCourseInfoModel>> map=new HashMap<>();
        initMap.clear();
        if(courseId!=null)
            initMap.put("courseId",courseId);
        if(useType!=null)
            initMap.put("useType",useType);
        for (TestResult testResult : testResults) {
            Integer testPaperId = testResult.getTestPaperId();
            if(null!=testPaperId){
                initMap.put("testPaperId",testPaperId);
                List<TestPaper> testPaperLst = testPaperDao.selectList(initMap);
                if(testPaperLst.size()<=0)
                    continue;
                TestPaper testPaper=testPaperLst.get(0);
                if(null!=testPaper){
                    Course course = courseDao.selectByPrimaryKey(testPaper.getCourseId());
                    if(course==null||StringUtils.isEmpty(course.getName())){
                        log.error("课程id为:"+testPaper.getCourseId()+"的课程不存在");
                        continue;
                    }
                    String name = course.getName();
                    StudentCourseInfoModel studentCourseInfoModel = new StudentCourseInfoModel();
                    studentCourseInfoModel.setStatus(testResult.getStatus());
                    studentCourseInfoModel.setTestPaperId(testPaperId);
                    studentCourseInfoModel.setTestPaperName(testPaper.getName());
                    Date startTime = testPaper.getStartTime();
                    String startTimeStr = DateUtils.date2String(startTime, "yyy-MM-dd");
                    Date endTime = testPaper.getEndTime();
                    String endTimeStr =DateUtils.date2String(endTime, "yyy-MM-dd");
                    studentCourseInfoModel.setDateTime(startTimeStr+"~"+endTimeStr);
                    if(map.containsKey(course.getCourseId())){
                        List<StudentCourseInfoModel> studentCourseInfoModels = map.get(course.getCourseId());
                        studentCourseInfoModels.add(studentCourseInfoModel);
                    }else {
                        List<StudentCourseInfoModel> studentCourseInfoModels=new ArrayList<>();
                        studentCourseInfoModels.add(studentCourseInfoModel);
                        map.put(course.getCourseId(),studentCourseInfoModels);
                    }
                }
            }
        }
        List<StudentCourseModel> resList=new ArrayList<>();
        for(Map.Entry<Integer, List<StudentCourseInfoModel>> entry:map.entrySet()){
            StudentCourseModel temp=new StudentCourseModel();
            Course course = courseDao.selectByPrimaryKey(entry.getKey());
            temp.setCourseId(entry.getKey());
            temp.setCourseName(course.getName());
            temp.setStudentCourseInfoLst(entry.getValue());
            resList.add(temp);
        }

        return resList;
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?")
    //@Scheduled(cron = "0 1/1 * * * ?")
    @Transactional
    public void checkTestResultStatus() {
        log.info("开始执行定时任务....");
        Map<String, Object> initMap = MapUtils.initMap("maxStatus", 1);
        initMap.put("forUpdate",true);
        List<TestResult> testResults = getDao().selectList(initMap);
        Date now=new Date();
        for (TestResult testResult : testResults) {
            Integer testPaperId = testResult.getTestPaperId();
            TestPaper testPaper = testPaperDao.selectByPrimaryKey(testPaperId);
            if(testPaper.getEndTime().before(now)){
                testResult.setStatus(5);
                getDao().updateByPrimaryKeySelective(testResult);
            }
        }
    }

    @Override
    public void exporTestPaperById(HttpServletRequest httpServletRequest, HttpServletResponse response, int testPagerId) {
        TestPaper testPaper = testPaperDao.selectByPrimaryKey(testPagerId);
        if(null==testPaper){
            log.info("试卷id为"+testPagerId+"的试卷不存在");
            throw new ServiceException("试卷id为"+testPagerId+"的试卷不存在");
        }
        if(!StringUtils.isEmpty(testPaper.getExportStuPath())){
            dfsClient.webDownFile(httpServletRequest,response,testPaper.getExportStuPath(),testPaper.getName());
        }else {
            String relPath=null;
            try {
                relPath = testPaperQuestionService.createNewWord(testPagerId,false,false);
                testPaper.setExportStuPath(relPath);
                testPaperDao.updateByPrimaryKeySelective(testPaper);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("创建试卷word失败");
            }
            dfsClient.webDownFile(httpServletRequest,response,relPath,testPaper.getName());
        }
    }

    @Override
    public void syncStudentTestPager(Integer courseId, Integer courseDetailId, Integer insertTestPager, Integer deletePager) {
        //1.根据课程id查询购买该课程的用户信息集合，返回Set<String>,通过调用李程的接口获取
        //2.根据userName查询userId的集合
        Set<String> userNameSet=new HashSet<>();
        Set<Integer> studentSet=new HashSet<>();
        Map<String,Integer> nameIdMap=new HashMap<>();
        for (String s : userNameSet) {
            SsoUser userByUserNameInter = ssoUserService.getUserByUserNameInter(s);
            if(!StringUtils.isEmpty(userByUserNameInter)&&userByUserNameInter.getId()!=null) {
                studentSet.add(userByUserNameInter.getId());
                nameIdMap.put(s,userByUserNameInter.getId());
            }
        }
        if(null!=insertTestPager && insertTestPager.intValue()>0){
            for (Integer integer : studentSet) {
                TestResult testResult = new TestResult();
                testResult.setStudentId(integer);
                testResult.setStatus(0);
                testResult.setTestPaperId(insertTestPager);
                testResultDao.insertNoExists(testResult);
            }
        }
        if(null!=deletePager&&deletePager.intValue()>0){
            //判断该试卷是否可以删除
            for (String s : userNameSet) {
                List<MallOrderInfoVo> orderInfoVos = orderInfoService.selectMallOrderInfoVoByUserName(s);
                List<MallCommodityOrderVo> mallCommodityOrderVos = new ArrayList<>();
                orderInfoVos.forEach(o->mallCommodityOrderVos.addAll(o.getMallCommodityOrderVoList()));
                Set<Integer> courseIdSet=new HashSet<>();
                for (MallCommodityOrderVo mallCommodityOrderVo : mallCommodityOrderVos) {
                    String courseId1 = mallCommodityOrderVo.getCommodityInfoFileVo().getCourseId();
                    if(StringUtils.isEmpty(courseId1)&& Integer.valueOf(courseId1).intValue()>0){
                        courseIdSet.add(Integer.valueOf(courseId1));
                    }
                }
                //如果其他课程都没包含这张试卷，则删除这个学生的这张试卷
                if(!isContainsTestPaper(courseIdSet,deletePager)){
                    Integer integer = nameIdMap.get(s);
                    if(null!=integer){
                        Map<String, Object> initMap = MapUtils.initMap("studentId", integer);
                        initMap.put("testPaperId",deletePager);
                        List<TestResult> objects = testResultDao.selectList(initMap);
                        if(null!=objects && objects.size()>0){
                            Integer testResultId = objects.get(0).getTestResultId();
                            testResultDetailDao.deleteByParams(MapUtils.initMap("testResultId",testResultId));
                            testResultDao.deleteByPrimaryKey(testResultId);
                        }
                    }
                }
            }
            if(null!=courseDetailId) {
                Map<String, Object> courseId1 = MapUtils.initMap("courseId", courseId);
                courseId1.put("courseDetailId",courseDetailId);
                if (null != insertTestPager && insertTestPager.intValue() > 0
                        && null == deletePager) {
                    for (Integer integer : studentSet) {
                        courseId1.put("studentId",integer);
                        List<LearnActive> objects = learnActiveService.selectList(courseId1);
                        if(null==objects ||objects.size()<=0){
                            LearnActive learnActive = new LearnActive();
                            learnActive.setStudentId(integer);
                            learnActive.setCourseId(courseId);
                            learnActive.setCourseDetailId(courseDetailId);
                            learnActive.setLearnFlag(0);
                            learnActiveService.insertSelective(learnActive);
                        }
                    }
                }
                if(null!=deletePager && deletePager.intValue()>0
                    && null==insertTestPager){
                    learnActiveService.deleteByParams(courseId1);

                }
                if(null!=deletePager && deletePager.intValue()>0
                    && null!=insertTestPager && insertTestPager.intValue()>0){
                    List<LearnActive> objects = learnActiveService.selectList(courseId1);
                    for (LearnActive object : objects) {
                        LearnActive learnActive = new LearnActive();
                        learnActive.setLearnFlag(0);
                        learnActive.setLearnTime(null);
                        learnActive.setLearnActiveId(object.getLearnActiveId());
                        learnActiveService.updateByPrimaryKeySelective(learnActive);
                    }
                }
            }

        }
    }

    /**
     * 判断课程集合是否包含某个试卷
     * @param courseIdSet
     * @param testPager
     * @return
     */
    private boolean isContainsTestPaper(Set<Integer> courseIdSet,int testPager){
        for (Integer integer : courseIdSet) {
            List<CourseDetail> courseDetailList = courseDetailService.selectList(MapUtils.initMap("courseId", integer));
            if(null!=courseDetailList && courseDetailList.size()>0){
                for (CourseDetail courseDetail : courseDetailList) {
                    Integer resourcesId = courseDetail.getResourcesId();
                    if(null!=resourcesId && resourcesId.intValue()>0){
                        Resources resources = resourcesService.selectByPrimaryKey(resourcesId);
                        if(null!=resources &&resources.getType().intValue()<=1){
                            String content = resources.getContent();
                            if(!StringUtils.isEmpty(content)){
                                TestPaper testPaper = testPaperDao.selectByPrimaryKey(Integer.valueOf(content));
                                if(null!=testPaper &&testPaper.getTestPaperId().intValue()==testPager)
                                    return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}