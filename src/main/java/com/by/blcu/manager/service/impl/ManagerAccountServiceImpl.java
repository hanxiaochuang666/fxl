package com.by.blcu.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.manager.common.*;
import com.by.blcu.manager.dao.ManagerAccountMapper;
import com.by.blcu.manager.dao.ManagerOrganizationMapper;
import com.by.blcu.manager.dao.SsoUserMapper;
import com.by.blcu.manager.model.*;
import com.by.blcu.manager.model.sql.InputAccount;
import com.by.blcu.manager.model.sql.OrgPermission;
import com.by.blcu.manager.service.*;
import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.manager.umodel.AccountRoleRModel;
import com.by.blcu.manager.umodel.AccountSearchModel;
import com.by.blcu.manager.umodel.AccountUpdateModel;
import com.by.blcu.manager.umodel.UserSearchModel;
import com.github.pagehelper.PageInfo;
import org.apache.catalina.Manager;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @Description: ManagerAccountService接口实现类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@Service
public class ManagerAccountServiceImpl extends AbstractService<ManagerAccount> implements ManagerAccountService {

    @Resource
    private ManagerAccountMapper managerAccountMapper;

    @Resource
    private SsoUserService ssoUserService;

    @Resource
    private ManagerAccountRoleRService managerAccountRoleRService;

    @Resource
    private ManagerOrganizationService managerOrganizationService;

    @Resource
    private RedisHelper redisHelper;

    //系统日志记录
    @Resource
    private ManagerLogService managerLogService;
    @Resource
    private ManagerOrganizationMapper managerOrganizationMapper;

    public RetResult<List<SsoUser>> findAccountList(String search, UserSessionHelper helper){
        UserSearchModel userSearch =new UserSearchModel();
        userSearch.setUserId(search);
        userSearch.setUserName(search);
        userSearch.setPhone(search);
        userSearch.setEmail(search);
        List<SsoUser> ssoUserRetResult = ssoUserService.selectListOr(userSearch,helper);
        if(ssoUserRetResult==null || ssoUserRetResult.isEmpty()){
            return RetResponse.makeErrRsp("未获取到数据");
        }
        return RetResponse.makeOKRsp(ssoUserRetResult);
    }

    public RetResult<SsoUser> findAccount(String search, UserSessionHelper helper){
        if (StringHelper.IsNullOrWhiteSpace(search)) {
            return RetResponse.makeErrRsp("参数不能为空");
        }
        UserSearchModel userSearch =new UserSearchModel();
        userSearch.setUserId(search);
        userSearch.setUserName(search);
        userSearch.setPhone(search);
        userSearch.setEmail(search);
        List<SsoUser> ssoUserRetResult = ssoUserService.selectListOr(userSearch,helper);
        if(ssoUserRetResult==null || ssoUserRetResult.isEmpty()){
            return RetResponse.makeErrRsp("未获取到数据");
        }
        return RetResponse.makeOKRsp(ssoUserRetResult.get(0));
    }
    public RetResult<Integer> addAccount(AccountUpdateModel user, UserSessionHelper helper){
        if (user==null|| StringUtils.isEmpty(user.getUserId()) ||StringUtils.isEmpty(user.getUserName()) ) {
            return RetResponse.makeErrRsp("[用户Id，用户名]不能为空");
        }
        RetResult<SsoUser> ssoUserRetResult = ssoUserService.selectUserByUserName(user.getUserName(),helper);
        if(ssoUserRetResult.getCode()!=200){
            return RetResponse.makeErrRsp("用户不存在");
        }

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            user.setOrgCode(helper.getOrgCode());
        }

        SsoUser ssoUser =ssoUserRetResult.getData();
        Map<String,String> map =new HashMap<String,String>();
        map.put("orgCode",user.getOrgCode());
        map.put("userName",user.getUserName());
        List<ManagerAccount> listAccount = managerAccountMapper.findAccountEquAnd(map);
        if(listAccount!=null && !listAccount.isEmpty()){
            return RetResponse.makeErrRsp("用户已关联到此机构");
        }
        ManagerAccount accountModel  =(ManagerAccount)user;

