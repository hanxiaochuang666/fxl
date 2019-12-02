package com.by.blcu.mall.service;

import com.by.blcu.mall.model.CourseCategoryInfo;
import com.by.blcu.core.universal.Service;
import com.by.blcu.mall.vo.CourseCategoryInfoVo;

import java.util.List;
import java.util.Map;

/**
* @Description: CourseCategoryInfoService接口
* @author 李程
* @date 2019/07/25 15:20
*/
public interface CourseCategoryInfoService extends Service<CourseCategoryInfo> {

    Integer updateUpSort(String ccId);

    Integer updateDownSort(String ccId);

    Integer selectMaxSortByParentId(String parentId);

    List<CourseCategoryInfo> selectByParentId(String parentId);

    List<CourseCategoryInfoVo> selectListRecursion();

    Integer deleteByCcId(String ccId);

    Integer updateNameByCcId(String ccId,String ccName);

    List<CourseCategoryInfo> selectAllBy();

    Integer updateCcStatusByCcId(String ccId);

    String selectCcNameByCcId(String ccId);

    Map<String,String> selectList();

    CourseCategoryInfo selectByCcId(String ccId);

    List<CourseCategoryInfo> selectByParentIdAndName(String parentId,String ccName);

    List<CourseCategoryInfo> selectListRecursionByOrgCode(String orgCode);

    List<CourseCategoryInfo> selectAllByCommodityStatus();

}