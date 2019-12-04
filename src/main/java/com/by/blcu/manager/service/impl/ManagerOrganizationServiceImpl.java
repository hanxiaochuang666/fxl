package com.by.blcu.manager.service.impl;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.ChineseCharacterUtil;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.common.ThymeleafHelper;
import com.by.blcu.manager.common.UserHelper;
import com.by.blcu.manager.dao.ManagerOrganizationMapper;
import com.by.blcu.manager.model.ManagerOrganization;
import com.by.blcu.manager.model.ManagerOrganization;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.model.sql.InputOrg;
import com.by.blcu.manager.service.ManagerOrganizationService;
import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.manager.umodel.OrganizationSearchModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @Description: ManagerOrganizationService接口实现类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@Service
public class ManagerOrganizationServiceImpl extends AbstractService<ManagerOrganization> implements ManagerOrganizationService {

    @Resource
    private ManagerOrganizationMapper managerOrganizationMapper;
    @Resource
    private ThymeleafHelper thymeleafHelper;

    public RetResult<Integer> addOrganization(ManagerOrganization managerOrganization){
        if (managerOrganization == null || StringHelper.IsNullOrWhiteSpace(managerOrganization.getOrganizationName()) ||StringHelper.IsNullOrWhiteSpace(managerOrganization.getOrgCode())
                || StringHelper.IsNullOrWhiteSpace(managerOrganization.getOrganizationLogo()) || StringHelper.IsNullOrWhiteSpace(managerOrganization.getWebPic())  ) {
            return RetResponse.makeErrRsp("[机构名称，入驻者组织编码，机构Logo图标，首页封面]不能为空");
        }

        //判重
        InputOrg checkExitModel =new InputOrg();
        checkExitModel.setOrgCode(managerOrganization.getOrgCode());
        checkExitModel.setOrganizationName(managerOrganization.getOrganizationName());
        List<ManagerOrganization> checkResult = managerOrganizationMapper.checkExit(checkExitModel);
        if(checkResult!=null && !checkResult.isEmpty()){
            Optional<ManagerOrganization>  checkCode= checkResult.stream().filter(t->t.getOrgCode().equals(managerOrganization.getOrgCode())).findAny();
            if(checkCode.isPresent()){
                return RetResponse.makeErrRsp("[入驻者组织编码]已存在");
            }
            Optional<ManagerOrganization> checkName= checkResult.stream().filter(t->t.getOrganizationName().equals(managerOrganization.getOrganizationName())).findAny();
            if(checkName.isPresent()){
                return RetResponse.makeErrRsp("[机构名称]已存在");
            }
        }

//        String orgCode = "";
//        Map<String,Integer> map =new HashMap<String,Integer>();
//        map.put("orgCodeLength",StringHelper.OrgCodeLenth);
//        List<ManagerOrganization> listOrganization = managerOrganizationMapper.findOrganizationEquAnd(map);
//        if(!StringHelper.IsNullOrEmpty(listOrganization)){
//            Optional<String> maxOpt = listOrganization.stream().map(t->t.getOrgCode()).distinct().sorted(Comparator.reverseOrder()).findFirst();
//            if(maxOpt.isPresent()){
//                try{
//                    orgCode=String.valueOf(Integer.parseInt(maxOpt.get())+1);
//                    while (orgCode.length()<StringHelper.OrgCodeLenth){
//                        orgCode="0"+orgCode;
//                    }
//                }
//                catch (Exception ex){
//                    orgCode="00000001";
//                }
//            }
//            else{
//                orgCode="00000001";
//            }
//        }
//        else{
//            orgCode="00000001";
//        }
//        managerOrganization.setOrgCode(orgCode);

        Date datetime = new Date();
        managerOrganization.setOrganizationId(ApplicationUtils.getUUID());
        if(StringHelper.IsNullOrZero(managerOrganization.getStatus())){
            managerOrganization.setStatus(1);
        }
        if(StringHelper.IsNullOrZero(managerOrganization.getType()) || managerOrganization.getType()==1){
            managerOrganization.setType(3);
        }
        if(StringHelper.IsNullOrZero(managerOrganization.getVerifyStatus())){
            managerOrganization.setVerifyStatus(0);
        }

        managerOrganization.setIsDeleted(false);
        managerOrganization.setCreateTime(datetime);
        managerOrganization.setModifyTime(datetime);
        managerOrganization.setCreateBy(UserHelper.UserName);
        managerOrganization.setModifyBy(UserHelper.UserName);

        Integer state = managerOrganizationMapper.insertSelective(managerOrganization);

        //自动生成机构页
        thymeleafHelper.generatorOrgIdex(managerOrganization);

        return RetResponse.makeOKRsp(state);
    }
    public RetResult<Integer> updateOrganization(ManagerOrganization managerOrganization){
        if (managerOrganization == null || StringUtils.isEmpty(managerOrganization.getOrganizationId())) {
            return RetResponse.makeErrRsp("[机构表Id]不能为空");
        }

        //判重
        InputOrg checkExitModel =new InputOrg();
        checkExitModel.setOrganizationName(managerOrganization.getOrganizationName());
        checkExitModel.setOrganizationId(managerOrganization.getOrganizationId());
        List<ManagerOrganization> checkResult = managerOrganizationMapper.checkExit(checkExitModel);
        if(checkResult!=null && !checkResult.isEmpty()){
            Optional<ManagerOrganization>  updateModelOpt= checkResult.stream().filter(t->t.getOrganizationId().equals(managerOrganization.getOrganizationId())).findFirst();
            if(!updateModelOpt.isPresent()){
                return RetResponse.makeErrRsp("不存在此机构");
            }
            ManagerOrganization model = updateModelOpt.get();
            Optional<ManagerOrganization> checkName= checkResult.stream().filter(t->t.getOrganizationName().equals(managerOrganization.getOrganizationName()) && !t.getOrganizationId().equals(model.getOrganizationId())).findAny();
            if(checkName.isPresent()){
                return RetResponse.makeErrRsp("[机构名称]已存在");
            }
            if(!StringHelper.IsNullOrWhiteSpace(managerOrganization.getOrgCode()) && !managerOrganization.getOrgCode().equals(model.getOrgCode()))
            {
                return RetResponse.makeErrRsp("不能修改机构编号");
            }
        }

        if(managerOrganization.getStatus()!=null && managerOrganization.getStatus()==0 ){
            managerOrganization.setStatus(1);
        }
//        if((managerOrganization.getType()!=null && managerOrganization.getType()==0 ) || managerOrganization.getType()==1){
//            managerOrganization.setType(3);
//        }
        Date datetime = new Date();
        managerOrganization.setType(null);
        managerOrganization.setOrgCode(null);
        managerOrganization.setCreateBy(null);
        managerOrganization.setCreateTime(null);
        managerOrganization.setModifyTime(datetime);
        managerOrganization.setModifyBy(UserHelper.UserName);

        Integer state = managerOrganizationMapper.updateByPrimaryKeySelective(managerOrganization);

        //自动生成机构页
        thymeleafHelper.generatorOrgIdex(managerOrganization);

        return RetResponse.makeOKRsp(state);
    }
    public RetResult<Integer> deleteByOrganizationIdList(List<String> organizationIdList) {
        if(organizationIdList == null || organizationIdList.isEmpty()){
            return RetResponse.makeErrRsp("参数不能为空");
        }
        Date datetime = new Date();
        Map<String,Object> map =new HashMap<String,Object>();
        map.put("modifyTime",datetime);
        map.put("modifyBy",UserHelper.UserName);
        map.put("organizationIdList",organizationIdList);
        Integer state = managerOrganizationMapper.deleteOrganizationById(map);
        return RetResponse.makeOKRsp(state);
    }
    public RetResult<ManagerOrganization> selectOrganizationByOrganizationId(String organizationId){
        if(StringHelper.IsNullOrWhiteSpace(organizationId)){
            return RetResponse.makeErrRsp("[机构表d]不能为空");
        }
        ManagerOrganization organization =  managerOrganizationMapper.selectByPrimaryKey(organizationId);
        if(organization==null){
            return RetResponse.makeErrRsp("不存在此机构");
        }
        else{
            return RetResponse.makeOKRsp(organization);
        }
    }
    public List<ManagerOrganization> selectOrganizationList(OrganizationSearchModel search){
        if (search==null) {
            return null;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("status",search.getStatus());
        map.put("verifyStatus",search.getVerifyStatus());
        map.put("type",search.getType());
        map.put("organizationId",search.getOrganizationId());
        map.put("organizationName",search.getOrganizationName());
        map.put("orgCode",search.getOrgCode());
        map.put("phone",search.getPhone());
        map.put("email",search.getEmail());
        List<ManagerOrganization> organizationList = managerOrganizationMapper.findOrganizationList(map);
        return organizationList;
    }
    public Integer selectOrganizationListCount(OrganizationSearchModel search){
        if (search==null) {
            return null;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("status",search.getStatus());
        map.put("verifyStatus",search.getVerifyStatus());
        map.put("type",search.getType());
        map.put("organizationId",search.getOrganizationId());
        map.put("organizationName",search.getOrganizationName());
        map.put("orgCode",search.getOrgCode());
        map.put("phone",search.getPhone());
        map.put("email",search.getEmail());
        Integer count = managerOrganizationMapper.findOrganizationListCount(map);
        return count;
    }
    public Map selectOrganizationNameByIdList(Set<String> organizationIdList){
        if(organizationIdList==null || organizationIdList.isEmpty()){
            return null;
        }
        organizationIdList =organizationIdList.stream().distinct().collect(Collectors.toSet());
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("organizationIdList",organizationIdList);
        List<ManagerOrganization> organizationList  = managerOrganizationMapper.findOrganizationEquAnd(map);
        if(StringHelper.IsNullOrEmpty(organizationList)){
            return null;
        }
        else{
            Map<String,String> resultData = new HashMap<String,String>();
            organizationList.forEach(t->{
                resultData.put(t.getOrganizationId(),t.getOrganizationName());
            });
            return resultData;
        }
    }
   public List<ManagerOrganization> selectOrganizationNameByIdList(List<String> organizationIdList){
       if(StringHelper.IsNullOrEmpty(organizationIdList)){
           return null;
       }
       Map<String,Object> map = new HashMap<String,Object>();
       map.put("organizationIdList",organizationIdList);
       List<ManagerOrganization> list = managerOrganizationMapper.findOrganizationEquAnd(map);
       return list;
   }
   public List<ManagerOrganization> selectOrganizationNameByCodeList(List<String> orgCodeList){
       if(StringHelper.IsNullOrEmpty(orgCodeList)){
           return null;
       }
       Map<String,Object> map = new HashMap<String,Object>();
       map.put("orgCodeList",orgCodeList);
       List<ManagerOrganization> list = managerOrganizationMapper.findOrganizationEquAnd(map);
       return list;
   }

   public ManagerOrganization selectOrganizationByOrgCode(String orgCode){
       if (StringHelper.IsNullOrWhiteSpace(orgCode)) {
           return null;
       }
       Map<String,Object> map = new HashMap<String,Object>();
       map.put("orgCode",orgCode);
       List<ManagerOrganization> organizationList = managerOrganizationMapper.findOrganizationEquAnd(map);
       if(StringHelper.IsNullOrEmpty(organizationList)){
           return null;
       }
       return organizationList.get(0);
   }
}