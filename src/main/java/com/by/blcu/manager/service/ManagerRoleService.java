package com.by.blcu.manager.service;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.ManagerPermission;
import com.by.blcu.manager.model.ManagerRole;
import com.by.blcu.core.universal.Service;
import com.by.blcu.manager.model.sql.OrgPermission;
import com.by.blcu.manager.umodel.RolePermissionRModel;
import com.by.blcu.manager.umodel.RoleSearchModel;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
* @Description: ManagerRoleService接口
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
public interface ManagerRoleService extends Service<ManagerRole> {


    RetResult<Integer> addRole(ManagerRole managerRole, UserSessionHelper helper);
    
    RetResult<Integer> updateRole(ManagerRole managerRole, UserSessionHelper helper);

    RetResult<Integer> deleteByRoleIdList(List<String> roleIdList, UserSessionHelper helper) ;

    RetResult<ManagerRole> selectRoleByRoleId(String roleId, UserSessionHelper helper);

    List<ManagerRole> selectRoleList(RoleSearchModel search, UserSessionHelper helper);
    Integer selectRoleListCount(RoleSearchModel search, UserSessionHelper helper);

    //endregion

    //region 给角色添加权限

    List<OrgPermission> getRolePermission(String orgCode,String roleId, UserSessionHelper helper);

    List<OrgPermission> getAccountRoleExclude(String orgCode, String roleId,String userId, UserSessionHelper helper);

    List<OrgPermission> getAccountRolePermission(String orgCode, String roleId,String userId, UserSessionHelper helper);

    List<OrgPermission> getPermissionAllByOrgCode(String orgCode, UserSessionHelper helper);

    RetResult<Integer> addRolePermission(RolePermissionRModel accountRoleR, UserSessionHelper helper);

    RetResult<Integer> deleteRolePermission(RolePermissionRModel accountRoleR, UserSessionHelper helper);

    RetResult<Integer> updateRolePermission(RolePermissionRModel accountRoleR, UserSessionHelper helper);

    //endregion

}