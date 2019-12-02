package com.by.blcu.mall.service.impl;

import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.mall.dao.MallPurchaseCommodityMapper;
import com.by.blcu.mall.model.MallPurchaseCommodity;
import com.by.blcu.mall.service.MallPurchaseCommodityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: MallPurchaseCommodityService接口实现类
* @author 李程
* @date 2019/11/05 18:43
*/
@Service
public class MallPurchaseCommodityServiceImpl extends AbstractService<MallPurchaseCommodity> implements MallPurchaseCommodityService {

    @Resource
    private MallPurchaseCommodityMapper mallPurchaseCommodityMapper;

    @Override
    public List<MallPurchaseCommodity> selectByChildCommoditySort(String orderId,String commodityId) {
        return mallPurchaseCommodityMapper.selectByChildCommoditySort(orderId,commodityId);
    }
}