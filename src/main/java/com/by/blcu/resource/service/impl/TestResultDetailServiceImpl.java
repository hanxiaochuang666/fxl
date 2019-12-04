package com.by.blcu.resource.service.impl;

import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.resource.dao.*;
import com.by.blcu.resource.dto.*;
import com.by.blcu.resource.model.TestPaperAnswerModel;
import com.by.blcu.resource.model.TestPaperAnswerViewModel;
import com.by.blcu.resource.model.TestPaperFormatLstVo;
import com.by.blcu.resource.model.TestPaperQuestionResModel;
import com.by.blcu.resource.service.ILearnActiveService;
import com.by.blcu.resource.service.IResourcesService;
import com.by.blcu.resource.service.ITestPaperQuestionService;
import com.by.blcu.resource.service.ITestResultDetailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.lang.ELArithmetic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.Min;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("testResultDetailService")
@Slf4j
public class TestResultDetailServiceImpl extends BaseServiceImpl implements ITestResultDetailService {
    @Autowired
    private ITestResultDetailDao testResultDetailDao;


    @Autowired
    private ITestResultDao testResultDao;

    @Autowired
    private IQuestionDao questionDao;

    @Autowired
    private IQuestionTypeDao questionTypeDao;

    @Autowired
    private ITestPaperFormatDao testPaperFormatDao;

    @Resource(name="resourcesService")
    private IResourcesService resourcesService;

    @Resource(name="testPaperQuestionService")
    private ITestPaperQuestionService testPaperQuestionService;

    @Resource(name="courseDetailService")
    private ICourseDetailService courseDetailService;

    @Resource(name="learnActiveService")
    private ILearnActiveService learnActiveService;

    @Override
    protected IBaseDao getDao() {
        return this.testResultDetailDao;
    }

