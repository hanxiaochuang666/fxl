package com.by.blcu.course.dao;

import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.course.dto.Course;

public interface ICourseDao extends IBaseDao {
     int logicDeleteByPrimaryKey(Integer id);
}