package com.by.blcu.manager.service.impl;

import com.by.blcu.manager.dao.ManagerTeacherExpMapper;
import com.by.blcu.manager.model.ManagerTeacherExp;
import com.by.blcu.manager.service.ManagerTeacherExpService;
import com.by.blcu.core.universal.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @Description: ManagerTeacherExpService接口实现类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@Service
public class ManagerTeacherExpServiceImpl extends AbstractService<ManagerTeacherExp> implements ManagerTeacherExpService {

    @Resource
    private ManagerTeacherExpMapper managerTeacherExpMapper;

}