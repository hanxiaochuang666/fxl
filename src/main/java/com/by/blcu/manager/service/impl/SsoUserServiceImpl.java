package com.by.blcu.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.MD5Util;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.manager.common.*;
import com.by.blcu.manager.dao.ManagerAccountRoleRMapper;
import com.by.blcu.manager.dao.ManagerRoleMapper;
import com.by.blcu.manager.dao.SsoUserMapper;
import com.by.blcu.manager.model.ManagerAccountRoleR;
import com.by.blcu.manager.model.ManagerOrganization;
import com.by.blcu.manager.model.ManagerRole;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.model.extend.PermissionOrgExtend;
import com.by.blcu.manager.model.extend.RoleOrgExtend;
import com.by.blcu.manager.model.sql.OrgPermission;
import com.by.blcu.manager.modelextend.UserDeleteMapperModel;
import com.by.blcu.manager.service.ManagerLogService;
import com.by.blcu.manager.service.ManagerOrganizationService;
import com.by.blcu.manager.service.SsoUserService;
import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.manager.umodel.OrganizationSearchModel;
import com.by.blcu.manager.umodel.UserSearchModel;
import io.swagger.annotations.ApiParam;
import org.mapstruct.Mapper;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 耿鹤闯
 * @Description: SsoUserService接口实现类
 * @date 2019/08/05 14:37
 */
@Service
public class SsoUserServiceImpl extends AbstractService<SsoUser> implements SsoUserService {

    @Resource
    private SsoUserMapper ssoUserMapper;

    @Resource
    private ManagerRoleMapper managerRoleMapper;

    @Resource
    private ManagerAccountRoleRMapper managerAccountRoleRMapper;

    @Resource
    private ManagerOrganizationService managerOrganizationService;

    @Resource
    private RedisHelper redisHelper;

    //系统日志记录
    @Resource
    private ManagerLogService managerLogService;

    //region 外部
    /**
     * 添加用户
     *
     * @param ssoUser 统一用户表
     * @return 结果
     */
    public RetResult<Integer> addUser(SsoUser ssoUser,UserSessionHelper helper) {
        if (ssoUser == null || StringHelper.isEmpty(ssoUser.getUserName()) || StringHelper.isEmpty(ssoUser.getPassword())) {
            return RetResponse.makeErrRsp("[账号，密码]不能为空");
        }
        if(ssoUser.getUserName().equals(ManagerHelper.UserName)){
            return RetResponse.makeErrRsp("[账号]已存在");
        }

        if(!RegexHelper.CheckPassword(ssoUser.getPassword())){
            return RetResponse.makeErrRsp("[密码]格式不正确，应为6至16位数字字母组合，不能使用纯数字或纯字母");
        }

        Map<String,String> map = new HashMap<String,String>();
        map.put("userName",ssoUser.getUserName());
        if(!StringHelper.IsNullOrWhiteSpace(ssoUser.getPhone())){
            map.put("phone",ssoUser.getPhone());
        }
        if(!StringHelper.IsNullOrWhiteSpace(ssoUser.getEmail())){
            map.put("email",ssoUser.getEmail());
        }
        List<SsoUser> list = ssoUserMapper.findUserEquOr(map);

        if (list != null && !list.isEmpty()) {
            Optional<SsoUser> isExistUserName = list.stream().filter(t -> t.getUserName().equals(ssoUser.getUserName())).findAny();
            if (isExistUserName.isPresent()) {
                return RetResponse.makeErrRsp("[账号]已存在");
            }
            Optional<SsoUser> isExistPhone = list.stream().filter(t -> t.getPhone()!=null && t.getPhone().equals(ssoUser.getPhone())).findAny();
            if (isExistPhone.isPresent()) {
                return RetResponse.makeErrRsp("[手机号]已存在");
            }
            if (!StringUtils.isEmpty(ssoUser.getEmail())) {
                Optional<SsoUser> isExistEmail = list.stream().filter(t -> t.getEmail()!=null && t.getEmail().equals(ssoUser.getEmail())).findAny();
                if (isExistPhone.isPresent()) {
                    return RetResponse.makeErrRsp("[邮箱]已存在");
                }
            }
        }
        Date datetime = new Date();
        ssoUser.setUserId(ApplicationUtils.getUUID());
        String username = org.apache.commons.lang3.StringUtils.lowerCase(ssoUser.getUserName());
        ssoUser.setIsDeleted(false);
        if(StringHelper.IsNullOrZero(ssoUser.getStatus())){
            ssoUser.setStatus(1);
        }
        ssoUser.setPassword(MD5Util.encrypt(username, ssoUser.getPassword()));
        ssoUser.setCreateTime(datetime);
        ssoUser.setModifyTime(datetime);
        ssoUser.setCreateBy(helper.getUserName());
        ssoUser.setModifyBy(helper.getUserName());

        Integer state = ssoUserMapper.insertSelective(ssoUser);
        managerLogService.addLogAsync(ManagerLogEnum.User_Add.getName(), JSON.toJSONString(ssoUser),helper);

        return RetResponse.makeOKRsp(state);
    }

