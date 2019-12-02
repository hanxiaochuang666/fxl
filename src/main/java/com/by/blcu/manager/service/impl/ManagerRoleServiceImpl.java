package com.by.blcu.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.manager.common.*;
import com.by.blcu.manager.dao.ManagerRoleMapper;
import com.by.blcu.manager.model.*;
import com.by.blcu.manager.model.sql.InputRole;
import com.by.blcu.manager.model.sql.OrgPermission;
import com.by.blcu.manager.service.ManagerLogService;
import com.by.blcu.manager.service.ManagerRoleService;
import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.manager.umodel.RolePermissionRModel;
import com.by.blcu.manager.umodel.RoleSearchModel;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @Description: ManagerRoleService接口实现类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@Service
public class ManagerRoleServiceImpl extends AbstractService<ManagerRole> implements ManagerRoleService {

    @Resource
    private ManagerRoleMapper managerRoleMapper;

    @Resource
    private RedisHelper redisHelper;

    //系统日志记录
    @Resource
    private ManagerLogService managerLogService;

    //region 基本

    public RetResult<Integer> addRole(ManagerRole managerRole, UserSessionHelper helper){
        if (managerRole == null || StringUtils.isEmpty(managerRole.getRoleName()) || StringUtils.isEmpty(managerRole.getOrgCode())) {
            return RetResponse.makeErrRsp("[角色名称，所属组织编码]不能为空");
        }
        //TODO ogrcode
        managerRole.setOrgCode(helper.getOrgCode());

        Map<String,String> map =new HashMap<String,String>();
        map.put("orgCode",managerRole.getOrgCode());
        map.put("roleName",managerRole.getRoleName());
        List<ManagerRole> listRole = managerRoleMapper.findRoleEquAnd(map);
        if(listRole!=null && !listRole.isEmpty()){
            return RetResponse.makeErrRsp("[角色名]已存在");
        }
        Date datetime = new Date();
        managerRole.setRoleId(ApplicationUtils.getUUID());
        if(StringHelper.IsNullOrZero(managerRole.getStatus())){
            managerRole.setStatus(1);
        }
        managerRole.setIsDeleted(false);
        managerRole.setCreateTime(datetime);
        managerRole.setModifyTime(datetime);
        managerRole.setCreateBy(helper.getUserName());
        managerRole.setModifyBy(helper.getUserName());

        Integer state = managerRoleMapper.insertSelective(managerRole);
        managerLogService.addLogAsync(ManagerLogEnum.Role_Add.getName(), JSON.toJSONString(managerRole),helper);

        return RetResponse.makeOKRsp(state);
    }

    public RetResult<Integer> updateRole(ManagerRole managerRole, UserSessionHelper helper){

        if (managerRole == null || StringUtils.isEmpty(managerRole.getRoleId())) {
            return RetResponse.makeErrRsp("[角色Id]不能为空");
        }
        //TODO ogrcode
        managerRole.setOrgCode(helper.getOrgCode());

        Map<String,String> map =new HashMap<String,String>();
        map.put("roleId",managerRole.getRoleId());
        List<ManagerRole> listRole = managerRoleMapper.findRoleEquAnd(map);
        if(listRole==null || listRole.isEmpty()){
            return RetResponse.makeErrRsp("不存在此角色");
        }

        Map<String,String> map2 =new HashMap<String,String>();
        map.put("orgCode",managerRole.getOrgCode());
        map.put("roleName",managerRole.getRoleName());
        List<ManagerRole> listRole2 = managerRoleMapper.findRoleEquAnd(map);
        if(listRole2!=null && !listRole2.isEmpty()){
            Optional<ManagerRole> tmp = listRole2.stream().filter(t->!t.getRoleId().equals(managerRole.getRoleId())).findAny();
            if(tmp.isPresent()){
                return RetResponse.makeErrRsp("角色名已存在");
            }
        }
        ManagerRole model =listRole.get(0);
        Date datetime = new Date();
        managerRole.setCreateTime(model.getCreateTime());
        managerRole.setCreateBy(model.getCreateBy());
        managerRole.setModifyTime(datetime);
        managerRole.setModifyBy(helper.getUserName());

        Integer state = managerRoleMapper.updateByPrimaryKeySelective(managerRole);
        managerLogService.addLogAsync(ManagerLogEnum.Role_Modify.getName(), JSON.toJSONString(managerRole),helper);

        //清理缓存
        redisHelper.cleanUserRole(null);

        return RetResponse.makeOKRsp(state);
    }

