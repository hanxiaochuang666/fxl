package com.by.blcu.mall.service.impl;

import com.by.blcu.mall.dao.SystemLogMapper;
import com.by.blcu.mall.service.SystemLogService;
import com.by.blcu.mall.model.SystemLog;
import com.by.blcu.core.universal.AbstractService;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.annotation.Resource;

/**
* @Description: SystemLogService接口实现类
* @author 李程
* @date 2019/06/28 09:11
*/
@Service
public class SystemLogServiceImpl extends AbstractService<SystemLog> implements SystemLogService {

    @Resource
    private SystemLogMapper systemLogMapper;
    
    @Override
    public Integer insertByBatch(List<SystemLog> list) {
        return systemLogMapper.insertByBatch(list);
    }

}