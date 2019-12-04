package com.by.blcu.manager.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.manager.model.ManagerOrganization;
import com.by.blcu.manager.model.ManagerOrganization;
import com.by.blcu.manager.model.sql.InputOrg;

import java.util.List;
import java.util.Map;

public interface ManagerOrganizationMapper extends Dao<ManagerOrganization> {
    //基本操作
    List<ManagerOrganization> findOrganizationEquAnd(Map map);
    List<ManagerOrganization> findOrganizationList(Map map);
    Integer findOrganizationListCount(Map map);
    Integer deleteOrganizationById(Map map);

    //添加与修改时判重名与重机构
    List<ManagerOrganization> checkExit(InputOrg input);
}