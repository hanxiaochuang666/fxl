package com.by.blcu.resource.dao;

import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.resource.dto.Question;
import com.by.blcu.resource.model.QuestionTypeCountModel;

import java.util.List;
import java.util.Map;

public interface IQuestionDao extends IBaseDao {

    List<QuestionTypeCountModel> queryQuestionTypeCount(List<Integer> questionIds);

    List<Question> selectListByPoints(Map<String,Object> map);

    List<Map<String,Object>> queryQuestionListCount(Map<String,Object> map);

    List<Question> selectListByCourseAndType(Map<String,Object> map);
}