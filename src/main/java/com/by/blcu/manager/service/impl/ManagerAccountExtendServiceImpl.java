package com.by.blcu.manager.service.impl;

import com.by.blcu.manager.dao.ManagerAccountExtendMapper;
import com.by.blcu.manager.model.ManagerAccountExtend;
import com.by.blcu.manager.service.ManagerAccountExtendService;
import com.by.blcu.core.universal.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @Description: ManagerAccountExtendService接口实现类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@Service
public class ManagerAccountExtendServiceImpl extends AbstractService<ManagerAccountExtend> implements ManagerAccountExtendService {

    @Resource
    private ManagerAccountExtendMapper managerAccountExtendMapper;

}