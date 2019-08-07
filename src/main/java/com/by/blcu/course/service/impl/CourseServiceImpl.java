package com.by.blcu.course.service.impl;

import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.course.dao.ICourseDao;
import com.by.blcu.course.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("courseService")
public class CourseServiceImpl extends BaseServiceImpl implements ICourseService {
    @Autowired
    private ICourseDao courseDao;

    @Override
    protected IBaseDao getDao() {
        return this.courseDao;
    }
}