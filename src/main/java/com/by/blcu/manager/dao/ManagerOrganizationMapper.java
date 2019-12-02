package com.by.blcu.manager.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.manager.model.ManagerOrganization;
import com.by.blcu.manager.model.ManagerOrganization;

import java.util.List;
import java.util.Map;

public interface ManagerOrganizationMapper extends Dao<ManagerOrganization> {
    //基本操作
    List<ManagerOrganization> findOrganizationEquAnd(Map map);
    List<ManagerOrganization> findOrganizationList(Map map);
    Integer findOrganizationListCount(Map map);
    Integer deleteOrganizationById(Map map);
}