    /**
     * 修改用户
     *
     * @param ssoUser 统一用户表
     * @return 结果
     */
    public RetResult<Integer> updateUserByUserName(SsoUser ssoUser, UserSessionHelper helper) {
        if (ssoUser == null || StringUtils.isEmpty(ssoUser.getUserName())) {
            return RetResponse.makeErrRsp("[账号]不能为空");
        }
        Map<String,String> map = new HashMap<String,String>();
        map.put("userName",ssoUser.getUserName());
        if(!StringHelper.IsNullOrWhiteSpace(ssoUser.getPhone())){
            map.put("phone",ssoUser.getPhone());
        }
        if(!StringHelper.IsNullOrWhiteSpace(ssoUser.getEmail())){
            map.put("email",ssoUser.getEmail());
        }
        List<SsoUser> list = ssoUserMapper.findUserEquOr(map);

        if (list == null || list.isEmpty()) {
            return RetResponse.makeErrRsp("不存在此用户");
        }
        Optional<SsoUser> modelOpt = list.stream().filter(t -> t.getUserName().equals(ssoUser.getUserName())).findFirst();
        if (!modelOpt.isPresent()) {
            return RetResponse.makeErrRsp("不存在此用户");
        }
        SsoUser model = modelOpt.get();
        if (!StringUtils.isEmpty(ssoUser.getPhone())) {
            Optional<SsoUser> isExistPhone = list.stream().filter(t ->t.getPhone()!=null && t.getPhone().equals(ssoUser.getPhone()) && t.getUserId() != model.getUserId()).findAny();
            if (isExistPhone.isPresent()) {
                return RetResponse.makeErrRsp("[手机号]已存在");
            }
        }
        if (!StringUtils.isEmpty(ssoUser.getEmail())) {
            Optional<SsoUser> isExistEmail = list.stream().filter(t ->t.getEmail()!=null && t.getEmail().equals(ssoUser.getEmail()) && t.getUserId() != model.getUserId()).findAny();
            if (isExistEmail.isPresent()) {
                return RetResponse.makeErrRsp("邮箱已存在");
            }
        }
        if(ssoUser.getStatus()!=null && ssoUser.getStatus()==0){
            ssoUser.setStatus(1);
        }
        Date datetime = new Date();
        ssoUser.setId(null);
        ssoUser.setUserId(null);
        ssoUser.setUserName(null);
        ssoUser.setPassword(null);
        ssoUser.setModifyTime(datetime);
        ssoUser.setModifyBy(helper.getUserName());

        Integer state = ssoUserMapper.updateByPrimaryKeySelective(ssoUser);
        managerLogService.addLogAsync(ManagerLogEnum.User_Modify.getName(), JSON.toJSONString(ssoUser),helper);

        return RetResponse.makeOKRsp(state);
    }

