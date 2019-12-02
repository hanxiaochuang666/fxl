package com.by.blcu.mall.service;

import com.by.blcu.core.universal.Service;
import com.by.blcu.mall.model.MallCommodityLecturer;

/**
* @Description: MallCommodityLecturerService接口
* @author 李程
* @date 2019/09/04 19:07
*/
public interface MallCommodityLecturerService extends Service<MallCommodityLecturer> {

    Integer updateCommodityLecturer(MallCommodityLecturer mallCommodityLecturer);

    void deleteByCommodityId(String commodityId);
}