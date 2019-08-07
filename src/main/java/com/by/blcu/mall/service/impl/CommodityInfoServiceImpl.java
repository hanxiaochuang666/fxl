package com.by.blcu.mall.service.impl;

import com.by.blcu.mall.dao.CommodityInfoMapper;
import com.by.blcu.mall.service.CommodityInfoService;
import com.by.blcu.mall.model.CommodityInfo;
import com.by.blcu.core.universal.AbstractService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: CommodityInfoService接口实现类
* @author 李程
* @date 2019/07/29 11:03
*/
@Service
public class CommodityInfoServiceImpl extends AbstractService<CommodityInfo> implements CommodityInfoService {

    @Resource
    private CommodityInfoMapper commodityInfoMapper;

    @Override
    public List<CommodityInfo> selectAllByCourseName(CommodityInfo commodityInfo) {

        return commodityInfoMapper.selectAllByCourseName(commodityInfo);
    }

    @Override
    public Integer updateComStatusById(String commodityId,Integer commodityStatus) {

        return commodityInfoMapper.updateComStatusById(commodityId,commodityStatus);
    }
}