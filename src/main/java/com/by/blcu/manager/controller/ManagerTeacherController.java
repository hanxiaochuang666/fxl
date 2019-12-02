package com.by.blcu.manager.controller;

import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.controller.ManagerTeacherController;
import com.by.blcu.manager.model.ManagerOrganization;
import com.by.blcu.manager.model.ManagerTeacher;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.service.ManagerTeacherService;
import com.by.blcu.manager.umodel.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@CheckToken
@RestController
@RequestMapping({"/managerTeacher"})
@Api(tags = {"Manager名师团队接口API"},  description = "包含接口：\n" +
        "1、后台名师团队-添加教师【addTeacher】\n" +
        "2、后台名师团队-更新教师【updateTeacher】\n" +
        "3、后台名师团队-删除教师【deleteByTeacherIdList】\n" +
        "4、后台名师团队-根据教师Id查询教师【selectTeacherByTeacherId】\n" +
        "5、后台名师团队-查询教师列表【searchTeacher】\n"+

        "6、首页名师团队-查询推存教师列表【selectTeacherRecommend】\n"+
        "7、首页名师团队-分页查询所有教师列表【selectTeacherTeam】\n"+
        "8、首页名师团队-根据教师Id查询教师【selectByTeacherId】\n" +
        "9、教师下拉菜单-获取机构下的教师【getOrganizationList】\n"+

        "10、后台首页名师推荐-获取所有推荐教师Id【selectRecommendTeacherId】\n"+
        "11、后台首页名师推荐与名师推荐-修改排序【updateSort】\n"+
        "12、后台首页名师推荐-添加推荐教师【addRecommendTeacher】\n"+
        "13、后台首页名师推荐-删除推荐教师【deleteRecommendTeacher】\n"+
        "14、后台首页名师推荐-更新推荐教师【updateRecommendTeacher】\n" +
        "15、首页教师搜索-根据教师名称检索【selectByTeacherName】")
public class ManagerTeacherController extends ManagerBase
{
    @Resource
    private ManagerTeacherService managerTeacherService;

    //region 后台名师团队

    @ApiOperation(value = "后台名师团队-添加教师", notes = "添加教师")
    @PostMapping({"/addTeacher"})
    @RequiresPermissions("webset:teacher")
    public RetResult<Integer> addTeacher(@RequestBody ManagerTeacher managerTeacher) throws Exception {
        return this.managerTeacherService.addTeacher(managerTeacher,userSessionHelper);
    }

    @ApiOperation(value = "后台名师团队-更新教师", notes = "更新教师")
    @PostMapping({"/updateTeacher"})
    @RequiresPermissions("webset:teacher")
    public RetResult<Integer> updateTeacher(@RequestBody ManagerTeacher managerTeacher) throws Exception {
        return this.managerTeacherService.updateTeacher(managerTeacher,userSessionHelper);
    }

    @ApiOperation(value = "后台名师团队-删除教师", notes = "删除教师")
    @PostMapping({"/deleteByTeacherIdList"})
    @RequiresPermissions("webset:teacher")
    public RetResult<Integer> deleteByTeacherIdList(@ApiParam(name = "teacherIdList", value = "教师Id列表") @RequestBody List<String> teacherIdList) throws 		Exception {
        return this.managerTeacherService.deleteByTeacherIdList(teacherIdList,userSessionHelper);
    }

    @ApiOperation(value = "后台名师团队-根据教师Id查询教师", notes = "根据教师Id，查询教师")
    @GetMapping({"/selectTeacherByTeacherId"})
    @RequiresPermissions("webset:teacher")
    public RetResult<TeacherShowModel> selectTeacherByTeacherId(@ApiParam(name = "teacherId", value = "教师Id") @RequestParam String teacherId) throws Exception {
        return managerTeacherService.selectTeacherByTeacherId(teacherId,userSessionHelper);
    }

    @ApiOperation(value = "后台名师团队-查询教师列表", notes = "查询教师，多条件分页查询")
    @GetMapping({"/searchTeacher"})
    @RequiresPermissions(value = {"webset:teacher","webset:rdteacher"},logical = Logical.OR)
    public RetResult<PageInfo<TeacherShowModel>> list(TeacherSearchModel search) throws Exception {
        if (search == null) {
            return RetResponse.makeErrRsp("参数不能为空");
        }
        Integer page = Integer.valueOf(1);
        Integer size = Integer.valueOf(1);
        if (search.getPage() == null || search.getPage().intValue() < 0) {
            page =0;
        } else {
            page = search.getPage();
        }
        if (search.getSize() == null || search.getSize().intValue() < 0) {
            size = 0;
        } else {
            size = search.getSize();
        }

        Integer count = this.managerTeacherService.selectTeacherListCount(search,userSessionHelper);
        PageInfo<TeacherShowModel> pageInfo = null;
        if (count.intValue() < 1) {
            pageInfo = new PageInfo<TeacherShowModel>();
        } else {

            PageHelper.startPage(page.intValue(), size.intValue());
            List<TeacherShowModel> list = this.managerTeacherService.selectTeacherList(search,userSessionHelper);
            pageInfo = new PageInfo<TeacherShowModel>(list);
            pageInfo.setTotal(count.intValue());
        }
        return RetResponse.makeOKRsp(pageInfo);
    }