    public RetResult<Integer> deleteByRoleIdList(List<String> roleIdList, UserSessionHelper helper) {
        if(roleIdList == null || roleIdList.isEmpty()){
            return RetResponse.makeErrRsp("参数不能为空");
        }

        //检查是否已关联了用户
        InputRole checkRModel = new InputRole();
        checkRModel.setRoleIdList(roleIdList);
        Integer isExist = managerRoleMapper.findRExist(checkRModel);
        if(isExist>0){
            return RetResponse.makeErrRsp("请先进行机构用户解绑");
        }

        Date datetime = new Date();
        Map<String,Object> map =new HashMap<String,Object>();
        map.put("modifyTime",datetime);
        map.put("modifyBy",helper.getUserName());
        map.put("roleIdList",roleIdList);
        Integer state = managerRoleMapper.deleteRoleById(map);

        managerLogService.addLogAsync(ManagerLogEnum.Role_Delete.getName(), JSON.toJSONString(roleIdList),helper);

        //清理缓存
        redisHelper.cleanUserRole(null);

        return RetResponse.makeOKRsp(state);
    }

    public RetResult<ManagerRole> selectRoleByRoleId(String roleId, UserSessionHelper helper){
        if(StringHelper.IsNullOrWhiteSpace(roleId)){
            return RetResponse.makeErrRsp("参数不能为空");
        }
        ManagerRole role =  managerRoleMapper.selectByPrimaryKey(roleId);
        if(role==null){
            return RetResponse.makeErrRsp("不存在此角色");
        }
        else{
            return RetResponse.makeOKRsp(role);
        }
    }

    public List<ManagerRole> selectRoleList(RoleSearchModel search, UserSessionHelper helper){
        if (search==null) {
            return null;
        }
        //TODO ogrcode
        search.setOrgCode(helper.getOrgCode());

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("status",search.getStatus());
        map.put("isSystem",search.getIsSystem());
        map.put("roleId",search.getRoleId());
        map.put("roleName",search.getRoleName());
        map.put("orgCode",search.getOrgCode());
        List<ManagerRole> listAccount = managerRoleMapper.findRoleList(map);
        return listAccount;
    }
    public Integer selectRoleListCount(RoleSearchModel search, UserSessionHelper helper){
        if (search==null) {
            return null;
        }
        //TODO ogrcode
        search.setOrgCode(helper.getOrgCode());

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("status",search.getStatus());
        map.put("isSystem",search.getIsSystem());
        map.put("roleId",search.getRoleId());
        map.put("roleName",search.getRoleName());
        map.put("orgCode",search.getOrgCode());
        Integer listAccount = managerRoleMapper.findRoleListCount(map);
        return listAccount;
    }

    //endregion

    //region 给角色添加权限

    public List<OrgPermission> getRolePermission(String orgCode,String roleId, UserSessionHelper helper){
        if(StringHelper.IsNullOrWhiteSpace(roleId)){
            return null;
        }
        Map<String,String> map =new HashMap<String,String>();
        map.put("roleId",roleId);
        List<OrgPermission> result =managerRoleMapper.findRolePermission(map);
        return result;
    }

    public List<OrgPermission> getAccountRoleExclude(String orgCode,String roleId, String userId, UserSessionHelper helper){
        if(StringHelper.IsNullOrWhiteSpace(orgCode)|| StringHelper.IsNullOrWhiteSpace(roleId) || StringHelper.IsNullOrWhiteSpace(userId) ){
            return null;
        }
        //TODO ogrcode
        orgCode = helper.getOrgCode();

        //已有权限
        List<OrgPermission> hasPermissionList = getRolePermission(orgCode,roleId,helper);
        //所有权限
        Map<String,String> map =new HashMap<String,String>();
        map.put("orgCode",orgCode);
        map.put("roleId",roleId);
        map.put("userId",userId);
        List<OrgPermission> allPermissionList =managerRoleMapper.findRolePermissionExcept(map);
        //还未添加的权限
        if(!StringHelper.IsNullOrEmpty(hasPermissionList)){
            List<String> pIdListA = hasPermissionList.stream().map(t->t.getPermissionId()).collect(Collectors.toList());
            List<String> pIdListB= allPermissionList.stream().map(t->t.getPermissionId()).distinct().collect(Collectors.toList());
            pIdListB.removeAll(pIdListA);
            allPermissionList =allPermissionList.stream().filter(t->pIdListB.contains(t.getPermissionId())).distinct().collect(Collectors.toList());
        }
        else{
            if(!StringHelper.IsNullOrEmpty(allPermissionList)){
                allPermissionList = allPermissionList.stream().distinct().collect(Collectors.toList());
            }
        }
        return allPermissionList;
    }

