package com.by.blcu.manager.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.manager.model.ManagerAccountRoleR;
import com.by.blcu.manager.model.ManagerRole;
import com.by.blcu.manager.model.ManagerRolePermissionR;
import com.by.blcu.manager.model.sql.InputRole;
import com.by.blcu.manager.model.sql.OrgPermission;
import com.by.blcu.manager.modelextend.CommonDeleteModel;
import org.apache.catalina.Manager;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ManagerRoleMapper extends Dao<ManagerRole> {

    //基本操作 查询
    List<ManagerRole> findRoleEquAnd(Map map);
    List<ManagerRole> findRoleList(Map map);
    Integer findRoleListCount(Map map);
    //删除
    Integer deleteRoleById(Map map);
    Integer findRExist(InputRole model);

    //权限相关
    List<OrgPermission> findRolePermission(Map map);
    List<OrgPermission> findAccountRolePermission(Map map);
    List<OrgPermission> findOrgPermission(Map map);
    List<OrgPermission> findRolePermissionExcept(Map map);

    Integer insertRolePermissionList(List<ManagerRolePermissionR> list);
    Integer deleteRolePermission(Map map);

}