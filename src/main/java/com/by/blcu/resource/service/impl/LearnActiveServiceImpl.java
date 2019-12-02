package com.by.blcu.resource.service.impl;

import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.resource.dao.ILearnActiveDao;
import com.by.blcu.resource.dto.LearnActive;
import com.by.blcu.resource.service.ILearnActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("learnActiveService")
public class LearnActiveServiceImpl extends BaseServiceImpl implements ILearnActiveService {
    @Autowired
    private ILearnActiveDao learnActiveDao;

    @Override
    protected IBaseDao getDao() {
        return this.learnActiveDao;
    }

    @Override
    public long insertNoExists(LearnActive learnActive) {
        return learnActiveDao.insertNoExists(learnActive);
    }
}