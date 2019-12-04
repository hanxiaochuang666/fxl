package com.by.blcu.manager.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.ManagerAccount;
import com.by.blcu.core.universal.Service;
import com.by.blcu.manager.model.ManagerOrgType;
import com.by.blcu.manager.model.ManagerRole;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.model.sql.InputAccount;
import com.by.blcu.manager.umodel.AccountRoleRModel;
import com.by.blcu.manager.umodel.AccountSearchModel;
import com.by.blcu.manager.umodel.AccountUpdateModel;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* @Description: ManagerAccountService接口
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
public interface ManagerAccountService extends Service<ManagerAccount> {

    RetResult<List<SsoUser>> findAccountList(String search, UserSessionHelper helper);

    RetResult<SsoUser> findAccount(String search, UserSessionHelper helper);

     RetResult<Integer> addAccount(AccountUpdateModel user, UserSessionHelper helper);

     RetResult<Integer> updateAccount(AccountUpdateModel user, UserSessionHelper helper);

     RetResult<Integer> deleteAccount(InputAccount model, UserSessionHelper helper);

     RetResult<AccountUpdateModel> selectAccountByUserName(String userName,String orgCode, UserSessionHelper helper);

     RetResult<AccountUpdateModel> selectAccountByUserId(String userId,String orgCode, UserSessionHelper helper);

     List<AccountUpdateModel> selectAccountList(AccountSearchModel search, UserSessionHelper helper);
     Integer selectAccountListCount(AccountSearchModel search, UserSessionHelper helper);

    //endregion

    //region 用户角色

    RetResult<List<ManagerRole>> getAccountRole(String orgCode, String userId, UserSessionHelper helper);
    RetResult<List<ManagerRole>> getAccountRoleExclude( String orgCode,String userId, UserSessionHelper helper);
    RetResult<List<ManagerRole>> getRoleAllByOrgCode( String orgCode, UserSessionHelper helper);
    RetResult<Integer> addAccountRole(AccountRoleRModel accountRoleR, UserSessionHelper helper);
    RetResult<Integer> deleteAccountRole(AccountRoleRModel accountRoleR, UserSessionHelper helper);
    RetResult<Integer> updateAccountRole(AccountRoleRModel accountRoleR, UserSessionHelper helper);

    List<ManagerOrgType> verifyOrgCodeByUsername(String userName, String orgCode) throws Exception;

}