package com.by.blcu.manager.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.manager.model.ManagerAccount;
import com.by.blcu.manager.model.ManagerAccountRoleR;
import com.by.blcu.manager.model.ManagerRole;
import com.by.blcu.manager.modelextend.CommonDeleteModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ManagerAccountMapper extends Dao<ManagerAccount> {
    /**
     * 删除 id  in idList
     * @param obj 删除类
     * @return
     */
    Integer deleteAccountById(@Param("dalModel") CommonDeleteModel obj);

    //通用查询类
    List<ManagerAccount> findAccountEquAnd(Map map);
    List<ManagerAccount> findAccountEquOr(Map map);
    List<ManagerAccount> findAccountList(Map map);
    Integer findAccountListCount(Map map);

    //通用删除
    Integer deleteAccount(Map map);

    //角色
    List<ManagerRole> findAccountRole(Map map);
    List<ManagerRole> findOrgRole(Map map);
    List<ManagerRole> findAccountRoleExcept(Map map);
    Integer insertAccountRoleList(List<ManagerAccountRoleR> list);
    Integer deleteAccountRole(Map map);

}