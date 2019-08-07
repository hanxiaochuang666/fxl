package com.by.blcu.resource.service.impl;

import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.resource.dao.IDiscussDao;
import com.by.blcu.resource.service.IDiscussService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("discussService")
public class DiscussServiceImpl extends BaseServiceImpl implements IDiscussService {
    @Autowired
    private IDiscussDao discussDao;

    @Override
    protected IBaseDao getDao() {
        return this.discussDao;
    }
}