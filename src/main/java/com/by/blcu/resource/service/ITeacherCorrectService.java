package com.by.blcu.resource.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.resource.model.TeacherPaperResultModel;
import com.by.blcu.resource.model.TestPaperFormatLstVo;
import com.by.blcu.resource.model.TestPaperQuestionResModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ITeacherCorrectService {

    RetResult getPaperListByCourseId(Integer courseId,String type,String paperName,Integer pageIndex,Integer pageSize) throws Exception;

    RetResult getCorrectList(String paperId, String type,Integer pageIndex,Integer pageSize,String userName) throws Exception;

    List<TestPaperQuestionResModel> getTestPaperInfo(Integer paperId, Integer studentId,TestPaperFormatLstVo testPaperFormatLstVo) throws Exception;

    void saveTeacherCorrect(TeacherPaperResultModel model) throws Exception;

    RetResult getCourseAndTestPaper(String courseName, Integer pageSize, Integer pageIndex, HttpServletRequest request) throws Exception;
}
