package com.by.blcu.manager.controller;

import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.manager.model.ManagerOrganization;
import com.by.blcu.manager.model.ManagerOrganization;
import com.by.blcu.manager.service.ManagerOrganizationService;
import com.by.blcu.manager.umodel.OrganizationSearchModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: ManagerOrganizationController类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@RestController
@CheckToken
@RequestMapping("/managerOrganization")
@Api(tags = "Manager机构接口API",  description = "包含接口：\n" +
        "1、后台机构-添加机构【addOrganization】\n" +
        "2、后台机构-更新机构【updateOrganization】\n" +
        "3、后台机构-删除机构【deleteByOrganizationIdList】\n" +
        "4、后台机构-根据机构Id查询机构【selectOrganizationByOrganizationId】\n" +
        "5、后台机构-根据所属组织编码查询机构【selectOrganizationByOrgCode】\n" +
        "6、后台机构-查询机构列表【searchOrganization】\n"+
        "7、机构下拉菜单-获取所有机构【getOrganizationList】\n")
public class ManagerOrganizationController extends ManagerBase {

    @Resource
    private ManagerOrganizationService managerOrganizationService;

    @ApiOperation(value = "后台机构-添加机构", notes = "添加机构")
    @PostMapping("/addOrganization")
    @RequiresPermissions("system:organization")
    public RetResult<Integer> addOrganization(@RequestBody ManagerOrganization managerOrganization) throws Exception {
        return managerOrganizationService.addOrganization(managerOrganization);
    }

    @ApiOperation(value = "后台机构-修改机构", notes = "修改机构信息，比如机构信息等")
    @PostMapping("/updateOrganization")
    @RequiresPermissions("system:organization")
    public RetResult<Integer> updateOrganization(@RequestBody ManagerOrganization managerOrganization) throws Exception {
        return managerOrganizationService.updateOrganization(managerOrganization);
    }

    @ApiOperation(value = "后台机构-删除机构", notes = "通过机构Id列表，批量删除机构")
    @PostMapping("/deleteByOrganizationIdList")
    @RequiresPermissions("system:organization")
    public RetResult<Integer> deleteByOrganizationIdList(@ApiParam(name = "organizationIdList", value = "机构Id列表")@RequestBody List<String> organizationIdList) throws Exception {
        return managerOrganizationService.deleteByOrganizationIdList(organizationIdList);
    }

    @ApiOperation(value = "后台机构-根据机构Id查询机构", notes = "根据机构Id，查找机构")
    @GetMapping("/selectOrganizationByOrganizationId")
    @RequiresPermissions("system:organization")
    public RetResult<ManagerOrganization> selectOrganizationByOrganizationId(@ApiParam(name = "organizationId", value = "机构Id") @RequestParam String organizationId) throws Exception {
        return managerOrganizationService.selectOrganizationByOrganizationId(organizationId);
    }

    @ApiOperation(value = "后台机构-根据所属组织编码查询机构", notes = "根据所属组织编码，查找机构")
    @GetMapping("/selectOrganizationByOrgCode")
    @RequiresPermissions("system:organization")
    public RetResult<ManagerOrganization> selectOrganizationByOrgCode(@ApiParam(name = "orgCode", value = "所属组织编码") @RequestParam String orgCode) throws Exception {
        ManagerOrganization data = managerOrganizationService.selectOrganizationByOrgCode(orgCode);
        if(data==null){
            return RetResponse.makeErrRsp("没有此机构");
        }
        return RetResponse.makeOKRsp(data);
    }

    @ApiOperation(value = "后台机构-查询机构", notes = "查询机构，多条件分页查询")
    @GetMapping("/searchOrganization")
    @RequiresPermissions("system:organization")
    public RetResult<PageInfo<ManagerOrganization>> searchOrganization(OrganizationSearchModel search) throws Exception {

        if (search == null) {
            return RetResponse.makeErrRsp("参数不能为空");
        }
        Integer page = 1;
        Integer size = 1;
        if (search.getPage() == null || search.getPage() < 0) {
            page = 0;
        } else {
            page = search.getPage();
        }
        if (search.getSize() == null || search.getSize() < 0) {
            size = 0;
        } else {
            size = search.getSize();
        }

        Integer count = managerOrganizationService.selectOrganizationListCount(search);
        PageInfo<ManagerOrganization> pageInfo=null;
        if(count<1){
            pageInfo = new PageInfo<ManagerOrganization>();
        }
        else{
            PageHelper.startPage(page, size);
            List<ManagerOrganization> list = managerOrganizationService.selectOrganizationList(search);
            pageInfo = new PageInfo<ManagerOrganization>(list);
            pageInfo.setTotal(count);
        }
        return RetResponse.makeOKRsp(pageInfo);
    }

    @ApiOperation(value = "机构下拉菜单-获取所有机构", notes = "获取所有机构")
    @GetMapping("/getOrganizationList")
    public RetResult<List<ManagerOrganization>> getOrganizationList() throws Exception {
        OrganizationSearchModel searchModel =  new OrganizationSearchModel();
        searchModel.setStatus(1);
        List<ManagerOrganization> data = managerOrganizationService.selectOrganizationList(searchModel);
        if(data==null){
            return RetResponse.makeErrRsp("暂无机构");
        }
        return RetResponse.makeOKRsp(data);
    }

}