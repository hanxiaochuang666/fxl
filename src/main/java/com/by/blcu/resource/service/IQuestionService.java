package com.by.blcu.resource.service;

import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.resource.dto.Question;
import com.by.blcu.resource.model.QuestionModel;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IQuestionService extends IBaseService {

    void insertQuestion(QuestionModel model, HttpServletRequest httpServletRequest) throws Exception;

    void importQuestion(Map<String,Object> paraMap, MultipartFile file, HttpServletRequest httpServletRequest) throws Exception;

    void editQuestion(QuestionModel model, HttpServletRequest httpServletRequest) throws Exception;

    List<Map<String,Object>> selectQuestionListCount(HttpServletRequest httpServletRequest, Map<String,Object> map) throws Exception;

    List<Question> selectQuestionList(HttpServletRequest httpServletRequest, Map<String,Object> map) throws Exception;

    void deleteQuestion(HttpServletRequest httpServletRequest,Integer id) throws Exception;

    void deleteQuestionList(HttpServletRequest httpServletRequest,String ids) throws Exception;

}