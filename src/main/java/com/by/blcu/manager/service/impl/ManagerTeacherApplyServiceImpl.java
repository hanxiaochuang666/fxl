package com.by.blcu.manager.service.impl;

import com.by.blcu.manager.dao.ManagerTeacherApplyMapper;
import com.by.blcu.manager.model.ManagerTeacherApply;
import com.by.blcu.manager.service.ManagerTeacherApplyService;
import com.by.blcu.core.universal.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @Description: ManagerTeacherApplyService接口实现类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@Service
public class ManagerTeacherApplyServiceImpl extends AbstractService<ManagerTeacherApply> implements ManagerTeacherApplyService {

    @Resource
    private ManagerTeacherApplyMapper managerTeacherApplyMapper;

}