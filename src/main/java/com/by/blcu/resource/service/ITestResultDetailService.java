package com.by.blcu.resource.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.resource.model.TestPaperAnswerViewModel;
import com.by.blcu.resource.model.TestPaperQuestionResModel;

import java.util.List;

public interface ITestResultDetailService extends IBaseService {
    /**
     * 学生试卷操作查询
     * @param studentId
     * @param testPaperId
     * @param optType
     * @return
     */
    List<TestPaperQuestionResModel> selectTestPaperInfo(int studentId, int testPaperId, int optType)throws Exception;

    /**
     * 学生提交试卷
     * @param studentId
     * @param testPaperAnswerViewModel
     * @return
     */
    RetResult saveTestResultDetailInfo(int studentId, TestPaperAnswerViewModel testPaperAnswerViewModel);
}