package com.by.blcu.mall.service;

import com.by.blcu.core.universal.Service;
import com.by.blcu.mall.model.MallPurchaseCommodity;

import java.util.List;

/**
* @Description: MallPurchaseCommodityService接口
* @author 李程
* @date 2019/11/05 18:43
*/
public interface MallPurchaseCommodityService extends Service<MallPurchaseCommodity> {

    List<MallPurchaseCommodity> selectByChildCommoditySort(String orderId,String commodityId);

}