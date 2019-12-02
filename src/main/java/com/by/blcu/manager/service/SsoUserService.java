package com.by.blcu.manager.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.ManagerPermission;
import com.by.blcu.manager.model.ManagerRole;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.core.universal.Service;
import com.by.blcu.manager.model.extend.PermissionOrgExtend;
import com.by.blcu.manager.model.extend.RoleOrgExtend;
import com.by.blcu.manager.umodel.UserSearchModel;
import org.mapstruct.Mapper;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

/**
 * @author 耿鹤闯
 * @Description: SsoUserService接口
 * @date 2019/08/05 14:37
 */
public interface SsoUserService extends Service<SsoUser> {

    //region 外部

    /**
     * 添加用户
     *
     * @param ssoUser 统一用户表
     * @return 结果
     */
    RetResult<Integer> addUser(SsoUser ssoUser, UserSessionHelper helper);

    /**
     * 修改用户
     *
     * @param ssoUser 统一用户表
     * @return 结果
     */
    RetResult<Integer> updateUserByUserName(SsoUser ssoUser, UserSessionHelper helper);

    /**
     * 删除用户
     *
     * @param userNameList 账号列表
     * @return 结果
     */
    RetResult<Integer> deleteUserByUserNameList(List<String> userNameList, UserSessionHelper helper);

    /**
     * 根据用户名查找用户
     *
     * @param userName
     * @return
     */
    RetResult<SsoUser> selectUserByUserName(String userName, UserSessionHelper helper);

    //endregion

    //region 内部使用 盛荣用

    /**
     * 根据用户账号获取用户信息
     *
     * @param userName
     * @return
     */
    SsoUser getUserByUserNameInter(String userName);

    /**
     * 根据用户Id获取用户信息
     *
     * @param userId
     * @return
     */
    SsoUser getUserByUserIdInter(String userId);

    /**
     * 根据用户表Id获取用户信息
     *
     * @param uid
     * @return
     */
    SsoUser getUserByIdInter(Integer uid);

    /**
     * 根据用户名与组织机构编码获取用户角色
     *
     * @param userName
     * @param orgCode
     * @return
     */
    Set<String> getUserRolesInter(String userName, String orgCode);

    /**
     * 根据用户名获取所有角色
     *
     * @param userName
     * @return
     */
    Set<RoleOrgExtend> getUserRolesInter(String userName);

    /**
     * 根据用户名与组织机构编码获取用户权限
     *
     * @param userName
     * @param orgCode
     * @return
     */
    Set<String> getUserPermissionsInter(String userName, String orgCode);

    /**
     * 根据用户名与组织机构编码获取用户权限
     *
     * @param userName
     * @return
     */
    Set<PermissionOrgExtend> getUserPermissionsInter(String userName);

//endregion

    //region 内部李程用

    /**
     * 根据用户Id列表，查询用户真实姓名
     * @param userIdList
     * @return
     */
    Map selectLectureNameByUserId(Set<String> userIdList);

    //endregion

    //region 前端用

    /**
     * 重置密码
     * @param userName 用户账号
     * @return
     */
    RetResult<Integer> resetPassword(String userName, UserSessionHelper helper);

    /**
     * 根据用户账号获取用户信息
     *
     * @param name 名称。类型相关
     * @param type 1 用户账号；2 UserId；3 表Id; 4 电话; 5 邮箱
     * @return
     */
    RetResult<SsoUser>  getUserByName(String name,Integer type, UserSessionHelper helper);

    /**
     * 查找用户
     *
     * @param name 用户账号/手机号/邮箱
     * @return
     */
    RetResult<SsoUser>  getUserByName(String name, UserSessionHelper helper);

    /**
     * 查找用户
     *
     * @param name 用户账号/手机号/邮箱
     * @return
     */
     RetResult<List<SsoUser>> getUserListByName(String name, UserSessionHelper helper);

    /**
     * 重置密码
     * @param userName 用户账号
     * @return
     */


    //endregion

    //region 检查类

    /**
     *  检查是否存在
     * @param name
     * @param type 1 账号，2 手机号， 3邮箱
     * @return
     */
    RetResult<Boolean> checkExist(String name,Integer type, UserSessionHelper helper);


    //endregion

    //region 内部，不知谁用了

    List<SsoUser> selectListAnd(UserSearchModel search);

    //endregion

    //region 查询类

    List<SsoUser> selectListAnd(UserSearchModel search, UserSessionHelper helper);
    Integer selectListAndCount(UserSearchModel searc, UserSessionHelper helperh);
    List<SsoUser> selectListOr(UserSearchModel search, UserSessionHelper helper);

    List<SsoUser> selectListByUserIdList(List<String> userIdList, UserSessionHelper helper);
    Boolean isTeacher(String userName);
    //endregion

}