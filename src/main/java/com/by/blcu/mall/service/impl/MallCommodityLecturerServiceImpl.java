package com.by.blcu.mall.service.impl;

import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.mall.dao.MallCommodityLecturerMapper;
import com.by.blcu.mall.model.MallCommodityLecturer;
import com.by.blcu.mall.service.MallCommodityLecturerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
* @Description: MallCommodityLecturerService接口实现类
* @author 李程
* @date 2019/09/04 19:07
*/
@Service
public class MallCommodityLecturerServiceImpl extends AbstractService<MallCommodityLecturer> implements MallCommodityLecturerService {

    @Resource
    private MallCommodityLecturerMapper mallCommodityLecturerMapper;

    @Override
    public Integer updateCommodityLecturer(MallCommodityLecturer mallCommodityLecturer) {
        return mallCommodityLecturerMapper.updateCommodityLecturer(mallCommodityLecturer);
    }

    @Override
    public void deleteByCommodityId(String commodityId) {
        mallCommodityLecturerMapper.deleteByCommodityId(commodityId);
    }
}