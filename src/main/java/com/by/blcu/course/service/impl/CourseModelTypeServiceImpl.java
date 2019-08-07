package com.by.blcu.course.service.impl;

import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.course.dao.ICourseModelTypeDao;
import com.by.blcu.course.service.ICourseModelTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("courseModelTypeService")
public class CourseModelTypeServiceImpl extends BaseServiceImpl implements ICourseModelTypeService {
    @Autowired
    private ICourseModelTypeDao courseModelTypeDao;

    @Override
    protected IBaseDao getDao() {
        return this.courseModelTypeDao;
    }
}