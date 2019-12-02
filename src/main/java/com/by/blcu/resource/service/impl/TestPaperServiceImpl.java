package com.by.blcu.resource.service.impl;

import com.by.blcu.core.aop.CourseCheck;
import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.CommonUtils;
import com.by.blcu.core.utils.FastDFSClientWrapper;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.dto.Course;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.course.service.ICourseService;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.resource.dao.*;
import com.by.blcu.resource.dto.Question;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.dto.TestPaper;
import com.by.blcu.resource.dto.TestPaperQuestion;
import com.by.blcu.resource.model.TestPaperCheckModel;
import com.by.blcu.resource.service.IResourcesService;
import com.by.blcu.resource.service.ITestPaperQuestionService;
import com.by.blcu.resource.service.ITestPaperService;
import com.by.blcu.resource.service.ITestResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;

@Service("testPaperService")
@Slf4j
                public class TestPaperServiceImpl extends BaseServiceImpl implements ITestPaperService {
    @Resource
    private ITestPaperDao testPaperDao;

    @Resource
    private ITestPaperFormatDao testPaperFormatDao;

    @Resource(name = "testPaperQuestionService")
    private ITestPaperQuestionService testPaperQuestionService;

    @Resource
    private IQuestionDao questionDao;

    @Resource
    private ICourseService courseService;

    @Resource(name="resourcesService")
    private IResourcesService resourcesService;

    @Resource(name="testResultService")
    private ITestResultService testResultService;

    @Resource(name = "courseDetailService")
    private ICourseDetailService courseDetailService;

    @Resource
    private IResourcesDao resourcesDao;


    @Autowired
    private FastDFSClientWrapper dfsClient;

    @Override
    protected IBaseDao getDao() {
        return this.testPaperDao;
    }

    @Override
    @Transactional
    public void deleteTestPaperById(int id) {
        //1.删除试卷
        getDao().deleteByPrimaryKey(id);
        //2.删除试卷组成
        Map<String, Object> initMap = MapUtils.initMap("testPaperId", id);
        testPaperFormatDao.deleteByParams(initMap);
        //3.删除试卷的题目关联
        //2.删除试卷组成
        Map<String, Object> initMap1 = MapUtils.initMap("testPagerId", id);
        testPaperQuestionService.deleteByParams(initMap1);
        //4.删除resources关联表
        initMap.clear();
        initMap.put("content",id+"");
        resourcesService.deleteByParams(initMap);
    }

    @Override
    public int deleteTestPaperBatch(String testPagerIds,int userId)throws Exception {
        boolean contains = testPagerIds.contains(";");
        if(!contains) {
            testPagerIds+=";";
        }
        String[] split = testPagerIds.split(";");
        int count=0;
        for (String s : split) {
            int testPagerId=Integer.valueOf(s).intValue();
            //添加是否绑定课程判定
            boolean usedResources = resourcesService.isUsedResources(testPagerId, null, 0, 1);
            if(usedResources)
                continue;
            //添加课程是否是自己的判断
            boolean operationDao = CommonUtils.isOperationDao(userId, testPagerId, testPaperDao);
            if(!operationDao){
                log.info(userId+"不能删除不是自己的试卷:"+testPagerId);
                continue;
            }
            count++;
            deleteTestPaperById(testPagerId);
        }
        return count;
    }

    @Override
    @Transactional
    public int createTestPaper(TestPaper testPaper){
        int i = testPaperDao.insertSelective(testPaper);
        Integer testPaperId = testPaper.getTestPaperId();
        Resources resources = new Resources();
        resources.setContent(testPaperId.toString());
        resources.setType(testPaper.getUseType());
        resources.setOrgCode(testPaper.getOrgCode());
        resources.setCreateUser(testPaper.getCreateUser());
        resources.setCreayeTime(testPaper.getCreateTime());
        resources.setUpdateUser(testPaper.getCreateUser());
        resources.setUpdateTime(testPaper.getCreateTime());
        resources.setCheckStatus(0);
        int insert = resourcesDao.insert(resources);
        return insert;
    }

    @Override
    public boolean isEdit(int testPaperId) {
        Map<String, Object> initMap = MapUtils.initMap("testPaperId", testPaperId);
        initMap.put("minStatus",1);
        long count = testResultService.selectCount(initMap);
        if(count>0)
            return false;
        return true;
    }

    @Override
    @Transactional
    @CourseCheck
    public int editTestPaper(TestPaper testPaper, CourseCheckModel courseCheckModel) {
        Map<String, Object> initMap = MapUtils.initMap("maxType", 1);
        initMap.put("content",testPaper.getTestPaperId()+"");
        List<Resources> objects = resourcesService.selectList(initMap);
        if(null==objects||objects.size()<=0){
            log.info("试卷无资源描述信息，testPaperId:"+testPaper.getTestPaperId());
            return -1;
        }
        Resources resources = new Resources();
        resources.setType(testPaper.getUseType());
        resources.setUpdateUser(testPaper.getUpdateUser());
        resources.setUpdateTime(testPaper.getUpdateTime());
        resources.setResourcesId(objects.get(0).getResourcesId());
        resourcesService.updateByPrimaryKeySelective(resources);
        Map<String, Object> objectMap = MapUtils.initMap("resourcesId", objects.get(0).getResourcesId());
        List<CourseDetail> objects1 = courseDetailService.selectList(objectMap);
        if(null!=objects1&&objects1.size()>0) {
            if(0==courseCheckModel.getIsUpper()) {
                for (CourseDetail courseDetail : objects1) {
                    Integer courseId = courseDetail.getCourseId();
                    Course course = new Course();
                    course.setCourseId(courseId);
                    course.setStatus(0);
                    courseService.updateByPrimaryKeySelective(course);
                }
            }
        }
        return getDao().updateByPrimaryKeySelective(testPaper);
    }

    @Override
    public TestPaperCheckModel getCheckInfo(int testPaperId,List<String> voiceLst) throws Exception{
        TestPaperCheckModel temp=new TestPaperCheckModel();
        TestPaper testPaper = getDao().selectByPrimaryKey(testPaperId);
        if(StringUtils.isEmpty(testPaper.getName())){
            log.info("试卷id为"+testPaperId+"的试卷不存在");
            return null;
        }
        temp.setName(testPaper.getName());
        List<Map<String,String>> questionCheckLst =new ArrayList<>();
        Map<String, Object> initMap = MapUtils.initMap("testPagerId", testPaperId);
        List<TestPaperQuestion> testPaperQuestionList= testPaperQuestionService.selectList(initMap);
        for (TestPaperQuestion testPaperQuestion : testPaperQuestionList) {
            Integer questionId = testPaperQuestion.getQuestionId();
            Question question = questionDao.selectByPrimaryKey(questionId);
            Map<String,String> tempMap=new HashMap<>();
            if(!StringUtils.isEmpty(question.getQuestionBody()))
                tempMap.put("questionBody",URLDecoder.decode(question.getQuestionBody(), "UTF-8"));
            if(!StringUtils.isEmpty(question.getQuestionSound())) {
                String s = question.getQuestionSound().replaceFirst("http://192.168.15.150:8000/", "https://review.teblcu.com/");
                voiceLst.add(s);
            }
            if(!StringUtils.isEmpty(question.getQuestionOpt()))
                tempMap.put("questionOpt",URLDecoder.decode(question.getQuestionOpt(), "UTF-8"));
            if(!StringUtils.isEmpty(question.getQuestionAnswer()))
                tempMap.put("questionAnswer",URLDecoder.decode(question.getQuestionAnswer(), "UTF-8"));
            if(!StringUtils.isEmpty(question.getQuestionResolve()))
                tempMap.put("questionResolve",URLDecoder.decode(question.getQuestionResolve(), "UTF-8"));
            questionCheckLst.add(tempMap);
        }
        temp.setQuestionCheckLst(questionCheckLst);
        return temp;
    }

    @Override
    public Set<Integer> getCourseLstByTestPaperId(int testPaperId) {
        Map<String, Object> maxType = MapUtils.initMap("maxType", 1);
        maxType.put("content",testPaperId+"");
        List<Resources> objects = resourcesDao.selectList(maxType);
        if(null==objects||objects.size()<=0)
            return null;
        Integer resourcesId = objects.get(0).getResourcesId();
        maxType.clear();
        maxType.put("resourcesId",resourcesId);
        List<CourseDetail> objects1 = courseDetailService.selectList(maxType);
        Set<Integer> courseSet = new HashSet<>();
        for (CourseDetail courseDetail : objects1) {
            Integer courseId = courseDetail.getCourseId();
            if(null!=courseId)
                courseSet.add(courseId);
        }
        return courseSet;
    }

    @Override
    public void exporTestPaperById(HttpServletRequest httpServletRequest, HttpServletResponse response, int testPagerId) {
        TestPaper testPaper = getDao().selectByPrimaryKey(testPagerId);
        if(null==testPaper){
            log.info("试卷id为"+testPagerId+"的试卷不存在");
            throw new ServiceException("试卷id为"+testPagerId+"的试卷不存在");
        }
        if(!StringUtils.isEmpty(testPaper.getExportPath())
                &&null!=testPaper.getExportTime()
                    && null!=testPaper.getUpdateTime()
                    && testPaper.getExportTime().after(testPaper.getUpdateTime())){
            dfsClient.webDownFile(httpServletRequest,response,testPaper.getExportPath(),testPaper.getName());
        }else {
            String relPath=null;
            try {
                relPath = testPaperQuestionService.createNewWord(testPagerId,true,true);
                testPaper.setExportPath(relPath);
                testPaper.setExportTime(new Date());
                testPaper.setStatus(0);
                testPaper.setExportStuPath(null);
                testPaperDao.updateByPrimaryKeySelective(testPaper);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("创建试卷word失败");
            }
            dfsClient.webDownFile(httpServletRequest,response,relPath,testPaper.getName());
        }
    }
}