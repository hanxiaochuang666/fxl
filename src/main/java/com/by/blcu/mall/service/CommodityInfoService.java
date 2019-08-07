package com.by.blcu.mall.service;

import com.by.blcu.mall.model.CommodityInfo;
import com.by.blcu.core.universal.Service;

import java.util.List;

/**
* @Description: CommodityInfoService接口
* @author 李程
* @date 2019/07/29 11:03
*/
public interface CommodityInfoService extends Service<CommodityInfo> {

    List<CommodityInfo> selectAllByCourseName(CommodityInfo commodityInfo);

    Integer updateComStatusById(String commodityId,Integer commodityStatus);

}