    //endregion

    //region 首页名师团队
    @ApiOperation(value = "首页名师团队-查询推存教师列表", notes = "查询推存教师列表")
    @GetMapping({"/selectTeacherRecommend"})
    public RetResult<List<TeacherShowModel>> selectTeacherRecommend(@ApiParam(name = "pageSize", value = "条数") @RequestParam Integer pageSize) throws Exception {
        Integer page = Integer.valueOf(1);
        Integer size = Integer.valueOf(1);
        if(pageSize==null || pageSize<0){
            size = 0;
            page=0;
        }else {
            size = pageSize;
        }
        TeacherSearchModel search=new TeacherSearchModel();
        search.setType(2);
        PageHelper.startPage(page, size.intValue());
        List<TeacherShowModel> list = this.managerTeacherService.selectTeacherList(search,userSessionHelper);
        PageInfo<TeacherShowModel> pageInfo = new PageInfo<TeacherShowModel>(list);
        return RetResponse.makeOKRsp(pageInfo.getList());
    }
    @ApiOperation(value = "首页名师团队-分页查询所有教师列表", notes = "分页查询所有教师列表")
    @GetMapping({"/selectTeacherTeam"})
    public RetResult<PageInfo<TeacherShowModel>> selectTeacherTeam(TeacherTeamSearch search) throws Exception {
        if (search == null) {
            return RetResponse.makeErrRsp("参数不能为空");
        }
        Integer page = Integer.valueOf(1);
        Integer size = Integer.valueOf(1);
        if (search.getPage() == null || search.getPage().intValue() < 0) {
            page = 0;
        } else {
            page = search.getPage();
        }
        if (search.getSize() == null || search.getSize().intValue() < 0) {
            size = 0;
        } else {
            size = search.getSize();
        }

        TeacherSearchModel teacherSearch = new TeacherSearchModel();
        teacherSearch.setType(search.getType());
        teacherSearch.setStatus(1);
        teacherSearch.setOrgCode(search.getOrgCode());
        teacherSearch.setCcId(search.getCcId());

        Integer count = this.managerTeacherService.selectTeacherListCount(teacherSearch,userSessionHelper);
        PageInfo<TeacherShowModel> pageInfo = null;
        if (count.intValue() < 1) {
            pageInfo = new PageInfo<TeacherShowModel>();
        } else {

            PageHelper.startPage(page.intValue(), size.intValue());
            List<TeacherShowModel> list = this.managerTeacherService.selectTeacherList(teacherSearch,userSessionHelper);
            pageInfo = new PageInfo<TeacherShowModel>(list);
            pageInfo.setTotal(count.intValue());
        }
        return RetResponse.makeOKRsp(pageInfo);
    }

    @ApiOperation(value = "首页名师团队-根据教师Id查询教师", notes = "根据教师Id，查询教师")
    @GetMapping({"/selectByTeacherId"})
    public RetResult<TeacherShowModel> selectByTeacherId(@ApiParam(name = "teacherId", value = "教师Id") @RequestParam String teacherId) throws Exception {
        return managerTeacherService.selectTeacherByTeacherId(teacherId,userSessionHelper);
    }

    @ApiOperation(value = "教师下拉菜单-获取机构下的教师（若都传空，则查所有）", notes = "获取所有机构")
    @GetMapping("/getOrganizationList")
    public RetResult<List<TeacherShowModel>> getTeacherByOrgCode(@ApiParam(name = "orgCode", value = "所属组织编码") @RequestParam String orgCode,@ApiParam(name = "ccId", value = "课程分类") @RequestParam String ccId) throws Exception {
        TeacherSearchModel teacherSearch = new TeacherSearchModel();
        teacherSearch.setStatus(1);
        teacherSearch.setOrgCode(orgCode);
        teacherSearch.setCcId(ccId);
        List<TeacherShowModel> data = managerTeacherService.selectTeacherList(teacherSearch,userSessionHelper);
        if(data==null){
            return RetResponse.makeErrRsp("暂无机构");
        }
        return RetResponse.makeOKRsp(data);
    }
    //endregion

    //region 推荐教师

