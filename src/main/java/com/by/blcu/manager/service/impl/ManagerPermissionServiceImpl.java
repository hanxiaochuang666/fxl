package com.by.blcu.manager.service.impl;

import com.alibaba.fastjson.JSON;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.manager.common.*;
import com.by.blcu.manager.dao.ManagerPermissionMapper;
import com.by.blcu.manager.model.ManagerPermission;
import com.by.blcu.manager.model.ManagerRole;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.model.extend.PermissionMenu;
import com.by.blcu.manager.model.extend.PermissionTree;
import com.by.blcu.manager.model.extend.SumParam;
import com.by.blcu.manager.model.sql.InputPermission;
import com.by.blcu.manager.service.ManagerLogService;
import com.by.blcu.manager.service.ManagerPermissionService;
import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.manager.service.ManagerRoleService;
import com.by.blcu.manager.service.SsoUserService;
import org.apache.catalina.Manager;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @Description: ManagerPermissionService接口实现类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@Service
public class ManagerPermissionServiceImpl extends AbstractService<ManagerPermission> implements ManagerPermissionService {

    @Resource
    private ManagerPermissionMapper managerPermissionMapper;

    @Resource
    private SsoUserService ssoUserService;

    //系统日志记录
    @Resource
    private ManagerLogService managerLogService;

    /**
     * 获取父类层级
     * @return
     */
    private void getLevel(List<ManagerPermission> listAll, String parentId, SumParam sumParam){
        if(StringHelper.IsNullOrWhiteSpace(parentId)){
            sumParam.sum=0;
            return ;
        }
        Optional<ManagerPermission> parentIsExit= listAll.stream().filter(t->t.getPermissionId().equals(parentId)).findFirst();
        if(parentIsExit.isPresent()){
            sumParam.sum=sumParam.sum+1;
            if(StringHelper.IsNullOrWhiteSpace(parentIsExit.get().getParentId())){
                return ;
            }
            else{
                getLevel(listAll,parentIsExit.get().getParentId(),sumParam);
            }
        }
    }

