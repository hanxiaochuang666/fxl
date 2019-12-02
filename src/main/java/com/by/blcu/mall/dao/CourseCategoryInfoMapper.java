package com.by.blcu.mall.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.mall.model.CourseCategoryInfo;
import com.by.blcu.mall.vo.CourseCategoryInfoVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CourseCategoryInfoMapper extends Dao<CourseCategoryInfo> {
    CourseCategoryInfo moveUp(@Param("ccSort")Integer ccSort,@Param("parentId") String parentId);

    CourseCategoryInfo moveDown(@Param("ccSort")Integer ccSort,@Param("parentId")String parentId);

    Integer selectMaxSortByParentId(String parentId);

    List<CourseCategoryInfo> selectByParentId(String parentId);

    Integer updateNameByCcId(@Param("ccId")String ccId,@Param("ccName")String ccName);

    List<CourseCategoryInfo> selectAllBy();

    Integer updateCcStatusByCcId(@Param("ccId")String ccId);

    String selectCcNameByCcId(@Param("ccId")String ccId);

    List<CourseCategoryInfo> selectList();

    CourseCategoryInfo selectByCcId(String ccId);

    List<CourseCategoryInfo> selectByParentIdAndName(@Param("parentId")String parentId, @Param("ccName")String ccName);

    List<CourseCategoryInfo> selectListRecursionByOrgCode(@Param("orgCode")String orgCode);

    List<CourseCategoryInfo> selectAllByCommodityStatus();

}