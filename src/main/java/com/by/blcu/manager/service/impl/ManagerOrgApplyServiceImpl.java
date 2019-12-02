package com.by.blcu.manager.service.impl;

import com.by.blcu.manager.dao.ManagerOrgApplyMapper;
import com.by.blcu.manager.model.ManagerOrgApply;
import com.by.blcu.manager.service.ManagerOrgApplyService;
import com.by.blcu.core.universal.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @Description: ManagerOrgApplyService接口实现类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@Service
public class ManagerOrgApplyServiceImpl extends AbstractService<ManagerOrgApply> implements ManagerOrgApplyService {

    @Resource
    private ManagerOrgApplyMapper managerOrgApplyMapper;

}