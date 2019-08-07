package com.by.blcu.mall.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.mall.model.CourseCategoryInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseCategoryInfoMapper extends Dao<CourseCategoryInfo> {
    CourseCategoryInfo moveUp(@Param("ccSort")Integer ccSort,@Param("parentId") String parentId);

    CourseCategoryInfo moveDown(@Param("ccSort")Integer ccSort,@Param("parentId")String parentId);

    Integer selectMaxSortByParentId(String parentId);

    List<CourseCategoryInfo> selectByParentId(String parentId);

    Integer updateNameByCcId(@Param("ccId")String ccId,@Param("ccName")String ccName);

    List<CourseCategoryInfo> selectAllBy();

    Integer updateCcStatusByCcId(@Param("ccId")String ccId);
}