    /**
     * 删除用户
     *
     * @param userNameList 账号列表
     * @return 结果
     */
    public RetResult<Integer> deleteUserByUserNameList(List<String> userNameList, UserSessionHelper helper) {
        if (userNameList == null || userNameList.size()<1) {
            return RetResponse.makeErrRsp("参数不能为空");
        }
        Map<String,List<String>> map = new HashMap<String,List<String>>();
        map.put("userNameList",userNameList);
        List<SsoUser> list = ssoUserMapper.findUserEquAnd(map);
        if (list == null || list.isEmpty()) {
            return RetResponse.makeErrRsp("用户不存在");
        }
        List<String> ids = list.stream().map(t->t.getUserId()).collect(Collectors.toList());
        Date datetime = new Date();
        UserDeleteMapperModel deleteModel=new UserDeleteMapperModel();
        deleteModel.setUserIdList(ids);
        deleteModel.setModifyTime(datetime);
        deleteModel.setModifyBy(helper.getUserName());

        Integer state = ssoUserMapper.deleteUserByUserIdList(deleteModel);
        managerLogService.addLogAsync(ManagerLogEnum.User_Delete.getName(), JSON.toJSONString(userNameList),helper);

        return RetResponse.makeOKRsp(state);
    }

    /**
     * 根据用户名查找用户
     * @param userName
     * @return
     */
    public RetResult<SsoUser> selectUserByUserName(String userName, UserSessionHelper helper)  {
        if (StringHelper.IsNullOrWhiteSpace(userName)) {
            return RetResponse.makeErrRsp("参数不能为空");
        }
        Map<String,String> map = new HashMap<String,String>();
        map.put("userName",userName);
        List<SsoUser> list = ssoUserMapper.findUserEquAnd(map);
        if (list == null || list.isEmpty()) {
            return RetResponse.makeErrRsp("用户不存在");
        }
        SsoUser model = list.get(0);
        return RetResponse.makeOKRsp(model);
    }

    /**
     * 重置密码
     * @param userName 用户账号
     * @return
     */
    public RetResult<Integer> resetPassword(String userName, UserSessionHelper helper)  {
        if (StringHelper.IsNullOrWhiteSpace(userName)) {
            return RetResponse.makeErrRsp("参数不能为空");
        }
        Map<String,String> map = new HashMap<String,String>();
        map.put("userName",userName);
        List<SsoUser> list = ssoUserMapper.findUserEquAnd(map);
        if (list == null || list.isEmpty()) {
            return RetResponse.makeErrRsp("用户不存在");
        }
        SsoUser model = list.get(0);
        if (model==null) {
            return RetResponse.makeErrRsp("用户不存在");
        }
        String usernameReset = org.apache.commons.lang3.StringUtils.lowerCase(userName);
        model.setPassword(MD5Util.encrypt(usernameReset, UserHelper.DefaultPassword));
        Date datetime = new Date();
        model.setModifyTime(datetime);
        model.setModifyBy(helper.getUserName());

        Integer state = ssoUserMapper.updateByPrimaryKeySelective(model);
        managerLogService.addLogAsync(ManagerLogEnum.User_Reset_Password.getName(), userName,helper);

        return RetResponse.makeOKRsp(state);
    }

    /**
     * 根据用户账号获取用户信息
     *
     * @param name 名称。类型相关
     * @param type 1 用户账号；2 UserId；3 表Id; 4 电话; 5 邮箱
     * @return
     */
    public  RetResult<SsoUser>  getUserByName(String name,Integer type, UserSessionHelper helper){
        if (StringHelper.IsNullOrWhiteSpace(name)) {
            return RetResponse.makeErrRsp("参数不能为空");
        }
        if(type==null){
            type=1;
        }
        Map<String,String> map = new HashMap<String,String>();
        switch (type){
            case 1:
                map.put("userName",name);
                break;
            case 2:
                map.put("userId",name);
                break;
            case 3:
                map.put("id",name);
                break;
            case 4:
                map.put("phone",name);
                break;
            case 5:
                map.put("email",name);
                break;
            default:
                map.put("userName",name);
                break;
        }
        List<SsoUser> list = ssoUserMapper.findUserEquAnd(map);
        if (list == null || list.isEmpty()) {
            return RetResponse.makeErrRsp("用户不存在");
        }
        SsoUser model = list.get(0);
        if (model==null) {
            return RetResponse.makeErrRsp("用户不存在");
        }
        return RetResponse.makeOKRsp(model);
    }