    public  List<OrgPermission> getAccountRolePermission(String orgCode, String roleId,String userId, UserSessionHelper helper){
        if(StringHelper.IsNullOrWhiteSpace(userId)){
            return null;
        }
        //TODO ogrcode
        orgCode = helper.getOrgCode();

        Map<String,String> map =new HashMap<String,String>();
        map.put("userId",userId);
        map.put("roleId",roleId);
        map.put("orgCode",orgCode);
        List<OrgPermission> result =managerRoleMapper.findAccountRolePermission(map);
        return result;
    }

    public List<OrgPermission> getPermissionAllByOrgCode(String orgCode, UserSessionHelper helper){
        if(StringHelper.IsNullOrWhiteSpace(orgCode)){
            return null;
        }
        //TODO ogrcode
        orgCode = helper.getOrgCode();

        Map<String,String> map =new HashMap<String,String>();
        map.put("orgCode",orgCode);
        List<OrgPermission> result =managerRoleMapper.findOrgPermission(map);
        if(StringHelper.IsNullOrEmpty(result)){
            return null;
        }
        else{
            result =result.stream().distinct().collect(Collectors.toList());
        }

        return result;
    }

    @Transactional
    public RetResult<Integer> addRolePermission(RolePermissionRModel accountRoleR, UserSessionHelper helper){
        if(accountRoleR==null || StringHelper.IsNullOrWhiteSpace(accountRoleR.getRoleId()) || StringHelper.IsNullOrEmpty(accountRoleR.getPermissionIdList())){
            return RetResponse.makeErrRsp("[所属组织编码，角色表Id，权限Id列表]不能为空");
        }
        //TODO ogrcode
        accountRoleR.setOrgCode(helper.getOrgCode());

        List<String> insertIdList=new ArrayList<String>();
        List<OrgPermission> hasPermission = getRolePermission(accountRoleR.getOrgCode(),accountRoleR.getRoleId(),helper);
        if(!StringHelper.IsNullOrEmpty(hasPermission)){
            List<String> hasPermissionIds =hasPermission.stream().map(t->t.getPermissionId()).distinct().collect(Collectors.toList());
            List<String> notHasPermissionIds = accountRoleR.getPermissionIdList().stream().distinct().collect(Collectors.toList());
            notHasPermissionIds.removeAll(hasPermissionIds);
            insertIdList=notHasPermissionIds;
        }
        else{
            insertIdList =  accountRoleR.getPermissionIdList().stream().distinct().collect(Collectors.toList());
        }

        List<ManagerRolePermissionR> list=new ArrayList<ManagerRolePermissionR>();
        Date datetime=new Date();
        insertIdList.forEach(t->{
            ManagerRolePermissionR model=new ManagerRolePermissionR();
            model.setRolePermissionRId(ApplicationUtils.getUUID());
            model.setPermissionId(t);
            model.setRoleId(accountRoleR.getRoleId());
            model.setCreateTime(datetime);
            model.setModifyTime(datetime);
            model.setCreateBy(helper.getUserName());
            model.setModifyBy(helper.getUserName());

            list.add(model);
        });
        Integer state=0;
        try{
             state = managerRoleMapper.insertRolePermissionList(list);

            //清理缓存
            redisHelper.cleanUserPermission(null);
        }
        catch (Exception ex){
            return RetResponse.makeErrRsp("角色或权限不正确");
        }
        managerLogService.addLogAsync(ManagerLogEnum.Role_Permission_Modify.getName(), "添加："+JSON.toJSONString(accountRoleR),helper);

        return RetResponse.makeOKRsp(state);
    }

