package com.by.blcu.resource.service.impl;

import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.DateUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.dao.ICourseDao;
import com.by.blcu.course.dto.Course;
import com.by.blcu.resource.dao.ITestPaperDao;
import com.by.blcu.resource.dao.ITestResultDao;
import com.by.blcu.resource.dto.TestPaper;
import com.by.blcu.resource.dto.TestResult;
import com.by.blcu.resource.model.StudentCourseInfoModel;
import com.by.blcu.resource.model.StudentCourseModel;
import com.by.blcu.resource.service.ITestResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    protected IBaseDao getDao() {
        return this.testResultDao;
    }

    @Override
    @Transactional
    public RetResult syncTestPaper(Integer userId, Integer courseId, Integer studentId) {
        Map<String, Object> initMap = MapUtils.initMap("courseId", courseId);
        initMap.put("createUser",userId);
        List<TestPaper> testPaperList = testPaperDao.selectList(initMap);
        if(testPaperList.size()<=0)
            return RetResponse.makeOKRsp();
        for (TestPaper testPaper : testPaperList) {
            TestResult testResult=new TestResult();
            testResult.setStudentId(studentId);
            testResult.setTestPaperId(testPaper.getTestPaperId());
            testResult.setStatus(0);
            testResultDao.insertNoExists(testResult);
        }
        return RetResponse.makeOKRsp();
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
    public List<StudentCourseModel> getStudentCourseLst(Integer useType, Integer studentId, Integer courseId) throws Exception {
        Map<String, Object> initMap = MapUtils.initMap("studentId", studentId);
        List<TestResult> testResults = testResultDao.selectList(initMap);
        Map<String,List<StudentCourseInfoModel>> map=new HashMap<>();
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
                    if(map.containsKey(name)){
                        List<StudentCourseInfoModel> studentCourseInfoModels = map.get(name);
                        studentCourseInfoModels.add(studentCourseInfoModel);
                    }else {
                        List<StudentCourseInfoModel> studentCourseInfoModels=new ArrayList<>();
                        studentCourseInfoModels.add(studentCourseInfoModel);
                        map.put(name,studentCourseInfoModels);
                    }
                }
            }
        }
        List<StudentCourseModel> resList=new ArrayList<>();
        for(Map.Entry<String, List<StudentCourseInfoModel>> entry:map.entrySet()){
            StudentCourseModel temp=new StudentCourseModel();
            temp.setCourseName(entry.getKey());
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
}