    /**
     * 查找用户
     *
     * @param name 用户账号/手机号/邮箱
     * @return
     */
    public RetResult<SsoUser>  getUserByName(String name, UserSessionHelper helper){
        Map<String,String> map = new HashMap<String,String>();
        map.put("userName",name);
        map.put("userId",name);
        map.put("phone",name);
        map.put("email",name);
        List<SsoUser> list = ssoUserMapper.findUserEquOr(map);
        if (list == null || list.isEmpty()) {
            return RetResponse.makeErrRsp("用户不存在");
        }
        SsoUser model = list.get(0);
        if (model==null) {
            return RetResponse.makeErrRsp("用户不存在");
        }
        return RetResponse.makeOKRsp(model);
    }

    /**
     * 查找用户
     *
     * @param name 用户账号/手机号/邮箱
     * @return
     */
    public RetResult<List<SsoUser>> getUserListByName(String name, UserSessionHelper helper){
        Map<String,String> map = new HashMap<String,String>();
        map.put("userName",name);
        map.put("userId",name);
        map.put("phone",name);
        map.put("email",name);
        List<SsoUser> list = ssoUserMapper.findUserEquOr(map);
        if (list == null || list.isEmpty()) {
            return RetResponse.makeErrRsp("用户不存在");
        }
        return RetResponse.makeOKRsp(list);
    }

    //endregion

    //region 验证相关类




    //endregion

    //region 内部 盛荣用

    /**
     * 根据用户账号获取用户信息
     *
     * @param userName
     * @return
     */
    public SsoUser getUserByUserNameInter(String userName){
        if (StringHelper.IsNullOrWhiteSpace(userName)) {
            return null;
        }
        Map<String,String> map = new HashMap<String,String>();
        map.put("userName",userName);
        List<SsoUser> list = ssoUserMapper.findUserEquAnd(map);
        if (list == null || list.isEmpty()) {
            return null;
        }
        SsoUser model = list.get(0);
        return model;
    }

    /**
     * 根据用户Id获取用户信息
     *
     * @param userId
     * @return
     */
    public SsoUser getUserByUserIdInter(String userId){
        if (StringHelper.IsNullOrWhiteSpace(userId)) {
            return null;
        }
        Map<String,String> map = new HashMap<String,String>();
        map.put("userId",userId);
        List<SsoUser> list = ssoUserMapper.findUserEquAnd(map);
        if (list == null || list.isEmpty()) {
            return null;
        }
        SsoUser model = list.get(0);
        return model;
    }

    /**
     * 根据用户表Id获取用户信息
     *
     * @param uid
     * @return
     */
    public SsoUser getUserByIdInter(Integer uid){
        if (StringHelper.IsNullOrZero(uid)) {
            return null;
        }
        Map<String,Integer> map = new HashMap<String,Integer>();
        map.put("id",uid);
        List<SsoUser> list = ssoUserMapper.findUserEquAnd(map);
        if (list == null || list.isEmpty()) {
            return null;
        }
        SsoUser model = list.get(0);
        return model;
    }

    /**
     * 根据用户名与组织机构编码获取用户角色
     *
     * @param userName
     * @param orgCode
     * @return
     */
    public Set<String> getUserRolesInter(String userName, String orgCode){

        if(ManagerHelper.isEnable && !StringHelper.IsNullOrWhiteSpace(userName) && userName.equals(ManagerHelper.UserName)){
            List<String> roleList = ManagerHelper.roleList;
            Set<String> result = new HashSet<String>(roleList);
            return result;
         }
        else{
            Map<String,String> map=new HashMap<>();
            map.put("userName",userName);
            map.put("orgCode",orgCode);
            List<ManagerRole> roleList = ssoUserMapper.getMyRole(map);
            if(roleList!=null && roleList.size()>0){
                Set<String> result = roleList.stream().filter(t->t.getOrgCode().equals(orgCode)).map(t->t.getRoleName()).collect(Collectors.toSet());
                return result;
            }
        }
       return null;
    }

