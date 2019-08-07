package com.by.blcu.course.service.impl;

import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.course.dao.ICourseDetailDao;
import com.by.blcu.course.service.ICourseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("courseDetailService")
public class CourseDetailServiceImpl extends BaseServiceImpl implements ICourseDetailService {
    @Autowired
    private ICourseDetailDao courseDetailDao;

    @Override
    protected IBaseDao getDao() {
        return this.courseDetailDao;
    }
}