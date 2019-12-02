package com.by.blcu.mall.service;

import com.by.blcu.core.universal.Service;
import com.by.blcu.mall.model.CourseCategoryInfo;
import com.by.blcu.mall.model.MallMealInfo;
import com.by.blcu.mall.vo.ChildCommoditySort;

import java.util.List;

/**
* @Description: MallMealInfoService接口
* @author 李程
* @date 2019/10/24 17:23
*/
public interface MallMealInfoService extends Service<MallMealInfo> {

    Integer deleteChildCommodity(String comCommodityId, String commodityId);

    Integer deleteBycomCommodityId(String comCommodityId);

    List<MallMealInfo> updateMallMealInfo(String comCommodityId, List<ChildCommoditySort> list, String mealType);

    List<MallMealInfo> insertMallMealInfo(String comCommodityId, List<ChildCommoditySort> list, String mealType);

    List<MallMealInfo> selectByComCommodityId(String commodityId);

    MallMealInfo moveUpChildCommodity(Integer childCommoditySort, String comCommodityId);

    MallMealInfo moveDownChildCommodity(Integer childCommoditySort, String comCommodityId);

}