    /**
     * 根据用户名获取所有角色
     *
     * @param userName
     * @return
     */
    public Set<RoleOrgExtend> getUserRolesInter(String userName){

        //redis获取
        Set<RoleOrgExtend> redisData =redisHelper.getUserRole(userName);
        if(redisData!=null){
            return redisData;
        }

        if(ManagerHelper.isEnable && !StringHelper.IsNullOrWhiteSpace(userName) && userName.equals(ManagerHelper.UserName)) {
            Set<RoleOrgExtend> result =new HashSet<RoleOrgExtend>();
            RoleOrgExtend model=new RoleOrgExtend();
            model.setRoleList(new HashSet<String>(ManagerHelper.roleList));
            model.setOrgCode("eblcu");
            result.add(model);
            //存入redis
            redisHelper.setUserRole(userName,result);
            return result;
        }
        else{
            Map<String,String> map=new HashMap<>();
            map.put("userName",userName);
            //map.put("orgCode",orgCode);
            List<ManagerRole> roleList = ssoUserMapper.getMyRole(map);
            if(roleList!=null && roleList.size()>0){
                Set<RoleOrgExtend> result =new HashSet<RoleOrgExtend>();
                Set<String> orgCodeList = roleList.stream().map(t->t.getOrgCode()).collect(Collectors.toSet());
                orgCodeList.forEach(item->{
                    RoleOrgExtend model=new RoleOrgExtend();
                    model.setOrgCode(item);
                    Set<String> thisRoleList = roleList.stream().filter(t->t.getOrgCode().equals(item)).map(t->t.getRoleName()).collect(Collectors.toSet());
                    model.setRoleList(thisRoleList);
                    result.add(model);
                });
                //存入redis
                redisHelper.setUserRole(userName,result);
                return result;
            }
        }

        return null;
    }

    /**
     * 根据用户名与组织机构编码获取用户权限
     *
     * @param userName
     * @param orgCode
     * @return
     */
    public  Set<String> getUserPermissionsInter(String userName, String orgCode){
        if(ManagerHelper.isEnable && !StringHelper.IsNullOrWhiteSpace(userName) && userName.equals(ManagerHelper.UserName)) {
            Map<String,Object> map=new HashMap<String,Object>();
            //map.put("userName",userName);
            map.put("orgCode",orgCode);
            map.put("isAdmin",true);
            List<OrgPermission> permissionList = ssoUserMapper.getMyPermision(map);
            if(permissionList!=null && permissionList.size()>0){
                Set<String> result = permissionList.stream().filter(t->t.getOrgCode().equals(orgCode) && !StringHelper.IsNullOrWhiteSpace(t.getPerms())).map(t->t.getPerms()).collect(Collectors.toSet());
                return result;
            }
        }
        else{
            Map<String,String> map=new HashMap<>();
            map.put("userName",userName);
            map.put("orgCode",orgCode);
            List<OrgPermission> permissionList = ssoUserMapper.getMyPermision(map);
            if(permissionList!=null && permissionList.size()>0){
                Set<String> result = permissionList.stream().filter(t->t.getOrgCode().equals(orgCode)  && !StringHelper.IsNullOrWhiteSpace(t.getPerms())).map(t->t.getPerms()).collect(Collectors.toSet());
                return result;
            }
        }
        return null;
    }

