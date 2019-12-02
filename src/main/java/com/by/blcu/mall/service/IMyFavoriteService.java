package com.by.blcu.mall.service;

import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.mall.vo.CommodityInfoFileVo;

import java.util.List;

public interface IMyFavoriteService extends IBaseService {

    public List<CommodityInfoFileVo> selectMyFavorite(Integer page, Integer size, Integer userId) throws Exception;

    public int addMyFavorite(int userId, String commodityId) throws Exception;

    public int deleteMyFavorite(Integer userId, String commodityId) throws Exception;

}