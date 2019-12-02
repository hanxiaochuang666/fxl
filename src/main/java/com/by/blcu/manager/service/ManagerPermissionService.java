package com.by.blcu.manager.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.ManagerAccount;
import com.by.blcu.manager.model.ManagerPermission;
import com.by.blcu.core.universal.Service;
import com.by.blcu.manager.model.extend.PermissionMenu;
import com.by.blcu.manager.model.extend.PermissionTree;
import com.by.blcu.manager.model.sql.InputPermission;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
* @Description: ManagerPermissionService接口
* @author 耿鹤闯
* @date 2019/08/05 14:37
 */
public interface ManagerPermissionService extends Service<ManagerPermission> {

    RetResult<Integer> addPermission(ManagerPermission managerPermission, UserSessionHelper helper);
    RetResult<Integer> updatePermission(ManagerPermission managerPermission, UserSessionHelper helper);
    RetResult<Integer> deleteByPermissionIdList(List<String> permissionIdList, UserSessionHelper helper);
    RetResult<ManagerPermission> selectPermissionByPermissionId(String permissionId);

    //后台操作
    List<PermissionTree> selectPermissionTree(String orgCode,Integer status);

    //后台菜单
    List<PermissionMenu> selectManagerTree(String orgCode,String useName);
}