    /**
     * 根据用户名与组织机构编码获取用户权限
     *
     * @param userName
     * @return
     */
    public  Set<PermissionOrgExtend> getUserPermissionsInter(String userName){

        //redis获取
        Set<PermissionOrgExtend> redisData =redisHelper.getUserPermission(userName);
        if(redisData!=null){
            return redisData;
        }

        if(ManagerHelper.isEnable && !StringHelper.IsNullOrWhiteSpace(userName) && userName.equals(ManagerHelper.UserName)) {
            Map<String,Object> map=new HashMap<String,Object>();
            //map.put("userName",userName);
            map.put("orgCode","");
            map.put("isAdmin",true);
            List<OrgPermission> permissionList = ssoUserMapper.getMyPermision(map);
            if(permissionList!=null && permissionList.size()>0){
                Set<PermissionOrgExtend> result =new HashSet<PermissionOrgExtend>();
                //取得所有机构
                OrganizationSearchModel searchModel =  new OrganizationSearchModel();
                searchModel.setStatus(1);
                List<ManagerOrganization> organizationList = managerOrganizationService.selectOrganizationList(searchModel);
                Set<String> orgCodeList =organizationList.stream().map(ManagerOrganization::getOrgCode).distinct().collect(Collectors.toSet());
                orgCodeList.forEach(item->{
                    PermissionOrgExtend model=new PermissionOrgExtend();
                    model.setOrgCode(item);
                    Set<String> thisPermsList = permissionList.stream().filter(t->!StringHelper.IsNullOrWhiteSpace(t.getPerms())).map(t->t.getPerms()).collect(Collectors.toSet());
                    model.setPermissionList(thisPermsList);
                    result.add(model);
                });

                //存入redis
                redisHelper.setUserPermission(userName,result);
                return result;
            }
        }
        else{
            Map<String,String> map=new HashMap<>();
            map.put("userName",userName);
            //map.put("orgCode",orgCode);
            List<OrgPermission> permissionList = ssoUserMapper.getMyPermision(map);
            if(permissionList!=null && permissionList.size()>0){
                Set<PermissionOrgExtend> result =new HashSet<PermissionOrgExtend>();
                Set<String> orgCodeList = permissionList.stream().map(t->t.getOrgCode()).collect(Collectors.toSet());
                orgCodeList.forEach(item->{
                    PermissionOrgExtend model=new PermissionOrgExtend();
                    model.setOrgCode(item);
                    Set<String> thisPermsList = permissionList.stream().filter(t->t.getOrgCode().equals(item) && !StringHelper.IsNullOrWhiteSpace(t.getPerms())).map(t->t.getPerms()).collect(Collectors.toSet());
                    model.setPermissionList(thisPermsList);
                    result.add(model);
                });
                //存入redis
                redisHelper.setUserPermission(userName,result);
                return result;
            }
        }
        return null;
    }

    //endregion

    //region 内部李程用

    /**
     * 根据用户Id列表，查询用户真实姓名
     * @param userIdList
     * @return
     */
    public Map selectLectureNameByUserId(Set<String> userIdList){
        Map model=new HashMap();
        if(userIdList!=null && !userIdList.isEmpty()){
            Map<String,List<String>> map=new HashMap<String,List<String>>();
            map.put("userIdList",new ArrayList<>(userIdList));
            List<SsoUser> ssoList = ssoUserMapper.findUserEquAnd(map);
           if(ssoList!=null){
               userIdList.forEach(t->
                   {
                      Optional<SsoUser> ssoUser=  ssoList.stream().filter(m->m.getUserId().equals(t)).findFirst();
                      if(ssoUser.isPresent()){
                          model.put(t,ssoUser.get().getRealName());
                      }
                      else{
                          model.put(t,"");
                      }
                   }
               );
           }
        }
        return model;
    }

    //endregion

    //region 检查类

