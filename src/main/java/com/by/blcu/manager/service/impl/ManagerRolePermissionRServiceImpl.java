package com.by.blcu.manager.service.impl;

import com.by.blcu.manager.dao.ManagerRolePermissionRMapper;
import com.by.blcu.manager.model.ManagerRolePermissionR;
import com.by.blcu.manager.service.ManagerRolePermissionRService;
import com.by.blcu.core.universal.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @Description: ManagerRolePermissionRService接口实现类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@Service
public class ManagerRolePermissionRServiceImpl extends AbstractService<ManagerRolePermissionR> implements ManagerRolePermissionRService {

    @Resource
    private ManagerRolePermissionRMapper managerRolePermissionRMapper;

}