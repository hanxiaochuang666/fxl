package com.by.blcu.manager.service.impl;

import com.by.blcu.manager.dao.ManagerAccountRoleRMapper;
import com.by.blcu.manager.model.ManagerAccountRoleR;
import com.by.blcu.manager.service.ManagerAccountRoleRService;
import com.by.blcu.core.universal.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @Description: ManagerAccountRoleRService接口实现类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@Service
public class ManagerAccountRoleRServiceImpl extends AbstractService<ManagerAccountRoleR> implements ManagerAccountRoleRService {

    @Resource
    private ManagerAccountRoleRMapper managerAccountRoleRMapper;

}