   public RetResult<Integer> addPermission(ManagerPermission managerPermission, UserSessionHelper helper){
       if (managerPermission == null || StringUtils.isEmpty(managerPermission.getMenuName())) {
           return RetResponse.makeErrRsp("[菜单名称]不能为空");
       }
       if(StringHelper.IsNullOrWhiteSpace(managerPermission.getParentId())){
           managerPermission.setParentId(null);
       }

       Date datetime = new Date();
       //检查
//       Map<String,Object> map =new HashMap<String,Object>();
//       map.put("parentId",managerPermission.getParentId());
//       map.put("permissionId",managerPermission.getPermissionId());
       List<ManagerPermission> listAll = managerPermissionMapper.findLevelAndParent(null);
       if(!StringHelper.IsNullOrEmpty(listAll)) {
           //深度
           Integer levelSum=0;

           //父类id是否存在
           if(!StringHelper.IsNullOrWhiteSpace(managerPermission.getParentId())){
               Optional<ManagerPermission> parentIsExit= listAll.stream().filter(t->t.getPermissionId().equals(managerPermission.getParentId())).findFirst();
               if(!parentIsExit.isPresent()){
                   return RetResponse.makeErrRsp("[父类Id]不存在");
               }
               //检查名称是否重
               List<ManagerPermission> levelList = listAll.stream().filter(t->t.getParentId()!=null && t.getParentId().equals(managerPermission.getParentId())).collect(Collectors.toList());
               if(!StringHelper.IsNullOrEmpty(levelList)){
                   Optional<ManagerPermission> nameIsExit= levelList.stream().filter(t->t.getMenuName()!=null && t.getMenuName().equals(managerPermission.getMenuName())).findFirst();
                   if(nameIsExit.isPresent()){
                       return RetResponse.makeErrRsp("[菜单名称]已存在");
                   }
               }
               //查询层级
               SumParam sumParam=new SumParam();
               sumParam.sum=0;
               getLevel(listAll,managerPermission.getParentId(),sumParam);
               levelSum =sumParam.sum+1;
           }
           else{
               //检查名称是否重
               List<ManagerPermission> levelList = listAll.stream().filter(t->t.getParentId() ==null || t.getParentId().equals("")).collect(Collectors.toList());
               if(!StringHelper.IsNullOrEmpty(levelList)){
                   Optional<ManagerPermission> nameIsExit= levelList.stream().filter(t->t.getMenuName()!=null && t.getMenuName().equals(managerPermission.getMenuName())).findFirst();
                   if(nameIsExit.isPresent()){
                       return RetResponse.makeErrRsp("[菜单名称]已存在");
                   }
               }
               levelSum=1;
           }

           if(levelSum<=3){
               if(managerPermission.getType()!=null && managerPermission.getType()!=1){
                   return RetResponse.makeErrRsp("前3级权限只允许为菜单权限");
               }
           }
           if(levelSum==4){
               if(managerPermission.getType()!=null && managerPermission.getType()!=2){
                   return RetResponse.makeErrRsp("第4级权限只允许为按钮权限");
               }
           }
           if(levelSum>5){
               return RetResponse.makeErrRsp("目前只支持4级");
           }
           if(managerPermission.getClassLayer()!=null && managerPermission.getClassLayer()!=levelSum){
               return RetResponse.makeErrRsp("[分类深度]不对");
           }
           else{
               managerPermission.setClassLayer(levelSum);
           }
       }

       managerPermission.setPermissionId(ApplicationUtils.getUUID());
       if(StringHelper.IsNullOrZero(managerPermission.getType())){
           managerPermission.setType(1);
       }
       if(StringHelper.IsNullOrFalse(managerPermission.getIsDisplay())){
           managerPermission.setIsDisplay(true);
       }
       if(StringHelper.IsNullOrZero(managerPermission.getStatus())){
           managerPermission.setStatus(1);
       }
       if(StringHelper.IsNullOrWhiteSpace(managerPermission.getParentId())){
           managerPermission.setParentId(null);
       }
       managerPermission.setIsDeleted(false);
       managerPermission.setCreateTime(datetime);
       managerPermission.setModifyTime(datetime);
       managerPermission.setCreateBy(UserHelper.UserName);
       managerPermission.setModifyBy(UserHelper.UserName);

       Integer state = managerPermissionMapper.insertSelective(managerPermission);
       managerLogService.addLogAsync(ManagerLogEnum.Permission_Add.getName(), JSON.toJSONString(managerPermission),helper);

       return RetResponse.makeOKRsp(state);
   }