    /**
     *  检查是否存在
     * @param name
     * @param type 1 账号，2 手机号， 3邮箱
     * @return
     */
    public RetResult<Boolean> checkExist(String name,Integer type, UserSessionHelper helper){
              if (StringHelper.IsNullOrWhiteSpace(name)) {
            return RetResponse.makeErrRsp("参数不能为空");
        }
        if(type==null){
            type=1;
        }
        Map<String,String> map = new HashMap<String,String>();
        switch (type){
            case 1:
                map.put("userName",name);
                break;
            case 2:
                map.put("phone",name);
                break;
            case 3:
                map.put("email",name);
                break;
            default:
                map.put("userName",name);
                break;
        }
        List<SsoUser> list = ssoUserMapper.findUserEquOr(map);
        if (list == null || list.isEmpty()) {
            return RetResponse.makeErrRsp("用户不存在");
        }
        SsoUser model = list.get(0);
        if (model==null) {
            return RetResponse.makeErrRsp("用户不存在");
        }
        return RetResponse.makeOKRsp(true);
    }

    //endregion

    //region 前端用

    //endregion

    //region 内部，不知谁用了

    public List<SsoUser> selectListAnd(UserSearchModel search){
        if(search==null){
            return null;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userName",search.getUserName());
        map.put("userId",search.getUserId());
        map.put("status",search.getStatus());
        map.put("realName",search.getRealName());
        map.put("phone",search.getPhone());
        map.put("email",search.getEmail());
        map.put("createTimeBegin",search.getCreateTimeBegin());
        map.put("createTimeEnd",search.getCreateTimeEnd());
        map.put("userIdList",search.getUserIdList());
        List<SsoUser> list = ssoUserMapper.findUserList(map);
        return list;
    }

    //endregion


    //region 查询

   public  List<SsoUser> selectListAnd(UserSearchModel search, UserSessionHelper helper){
        if(search==null){
            return null;
        }
       Map<String,Object> map = new HashMap<String,Object>();
       map.put("userName",search.getUserName());
       map.put("userId",search.getUserId());
       map.put("status",search.getStatus());
       map.put("realName",search.getRealName());
       map.put("phone",search.getPhone());
       map.put("email",search.getEmail());
       map.put("createTimeBegin",search.getCreateTimeBegin());
       map.put("createTimeEnd",search.getCreateTimeEnd());
       map.put("userIdList",search.getUserIdList());
       List<SsoUser> list = ssoUserMapper.findUserList(map);
        return list;
   }
    public Integer selectListAndCount(UserSearchModel search, UserSessionHelper helper){
        if(search==null){
            return null;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userName",search.getUserName());
        map.put("userId",search.getUserId());
        map.put("status",search.getStatus());
        map.put("realName",search.getRealName());
        map.put("phone",search.getPhone());
        map.put("email",search.getEmail());
        map.put("createTimeBegin",search.getCreateTimeBegin());
        map.put("createTimeEnd",search.getCreateTimeEnd());
        map.put("userIdList",search.getUserIdList());
        Integer count = ssoUserMapper.findUserListCount(map);
        return count;
    }
    public List<SsoUser> selectListOr(UserSearchModel search, UserSessionHelper helper){
        if(search==null){
            return null;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userName",search.getUserName());
        map.put("userId",search.getUserId());
        map.put("status",search.getStatus());
        map.put("realName",search.getRealName());
        map.put("phone",search.getPhone());
        map.put("email",search.getEmail());
        List<SsoUser> list = ssoUserMapper.findUserListOr(map);
        return list;
    }

    public List<SsoUser> selectListByUserIdList(List<String> userIdList, UserSessionHelper helper){
        if(StringHelper.IsNullOrEmpty(userIdList)){
            return null;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userIdList",userIdList);
        List<SsoUser> list = ssoUserMapper.findUserEquAnd(map);
        return list;
    }

    @Override
    public Boolean isTeacher(String userName) {
        //判定用户是否为教师
        Set<PermissionOrgExtend> userPermissions = getUserPermissionsInter(userName);
        if(!StringHelper.IsNullOrEmpty(userPermissions)) {
            long exit = userPermissions.stream().filter(t -> !StringHelper.IsNullOrEmpty(t.getPermissionList()))
                    .filter(m -> m.getPermissionList().contains("teacher")).count();
            if (exit > 0) {
                return true;
            }
        }
        return false;
    }

    //endregion

}