    @Override
    public List<TestPaperQuestionResModel> selectTestPaperInfo(int studentId, int testPaperId, int optType,TestPaperFormatLstVo testPaperFormatLstVo) throws Exception {
        Integer numAnswer=0;
        if(optType<2) {
            Date date = new Date();
            List<TestPaperQuestionResModel> testPaperQuestionResModels = testPaperQuestionService.queryTestPaper(testPaperId, 0);
            Map<String, Object> initMap = MapUtils.initMap("studentId", studentId);
            initMap.put("testPaperId",testPaperId);
            List<TestResult> objects = testResultDao.selectList(initMap);
            if(null==objects ||objects.size()<=0){
                log.info("试卷不匹配");
                return null;
            }
            TestResult testResult = objects.get(0);
            Date startTime = testResult.getStartTime();
            Date endTime = testResult.getEndTime();
            TestResult testResult1 = new TestResult();
            if(null==endTime){ //还未提交
                if(null==startTime)
                    testResult1.setStartTime(date);
                else {
                    if(!startTime.before(date))
                        testResult1.setStartTime(date);
                }
            }else{
                if(!date.before(endTime)){
                    if(null==startTime)     //什么情况?
                        testResult1.setStartTime(date);
                    else {
                        if(!startTime.before(date))
                            testResult1.setStartTime(date);
                    }
                }
            }
            testResult1.setTestResultId(testResult.getTestResultId());
            //fix for when date is not earlier than startTime
            if(null != testResult1.getStartTime()){
                testResultDao.updateByPrimaryKeySelective(testResult1);
            }

            if(optType==0)
                return testPaperQuestionResModels;
            Integer testResultId = testResult.getTestResultId();
            initMap.clear();
            initMap.put("testResultId",testResultId.intValue());
            for (TestPaperQuestionResModel testPaperQuestionResModel : testPaperQuestionResModels) {
                List<Map<String, Object>> questionLst = testPaperQuestionResModel.getQuestionLst();
                for (Map<String, Object> stringObjectMap : questionLst) {
                    initMap.put("questionId",stringObjectMap.get("questionId"));
                    List<TestResultDetail> objectList = testResultDetailDao.selectList(initMap);
                    if(null!=objectList && objectList.size()>0){
                        String giveAnswer = objectList.get(0).getGiveAnswer();
                        if(!StringUtils.isEmpty(giveAnswer)) {
                            stringObjectMap.put("giveAnswer",giveAnswer);
                            numAnswer++;
                        }
                    }
                    if(stringObjectMap.containsKey("modelList")){
                        List<Map<String,Object>> temp=(List<Map<String,Object>> )stringObjectMap.get("modelList");
                        boolean isAnswer=false;
                        for (Map<String, Object> objectMap : temp) {
                            initMap.put("questionId",objectMap.get("questionId"));
                            List<TestResultDetail> objectList1 = testResultDetailDao.selectList(initMap);
                            if(null!=objectList1 && objectList1.size()>0){
                                String giveAnswer = objectList1.get(0).getGiveAnswer();
                                if(!StringUtils.isEmpty(giveAnswer)) {
                                    objectMap.put("giveAnswer",giveAnswer);
                                    isAnswer=true;
                                }
                            }
                        }
                        if(isAnswer)
                            numAnswer++;
                    }

                }
            }
            testPaperFormatLstVo.setTotalNum(numAnswer);
            return testPaperQuestionResModels;
        }else {
            List<TestPaperQuestionResModel> testPaperQuestionResModels = testPaperQuestionService.queryTestPaper(testPaperId, 1);
            Map<String, Object> initMap = MapUtils.initMap("studentId", studentId);
            initMap.put("testPaperId",testPaperId);
            List<TestResult> objects = testResultDao.selectList(initMap);
            if(null==objects ||objects.size()<=0){
                log.info("试卷不匹配");
                return null;
            }
            Integer testResultId = objects.get(0).getTestResultId();
            initMap.clear();
            initMap.put("testResultId",testResultId.intValue());
            for (TestPaperQuestionResModel testPaperQuestionResModel : testPaperQuestionResModels) {
                List<Map<String, Object>> questionLst = testPaperQuestionResModel.getQuestionLst();
                for (Map<String, Object> stringObjectMap : questionLst) {
                    initMap.put("questionId",stringObjectMap.get("questionId"));
                    List<TestResultDetail> objectList = testResultDetailDao.selectList(initMap);
                    if(null!=objectList && objectList.size()>0){
                        TestResultDetail testResultDetail = objectList.get(0);
                        String giveAnswer = testResultDetail.getGiveAnswer();
                        Integer score = testResultDetail.getScore();
                        if(!StringUtils.isEmpty(giveAnswer)) {
                            numAnswer++;
                            if(null==score){
                                stringObjectMap.put("isMark", false);
                            } else{
                                stringObjectMap.put("isMark", true);
                                if(score>0)
                                    stringObjectMap.put("isTrue", true);
                                else
                                    stringObjectMap.put("isTrue", false);
                                stringObjectMap.put("getScore", score.intValue());
                            }
                            stringObjectMap.put("giveAnswer", giveAnswer);
                        }else {
                            stringObjectMap.put("getScore", score.intValue());
                            stringObjectMap.put("giveAnswer", "");
                        }
                        String comment = testResultDetail.getComment();
                        if(!StringUtils.isEmpty(comment)){
                            stringObjectMap.put("comment", comment);
                        }else{
                            stringObjectMap.put("comment", "");
                        }
                    }
                    if(stringObjectMap.containsKey("modelList")){
                        List<Map<String,Object>> temp=(List<Map<String,Object>> )stringObjectMap.get("modelList");
                        boolean isAnswer=false;
                        int perTotalScore=0;
                        for (Map<String, Object> objectMap : temp) {
                            initMap.put("questionId",objectMap.get("questionId"));
                            List<TestResultDetail> objectList1 = testResultDetailDao.selectList(initMap);
                            if(null!=objectList1 && objectList1.size()>0){
                                TestResultDetail testResultDetail = objectList1.get(0);
                                String giveAnswer = testResultDetail.getGiveAnswer();
                                Integer score = testResultDetail.getScore();

                                if(!StringUtils.isEmpty(giveAnswer)) {
                                    isAnswer=true;
                                    objectMap.put("giveAnswer",giveAnswer);
                                    if(null==score){
                                        objectMap.put("isMark", false);
                                    } else{
                                        objectMap.put("isMark", true);
                                        if(score>0)
                                            objectMap.put("isTrue", true);
                                        else
                                            objectMap.put("isTrue", false);
                                        objectMap.put("getScore", score.intValue());
                                        perTotalScore+=score.intValue();
                                    }
                                }else {
                                    objectMap.put("getScore", score.intValue());
                                    objectMap.put("giveAnswer", " ");
                                }
                                String comment = testResultDetail.getComment();
                                if(!StringUtils.isEmpty(comment)){
                                    objectMap.put("comment", comment);
                                }else{
                                    objectMap.put("comment", "");
                                }
                            }
                        }
                        stringObjectMap.put("getScore", perTotalScore);
                        if(isAnswer)
                            numAnswer++;
                    }

                }

            }
            testPaperFormatLstVo.setTotalNum(numAnswer);
            return testPaperQuestionResModels;
        }
    }

