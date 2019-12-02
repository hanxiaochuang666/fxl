package com.by.blcu.mall.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.mall.model.MallPurchaseCommodity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallPurchaseCommodityMapper extends Dao<MallPurchaseCommodity> {

    List<MallPurchaseCommodity> selectByChildCommoditySort(@Param("orderId")String orderId, @Param("commodityId")String commodityId);

}