package com.by.blcu.resource.service.impl;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.CommonUtils;
import com.by.blcu.core.utils.DateUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.dao.ICourseDao;
import com.by.blcu.course.dao.ICourseDetailDao;
import com.by.blcu.course.dto.Course;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.service.SsoUserService;
import com.by.blcu.manager.umodel.UserSearchModel;
import com.by.blcu.resource.dao.*;
import com.by.blcu.resource.dto.*;
import com.by.blcu.resource.model.*;
import com.by.blcu.resource.service.ITeacherCorrectService;
import com.by.blcu.resource.service.ITestResultDetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.*;

import static com.by.blcu.core.ret.RetResponse.makeRsp;

@Service
@Slf4j
public class TeacherCorrectServiceImpl implements ITeacherCorrectService {

    @Autowired
    private ITestResultDao testResultDao;
    @Autowired
    private ITestResultDetailDao testResultDetailDao;
    @Autowired
    private ITestPaperDao testPaperDao;
    @Autowired
    private IQuestionTypeDao questionTypeDao;
    @Autowired
    private ITestResultDetailDao detailDao;
    @Autowired
    private ICourseDao courseDao;
    @Autowired
    private ICourseDetailDao courseDetailDao;
    @Autowired
    private IResourcesDao resourcesDao;
    @Autowired
    private ITestResultDetailService testResultDetailService;

    @Resource
    private SsoUserService ssoUserService;

    @Override
    public RetResult getPaperListByCourseId(Integer courseId,String type,String paperName,Integer pageIndex,Integer pageSize) throws Exception {


        pageSize = StringUtils.isEmpty(pageSize)?10:pageSize;
        pageIndex = StringUtils.isEmpty(pageIndex)?1:pageIndex;
        // 根据课程查询下面的试卷和测试列表
        Course course = courseDao.selectByPrimaryKey(courseId);
        if(null == course){
            throw new ServiceException("根据课程id查询课程不存在！【"+courseId+"】");
        }
        PageHelper.startPage(pageIndex, pageSize);
        Map<String,Object> pmap = new HashMap<>();
        pmap.put("courseId",courseId);
        pmap.put("name",paperName);
        pmap.put("type",type);// 0 : 测试 1：作业
        List<String> paperIdList = courseDetailDao.getIdList(pmap);
        PageInfo<String> pageInfo = new PageInfo<>(paperIdList);
        Long total = pageInfo.getTotal();
        List<CourseAndTestModel> list = new ArrayList<>();
        if (!CommonUtils.listIsEmptyOrNull(paperIdList)) {
            for (String id : paperIdList) {
                Map<String, Object> map = new HashMap<>();
                map.put("testPaperId", id);
                List<TestResult> results = testResultDao.selectList(map);
                Integer shouldCommit = null;
                Long haveCommit = null;
                Long haveCorrect = null;
                Long haveNotCorrect = null;
                if (!CommonUtils.listIsEmptyOrNull(results)) {
                    shouldCommit = results.size();
                    haveCommit = results.stream().filter(r -> r.getStatus() == 2 || r.getStatus() == 3 || r.getStatus() == 4).count();
                    haveCorrect = results.stream().filter(r -> r.getStatus() == 4).count();
                    haveNotCorrect = haveCommit - haveCorrect;
                }
                TestPaper paper = new TestPaper();
                paper = testPaperDao.selectByPrimaryKey(Integer.parseInt(id));
                if (null == paper) {
                    throw new ServiceException("未查询到试卷！");
                }
                CourseAndTestModel model = new CourseAndTestModel();
                model.setHaveCommit(haveCommit);
                model.setShouldCommit(shouldCommit);
                model.setHaveCorrect(haveCorrect);
                model.setHaveNotCorrect(haveNotCorrect);
                model.setPaperId(paper.getTestPaperId());
                model.setStartTime(paper.getStartTime());
                model.setEndTime(paper.getEndTime());
                model.setPaperName(paper.getName());
                list.add(model);
            }
        }
        return RetResponse.makeRsp(list,total);
    }

