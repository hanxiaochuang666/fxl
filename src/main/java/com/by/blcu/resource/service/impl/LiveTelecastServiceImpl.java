package com.by.blcu.resource.service.impl;

import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.resource.dao.ILiveTelecastDao;
import com.by.blcu.resource.service.ILiveTelecastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("liveTelecastService")
public class LiveTelecastServiceImpl extends BaseServiceImpl implements ILiveTelecastService {
    @Autowired
    private ILiveTelecastDao liveTelecastDao;

    @Override
    protected IBaseDao getDao() {
        return this.liveTelecastDao;
    }
}