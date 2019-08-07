package com.by.blcu.resource.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.resource.dto.TestPaperQuestion;
import com.by.blcu.resource.model.TestPaperQuestionResModel;

import java.util.List;

public interface ITestPaperQuestionService extends IBaseService {
    /**
     * 试卷内容查询
     * @param testPaperId
     * @return
     * @throws Exception
     */
    List<TestPaperQuestionResModel> queryTestPaper(int testPaperId, Integer isNeedAnswer)throws Exception;

    /**
     * 保存试卷内容
     * @param testPaperQuestionLst
     * @return
     */
    RetResult saveTestPaperQuestion(List<TestPaperQuestion> testPaperQuestionLst)throws Exception;

    /**
     * 智能组卷
     * @param testPaperId
     * @param knowledges
     */
    RetResult intellectPaper(int testPaperId,String knowledges)throws Exception;

    /**
     * 根据试卷id生成试卷word
     * @param testPaperId
     * @return String
     */
    String createNewWord(int testPaperId)throws Exception;
}