   public RetResult<Integer> updatePermission(ManagerPermission managerPermission, UserSessionHelper helper){
       if (managerPermission == null || StringUtils.isEmpty(managerPermission.getPermissionId())) {
           return RetResponse.makeErrRsp("[表Id]不能为空");
       }
       //检查
//       Map<String,Object> map =new HashMap<String,Object>();
//       map.put("parentId",managerPermission.getParentId());
//       map.put("permissionId",managerPermission.getPermissionId());
       List<ManagerPermission> listAll = managerPermissionMapper.findLevelAndParent(null);
       ManagerPermission manager=null;
       if(!StringHelper.IsNullOrEmpty(listAll)) {
           //是否存在
           Optional<ManagerPermission> modelExit= listAll.stream().filter(t->t.getPermissionId().equals(managerPermission.getPermissionId())).findFirst();
           if(!modelExit.isPresent()){
               return RetResponse.makeErrRsp("[权限]不存在");
           }
           manager=modelExit.get();
           //深度
           Integer levelSum=0;

           //父类id是否存在
           if(!StringHelper.IsNullOrWhiteSpace(managerPermission.getParentId())){
               Optional<ManagerPermission> parentIsExit= listAll.stream().filter(t->t.getPermissionId().equals(managerPermission.getParentId())).findFirst();
               if(!parentIsExit.isPresent()){
                   return RetResponse.makeErrRsp("[父类Id]不存在");
               }
               //检查名称是否重
               List<ManagerPermission> levelList = listAll.stream().filter(t->t.getParentId()!=null && t.getParentId().equals(managerPermission.getParentId())).collect(Collectors.toList());
               if(!StringHelper.IsNullOrEmpty(levelList)){
                   Optional<ManagerPermission> nameIsExit= levelList.stream().filter(t->t.getMenuName()!=null && t.getMenuName().equals(managerPermission.getMenuName()) && !t.getPermissionId().equals(managerPermission.getPermissionId())).findFirst();
                   if(nameIsExit.isPresent()){
                       return RetResponse.makeErrRsp("[菜单名称]已存在");
                   }
               }
               //查询层级
               SumParam sumParam=new SumParam();
               sumParam.sum=0;
               getLevel(listAll,managerPermission.getParentId(),sumParam);
               levelSum =sumParam.sum+1;
           }
           else{
               //检查名称是否重
               List<ManagerPermission> levelList = listAll.stream().filter(t->t.getParentId() ==null).collect(Collectors.toList());
               if(!StringHelper.IsNullOrEmpty(levelList)){
                   Optional<ManagerPermission> nameIsExit= levelList.stream().filter(t->t.getMenuName()!=null && t.getMenuName().equals(managerPermission.getMenuName()) && !t.getPermissionId().equals(managerPermission.getPermissionId())).findFirst();
                   if(nameIsExit.isPresent()){
                       return RetResponse.makeErrRsp("[菜单名称]已存在");
                   }
               }
               levelSum=1;
           }

           if(levelSum<=3){
               if(managerPermission.getType()!=null && managerPermission.getType()!=1){
                   return RetResponse.makeErrRsp("前3级权限只允许为菜单权限");
               }
           }
           if(levelSum==4){
               if(managerPermission.getType()!=null && managerPermission.getType()!=2){
                   return RetResponse.makeErrRsp("第4级权限只允许为按钮权限");
               }
           }
           if(levelSum>5){
               return RetResponse.makeErrRsp("目前只支持4级");
           }
           if(managerPermission.getClassLayer()!=null && managerPermission.getClassLayer()!=levelSum){
               return RetResponse.makeErrRsp("[分类深度]不对");
           }
           else{
               managerPermission.setClassLayer(levelSum);
           }
       }
       Date datetime = new Date();
       if(StringHelper.IsNullOrWhiteSpace(managerPermission.getParentId())){
           managerPermission.setParentId(null);
       }
       managerPermission.setCreateTime(manager.getCreateTime());
       managerPermission.setCreateBy(manager.getCreateBy());
       managerPermission.setModifyTime(datetime);
       managerPermission.setModifyBy(UserHelper.UserName);
       Integer state = managerPermissionMapper.updateByPrimaryKeySelective(managerPermission);
       managerLogService.addLogAsync(ManagerLogEnum.Permission_Modify.getName(), JSON.toJSONString(managerPermission),helper);

       return RetResponse.makeOKRsp(state);
    }

   public RetResult<Integer> deleteByPermissionIdList(List<String> permissionIdList, UserSessionHelper helper){
       if (permissionIdList == null || permissionIdList.isEmpty()) {
           return RetResponse.makeErrRsp("参数不能为空");
       }

       //检查是否已关联了用户
       InputPermission checkRModel = new InputPermission();
       checkRModel.setPermissionIdList(permissionIdList);
       Integer isExist = managerPermissionMapper.findRExist(checkRModel);
       if(isExist>0){
           return RetResponse.makeErrRsp("请先进行机构用户解绑");
       }

       Date datetime = new Date();
       Map<String,Object> map =new HashMap<String,Object>();
       map.put("modifyTime",datetime);
       map.put("modifyBy",UserHelper.UserName);
       map.put("permissionIdList",permissionIdList);
       Integer state = managerPermissionMapper.deletePermissionById(map);
       managerLogService.addLogAsync(ManagerLogEnum.Permission_Delete.getName(), JSON.toJSONString(permissionIdList),helper);

       return RetResponse.makeOKRsp(state);
   }