    @ApiOperation(value = "后台首页名师推荐-获取所有推荐教师Id", notes = "获取所有推荐教师Id")
    @PostMapping({"/selectRecommendTeacherId"})
    @RequiresPermissions("webset:rdteacher")
    public RetResult<List<String>> selectRecommendTeacherId(@RequestBody ManagerTeacherModel search) throws Exception {
        if (search == null || StringHelper.IsNullOrWhiteSpace(search.getOrgCode())) {
            return RetResponse.makeErrRsp("[入驻者组织编码]不能为空");
        }
        TeacherSearchModel teacherSearch = new TeacherSearchModel();
        teacherSearch.setType(2);
        teacherSearch.setOrgCode(search.getOrgCode());
        List<TeacherShowModel> list = this.managerTeacherService.selectTeacherList(teacherSearch,userSessionHelper);
        if(StringHelper.IsNullOrEmpty(list)){
            return RetResponse.makeOKRsp();
        }
        List<String> data =list.stream().map(ManagerTeacher::getTeacherId).collect(Collectors.toList());
        return RetResponse.makeOKRsp(data);
    }

    @ApiOperation(value = "后台名师团队与名师推荐-修改排序", notes = "修改排序")
    @PostMapping({"/updateSort"})
    @RequiresPermissions("webset:rdteacher")
    public RetResult<Integer> updateSort(@RequestBody ManagerTeacherModel managerTeacher) throws Exception {
        if(managerTeacher==null ||  StringHelper.IsNullOrWhiteSpace(managerTeacher.getTeacherId())){
            return RetResponse.makeErrRsp("[教师Id]不能为空");
        }
        ManagerTeacher teacher =new ManagerTeacher();
        teacher.setTeacherId(managerTeacher.getTeacherId());
        teacher.setSort(managerTeacher.getSort());
        return this.managerTeacherService.updateTeacher(teacher,userSessionHelper);
    }

    @ApiOperation(value = "后台首页名师推荐-添加推荐教师", notes = "添加推荐教师")
    @PostMapping({"/addRecommendTeacher"})
    @RequiresPermissions("webset:rdteacher")
    public RetResult<Integer> addRecommendTeacher(@RequestBody ManagerTeacherModel managerTeacher) throws Exception {
        if(managerTeacher==null || (StringHelper.IsNullOrEmpty(managerTeacher.getTeacherIdList()))){
            return RetResponse.makeErrRsp("[教师Id列表]不能为空");
        }
        //禁用状态的教师不允许添加到推荐教师
        List<String> IdList =managerTeacher.getTeacherIdList().stream().distinct().collect(Collectors.toList());
        TeacherSearchModel map =new TeacherSearchModel();
        map.setTeacherIdList(IdList);
        List<TeacherShowModel> list =  managerTeacherService.selectTeacherList(map,userSessionHelper);
        if(StringHelper.IsNullOrEmpty(list)|| list.size()<IdList.size()){
            return RetResponse.makeErrRsp("[教师Id列表]部分教师Id不正确");
        }
        long count = list.stream().filter(t->t.getStatus()==2).count();
        if(count>0){
            return RetResponse.makeErrRsp("禁用状态的教师不允许添加到推荐教师");
        }
        TeacherSearchModel map2 =new TeacherSearchModel();
        map2.setStatus(2);
        map2.setType(2);
        List<TeacherShowModel> list2 =  managerTeacherService.selectTeacherList(map2,userSessionHelper);
        if(!StringHelper.IsNullOrEmpty(list2) && (list2.size()+IdList.size())>5){
            return RetResponse.makeErrRsp("推荐教师的数量不能大于5个");
        }

        Date datetime = new Date();
        managerTeacher.setModifyBy(userNameBse);
        managerTeacher.setModifyTime(datetime);
        Integer state =managerTeacherService.insertRecommendTeacherList(managerTeacher,userSessionHelper);
        return RetResponse.makeOKRsp(state);
    }

