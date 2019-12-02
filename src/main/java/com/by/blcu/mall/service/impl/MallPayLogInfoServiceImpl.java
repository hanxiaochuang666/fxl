package com.by.blcu.mall.service.impl;

import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.mall.dao.MallPayLogInfoMapper;
import com.by.blcu.mall.model.MallPayLogInfo;
import com.by.blcu.mall.service.MallPayLogInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @Description: MallPayLogInfoService接口实现类
* @author 李程
* @date 2019/10/08 19:44
*/
@Service
public class MallPayLogInfoServiceImpl extends AbstractService<MallPayLogInfo> implements MallPayLogInfoService {

    @Resource
    private MallPayLogInfoMapper mallPayLogInfoMapper;

}