    @Override
    @Transactional
    public RetResult saveTestResultDetailInfo(int studentId, TestPaperAnswerViewModel testPaperAnswerViewModel) throws Exception{
        Integer testPaperId = testPaperAnswerViewModel.getTestPaperId();
        if(testPaperId==null || testPaperId.intValue()<=0){
            log.info("试卷id不合法");
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"试卷id不合法");
        }
        Integer optFlag = testPaperAnswerViewModel.getOptFlag();
        if(optFlag==null || optFlag.intValue()<0 ||optFlag.intValue()>1) {
            log.info("操作标志不合法");
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"操作标志不合法");
        }
        Map<String, Object> initMap = MapUtils.initMap("testPaperId", testPaperId);
        initMap.put("studentId",studentId);
        List<TestResult> testResultList = testResultDao.selectList(initMap);
        if(null==testResultList || testResultList.size()==0){
            log.info("试卷不存在");
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"试卷不存在");
        }
        Date date = new Date();
        TestResult testResult = testResultList.get(0);
        int testResultId=testResult.getTestResultId();
        Map<String, Object> initMap1 = MapUtils.initMap("testResultId", testResultId);
        List<TestPaperAnswerModel> testPaperAnswerLst = testPaperAnswerViewModel.getTestPaperAnswerLst();
        if(null==testPaperAnswerLst ||testPaperAnswerLst.size()==0)
            return RetResponse.makeOKRsp();
        for (TestPaperAnswerModel testPaperAnswerModel : testPaperAnswerLst) {
            Integer questionId = testPaperAnswerModel.getQuestionId();
            String giveAnswer = testPaperAnswerModel.getGiveAnswer();
            TestResultDetail testResultDetail = new TestResultDetail();
            if(null!=questionId && questionId.intValue()>0)
                testResultDetail.setQuestionId(questionId);
            if(!StringUtils.isEmpty(giveAnswer))
                testResultDetail.setGiveAnswer(giveAnswer);
            testResultDetail.setTestResultId(testResultId);
            initMap1.put("questionId",questionId);
            List<TestResultDetail> testResultDetailLst = getDao().selectList(initMap1);
            if(testResultDetailLst==null || testResultDetailLst.size()<=0)
                getDao().insertSelective(testResultDetail);
            else{
                testResultDetail.setTestResultDetailId(testResultDetailLst.get(0).getTestResultDetailId());
                getDao().updateByPrimaryKeySelective(testResultDetail);
            }
        }
        if(optFlag.intValue()==0) {
            Integer status = testResult.getStatus();
            if(status.intValue()==0) {
                testResult.setStatus(1);
                Date startTime = testResult.getStartTime();
                Integer useTime=testResult.getUseTime();
                if(null!=startTime) {
                    long time = date.getTime();
                    long time1 = startTime.getTime();
                    long s=time-time1;
                    long min=s/(1000 * 60);
                    if(useTime!=null){
                        useTime=useTime+(int)min;
                    }else{
                        useTime=(int)min;
                    }
                }
                testResult.setUseTime(useTime);
                testResult.setEndTime(date);
                testResultDao.updateByPrimaryKeySelective(testResult);
            }
        }else if(optFlag.intValue()==1) {
            //自动批卷客观题
            int correctTestPaper = correctTestPaper(testResult);
            List<TestPaperQuestion> testPagerQuesLst = testPaperQuestionService.selectList(MapUtils.initMap("testPagerId", testResult.getTestPaperId()));
            boolean isAll=true;
            for (TestPaperQuestion testPaperQuestion : testPagerQuesLst) {
                Integer questionId = testPaperQuestion.getQuestionId();
                if( null!=questionId && questionId.intValue()>0) {
                    Question question = questionDao.selectByPrimaryKey(questionId);
                    if(null!=question){
                        QuestionType questionType = questionTypeDao.selectByPrimaryKey(question.getQuestionType());
                        Integer isObjective = questionType.getIsObjective();
                        if(null==isObjective)
                            throw new ServiceException("数据错误");
                        if(1==isObjective.intValue()) {
                            isAll = false;
                            break;
                        }
                    }
                }
            }
            if(!isAll) {
                testResult.setStatus(3);//批阅中
            } else {
                testResult.setMarkingTime(date);
                testResult.setStatus(4);//批阅完成
                testResult.setTotalScore(correctTestPaper);
                testResult.setSubjectiveScore(0);
            }
            Date startTime = testResult.getStartTime();
            Integer useTime=testResult.getUseTime();
            if(null!=startTime) {
                long time = date.getTime();
                long time1 = startTime.getTime();
                long s=time-time1;
                long min=s/(1000 * 60);
                if(useTime!=null){
                    useTime=useTime+(int)min;
                }else{
                    useTime=(int)min;
                }
            }
            testResult.setUseTime(useTime);
            testResult.setEndTime(date);
            testResultDao.updateByPrimaryKeySelective(testResult);
            //提交作业时，同步学习行为
            Integer courseId = testPaperAnswerViewModel.getCourseId();
            if(null!=courseId){
                Integer testPaperId1 = testResult.getTestPaperId();
                Map<String, Object> initMapss = MapUtils.initMap("maxType", 1);
                initMapss.put("content",testPaperId1+"");
                List<Resources> objects = resourcesService.selectList(initMapss);
                if(null!=objects&& objects.size()>0){
                    Integer resourcesId = objects.get(0).getResourcesId();
                    initMapss.clear();
                    initMapss.put("courseId",courseId);
                    initMapss.put("resourcesId",resourcesId);
                    List<CourseDetail> objectList = courseDetailService.selectList(initMapss);
                    if(null!=objectList && objectList.size()>0){
                        for (CourseDetail courseDetail : objectList) {
                            Integer courseDetailId = courseDetail.getCourseDetailId();
                            initMapss.clear();
                            initMapss.put("courseId",courseId);
                            initMapss.put("studentId",studentId);
                            initMapss.put("courseDetailId",courseDetailId);
                            List<LearnActive> objects1 = learnActiveService.selectList(initMapss);
                            if(null!=objects1&&objects1.size()>0){
                                LearnActive learnActive = objects1.get(0);
                                learnActive.setLearnFlag(1);
                                learnActive.setLearnTime(date);
                                learnActiveService.updateByPrimaryKeySelective(learnActive);
                            }
                        }
                    }
                }
            }
        }
        return RetResponse.makeOKRsp();
    }
    /**
     * 自动批阅客观题
     *
     */
    private int  correctTestPaper(TestResult testResult) throws Exception {
        Integer testResultId = testResult.getTestResultId();
        Map<String, Object> initMap = MapUtils.initMap("testResultId", testResultId);
        List<TestResultDetail> testResultDetailLst = getDao().selectList(initMap);
        int totalScore=0;
        for (TestResultDetail testResultDetail : testResultDetailLst) {
            Integer questionId = testResultDetail.getQuestionId();
            Question que = questionDao.selectByPrimaryKey(questionId);
            Integer questionType = que.getQuestionType();
            int intValue = questionType.intValue();
            if(StringUtils.isEmpty(testResultDetail.getGiveAnswer())) {
                testResultDetail.setScore(0);
                getDao().updateByPrimaryKeySelective(testResultDetail);
                continue;
            }
            if(intValue>100){
                //附属子题
                if(StringUtils.isEmpty(que.getQuestionAnswer())){
                    /*Integer parentQuestionId = que.getParentQuestionId();
                    Question que1 = questionDao.selectByPrimaryKey(parentQuestionId);
                    Integer questionType1 = que1.getQuestionType();
                    QuestionType queType = questionTypeDao.selectByPrimaryKey(questionType);
                    if (queType.getIsObjective() == 1)
                        continue;
                    initMap.clear();
                    initMap.put("testPaperId", testResult.getTestPaperId());
                    initMap.put("questionType", questionType1);
                    List<TestPaperFormat> testPaperFormatList = testPaperFormatDao.selectList(initMap);
                    TestPaperFormat testPaperFormat = testPaperFormatList.get(0);
                    int intValue1 = testPaperFormat.getQuestionSpec().intValue();
                    String questionOpt = que1.getQuestionOpt();
                    if (!questionOpt.contains(";")) {
                        testResultDetail.setScore(intValue1);
                    } else {
                        String[] split = questionOpt.split(";");
                        int length = split.length;
                        int score = intValue1 / length;
                        int yushu = intValue1 % length;
                        int lastQueId = Integer.valueOf(split[length - 1]);
                        if (lastQueId == que1.getQuestionId().intValue()) {
                            testResultDetail.setScore(score + yushu);
                            totalScore += (score + yushu);
                        } else {
                            testResultDetail.setScore(score);
                            totalScore += score;
                        }
                    }*/
                }else {
                    log.info("试题id为" + questionId + "的标准答案:" + URLDecoder.decode(que.getQuestionAnswer(), "UTF-8"));
                    log.info("学生id:" + testResult.getStudentId() + "给出的试题id为:" + questionId + "的答案:" + URLDecoder.decode(testResultDetail.getGiveAnswer(), "UTF-8"));
                    if (testResultDetail.getGiveAnswer().equals(que.getQuestionAnswer())) {
                        Integer parentQuestionId = que.getParentQuestionId();
                        Question que1 = questionDao.selectByPrimaryKey(parentQuestionId);
                        Integer questionType1 = que1.getQuestionType();

                        QuestionType queType = questionTypeDao.selectByPrimaryKey(questionType-100);
                        if (queType.getIsObjective() == 1)
                            continue;
                        initMap.clear();
                        initMap.put("testPaperId", testResult.getTestPaperId());
                        initMap.put("questionType", questionType1);
                        List<TestPaperFormat> testPaperFormatList = testPaperFormatDao.selectList(initMap);
                        TestPaperFormat testPaperFormat = testPaperFormatList.get(0);
                        int intValue1 = testPaperFormat.getQuestionSpec().intValue();
                        String questionOpt = que1.getQuestionOpt();
                        if (!questionOpt.contains(";")) {
                            testResultDetail.setScore(intValue1);
                        } else {
                            String[] split = questionOpt.split(";");
                            int length = split.length;
                            int score = intValue1 / length;
                            int yushu = intValue1 % length;
                            int lastQueId = Integer.valueOf(split[length - 1]);
                            if (lastQueId == que1.getQuestionId().intValue()) {
                                testResultDetail.setScore(score + yushu);
                                totalScore += (score + yushu);
                            } else {
                                testResultDetail.setScore(score);
                                totalScore += score;
                            }
                        }
                    } else {
                        testResultDetail.setScore(0);
                    }
                }
            }else {
                if(StringUtils.isEmpty(que.getQuestionAnswer())){
                    /*QuestionType queType = questionTypeDao.selectByPrimaryKey(questionType);
                    initMap.clear();
                    initMap.put("testPaperId", testResult.getTestPaperId());
                    initMap.put("questionType", queType.getQuestionTypeId());
                    List<TestPaperFormat> testPaperFormatList = testPaperFormatDao.selectList(initMap);
                    TestPaperFormat testPaperFormat = testPaperFormatList.get(0);
                    testResultDetail.setScore(testPaperFormat.getQuestionSpec());
                    totalScore+=testPaperFormat.getQuestionSpec();*/
                }else {
                    log.info("试题id为" + questionId + "的标准答案:" + URLDecoder.decode(que.getQuestionAnswer(), "UTF-8"));
                    log.info("学生id:" + testResult.getStudentId() + "给出的试题id为:" + questionId + "的答案:" + URLDecoder.decode(testResultDetail.getGiveAnswer(), "UTF-8"));
                    QuestionType queType = questionTypeDao.selectByPrimaryKey(questionType);
                    if (queType.getIsObjective() == 1)
                        continue;
                    if (StringUtils.isEmpty(testResultDetail.getGiveAnswer()) || StringUtils.isEmpty(que.getQuestionAnswer()))
                        continue;
                    if (testResultDetail.getGiveAnswer().equals(que.getQuestionAnswer())) {
                        initMap.clear();
                        initMap.put("testPaperId", testResult.getTestPaperId());
                        initMap.put("questionType", queType.getQuestionTypeId());
                        List<TestPaperFormat> testPaperFormatList = testPaperFormatDao.selectList(initMap);
                        TestPaperFormat testPaperFormat = testPaperFormatList.get(0);
                        testResultDetail.setScore(testPaperFormat.getQuestionSpec());
                        totalScore += testPaperFormat.getQuestionSpec();
                    } else {
                        testResultDetail.setScore(0);
                    }
                }
            }
            getDao().updateByPrimaryKeySelective(testResultDetail);
        }
        testResult.setObjectiveScore(totalScore);
        return totalScore;
    }
}