  public RetResult<ManagerPermission> selectPermissionByPermissionId(String permissionId){
      ManagerPermission premission= managerPermissionMapper.selectByPrimaryKey(permissionId);
      if(premission==null){
        return RetResponse.makeErrRsp("不存在此权限");
      }else{
          return RetResponse.makeOKRsp(premission);
      }
  }

  //region 操作树
    /**
     * 操作树
     * @return
     */
   public List<PermissionTree> selectPermissionTree(String orgCode,Integer status){
       Map<String,Object> map =new HashMap<>();
       map.put("status",status);
       List<ManagerPermission> permissionsListAll = managerPermissionMapper.findPermissionList(map);
       if(permissionsListAll==null || permissionsListAll.isEmpty()){
            return null;
       }
       List<PermissionTree> resultList =new ArrayList<PermissionTree>();
       List<ManagerPermission> firstList = permissionsListAll.stream().filter(t->t.getParentId()==null || t.getParentId().equals("")).collect(Collectors.toList());
       if(firstList!=null && !firstList.isEmpty()){
           for(ManagerPermission item : firstList){
               if(item!=null){
                   PermissionTree treeItem = new PermissionTree();
                   try{
                       ReflexHelper.Copy(item,treeItem);
                   }catch (Exception ex){

                   }
                   resultList.add(treeItem);
               }
           }
           for(PermissionTree item:resultList) {
               item.setChildren(selectPermissionTreeChildren(permissionsListAll,item));
           }
       }
       return resultList;
   }

   private List<PermissionTree> selectPermissionTreeChildren(List<ManagerPermission> permissionsListAll, PermissionTree parentPermission){
        if(parentPermission==null){
            return null;
        }
       List<PermissionTree> resultList =new ArrayList<PermissionTree>();
       List<ManagerPermission> firstList = permissionsListAll.stream().filter(t->t.getParentId()!=null && t.getParentId().equals(parentPermission.getPermissionId())).collect(Collectors.toList());
       if(firstList!=null && !firstList.isEmpty()){
           for(ManagerPermission item : firstList){
               if(item!=null){
                   PermissionTree treeItem = new PermissionTree();
                   try{
                       ReflexHelper.Copy(item,treeItem);
                   }catch (Exception ex){

                   }
                   resultList.add(treeItem);
               }
           }
           for(PermissionTree item:resultList) {
                item.setChildren(selectPermissionTreeChildren(permissionsListAll,item));
           }
       }
        return resultList;
   }

   //endregion

    //region 菜单树

    //获取所有当前节点以及父节点
    private void _getMenuList(List<ManagerPermission> listAll, String permissionId, SumParam sumParam){
       if(StringHelper.IsNullOrWhiteSpace(permissionId)){
           return;
       }
        Optional<ManagerPermission> thisExit= listAll.stream().filter(t->t.getPermissionId().equals(permissionId)).findFirst();
        if(thisExit.isPresent()){
            sumParam.permissionIdList.add(permissionId);
            ManagerPermission permission = thisExit.get();
            if(!StringHelper.IsNullOrWhiteSpace(permission.getParentId())){
                _getMenuList(listAll,permission.getParentId(),sumParam);
            }
        }
    }