    @ApiOperation(value = "后台首页名师推荐-删除推荐教师", notes = "删除推荐教师")
    @PostMapping({"/deleteRecommendTeacher"})
    @RequiresPermissions("webset:rdteacher")
    public RetResult<Integer> deleteRecommendTeacher(@RequestBody ManagerTeacherModel managerTeacher) throws Exception {
        if(managerTeacher==null || StringHelper.IsNullOrEmpty(managerTeacher.getTeacherIdList())){
            return RetResponse.makeErrRsp("[教师Id列表]不能为空");
        }
        Date datetime = new Date();
        managerTeacher.setModifyBy(userSessionHelper.getUserName());
        managerTeacher.setModifyTime(datetime);
        Integer state =managerTeacherService.deleteRecommendTeacherList(managerTeacher,userSessionHelper);
        return RetResponse.makeOKRsp(state);
    }
    @ApiOperation(value = "后台首页名师推荐-更新推荐教师", notes = "更新推荐教师")
    @PostMapping({"/updateRecommendTeacher"})
    @RequiresPermissions("webset:rdteacher")
    @Transactional
    public RetResult<Integer> updateRecommendTeacher(@RequestBody ManagerTeacherModel managerTeacher) throws Exception {
        if (managerTeacher == null || StringHelper.IsNullOrWhiteSpace(managerTeacher.getOrgCode())) {
            return RetResponse.makeErrRsp("[入驻者组织编码]不能为空");
        }
        List<String> insertIdList=new ArrayList<String>();
        List<String> deleteIdList=new ArrayList<String>();
        Integer state=0;
        if(StringHelper.IsNullOrEmpty(managerTeacher.getTeacherIdList())){
            //删除
            state = managerTeacherService.deleteRecommendTeacherList(managerTeacher,userSessionHelper);
        }
        else{
            List<String> IdList =managerTeacher.getTeacherIdList().stream().distinct().collect(Collectors.toList());
            if(IdList.size()!=5){
                return RetResponse.makeErrRsp("推荐教师数量固定为5个");
            }

            //禁用状态的教师不允许添加到推荐教师

            TeacherSearchModel map =new TeacherSearchModel();
            map.setTeacherIdList(IdList);
            List<TeacherShowModel> list =  managerTeacherService.selectTeacherList(map,userSessionHelper);
            if(StringHelper.IsNullOrEmpty(list)|| list.size()<IdList.size()){
                return RetResponse.makeErrRsp("[教师Id列表]部分教师Id不正确");
            }
            long count = list.stream().filter(t->t.getStatus()==2).count();
            if(count>0){
                return RetResponse.makeErrRsp("禁用状态的教师不允许添加到推荐教师");
            }

            TeacherSearchModel teacherSearch = new TeacherSearchModel();
            teacherSearch.setType(2);
            teacherSearch.setOrgCode(managerTeacher.getOrgCode());
            List<TeacherShowModel> hasModel = this.managerTeacherService.selectTeacherList(teacherSearch,userSessionHelper);
            if(!StringHelper.IsNullOrEmpty(hasModel)){
                List<String> hasIdList =hasModel.stream().map(t->t.getTeacherId()).distinct().collect(Collectors.toList());
                List<String> newIdList = managerTeacher.getTeacherIdList().stream().distinct().collect(Collectors.toList());
                insertIdList=new ArrayList<String>(newIdList);
                insertIdList.removeAll(hasIdList);

                deleteIdList = new ArrayList<String>(hasIdList);
                deleteIdList.removeAll(newIdList);
            }
            else{
                insertIdList = managerTeacher.getTeacherIdList().stream().distinct().collect(Collectors.toList());
            }
            Date datetime = new Date();
            //添加
            if(!StringHelper.IsNullOrEmpty(insertIdList)){
                ManagerTeacherModel insertTeacher=new ManagerTeacherModel();
                insertTeacher.setModifyBy(userNameBse);
                insertTeacher.setModifyTime(datetime);
                insertTeacher.setTeacherIdList(insertIdList);
                state = managerTeacherService.insertRecommendTeacherList(insertTeacher,userSessionHelper);
            }
            //删除
            if(!StringHelper.IsNullOrEmpty(deleteIdList)){
                ManagerTeacherModel deleteTeacher=new ManagerTeacherModel();
                deleteTeacher.setOrgCode(managerTeacher.getOrgCode());
                deleteTeacher.setModifyBy(userNameBse);
                deleteTeacher.setModifyTime(datetime);
                deleteTeacher.setTeacherIdList(deleteIdList);
                state = managerTeacherService.deleteRecommendTeacherList(deleteTeacher,userSessionHelper);
            }
        }
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/searchTeacherByName")
    @ApiOperation(value = "首页教师搜索-根据教师名称检索", notes = "名称模糊搜索")
    RetResult selectByTeacherName(@RequestBody TeacherTeamSearch teacher, HttpServletRequest request) throws Exception {
        String orgCode = String.valueOf(request.getAttribute("orgCode"));
        String orgType = String.valueOf(request.getAttribute("orgType"));
        //分页
        Integer page = 0;
        Integer size = 0;
        if(!StringUtils.isEmpty(teacher.getPage())){
            page = teacher.getPage();
        }
        if(!StringUtils.isEmpty(teacher.getSize())){
            size = teacher.getSize();
        }
        //机构类型1 本部机构查所有
        if("1".equals(orgType)){
            return managerTeacherService.selectByTeacherName(teacher, "", page, size);
        }
        return managerTeacherService.selectByTeacherName(teacher, orgCode, page, size);
    }
    //endregion
}

