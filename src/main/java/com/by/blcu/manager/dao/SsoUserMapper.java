package com.by.blcu.manager.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.manager.model.ManagerPermission;
import com.by.blcu.manager.model.ManagerRole;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.model.sql.OrgPermission;
import com.by.blcu.manager.modelextend.UserDeleteMapperModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SsoUserMapper extends Dao<SsoUser> {
    /**
     * 删除 user_name  in userNameList
     * @param obj 用户删除类
     * @return
     */
    Integer deleteUserByUserIdList(@Param("dalModel") UserDeleteMapperModel obj);

    //内部方法
    List<ManagerRole> getMyRole(Map map);
    List<OrgPermission> getMyPermision(Map map);

    //通用查询
    List<SsoUser> findUserEquAnd(Map map);
    List<SsoUser> findUserEquOr(Map map);
    List<SsoUser> findUserEquOrExist(Map map);
    List<SsoUser> findUserList(Map map);
    Integer findUserListCount(Map map);
    List<SsoUser> findUserListOr(Map map);

}