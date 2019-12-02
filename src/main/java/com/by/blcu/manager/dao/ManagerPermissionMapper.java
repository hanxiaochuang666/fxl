package com.by.blcu.manager.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.manager.model.ManagerPermission;
import com.by.blcu.manager.model.extend.PermissionMenu;
import com.by.blcu.manager.model.sql.InputPermission;
import com.by.blcu.manager.model.sql.OrgPermission;

import java.util.List;
import java.util.Map;

public interface ManagerPermissionMapper extends Dao<ManagerPermission> {
    List<ManagerPermission> findPermissionEquAnd(Map map);
    List<ManagerPermission> findPermissionList(Map map);

    //删除
    Integer deletePermissionById(Map map);
    Integer findRExist(InputPermission model);

    //查询父子级别菜单
    List<ManagerPermission> findLevelAndParent(Map map);

    //查询机构用户权限
    List<String> findAccountPermission(InputPermission inputPermission);
}