    public RetResult<Integer> deleteRolePermission(RolePermissionRModel accountRoleR, UserSessionHelper helper){

        if(accountRoleR==null || StringHelper.IsNullOrWhiteSpace(accountRoleR.getRoleId())){
            return RetResponse.makeErrRsp("[角色表Id]不能为空");
        }
        //TODO ogrcode
        accountRoleR.setOrgCode(helper.getOrgCode());

        Map<String,Object> map =new HashMap<String,Object>();
        map.put("roleId",accountRoleR.getRoleId());
        map.put("permissionIdList",accountRoleR.getPermissionIdList());

        Integer state=0;
        try{
            state = managerRoleMapper.deleteRolePermission(map);

            //清理缓存
            redisHelper.cleanUserPermission(null);
        }
        catch (Exception ex){
            return RetResponse.makeErrRsp("角色或权限不正确");
        }
        managerLogService.addLogAsync(ManagerLogEnum.Role_Permission_Modify.getName(), "删除："+JSON.toJSONString(accountRoleR),helper);

        return RetResponse.makeOKRsp(state);
    }

    @Transactional
    public RetResult<Integer> updateRolePermission(RolePermissionRModel accountRoleR, UserSessionHelper helper){
        if(accountRoleR==null || StringHelper.IsNullOrWhiteSpace(accountRoleR.getRoleId())){
            return RetResponse.makeErrRsp("[角色表Id]不能为空");
        }
        //TODO ogrcode
        accountRoleR.setOrgCode(helper.getOrgCode());

        List<String> insertIdList=new ArrayList<String>();
        List<String> deleteIdList=new ArrayList<String>();
        Integer state=0;

        try {
            if (StringHelper.IsNullOrEmpty(accountRoleR.getPermissionIdList())) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("roleId", accountRoleR.getRoleId());
                state = managerRoleMapper.deleteRolePermission(map);
            } else {
                List<OrgPermission> hasPermission = getRolePermission(accountRoleR.getOrgCode(), accountRoleR.getRoleId(),helper);
                if (!StringHelper.IsNullOrEmpty(hasPermission)) {
                    List<String> hasPermissionIds = hasPermission.stream().map(t -> t.getPermissionId()).distinct().collect(Collectors.toList());
                    List<String> newPermissionIds = accountRoleR.getPermissionIdList().stream().distinct().collect(Collectors.toList());
                    insertIdList = new ArrayList<String>(newPermissionIds);
                    insertIdList.removeAll(hasPermissionIds);

                    deleteIdList = new ArrayList<String>(hasPermissionIds);
                    deleteIdList.removeAll(newPermissionIds);
                } else {
                    insertIdList = accountRoleR.getPermissionIdList().stream().distinct().collect(Collectors.toList());
                }

                //添加
                if (!StringHelper.IsNullOrEmpty(insertIdList)) {
                    List<ManagerRolePermissionR> list = new ArrayList<ManagerRolePermissionR>();
                    Date datetime = new Date();
                    insertIdList.forEach(t -> {
                        ManagerRolePermissionR model = new ManagerRolePermissionR();
                        model.setRolePermissionRId(ApplicationUtils.getUUID());
                        model.setPermissionId(t);
                        model.setRoleId(accountRoleR.getRoleId());
                        model.setCreateTime(datetime);
                        model.setModifyTime(datetime);
                        model.setCreateBy(helper.getUserName());
                        model.setModifyBy(helper.getUserName());

                        list.add(model);
                    });

                    state = managerRoleMapper.insertRolePermissionList(list);
                }
                //删除
                if (!StringHelper.IsNullOrEmpty(deleteIdList)) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("roleId", accountRoleR.getRoleId());
                    map.put("permissionIdList", deleteIdList);
                    state = managerRoleMapper.deleteRolePermission(map);
                }
            }

            //清理缓存
            redisHelper.cleanUserPermission(null);
        }
        catch (Exception ex){
            return RetResponse.makeErrRsp("角色或权限不正确");
        }
        managerLogService.addLogAsync(ManagerLogEnum.Role_Permission_Modify.getName(), "修改："+JSON.toJSONString(accountRoleR),helper);

        return RetResponse.makeOKRsp(state);
    }

    //endregion

}