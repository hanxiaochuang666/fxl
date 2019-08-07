package com.by.blcu.course.service.impl;

import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.course.dao.ICourseModelRelationDao;
import com.by.blcu.course.service.ICourseModelRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("courseModelRelationService")
public class CourseModelRelationServiceImpl extends BaseServiceImpl implements ICourseModelRelationService {
    @Autowired
    private ICourseModelRelationDao courseModelRelationDao;

    @Override
    protected IBaseDao getDao() {
        return this.courseModelRelationDao;
    }
}