package com.by.blcu.manager.controller;

import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.common.UserHelper;
import com.by.blcu.manager.model.ManagerPermission;
import com.by.blcu.manager.model.ManagerPermission;
import com.by.blcu.manager.model.extend.PermissionMenu;
import com.by.blcu.manager.model.extend.PermissionTree;
import com.by.blcu.manager.service.ManagerPermissionService;
import com.by.blcu.manager.umodel.PermissionTreeModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
* @Description: ManagerPermissionController类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@CheckToken
@RestController
@RequestMapping("/managerPermission")
@Api(tags = "Manager权限接口API", description = "包含接口：\n" +
        "1、权限管理-添加权限【addPermission】\n" +
        "2、权限管理-修改权限【updatePermission】\n" +
        "3、权限管理-删除权限【deleteByPermissionIdList】\n" +
        "4、权限管理-查询权限【selectPermissionByPermissionId】\n"+
        "5、权限管理-获取权限树【selectPermission】\n" +
        "6、菜单-获取菜单【selectMenu】\n")
public class ManagerPermissionController extends ManagerBase {

    @Resource
    private ManagerPermissionService managerPermissionService;

    //region 基本操作

    @ApiOperation(value = "权限管理-添加权限", notes = "添加权限")
    @PostMapping("/addPermission")
    @RequiresPermissions("system:permission")
    public RetResult<Integer> addPermission(@RequestBody ManagerPermission managerPermission) throws Exception {
        return managerPermissionService.addPermission(managerPermission,userSessionHelper);
    }

    @ApiOperation(value = "权限管理-修改权限", notes = "修改权限信息，比如教师信息等")
    @PostMapping("/updatePermission")
    @RequiresPermissions("system:permission")
    public RetResult<Integer> updatePermission(@RequestBody ManagerPermission managerPermission) throws Exception {
        return managerPermissionService.updatePermission(managerPermission,userSessionHelper);
    }

    @ApiOperation(value = "权限管理-删除权限", notes = "通过权限Id列表，批量删除权限")
    @PostMapping("/deleteByPermissionIdList")
    @RequiresPermissions("system:permission")
    public RetResult<Integer> deleteByPermissionIdList(@ApiParam(name = "permissionIdList", value = "权限名列表")@RequestBody List<String> permissionIdList) throws Exception {
        return managerPermissionService.deleteByPermissionIdList(permissionIdList,userSessionHelper);
    }

    @ApiOperation(value = "权限管理-查询权限", notes = "根据权限Id，查找权限")
    @GetMapping("/selectPermissionByPermissionId")
    @RequiresPermissions("system:permission")
    public RetResult<ManagerPermission> selectPermissionByPermissionId(@ApiParam(name = "permissionId", value = "权限表Id") @RequestParam String permissionId) throws Exception {
        ManagerPermission user = new ManagerPermission();
        return managerPermissionService.selectPermissionByPermissionId(permissionId);
    }

    @ApiOperation(value = "权限管理-获取权限树", notes = "获取权限树，全部")
    @GetMapping("/selectPermission")
    @RequiresPermissions("system:permission")
    public RetResult<List<PermissionTree>> selectPermission(@ApiParam(name = "orgCode", value = "组织编码") @RequestParam String orgCode,@ApiParam(name = "status", value = "菜单状态（1 正常 ，2 停用）") @RequestParam Integer status) throws Exception {
        List<PermissionTree> list = managerPermissionService.selectPermissionTree(orgCode,status);
        return RetResponse.makeOKRsp(list);
    }

    //endregion

    //region 菜单

    @ApiOperation(value = "菜单-获取菜单", notes = "获取后台菜单")
    @GetMapping("/selectMenu")
    public RetResult<List<PermissionMenu>> selectMenu(@ApiParam(name = "orgCode", value = "组织编码") @RequestParam String orgCode,HttpServletRequest request) throws Exception {
        List<PermissionMenu> list = managerPermissionService.selectManagerTree(orgCode, userNameBse);
        return RetResponse.makeOKRsp(list);
    }

    //endregion

}