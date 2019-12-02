package com.by.blcu.mall.service.impl;

import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.mall.dao.MallCommodityOrderMapper;
import com.by.blcu.mall.model.MallCommodityOrder;
import com.by.blcu.mall.service.MallCommodityOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @Description: MallCommodityOrderService接口实现类
* @author 李程
* @date 2019/09/09 11:23
*/
@Service
public class MallCommodityOrderServiceImpl extends AbstractService<MallCommodityOrder> implements MallCommodityOrderService {

    @Resource
    private MallCommodityOrderMapper mallCommodityOrderMapper;

}