   public List<PermissionMenu> selectManagerTree(String orgCode,String useName){
       if(StringHelper.IsNullOrWhiteSpace(useName)){
           return null;
       }
       Map<String,Object> map =new HashMap<>();
       map.put("status",1);
       map.put("type",1);
       map.put("isDisplay",true);
       List<ManagerPermission> permissionsListAll = managerPermissionMapper.findPermissionList(map);
       if(permissionsListAll==null || permissionsListAll.isEmpty()){
           return null;
       }
       //获取用户权限
       InputPermission accountPermissionMap=new InputPermission();
       if(!StringHelper.IsNullOrWhiteSpace(useName)){
           accountPermissionMap.setUserName(useName);
       }
       if(!StringHelper.IsNullOrWhiteSpace(orgCode)){
           accountPermissionMap.setOrgCode(orgCode);
       }
       //获取用户权限树
       List<PermissionMenu> resultList =new ArrayList<PermissionMenu>();

       if(ManagerHelper.isEnable && useName.equals(ManagerHelper.UserName)){
           List<ManagerPermission> firstList = permissionsListAll.stream().filter(t->t.getParentId()==null || t.getParentId().equals("")).collect(Collectors.toList());
           if(firstList!=null && !firstList.isEmpty()){
               for(ManagerPermission item : firstList){
                   if(item!=null){
                       PermissionMenu treeItem = new PermissionMenu();
                       try{
                           ReflexHelper.Copy(item,treeItem);
                       }catch (Exception ex){

                       }
                       resultList.add(treeItem);
                   }
               }
               for(PermissionMenu item:resultList) {
                   item.setChildren(selectManagerTreeChildren(permissionsListAll,item,null));
               }
           }
       }
       else{
           List<String> myPremissionList = managerPermissionMapper.findAccountPermission(accountPermissionMap);
           if(StringHelper.IsNullOrEmpty(myPremissionList)){
               return null;
           }
           List<String> myPermissionIdList =new ArrayList<>();
           SumParam sumParam=new SumParam();
           sumParam.permissionIdList=new ArrayList<>();
           myPremissionList.forEach(t->{
               _getMenuList(permissionsListAll,t,sumParam);
           });
           List<String> myPermissionIdListAll = sumParam.permissionIdList;
           List<ManagerPermission> firstList = permissionsListAll.stream().filter(t->(t.getParentId()==null || t.getParentId().equals("")) && myPermissionIdListAll.contains(t.getPermissionId())).collect(Collectors.toList());
           if(!StringHelper.IsNullOrEmpty(firstList)){
               for(ManagerPermission item : firstList){
                   if(item!=null){
                       PermissionMenu treeItem = new PermissionMenu();
                       try{
                           ReflexHelper.Copy(item,treeItem);
                       }catch (Exception ex){

                       }
                       resultList.add(treeItem);
                   }
               }
               for(PermissionMenu item:resultList) {
                   item.setChildren(selectManagerTreeChildren(permissionsListAll,item,myPermissionIdListAll));
               }
           }
       }
       return resultList;
   }

   private List<PermissionMenu> selectManagerTreeChildren(List<ManagerPermission> permissionsListAll, PermissionMenu parentPermission,List<String> myPermissionIdListAll){
        if(parentPermission==null){
            return null;
        }
        List<PermissionMenu> resultList =new ArrayList<PermissionMenu>();
        List<ManagerPermission> firstList = null;
        if(StringHelper.IsNullOrEmpty(myPermissionIdListAll)){
            firstList = permissionsListAll.stream().filter(t->(t.getParentId()!=null && t.getParentId().equals(parentPermission.getPermissionId()))).collect(Collectors.toList());
        }
        else{
            firstList = permissionsListAll.stream().filter(t->(t.getParentId()!=null && t.getParentId().equals(parentPermission.getPermissionId())) && myPermissionIdListAll.contains(t.getPermissionId())).collect(Collectors.toList());
        }

        if(!StringHelper.IsNullOrEmpty(firstList)){
            for(ManagerPermission item : firstList){
                if(item!=null){
                    PermissionMenu treeItem = new PermissionMenu();
                    try{
                        ReflexHelper.Copy(item,treeItem);
                    }catch (Exception ex){

                    }
                    resultList.add(treeItem);
                }
            }
            for(PermissionMenu item:resultList) {
                item.setChildren(selectManagerTreeChildren(permissionsListAll,item,myPermissionIdListAll));
            }
        }
        return resultList;
    }
    //endregion

}