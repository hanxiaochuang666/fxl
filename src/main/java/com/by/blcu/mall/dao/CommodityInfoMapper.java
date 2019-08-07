package com.by.blcu.mall.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.mall.model.CommodityInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommodityInfoMapper extends Dao<CommodityInfo> {
    List<CommodityInfo> selectAllByCourseName(CommodityInfo commodityInfo);

    Integer updateComStatusById(@Param("commodityId")String commodityId, @Param("commodityStatus")Integer commodityStatus);

}