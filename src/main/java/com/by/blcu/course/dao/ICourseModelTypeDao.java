package com.by.blcu.course.dao;

import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.course.dto.CourseModelType;

import java.util.List;

public interface ICourseModelTypeDao extends IBaseDao {

    List<CourseModelType> getAllModel();

    List<CourseModelType> getModelByCourseId(Integer courseId);
}