package com.by.blcu.resource.service;

import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.resource.model.QuestionCountModel;
import com.by.blcu.resource.model.QuestionModel;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IQuestionService extends IBaseService {

    Integer insertQuestion(QuestionModel model, HttpServletRequest httpServletRequest) throws Exception;

    void importQuestion(Map<String,Object> paraMap, MultipartFile file, HttpServletRequest httpServletRequest) throws Exception;

    void editQuestion(QuestionModel model, HttpServletRequest httpServletRequest, CourseCheckModel courseCheckModel) throws Exception;

    List<QuestionCountModel> selectQuestionListCount(HttpServletRequest httpServletRequest, Map<String,Object> map) throws Exception;

    Map selectQuestionList(HttpServletRequest httpServletRequest, Map<String,Object> map) throws Exception;

    QuestionModel selectQuestion(Integer questionId) throws Exception;

    void deleteQuestion(HttpServletRequest httpServletRequest,Integer id,Integer fatherId) throws Exception;

    void deleteQuestionList(HttpServletRequest httpServletRequest,String ids) throws Exception;

}