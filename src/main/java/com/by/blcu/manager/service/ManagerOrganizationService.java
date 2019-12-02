package com.by.blcu.manager.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.manager.model.ManagerOrganization;
import com.by.blcu.core.universal.Service;
import com.by.blcu.manager.model.ManagerRole;
import com.by.blcu.manager.model.ManagerOrganization;
import com.by.blcu.manager.umodel.RoleSearchModel;
import com.by.blcu.manager.umodel.OrganizationSearchModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @Description: ManagerOrganizationService接口
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
public interface ManagerOrganizationService extends Service<ManagerOrganization> {
    RetResult<Integer> addOrganization(ManagerOrganization managerOrganization);
    RetResult<Integer> updateOrganization(ManagerOrganization managerOrganization);
    RetResult<Integer> deleteByOrganizationIdList(List<String> roleIdList) ;
    RetResult<ManagerOrganization> selectOrganizationByOrganizationId(String roleId);
    List<ManagerOrganization> selectOrganizationList(OrganizationSearchModel search);
    Integer selectOrganizationListCount(OrganizationSearchModel search);

    List<ManagerOrganization> selectOrganizationNameByIdList(List<String> organizationIdList);
    List<ManagerOrganization> selectOrganizationNameByCodeList(List<String> orgCodeList);

    ManagerOrganization selectOrganizationByOrgCode(String orgCode);
}