    @Override
    public RetResult getCorrectList(String paperId, String type,Integer pageIndex,Integer pageSize,String userName) throws Exception {

        pageSize = StringUtils.isEmpty(pageSize)?10:pageSize;
        pageIndex = StringUtils.isEmpty(pageIndex)?1:pageIndex;
        List<Map<String, Object>> list = new ArrayList<>();
        TestPaper paper = new TestPaper();
        paper = testPaperDao.selectByPrimaryKey(Integer.parseInt(paperId));
        if (null == paper) {
            throw new ServiceException("未查询到试卷！");
        }
        List<Integer> userIds = new ArrayList<>();
        if(StringUtils.isEmpty(userName)){
            UserSearchModel searchModel = new UserSearchModel();
            searchModel.setUserName(userName);
            List<SsoUser> userList = ssoUserService.selectListAnd(searchModel);
            if(!CommonUtils.listIsEmptyOrNull(userList)){
                for (SsoUser ssoUser : userList) {
                    userIds.add(ssoUser.getId());
                }
            }
        }
        Long total = 0L;
        if ("1".equals(type)) {// 已批阅
            PageHelper.startPage(pageIndex, pageSize);
            Map<String, Object> map = new HashMap<>();
            map.put("testPaperId", paperId);
            map.put("status", "4");// 4：已批阅
            map.put("studentIds", userIds);// 用户id
            List<TestResult> results = testResultDao.selectList(map);
            PageInfo<TestResult> pageInfo = new PageInfo<>(results);
            total = pageInfo.getTotal();
            for (TestResult r : results) {
                Map<String, Object> map1 = new HashMap<>();
                SsoUser userByUserIdInter = ssoUserService.getUserByIdInter(r.getStudentId());
                if(null != userByUserIdInter){
                    map1.put("studentName", userByUserIdInter.getUserName());
                }
                map1.put("endTime", r.getEndTime());
                map1.put("markingTime", r.getMarkingTime());
                map1.put("totalScore", r.getTotalScore());
                map1.put("studentId", r.getStudentId());
                map1.put("testPaperId", paperId);
                list.add(map1);
            }
        } else {// 未批阅
            PageHelper.startPage(pageIndex, pageSize);
            Map<String, Object> map = new HashMap<>();
            map.put("testPaperId", paperId);
            map.put("status", "3");// 3：批改中
            map.put("studentIds", userIds);// 用户id
            List<TestResult> results = testResultDao.selectList(map);
            PageInfo<TestResult> pageInfo = new PageInfo<>(results);
            total = pageInfo.getTotal();
            for (TestResult r : results) {
                Map<String, Object> map1 = new HashMap<>();
                SsoUser userByUserIdInter = ssoUserService.getUserByIdInter(r.getStudentId());
                map1.put("studentName", userByUserIdInter.getUserName());
                map1.put("endTime", r.getEndTime());
                map1.put("studentId", r.getStudentId());
                map1.put("testPaperId", paperId);
                list.add(map1);
            }
        }

        return RetResponse.makeRsp(list,total);
    }

    @Override
    public List<TestPaperQuestionResModel> getTestPaperInfo(Integer paperId, Integer studentId,TestPaperFormatLstVo testPaperFormatLstVo) throws Exception {

        List<TestPaperQuestionResModel> testPaperQuestionResModels = testResultDetailService.selectTestPaperInfo(studentId, paperId, 2,testPaperFormatLstVo);
        Map<String, Object> initMap = MapUtils.initMap("studentId", studentId);
        initMap.put("testPaperId", paperId);
        List<TestResult> objects = testResultDao.selectList(initMap);
        if (CommonUtils.listIsEmptyOrNull(objects)) {
            log.info("试卷不匹配");
            throw new ServiceException("试卷不匹配");
        }
        Integer testResultId = objects.get(0).getTestResultId();
        initMap.clear();
        initMap.put("testResultId", testResultId);
        Iterator<TestPaperQuestionResModel> paperIterator = testPaperQuestionResModels.iterator();
        while (paperIterator.hasNext()){
            TestPaperQuestionResModel testPaperQuestionResModel = paperIterator.next();
            QuestionType questionType = questionTypeDao.selectByPrimaryKey(testPaperQuestionResModel.getQuestionType());
            // 带子题的不会有主观题，问过产品了
//            if (0 == questionType.getIsObjective() && "ZONGHE".equals(questionType.getCode())  && "PEIDUI".equals(questionType.getCode())) {// 客观题和不带小题的跳过
            if (0 == questionType.getIsObjective()) {// 客观题跳过
                paperIterator.remove();
                continue;
            }
            List<Map<String, Object>> questionLst = testPaperQuestionResModel.getQuestionLst();
            if(!CommonUtils.listIsEmptyOrNull(questionLst)){
                Iterator<Map<String, Object>> iterator = questionLst.iterator();
                while(iterator.hasNext()){
                    Map<String, Object> stringObjectMap = iterator.next();
                    QuestionType qType = questionTypeDao.selectByPrimaryKey(Integer.parseInt(stringObjectMap.get("questionType").toString()));
                    if (0 == qType.getIsObjective()) {// 客观题跳过
                        iterator.remove();
                        continue;
                    }
                    initMap.put("questionId", stringObjectMap.get("questionId"));
                    List<TestResultDetail> objectList = testResultDetailDao.selectList(initMap);
                    if (!CommonUtils.listIsEmptyOrNull(objectList)) {
                        TestResultDetail resultDetail = objectList.get(0);
                        String giveAnswer = resultDetail.getGiveAnswer();
                        if (!StringUtils.isEmpty(giveAnswer)) {
                            stringObjectMap.put("giveAnswer", giveAnswer);
                        }else{
                            stringObjectMap.put("giveAnswer", "");
                        }
                        Integer score = resultDetail.getScore();
                        if (score != null) {
                            if (score > 0) {
                                stringObjectMap.put("isTrue", true);
                            } else {
                                stringObjectMap.put("isTrue", false);
                            }
                            stringObjectMap.put("getScore", score);
                        }else{
                            stringObjectMap.put("getScore", "");
                        }
                        String comment = resultDetail.getComment();
                        if (!StringUtils.isEmpty(comment)) {
                            stringObjectMap.put("comment", comment);
                        }else{
                            stringObjectMap.put("comment", "");
                        }
                    }else{
                        throw new ServiceException("未查询到学生作答结果！");
                    }
                }
            }
        }
        return testPaperQuestionResModels;
    }

