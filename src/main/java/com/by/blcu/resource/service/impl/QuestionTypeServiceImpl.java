package com.by.blcu.resource.service.impl;

import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.resource.dao.IQuestionTypeDao;
import com.by.blcu.resource.service.IQuestionTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("questionTypeService")
public class QuestionTypeServiceImpl extends BaseServiceImpl implements IQuestionTypeService {
    @Autowired
    private IQuestionTypeDao questionTypeDao;

    @Override
    protected IBaseDao getDao() {
        return this.questionTypeDao;
    }
}