package com.by.blcu.course.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.course.dto.TaskModel;
import com.by.blcu.course.model.TaskViewModel;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.resource.dto.TestPaper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ICourseDetailService extends IBaseService {

    List<TaskModel> getTaskList(TaskViewModel taskViewModel) throws Exception;

    Integer addTask(TaskViewModel taskViewModel, HttpServletRequest request, CourseCheckModel courseCheckModel) throws Exception;

    Integer deleteTask(TaskViewModel taskViewModel, HttpServletRequest request) throws Exception;

    List<TestPaper> getTestPaperList(TaskViewModel taskViewModel, Integer useType, HttpServletRequest request) throws Exception;

}