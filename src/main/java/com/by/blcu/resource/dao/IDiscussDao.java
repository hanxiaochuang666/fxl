package com.by.blcu.resource.dao;

import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.resource.dto.Discuss;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.model.DiscussModel;

import java.util.List;
import java.util.Map;

public interface IDiscussDao extends IBaseDao {

    List<DiscussModel> queryDiscussList(Map<String,Object> paraMap);
}