    @Override
    public void saveTeacherCorrect(TeacherPaperResultModel model) throws Exception{

        Integer totalScore = 0;
        Integer zhuGuanScore = 0;
        Integer paperId = model.getPaperId();
        Integer studentId = model.getStudentId();
        List<TeacherCorrectResultModel> resultList = model.getResultList();

        Map<String,Object> map = new HashMap<>();
        map.put("studentId", studentId);
        map.put("testPaperId", paperId);
        List<TestResult> results = testResultDao.selectList(map);
        if(CommonUtils.listIsEmptyOrNull(results)){
            throw new ServiceException("未查询到试卷！");
        }
        TestResult testResult = results.get(0);

        if(!CommonUtils.listIsEmptyOrNull(resultList)){
            for (TeacherCorrectResultModel resultModel : resultList) {
                Integer score = resultModel.getScore();
                if(StringUtils.isEmpty(score)){
                    throw new ServiceException("试题得分不能为空！");
                }
                zhuGuanScore += score;
                Map<String,Object> pmap = MapUtils.initMap("testResultId",testResult.getTestResultId());
                pmap.put("questionId",resultModel.getQuestionId());
                List<TestResultDetail> details = testResultDetailDao.selectList(pmap);
                if(CommonUtils.listIsEmptyOrNull(details)){
                   throw new ServiceException("未查到学生作答结果");
                }
                TestResultDetail detail = details.get(0);
                detail.setComment(StringUtils.isEmpty(resultModel.getComment())?URLEncoder.encode("已批阅","UTF-8"):URLEncoder.encode(resultModel.getComment(),"UTF-8"));
                detail.setScore(score);
                detailDao.updateByPrimaryKeySelective(detail);
            }
        }

        // 更新分数和时间
        totalScore = testResult.getObjectiveScore() + zhuGuanScore;
        testResult.setStatus(4);
        testResult.setMarkingTime(DateUtils.now());
        testResult.setSubjectiveScore(zhuGuanScore);
        testResult.setTotalScore(totalScore);
        testResultDao.updateByPrimaryKeySelective(testResult);

    }

    @Override
    public RetResult getCourseAndTestPaper(String courseName, Integer pageSize, Integer pageIndex, HttpServletRequest request) throws Exception {

        pageSize = StringUtils.isEmpty(pageSize)?10:pageSize;
        pageIndex = StringUtils.isEmpty(pageIndex)?1:pageIndex;
        List<Map<String, Object>> retList = new ArrayList<>();
        int userId = Integer.valueOf(request.getAttribute("userId").toString());
        Map<String,Object> pMap = new HashMap<>();
        pMap.put("name",courseName);
        pMap.put("createUser",userId);
        pMap.put("_sort_line","course_id");
        List<Course> courses = courseDao.selectList(pMap);
        Integer total = courses.size();
        List<Course> pageCourse = new ArrayList<>();

        if(!CommonUtils.listIsEmptyOrNull(courses)){
            // 截取数据
            int endSize =  pageSize * pageIndex;
            int startSize =  endSize - pageSize;
            if(endSize >= total){
                endSize = Math.toIntExact(total);
            }
            pageCourse = courses.subList(startSize,endSize);
            for (Course cours : pageCourse) {
                Map<String,Object> map = new HashMap<>();
                map.put("courseId",cours.getCourseId());
                map.put("type","0");// 0 测试
                List<CourseDetail> testDetail = courseDetailDao.getDetailList(map);
                map.put("type","1");// 1 作业
                List<CourseDetail> workDetail = courseDetailDao.getDetailList(map);
                Map<String,Object> retMap = new HashMap<>();
                retMap.put("homeWorkCount",workDetail.size());
                retMap.put("testCount",testDetail.size());
                retMap.put("courseName",cours.getName());
                retMap.put("courseId",cours.getCourseId());
                retList.add(retMap);
            }
        }else{
            throw new ServiceException("试卷不存在！");
        }

        return makeRsp(retList,total.longValue());
    }
}
