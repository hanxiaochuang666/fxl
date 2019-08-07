package com.by.blcu.resource.service.impl;

import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.resource.dao.*;
import com.by.blcu.resource.dto.*;
import com.by.blcu.resource.model.TestPaperAnswerModel;
import com.by.blcu.resource.model.TestPaperAnswerViewModel;
import com.by.blcu.resource.model.TestPaperQuestionResModel;
import com.by.blcu.resource.service.ITestPaperQuestionService;
import com.by.blcu.resource.service.ITestResultDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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

    @Resource(name="testPaperQuestionService")
    private ITestPaperQuestionService testPaperQuestionService;

    @Override
    protected IBaseDao getDao() {
        return this.testResultDetailDao;
    }

    @Override
    public List<TestPaperQuestionResModel> selectTestPaperInfo(int studentId, int testPaperId, int optType) throws Exception {
        if(optType<2) {
            List<TestPaperQuestionResModel> testPaperQuestionResModels = testPaperQuestionService.queryTestPaper(testPaperId, 0);
            if(optType==0)
                return testPaperQuestionResModels;
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
                        String giveAnswer = objectList.get(0).getGiveAnswer();
                        if(!StringUtils.isEmpty(giveAnswer)) {
                            stringObjectMap.put("giveAnswer",giveAnswer);
                        }
                    }

                }
            }
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
                        if(!StringUtils.isEmpty(giveAnswer))
                            stringObjectMap.put("giveAnswer",giveAnswer);

                        Integer score = testResultDetail.getScore();
                        if(score!=null) {
                            if(score>0)
                                stringObjectMap.put("isTrue", true);
                            else
                                stringObjectMap.put("isTrue", false);
                            stringObjectMap.put("score", score.intValue());
                        }
                        String comment = testResultDetail.getComment();
                        if(StringUtils.isEmpty(comment)){
                            stringObjectMap.put("comment", comment);
                        }
                    }

                }
            }
            return testPaperQuestionResModels;
        }
    }

    @Override
    @Transactional
    public RetResult saveTestResultDetailInfo(int studentId, TestPaperAnswerViewModel testPaperAnswerViewModel) {
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
                testResult.setStartTime(new Date());
                testResultDao.updateByPrimaryKeySelective(testResult);
            }
        }else if(optFlag.intValue()==1) {
            testResult.setEndTime(new Date());
            testResult.setStatus(3);
            //自动批卷客观题
            correctTestPaper(testResult);
            testResultDao.updateByPrimaryKeySelective(testResult);

        }
        return RetResponse.makeOKRsp();
    }
    /**
     * 自动批阅主观题
     *
     */
    private void correctTestPaper(TestResult testResult){
        Integer testResultId = testResult.getTestResultId();
        Map<String, Object> initMap = MapUtils.initMap("testResultId", testResultId);
        List<TestResultDetail> testResultDetailLst = getDao().selectList(initMap);
        int totalScore=0;
        for (TestResultDetail testResultDetail : testResultDetailLst) {
            Integer questionId = testResultDetail.getQuestionId();
            Question que = questionDao.selectByPrimaryKey(questionId);
            Integer questionType = que.getQuestionType();
            int intValue = questionType.intValue();
            if(intValue>100){
                //附属子题
                if (testResultDetail.getGiveAnswer().equals(que.getQuestionAnswer())) {
                    Integer parentQuestionId = que.getParentQuestionId();
                    Question que1= questionDao.selectByPrimaryKey(parentQuestionId);
                    Integer questionType1 = que1.getQuestionType();
                    QuestionType queType = questionTypeDao.selectByPrimaryKey(questionType);
                    if (queType.getIsObjective()==1)
                        continue;
                    initMap.clear();
                    initMap.put("testPaperid", testResult.getTestPaperId());
                    initMap.put("questionType", questionType1);
                    List<TestPaperFormat> testPaperFormatList = testPaperFormatDao.selectList(initMap);
                    TestPaperFormat testPaperFormat = testPaperFormatList.get(0);
                    int intValue1 = testPaperFormat.getQuestionSpec().intValue();
                    String questionOpt = que1.getQuestionOpt();
                    if(!questionOpt.contains(";")){
                        testResultDetail.setScore(intValue1);
                    }else {
                        String[] split = questionOpt.split(";");
                        int length = split.length;
                        int score=intValue1/length;
                        int yushu=intValue1%length;
                        int lastQueId=Integer.valueOf(split[length-1]);
                        if(lastQueId==que1.getQuestionId().intValue()) {
                            testResultDetail.setScore(score + yushu);
                            totalScore+=(score + yushu);
                        }
                        else {
                            testResultDetail.setScore(score);
                            totalScore+=score;
                        }
                    }
                }else {
                    testResultDetail.setScore(0);
                }


            }else {
                QuestionType queType = questionTypeDao.selectByPrimaryKey(questionType);
                if (queType.getIsObjective()==1)
                    continue;
                if (testResultDetail.getGiveAnswer().equals(que.getQuestionAnswer())) {
                    initMap.clear();
                    initMap.put("testPaperid", testResult.getTestPaperId());
                    initMap.put("questionType", queType.getQuestionTypeId());
                    List<TestPaperFormat> testPaperFormatList = testPaperFormatDao.selectList(initMap);
                    TestPaperFormat testPaperFormat = testPaperFormatList.get(0);
                    testResultDetail.setScore(testPaperFormat.getQuestionSpec());
                    totalScore+=testPaperFormat.getQuestionSpec();
                } else {
                    testResultDetail.setScore(0);
                }
            }
            getDao().updateByPrimaryKeySelective(testResultDetail);
        }
        testResult.setObjectiveScore(totalScore);
    }
}