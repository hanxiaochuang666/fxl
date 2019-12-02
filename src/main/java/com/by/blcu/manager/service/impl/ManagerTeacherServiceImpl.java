package com.by.blcu.manager.service.impl;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.manager.common.*;
import com.by.blcu.manager.dao.ManagerTeacherMapper;
import com.by.blcu.manager.model.ManagerOrganization;
import com.by.blcu.manager.model.ManagerTeacher;
import com.by.blcu.manager.service.ManagerOrganizationService;
import com.by.blcu.manager.service.ManagerTeacherService;
import com.by.blcu.manager.umodel.ManagerTeacherModel;
import com.by.blcu.manager.umodel.TeacherSearchModel;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;

import com.by.blcu.manager.umodel.TeacherShowModel;
import com.by.blcu.manager.umodel.TeacherTeamSearch;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.stereotype.Service;


@Service
public class ManagerTeacherServiceImpl
        extends AbstractService<ManagerTeacher>
        implements ManagerTeacherService
{
    @Resource
    private ManagerTeacherMapper managerTeacherMapper;

    @Resource
    private ManagerOrganizationService managerOrganizationService;

    public RetResult<Integer> addTeacher(ManagerTeacher managerTeacher, UserSessionHelper helper) {
        if (managerTeacher == null || StringUtils.isEmpty(managerTeacher.getTeacherName())) {
            return RetResponse.makeErrRsp("[教师姓名]不能为空");
        }

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            managerTeacher.setOrgCode(helper.getOrgCode());
        }

        Date datetime = new Date();
        managerTeacher.setTeacherId(ApplicationUtils.getUUID());
        if (StringHelper.IsNullOrZero(managerTeacher.getStatus())) {
            managerTeacher.setStatus(Integer.valueOf(1));
        }
        if (StringHelper.IsNullOrZero(managerTeacher.getType())) {
            managerTeacher.setType(Integer.valueOf(1));
        }
        if(StringHelper.IsNullOrZero(managerTeacher.getSort())){
            managerTeacher.setSort(0);
        }

        managerTeacher.setIsDeleted(Boolean.valueOf(false));
        managerTeacher.setCreateTime(datetime);
        managerTeacher.setModifyTime(datetime);
        managerTeacher.setCreateBy(helper.getUserName());
        managerTeacher.setModifyBy(helper.getUserName());

        Integer state = Integer.valueOf(this.managerTeacherMapper.insertSelective(managerTeacher));
        return RetResponse.makeOKRsp(state);
    }
    public RetResult<Integer> updateTeacher(ManagerTeacher managerTeacher, UserSessionHelper helper) {
        if (managerTeacher == null || StringUtils.isEmpty(managerTeacher.getTeacherId())) {
            return RetResponse.makeErrRsp("[名师团队表Id]不能为空");
        }

        ManagerTeacher teacher=managerTeacherMapper.selectByPrimaryKey(managerTeacher.getTeacherId());
        if (teacher == null) {
            return RetResponse.makeErrRsp("不存在此教师");
        }
        if (managerTeacher.getStatus() != null && managerTeacher.getStatus().intValue() == 0) {
            managerTeacher.setStatus(Integer.valueOf(1));
        }
        if (managerTeacher.getType() != null && managerTeacher.getType().intValue() == 0) {
            managerTeacher.setType(Integer.valueOf(1));
        }
        if(managerTeacher.getStatus()!=null && managerTeacher.getStatus()==2 && teacher.getStatus()!=null && teacher.getType()==2){
            return RetResponse.makeErrRsp("请先删除推荐");
        }
        if(StringHelper.IsNullOrZero(managerTeacher.getSort()) && StringHelper.IsNullOrZero(teacher.getSort())){
            managerTeacher.setSort(0);
        }
        Date datetime = new Date();
        managerTeacher.setOrgCode(null);
        managerTeacher.setCreateTime(null);
        managerTeacher.setCreateBy(null);
        managerTeacher.setModifyTime(datetime);
        managerTeacher.setModifyBy(helper.getUserName());

        Integer state = Integer.valueOf(this.managerTeacherMapper.updateByPrimaryKeySelective(managerTeacher));

        return RetResponse.makeOKRsp(state);
    }
    public RetResult<Integer> deleteByTeacherIdList(List<String> teacherIdList, UserSessionHelper helper) {
        if (teacherIdList == null || teacherIdList.isEmpty()) {
            return RetResponse.makeErrRsp("参数不能为空");
        }
        //已经添加到推荐的教师，不允许修改教师状态为禁用，需要进行提示先删除推荐
        Map<String, Object> mapCheck = new HashMap<String, Object>();
        mapCheck.put("teacherIdList",teacherIdList);
        mapCheck.put("type",2);
        List<ManagerTeacher> teacherList = managerTeacherMapper.findTeacherEquAnd(mapCheck);
        if(!StringHelper.IsNullOrEmpty(teacherIdList)){
            return RetResponse.makeErrRsp("请先删除推荐");
        }
        //删除
        Date datetime = new Date();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("modifyTime", datetime);
        map.put("modifyBy", helper.getUserName());
        map.put("teacherIdList", teacherIdList);
        Integer state = this.managerTeacherMapper.deleteTeacherById(map);
        return RetResponse.makeOKRsp(state);
    }
    public RetResult<TeacherShowModel> selectTeacherByTeacherId(String teacherId, UserSessionHelper helper) {
        if (StringHelper.IsNullOrWhiteSpace(teacherId)) {
            return RetResponse.makeErrRsp("[名师团队表Id]不能为空");
        }

        TeacherShowModel data =new TeacherShowModel();

        ManagerTeacher teacher = (ManagerTeacher)this.managerTeacherMapper.selectByPrimaryKey(teacherId);
        if (teacher == null) {
            return RetResponse.makeErrRsp("不存在此角色");
        }
        try {
            ReflexHelper.Copy(teacher,data);
        }
        catch (Exception ex){

        }

        if(!StringHelper.IsNullOrWhiteSpace(data.getOrgCode())){
            List<String> orgCodeList = new ArrayList<>();
            orgCodeList.add(data.getOrgCode());
            List<ManagerOrganization> orgList = managerOrganizationService.selectOrganizationNameByCodeList(orgCodeList);
            if(!StringHelper.IsNullOrEmpty(orgList) &&orgList.get(0)!=null){
                data.setOrgName(orgList.get(0).getOrganizationName());
            }
        }

        return RetResponse.makeOKRsp(data);
    }

    public List<TeacherShowModel> selectTeacherList(TeacherSearchModel search, UserSessionHelper helper) {
        if (search == null) {
            return null;
        }

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            search.setOrgCode(helper.getOrgCode());
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", search.getStatus());
        map.put("type", search.getType());
        map.put("ccId", search.getCcId());
        map.put("teacherId", search.getTeacherId());
        map.put("teacherName", search.getTeacherName());
        map.put("orgCode", search.getOrgCode());
        map.put("teacherIdList", search.getTeacherIdList());
        List<ManagerTeacher> searchResult = this.managerTeacherMapper.findTeacherList(map);
        List<TeacherShowModel> listData =new ArrayList<TeacherShowModel>();
        if(!StringHelper.IsNullOrEmpty(searchResult)){

            searchResult.forEach(t->{
                TeacherShowModel model =new TeacherShowModel();
                try {
                    ReflexHelper.Copy(t,model);
                    listData.add(model);
                }
                catch (Exception ex){

                }
            });

            List<String> orgCodeList = listData.stream().filter(t->t.getOrgCode()!=null).map(t->t.getOrgCode()).distinct().collect(Collectors.toList());
            if(!StringHelper.IsNullOrEmpty(orgCodeList)){
                List<ManagerOrganization> orgList = managerOrganizationService.selectOrganizationNameByCodeList(orgCodeList);
                if(!StringHelper.IsNullOrEmpty(orgList)){
                    listData.forEach(t->{
                        Optional<String> orgName= orgList.stream().filter(m->m.getOrgCode().equals(t.getOrgCode())).map(m->m.getOrganizationName()).findFirst();
                        if(orgName.isPresent()){
                            t.setOrgName(orgName.get());
                        }
                    });
                }
            }
        }
        return listData;
    }

    public Integer selectTeacherListCount(TeacherSearchModel search, UserSessionHelper helper) {
        if (search == null) {
            return null;
        }

        if(!helper.getOrgType().equals(ManagerHelper.OrgType)){
            search.setOrgCode(helper.getOrgCode());
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", search.getStatus());
        map.put("teacherName", search.getTeacherName());
        map.put("orgCode", search.getOrgCode());
        map.put("teacherIdList", search.getTeacherIdList());
        return this.managerTeacherMapper.findTeacherListCount(map);
    }

    //region 李程用

    public Map selectLectureNameByUserId(Set<String> teacherIdList) {
        if (teacherIdList == null || teacherIdList.isEmpty()) {
            return null;
        }
        List<String> result = new ArrayList<>(teacherIdList);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("teacherIdList", result);
        List<ManagerTeacher> teacherList = this.managerTeacherMapper.findTeacherEquAnd(map);
        if (StringHelper.IsNullOrEmpty(teacherList)) {
            return null;
        }

        Map<String, String> resultData = new HashMap<String, String>();
        teacherList.forEach(t ->
                resultData.put(t.getTeacherId(), t.getTeacherName()));

        return resultData;
    }

    @Override
    public ManagerTeacher selectTeacherByTeacherIdInter(String paramString) {
        return managerTeacherMapper.selectByPrimaryKey(paramString);
    }

    //endregion


    //推荐教师操作
    public Integer insertRecommendTeacherList(ManagerTeacherModel model, UserSessionHelper helper){
        if(model==null || StringHelper.IsNullOrEmpty(model.getTeacherIdList())){
            return 0;
        }

        return managerTeacherMapper.insertRecommendTeacherList(model);
    }
    public Integer deleteRecommendTeacherList(ManagerTeacherModel model, UserSessionHelper helper){
        if(model ==null || StringHelper.IsNullOrWhiteSpace(model.getOrgCode())){
            return 0;
        }
        return managerTeacherMapper.deleteRecommendTeacherList(model);
    }

    @Override
    public RetResult selectByTeacherName(TeacherTeamSearch teacher, String orgCode, Integer page, Integer size) {
        Map param = MapUtils.initMap();
        if(!StringUtils.isBlank(teacher.getTeacherName())){
            param.put("teacherName", teacher.getTeacherName());
        }
        if(!StringUtils.isBlank(orgCode)){
            param.put("orgCode", orgCode);
        }
        param.put("status", 1);
        PageHelper.startPage(page, size);
        List<ManagerTeacher> teachers = managerTeacherMapper.findTeacherListWithOrg(param);
        PageInfo<ManagerTeacher> pageInfo = new PageInfo<>(teachers);
        return RetResponse.makeOKRsp(pageInfo).setTotal(pageInfo.getTotal());
    }
}