        Date datetime = new Date();
        accountModel.setAccountId(ApplicationUtils.getUUID());
        accountModel.setIsDeleted(false);
        if(StringHelper.IsNullOrZero(user.getStatus())){
            accountModel.setStatus(1);
        }
        accountModel.setCreateTime(datetime);
        accountModel.setModifyTime(datetime);
        accountModel.setCreateBy(helper.getUserName());
        accountModel.setModifyBy(helper.getUserName());

        Integer state = managerAccountMapper.insertSelective(accountModel);
        managerLogService.addLogAsync(ManagerLogEnum.Account_Add.getName(), JSON.toJSONString(accountModel),helper);

        return RetResponse.makeOKRsp(state);
    }

    public RetResult<Integer> updateAccount(AccountUpdateModel user, UserSessionHelper helper){
        if (user==null|| StringUtils.isEmpty(user.getAccountId())) {
            return RetResponse.makeErrRsp("[后台用户表Id]不能为空");
        }

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            user.setOrgCode(helper.getOrgCode());
        }

        Map<String,String> map =new HashMap<String,String>();
        map.put("orgCode",user.getOrgCode());
        map.put("accountId",user.getAccountId());
        List<ManagerAccount> listAccount = managerAccountMapper.findAccountEquAnd(map);
        if(listAccount==null || listAccount.isEmpty()){
            return RetResponse.makeErrRsp("用户不存在");
        }
        ManagerAccount accountModel  =listAccount.get(0);
        if(user.getStatus()!=null && user.getStatus()==0){
            user.setStatus(1);
        }
        Date datetime = new Date();
        user.setAccountId(null);
        user.setUserId(null);
        user.setUserName(null);
        user.setOrgCode(null);
        user.setCreateTime(null);
        user.setCreateBy(null);
        user.setModifyTime(datetime);
        user.setModifyBy(helper.getUserName());

        Integer state = managerAccountMapper.updateByPrimaryKeySelective(accountModel);
        managerLogService.addLogAsync(ManagerLogEnum.Account_Modify.getName(), JSON.toJSONString(user),helper);

        return RetResponse.makeOKRsp(state);
    }

    public RetResult<Integer> deleteAccount(InputAccount model, UserSessionHelper helper){
        if(model==null ||  StringHelper.IsNullOrEmpty(model.getAccountIdList())){
            return RetResponse.makeErrRsp("[后台用户Id列表]不能为空");
        }

        Date datetime = new Date();
        InputAccount deleteModel =new InputAccount();
        deleteModel.setModifyTime(datetime);
        deleteModel.setModifyBy(helper.getUserName());
        deleteModel.setAccountIdList(model.getAccountIdList());

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            deleteModel.setOrgCode(helper.getOrgCode());
        }
        Integer state = managerAccountMapper.deleteAccount(deleteModel);
        managerLogService.addLogAsync(ManagerLogEnum.Account_Delete.getName(), JSON.toJSONString(deleteModel),helper);

        return RetResponse.makeOKRsp(state);
    }

    public RetResult<AccountUpdateModel> selectAccountByUserName(String userName,String orgCode, UserSessionHelper helper){
        if (StringHelper.IsNullOrWhiteSpace(userName)||StringHelper.IsNullOrWhiteSpace(orgCode)) {
            return RetResponse.makeErrRsp("[用户账号，所属组织编码]不能为空");
        }

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            orgCode = helper.getOrgCode();
        }

        Map<String,String> map =new HashMap<String,String>();
        map.put("orgCode",orgCode);
        map.put("userName",userName);
        List<ManagerAccount> listAccount = managerAccountMapper.findAccountEquAnd(map);
        if(listAccount==null || listAccount.isEmpty()){
            return RetResponse.makeErrRsp("用户不存在");
        }
        ManagerAccount accountModel  =listAccount.get(0);
        AccountUpdateModel user  = new AccountUpdateModel();
        try {
            ReflexHelper.Copy(accountModel, user);
        }
        catch (Exception ex){

        }
        List<String> userIdList =new ArrayList<>();
        userIdList.add(user.getUserId());
        List<SsoUser> ssoUserList = ssoUserService.selectListByUserIdList(userIdList,helper);
        SsoUser userModel = ssoUserList.get(0);
        user.setRealName(userModel.getRealName());
        user.setPhone(userModel.getPhone());
        return RetResponse.makeOKRsp(user);
    }

    public RetResult<AccountUpdateModel> selectAccountByUserId(String userId,String orgCode, UserSessionHelper helper){
        if (StringHelper.IsNullOrWhiteSpace(userId)||StringHelper.IsNullOrWhiteSpace(orgCode)) {
            return RetResponse.makeErrRsp("[用户Id，所属组织编码]不能为空");
        }
        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            orgCode = helper.getOrgCode();
        }

        Map<String,String> map =new HashMap<String,String>();
        map.put("orgCode",orgCode);
        map.put("userId",userId);
        List<ManagerAccount> listAccount = managerAccountMapper.findAccountEquAnd(map);
        if(listAccount==null || listAccount.isEmpty()){
            return RetResponse.makeErrRsp("用户不存在");
        }
        ManagerAccount accountModel  =listAccount.get(0);
        AccountUpdateModel user  = new AccountUpdateModel();
        try {
            ReflexHelper.Copy(accountModel, user);
        }
        catch (Exception ex){

        }

        List<String> userIdList =new ArrayList<>();
        userIdList.add(userId);
        List<SsoUser> ssoUserList = ssoUserService.selectListByUserIdList(userIdList,helper);
        SsoUser userModel = ssoUserList.get(0);
        user.setRealName(userModel.getRealName());
        user.setPhone(userModel.getPhone());
        return RetResponse.makeOKRsp(user);
    }

    public List<AccountUpdateModel> selectAccountList(AccountSearchModel search, UserSessionHelper helper){
        if (search==null) {
            return null;
        }

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            search.setOrgCode(helper.getOrgCode());
        }

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("status",search.getStatus());
        map.put("type",search.getType());
        map.put("isManager",search.getIsManager());
        map.put("accountId",search.getAccountId());
        map.put("userId",search.getUserId());
        map.put("userName",search.getUserName());
        map.put("orgCode",search.getOrgCode());
        map.put("phone",search.getPhone());
        map.put("realName",search.getRealName());
        List<ManagerAccount> list = managerAccountMapper.findAccountList(map);
        List<AccountUpdateModel> resultData=new ArrayList<AccountUpdateModel>();
        if(!StringHelper.IsNullOrEmpty(list)){
            List<String> userIdList =list.stream().map(t->t.getUserId()).collect(Collectors.toList());
            List<SsoUser> ssoUserList = ssoUserService.selectListByUserIdList(userIdList,helper);
            if(StringHelper.IsNullOrEmpty(ssoUserList)){
                return null;
            }
            List<String> orgCodeList = list.stream().filter(t->t.getOrgCode()!=null).map(t->t.getOrgCode()).distinct().collect(Collectors.toList());
            List<ManagerOrganization> orgList = managerOrganizationService.selectOrganizationNameByCodeList(orgCodeList);

            if(StringHelper.IsNullOrEmpty(orgList)){
                return null;
            }
            list .forEach(t->{
                AccountUpdateModel model=new AccountUpdateModel();
                try{
                    ReflexHelper.Copy(t,model);
                    Optional<SsoUser> userModel = ssoUserList.stream().filter(m->m.getUserId().equals(t.getUserId())).findFirst();
                    if(userModel.isPresent()){
                        SsoUser ssoUserModel = userModel.get();
                        model.setRealName(ssoUserModel.getRealName());
                        model.setPhone(ssoUserModel.getPhone());
                        Optional<ManagerOrganization> orgModelOpt = orgList.stream().filter(n->t.getOrgCode()!=null && n.getOrgCode().equals(t.getOrgCode())).findFirst();
                        if(orgModelOpt.isPresent()){
                            ManagerOrganization orgModel = orgModelOpt.get();
                            model.setOrgName(orgModel.getOrganizationName());
                        }
                        resultData.add(model);
                    }else{
                        resultData.add(model);
                    }
                }
                catch (Exception ex){

                }
            });
        }
         return resultData;
    }
    public Integer selectAccountListCount(AccountSearchModel search, UserSessionHelper helper){
        if (search==null) {
            return null;
        }
        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            search.setOrgCode(helper.getOrgCode());
        }

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("status",search.getStatus());
        map.put("type",search.getType());
        map.put("isManager",search.getIsManager());
        map.put("accountId",search.getAccountId());
        map.put("userId",search.getUserId());
        map.put("userName",search.getUserName());
        map.put("orgCode",search.getOrgCode());
        map.put("phone",search.getPhone());
        map.put("realName",search.getRealName());
        Integer listAccount = managerAccountMapper.findAccountListCount(map);
        return listAccount;
    }
    //endregion

    //region 用户角色

    public RetResult<List<ManagerRole>> getAccountRole(String orgCode,  String userId, UserSessionHelper helper){
        if (StringHelper.IsNullOrWhiteSpace(orgCode)||StringHelper.IsNullOrWhiteSpace(userId)) {
            return RetResponse.makeErrRsp("参数不能为空");
        }

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            orgCode = helper.getOrgCode();
        }

        Map<String,String> map =new HashMap<String,String>();
        map.put("orgCode",orgCode);
        map.put("userId",userId);
        List<ManagerRole> listAccount = managerAccountMapper.findAccountRole(map);
        return RetResponse.makeOKRsp(listAccount);
    }

    public RetResult<List<ManagerRole>> getAccountRoleExclude( String orgCode,String userId, UserSessionHelper helper){
        if (StringHelper.IsNullOrWhiteSpace(orgCode)||StringHelper.IsNullOrWhiteSpace(userId)) {
            return RetResponse.makeErrRsp("参数不能为空");
        }
        //TODO ogrcode
        orgCode = helper.getOrgCode();

        Map<String,String> mapAll =new HashMap<String,String>();
        mapAll.put("orgCode",orgCode);
        List<ManagerRole> listAccountAll = managerAccountMapper.findOrgRole(mapAll);

        Map<String,String> map =new HashMap<String,String>();
        map.put("orgCode",orgCode);
        map.put("userId",userId);
        List<ManagerRole> listAccountHas = managerAccountMapper.findAccountRole(map);

        List<ManagerRole> result=new ArrayList<ManagerRole>();
        if(!StringHelper.IsNullOrEmpty(listAccountAll)){
            if(StringHelper.IsNullOrEmpty(listAccountHas)){
                result =listAccountAll;
            }
            else{
                List<String> has = listAccountHas.stream().map(t->t.getRoleId()).collect(Collectors.toList());
                List<String> except = listAccountAll.stream().map(t->t.getRoleId()).collect(Collectors.toList());
                except.removeAll(has);
                result = listAccountAll.stream().filter(t->except.contains(t.getRoleId())).collect(Collectors.toList());
            }
        }

        return RetResponse.makeOKRsp(result);
    }

    public RetResult<List<ManagerRole>> getRoleAllByOrgCode( String orgCode, UserSessionHelper helper){
        if (StringHelper.IsNullOrWhiteSpace(orgCode)) {
            return RetResponse.makeErrRsp("参数不能为空");
        }

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            orgCode = helper.getOrgCode();
        }

        Map<String,String> map =new HashMap<String,String>();
        map.put("orgCode",orgCode);
        List<ManagerRole> listAccount = managerAccountMapper.findOrgRole(map);
        return RetResponse.makeOKRsp(listAccount);
    }

    @Transactional
    public RetResult<Integer> addAccountRole(AccountRoleRModel accountRoleR, UserSessionHelper helper){
        if (accountRoleR==null || StringHelper.IsNullOrWhiteSpace(accountRoleR.getAccountId()) || StringHelper.IsNullOrWhiteSpace(accountRoleR.getOrgCode()) || StringHelper.IsNullOrEmpty(accountRoleR.getRoleIdList())) {
            return RetResponse.makeErrRsp("[后台用户表Id，所属组织编码，角色Id列表]参数不能为空");
        }

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            accountRoleR.setOrgCode(helper.getOrgCode());
        }

        List<ManagerAccountRoleR> list=new ArrayList<>();
        Date datetime=new Date();
        accountRoleR.getRoleIdList().forEach(t->{
            ManagerAccountRoleR model=new ManagerAccountRoleR();
            model.setAccountRoleRId(ApplicationUtils.getUUID());
            model.setAccountId(accountRoleR.getAccountId());
            model.setRoleId(t);
            model.setCreateTime(datetime);
            model.setModifyTime(datetime);
            model.setCreateBy(helper.getUserName());
            model.setModifyBy(helper.getUserName());

            list.add(model);
        });

        Integer state=0;
        try{
            state = managerAccountMapper.insertAccountRoleList(list);

            //清理缓存
            redisHelper.cleanUserPermission(null);
            redisHelper.cleanUserRole(null);
        }
        catch (Exception ex){
            return RetResponse.makeErrRsp("用户或角色不正确");
        }
        managerLogService.addLogAsync(ManagerLogEnum.Accont_Role_Modify.getName(), "添加："+JSON.toJSONString(accountRoleR),helper);

        return RetResponse.makeOKRsp(state);
    }

    public RetResult<Integer> deleteAccountRole(AccountRoleRModel accountRoleR, UserSessionHelper helper){
        if(accountRoleR==null || StringHelper.IsNullOrWhiteSpace(accountRoleR.getAccountId())){
            return RetResponse.makeErrRsp("参数不能为空");
        }

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            accountRoleR.setOrgCode(helper.getOrgCode());
        }

        Map<String,Object> map =new HashMap<String,Object>();
        map.put("accountId",accountRoleR.getAccountId());
        map.put("roleIdList",accountRoleR.getRoleIdList());

        Integer state=0;
        try{
            state = managerAccountMapper.deleteAccountRole(map);

            //清理缓存
            redisHelper.cleanUserPermission(null);
            redisHelper.cleanUserRole(null);
        }
        catch (Exception ex){
            return RetResponse.makeErrRsp("用户或角色不正确");
        }
        managerLogService.addLogAsync(ManagerLogEnum.Accont_Role_Modify.getName(),"删除："+ JSON.toJSONString(accountRoleR),helper);

        return RetResponse.makeOKRsp(state);
    }

    public RetResult<Integer> updateAccountRole(AccountRoleRModel accountRoleR, UserSessionHelper helper){
        if(accountRoleR==null || StringHelper.IsNullOrWhiteSpace(accountRoleR.getAccountId())){
            return RetResponse.makeErrRsp("[后台用户表Id]不能为空");
        }

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            accountRoleR.setOrgCode(helper.getOrgCode());
        }

        List<String> insertIdList=new ArrayList<String>();
        List<String> deleteIdList=new ArrayList<String>();
        Integer state=0;

        try {
            if (StringHelper.IsNullOrEmpty(accountRoleR.getRoleIdList())) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("accountId", accountRoleR.getAccountId());
                state = managerAccountMapper.deleteAccountRole(map);
            } else {
                Map<String, String> mapHas = new HashMap<String, String>();
                mapHas.put("orgCode", accountRoleR.getOrgCode());
                mapHas.put("accountId", accountRoleR.getAccountId());
                List<ManagerRole> hasRole = managerAccountMapper.findAccountRole(mapHas);
                if (!StringHelper.IsNullOrEmpty(hasRole)) {
                    List<String> hasRoleIds = hasRole.stream().map(t -> t.getRoleId()).distinct().collect(Collectors.toList());
                    List<String> newRoleIds = accountRoleR.getRoleIdList().stream().distinct().collect(Collectors.toList());
                    insertIdList = new ArrayList<String>(newRoleIds);
                    insertIdList.removeAll(hasRoleIds);

                    deleteIdList = new ArrayList<String>(hasRoleIds);
                    deleteIdList.removeAll(newRoleIds);
                } else {
                    insertIdList = accountRoleR.getRoleIdList().stream().distinct().collect(Collectors.toList());
                }

                //添加
                if (!StringHelper.IsNullOrEmpty(insertIdList)) {
                    List<ManagerAccountRoleR> list = new ArrayList<ManagerAccountRoleR>();
                    Date datetime = new Date();
                    insertIdList.forEach(t -> {
                        ManagerAccountRoleR model = new ManagerAccountRoleR();
                        model.setAccountRoleRId(ApplicationUtils.getUUID());
                        model.setRoleId(t);
                        model.setAccountId(accountRoleR.getAccountId());
                        model.setCreateTime(datetime);
                        model.setModifyTime(datetime);
                        model.setCreateBy(helper.getUserName());
                        model.setModifyBy(helper.getUserName());

                        list.add(model);
                    });

                    state = managerAccountMapper.insertAccountRoleList(list);
                }
                //删除
                if (!StringHelper.IsNullOrEmpty(deleteIdList)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("accountId", accountRoleR.getAccountId());
                    map.put("roleIdList", deleteIdList);
                    state = managerAccountMapper.deleteAccountRole(map);
                }
            }
        }
        catch (Exception ex){
            return RetResponse.makeErrRsp("用户或角色不正确");
        }

        //清理缓存
        redisHelper.cleanUserPermission(null);
        redisHelper.cleanUserRole(null);
        managerLogService.addLogAsync(ManagerLogEnum.Accont_Role_Modify.getName(),"修改："+ JSON.toJSONString(accountRoleR),helper);

        return RetResponse.makeOKRsp(state);
    }

    @Override
    public List<ManagerOrgType> verifyOrgCodeByUsername(String userName, String orgCode) throws Exception {
        List<ManagerOrgType> orgCodes = new ArrayList<>();

        //0.获取用户关联机构
        Map param = MapUtils.initMap("userName", userName);
        List<ManagerAccount> listAccount = managerAccountMapper.findAccountEquAnd(param);

        //1.未传入机构
        if(StringUtils.isBlank(orgCode)){
            if(listAccount != null && listAccount.size() > 0){
                //2.用户存在多个机构 返回全部机构
                listAccount.forEach(account->{
                    if(!StringUtils.isBlank(account.getOrgCode())){
                        ManagerOrgType orgType = getOrgTypeByCode(account.getOrgCode());
                        orgCodes.add(orgType);
                    }
                });
            }else{
                //3.用户为自由人无所属机构 查询本部机构
                Map orgParam = MapUtils.initMap("type", 1);
                List<ManagerOrganization> organizations = managerOrganizationMapper.findOrganizationEquAnd(orgParam);
                if(organizations != null && organizations.size() > 0){
                    ManagerOrgType orgType = new ManagerOrgType();
                    orgType.setOrgCode(organizations.get(0).getOrgCode());
                    orgType.setType("1");
                    orgCodes.add(orgType);
                }else{
                    throw new ServiceException("本部机构信息缺失，请联系后台管理员！");
                }
            }

        }else{
            //4.判定用户是否匹配传入机构
            Optional<ManagerAccount> accountModel = listAccount.stream().filter(t->t.getOrgCode().equals(orgCode)).findFirst();
            if(accountModel.isPresent()){
                ManagerAccount managerAccount = accountModel.get();
                ManagerOrgType orgType = getOrgTypeByCode(managerAccount.getOrgCode());
                orgCodes.add(orgType);
            }else{
                throw new ServiceException("传入机构号：" + orgCode +"，与用户【"+userName+"】所属机构不匹配！");
            }

        }

        return orgCodes;
    }

    private ManagerOrgType getOrgTypeByCode(String orgCode){
        ManagerOrgType orgType = new ManagerOrgType();
        orgType.setOrgCode(orgCode);
        orgType.setType("1");
        Map orgParam = MapUtils.initMap("orgCode", orgCode);
        List<ManagerOrganization> organizations = managerOrganizationMapper.findOrganizationEquAnd(orgParam);
        if(organizations != null && organizations.size() > 0){
            orgType.setType(String.valueOf(organizations.get(0).getType()));
        }
        return orgType;
    }

}