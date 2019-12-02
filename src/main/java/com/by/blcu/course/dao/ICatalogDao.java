package com.by.blcu.course.dao;

import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.course.dto.Catalog;

import java.util.List;
import java.util.Map;

public interface ICatalogDao extends IBaseDao {

    List<Catalog> selectChildNode(Map<String,Object> map);

//    List<Map<String,Object>> selectResourceAndCheckResult(Map<String,Object> map);
}