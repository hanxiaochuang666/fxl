package com.by.blcu.mall.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.mall.model.MallMealInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallMealInfoMapper extends Dao<MallMealInfo> {
    Integer deleteBycomCommodityId(String comCommodityId);

    List<MallMealInfo> selectByComCommodityId(@Param("comCommodityId")String comCommodityId);

    MallMealInfo moveUpChildCommodity(@Param("childCommoditySort")Integer childCommoditySort, @Param("comCommodityId")String comCommodityId);

    MallMealInfo moveDownChildCommodity(@Param("childCommoditySort")Integer childCommoditySort, @Param("comCommodityId")String comCommodityId);

    Integer deleteChildCommodity(@Param("comCommodityId")String comCommodityId, @Param("commodityId")String commodityId);

}