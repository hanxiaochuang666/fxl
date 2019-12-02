package com.by.blcu.course.dao;

import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.dto.TaskModel;
import com.by.blcu.course.model.TaskViewModel;

import java.util.List;
import java.util.Map;

public interface ICourseDetailDao extends IBaseDao {

    List<TaskModel> getTaskList(TaskViewModel taskViewModel);

    List<String> getIdList(Map<String,Object> map);

    List<CourseDetail> getDetailList(Map<String,Object> map);

}