package com.by.blcu.mall.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.mall.model.MallCommodityLecturer;
import org.apache.ibatis.annotations.Param;

public interface MallCommodityLecturerMapper extends Dao<MallCommodityLecturer> {

    Integer updateCommodityLecturer(MallCommodityLecturer mallCommodityLecturer);

    void deleteByCommodityId(@Param("commodityId")String commodityId);

}