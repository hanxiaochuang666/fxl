package com.by.blcu.resource.dao;

import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.mall.vo.CommodityInfoFileVo;
import com.by.blcu.resource.dto.MyFavorite;

import java.util.List;
import java.util.Map;

public interface IMyFavoriteDao extends IBaseDao {

    public List<String> selectCommdityNameListFromMyFavorite(Map<String, Object> map);

}