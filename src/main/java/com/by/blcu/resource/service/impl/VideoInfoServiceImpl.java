package com.by.blcu.resource.service.impl;

import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.resource.dao.IVideoInfoDao;
import com.by.blcu.resource.service.IVideoInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("videoInfoService")
public class VideoInfoServiceImpl extends BaseServiceImpl implements IVideoInfoService {
    @Autowired
    private IVideoInfoDao videoInfoDao;

    @Override
    protected IBaseDao getDao() {
        return this.videoInfoDao;
    }
}