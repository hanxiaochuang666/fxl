package com.by.blcu.mall.service.impl;

import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.mall.dao.MallMealInfoMapper;
import com.by.blcu.mall.model.MallMealInfo;
import com.by.blcu.mall.service.CommodityInfoService;
import com.by.blcu.mall.service.MallMealInfoService;
import com.by.blcu.mall.vo.ChildCommoditySort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: MallMealInfoService接口实现类
* @author 李程
* @date 2019/10/24 17:23
*/
@Service
public class MallMealInfoServiceImpl extends AbstractService<MallMealInfo> implements MallMealInfoService {

    @Resource
    private MallMealInfoMapper mallMealInfoMapper;

    @Resource
    private MallMealInfoService mallMealInfoService;

    @Resource
    private CommodityInfoService commodityInfoService;

    @Override
    public Integer deleteChildCommodity(String comCommodityId, String commodityId) {
        return mallMealInfoMapper.deleteChildCommodity(comCommodityId,commodityId);
    }

    @Override
    public Integer deleteBycomCommodityId(String comCommodityId) {
        return mallMealInfoMapper.deleteBycomCommodityId(comCommodityId);
    }

    @Override
    @Transactional
    public List<MallMealInfo> updateMallMealInfo(String comCommodityId, List<ChildCommoditySort> list, String mealType) {
        Integer integer = mallMealInfoService.deleteBycomCommodityId(comCommodityId);
        MallMealInfo mallMealInfo = new MallMealInfo();
        MallMealInfo mallMealInfoView = new MallMealInfo();
        mallMealInfo.setComCommodityId(comCommodityId);
        mallMealInfoView.setComCommodityId(comCommodityId);
        if(null != integer && integer > 0){
            for (ChildCommoditySort childCommoditySort : list) {
                mallMealInfo.setId(ApplicationUtils.getUUID());
                mallMealInfo.setCommodityId(childCommoditySort.getCommodityId());
                mallMealInfo.setChildCommoditySort(childCommoditySort.getChildCommoditySort());
                mallMealInfoService.insert(mallMealInfo);
            }
        }
        Integer i = commodityInfoService.updateMealTypeByCommodityId(comCommodityId,mealType);
        if(null == i){
            throw new ServiceException( "更新失败！");
        }
        List<MallMealInfo> select = mallMealInfoService.select(mallMealInfoView);
        return select;
    }

    @Override
    @Transactional
    public List<MallMealInfo> insertMallMealInfo(String comCommodityId, List<ChildCommoditySort> list, String mealType) {
        MallMealInfo mallMealInfo = new MallMealInfo();
        MallMealInfo mallMealInfoView = new MallMealInfo();
        mallMealInfo.setComCommodityId(comCommodityId);
        mallMealInfoView.setComCommodityId(comCommodityId);
        for (ChildCommoditySort childCommoditySort : list) {
            mallMealInfo.setId(ApplicationUtils.getUUID());
            mallMealInfo.setCommodityId(childCommoditySort.getCommodityId());
            mallMealInfo.setChildCommoditySort(childCommoditySort.getChildCommoditySort());
            mallMealInfoService.insert(mallMealInfo);
        }
        Integer i = commodityInfoService.updateMealTypeByCommodityId(comCommodityId,mealType);
        if(null == i){
            throw new ServiceException( "更新失败！");
        }
        List<MallMealInfo> select = mallMealInfoService.select(mallMealInfoView);
        return select;
    }

    @Override
    public List<MallMealInfo> selectByComCommodityId(String commodityId) {
        return mallMealInfoMapper.selectByComCommodityId(commodityId);
    }

    @Override
    public MallMealInfo moveUpChildCommodity(Integer childCommoditySort, String comCommodityId) {
        return mallMealInfoMapper.moveUpChildCommodity(childCommoditySort,comCommodityId);
    }

    @Override
    public MallMealInfo moveDownChildCommodity(Integer childCommoditySort, String comCommodityId) {
        return mallMealInfoMapper.moveDownChildCommodity(childCommoditySort,comCommodityId);
    }


}