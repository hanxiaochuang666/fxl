package com.by.blcu.manager.controller;

import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.model.ManagerAccount;
import com.by.blcu.manager.model.ManagerPermission;
import com.by.blcu.manager.model.ManagerRole;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.model.sql.OrgPermission;
import com.by.blcu.manager.service.ManagerRoleService;
import com.by.blcu.manager.umodel.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 耿鹤闯
 * @Description: ManagerRoleController类
 * @date 2019/08/05 14:37
 */
@CheckToken
@RestController
@RequestMapping("/managerRole")
@Api(tags = "Manager角色接口API", description = "包含接口：\n" +
        "1、后台角色-添加角色【addRole】\n" +
        "2、后台角色-修改角色【updateRole】\n" +
        "3、后台角色-删除角色【deleteByRoleIdList】\n" +
        "4、后台角色-根据角色Id查询角色【selectRoleByRoleId】\n" +
        "5、后台角色-查询角色列表【searchRole】\n" +

        "6、角色权限-获取角色中已有的权限【getRolePermission】\n" +
        "7、角色权限-获取角色中未添加的权限【getAccountRoleExclude】\n" +
        "8、角色权限-获取角色的最大权限【getAccountRolePremission】\n" +
        "9、角色权限-获取当前机构下所有的权限【getPermissionAllByOrgCode】\n" +
        "10、角色权限-给角色添加权限【addRolePermission】\n" +
        "11、角色权限-给角色去除权限【deleteRolePermission】\n"+
        "12、角色权限-给角色更新权限【updateRolePermission】\n")
public class ManagerRoleController extends ManagerBase {

    @Resource
    private ManagerRoleService managerRoleService;

    //region 基本操作

    @ApiOperation(value = "后台角色-添加角色", notes = "添加角色")
    @PostMapping("/addRole")
    @RequiresPermissions("system:role")
    public RetResult<Integer> addRole(@RequestBody ManagerRole managerRole) throws Exception {
        return managerRoleService.addRole(managerRole,userSessionHelper);
    }

    @ApiOperation(value = "后台角色-修改角色", notes = "修改角色信息，比如教师信息等")
    @PostMapping("/updateRole")
    @RequiresPermissions("system:role")
    public RetResult<Integer> updateRole(@RequestBody ManagerRole managerRole) throws Exception {
        return managerRoleService.updateRole(managerRole,userSessionHelper);
    }

    @ApiOperation(value = "后台角色-删除角色", notes = "通过角色Id列表，批量删除角色")
    @PostMapping("/deleteByRoleIdList")
    @RequiresPermissions("system:role")
    public RetResult<Integer> deleteByRoleIdList(@ApiParam(name = "roleIdList", value = "角色Id列表")@RequestBody List<String> roleIdList) throws Exception {
        return managerRoleService.deleteByRoleIdList(roleIdList,userSessionHelper);
    }

    @ApiOperation(value = "后台角色-根据角色Id查询角色", notes = "根据角色Id，查找角色")
    @GetMapping("/selectRoleByRoleId")
    @RequiresPermissions("system:role")
    public RetResult<ManagerRole> selectRoleByRoleId(@ApiParam(name = "roleId", value = "角色Id") @RequestParam String roleId) throws Exception {
        return managerRoleService.selectRoleByRoleId(roleId,userSessionHelper);
    }

    @ApiOperation(value = "后台角色-查询角色列表", notes = "查询角色，多条件分页查询")
    @GetMapping("/searchRole")
    @RequiresPermissions("system:role")
    public RetResult<PageInfo<ManagerRole>> searchRole(RoleSearchModel search) throws Exception {

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

        Integer count = managerRoleService.selectRoleListCount(search,userSessionHelper);
        PageInfo<ManagerRole> pageInfo=null;
        if(count<1){
            pageInfo = new PageInfo<ManagerRole>();
        }
        else{
            PageHelper.startPage(page, size);
            List<ManagerRole> list = managerRoleService.selectRoleList(search,userSessionHelper);
            pageInfo = new PageInfo<ManagerRole>(list);
            pageInfo.setTotal(count);
        }
        return RetResponse.makeOKRsp(pageInfo);
    }

    //endregion

    //region 给角色添加权限

    @ApiOperation(value = "角色权限-获取角色中已有的权限", notes = "获取角色中已有的权限")
    @GetMapping("/getRolePermission")
    @RequiresPermissions("system:role")
    public RetResult<List<OrgPermission>> getRolePermission(@ApiParam(name = "orgCode", value = "所属组织编码") @RequestParam String orgCode, @ApiParam(name = "roleId", value = "角色Id")  @RequestParam String roleId) throws Exception {
        List<OrgPermission> resultData = managerRoleService.getRolePermission(orgCode,roleId,userSessionHelper);
        return RetResponse.makeOKRsp(resultData);
    }

    @ApiOperation(value = "角色权限-获取角色中未添加的权限", notes = "获取角色中未添加的权限")
    @GetMapping("/getAccountRoleExclude")
    @RequiresPermissions("system:role")
    public RetResult<List<OrgPermission>> getAccountRoleExclude(@ApiParam(name = "orgCode", value = "所属组织编码")  @RequestParam String orgCode,@ApiParam(name = "roleId", value = "角色Id")  @RequestParam String roleId, @ApiParam(name = "userId", value = "用户Id") @RequestParam String userId) throws Exception {
        List<OrgPermission> resultData = managerRoleService.getAccountRoleExclude(orgCode,roleId,userId,userSessionHelper);
        return RetResponse.makeOKRsp(resultData);
    }

    @ApiOperation(value = "角色权限-获取角色的最大权限", notes = "获取角色的最大权限")
    @GetMapping("/getAccountRolePremission")
    @RequiresPermissions("system:role")
    public RetResult<List<OrgPermission>> getAccountRolePremission(@ApiParam(name = "orgCode", value = "所属组织编码")  @RequestParam String orgCode,@ApiParam(name = "roleId", value = "角色Id")  @RequestParam String roleId, @ApiParam(name = "userId", value = "用户Id") @RequestParam String userId) throws Exception {
        List<OrgPermission> resultData = managerRoleService.getAccountRolePermission(orgCode,roleId,userId,userSessionHelper);
        return RetResponse.makeOKRsp(resultData);
    }

    @ApiOperation(value = "角色权限-获取当前机构下所有的权限", notes = "获取当前机构下所有的权限")
    @GetMapping("/getPermissionAllByOrgCode")
    @RequiresPermissions("system:role")
    public RetResult<List<OrgPermission>> getPermissionAllByOrgCode(@ApiParam(name = "orgCode", value = "所属组织编码")  @RequestParam String orgCode) throws Exception {
        List<OrgPermission> resultData = managerRoleService.getPermissionAllByOrgCode(orgCode,userSessionHelper);
        return RetResponse.makeOKRsp(resultData);
    }

    @ApiOperation(value = "角色权限-给角色添加权限", notes = "给角色添加权限")
    @PostMapping("/addRolePermission")
    @RequiresPermissions("system:role")
    public RetResult<Integer> addRolePermission(@RequestBody RolePermissionRModel accountRoleR) throws Exception {
        return managerRoleService.addRolePermission(accountRoleR,userSessionHelper);
    }

    @ApiOperation(value = "角色权限-给角色去除权限", notes = "给角色去除权限")
    @PostMapping("/deleteRolePermission")
    @RequiresPermissions("system:role")
    public RetResult<Integer> deleteRolePermission(@RequestBody RolePermissionRModel accountRoleR) throws Exception {
        return managerRoleService.deleteRolePermission(accountRoleR,userSessionHelper);
    }

    @ApiOperation(value = "角色权限-给角色更新权限", notes = "给角色更新权限，包括新增以及删除")
    @PostMapping("/updateRolePermission")
    @RequiresPermissions("system:role")
    public RetResult<Integer> updateRolePermission(@RequestBody RolePermissionRModel accountRoleR) throws Exception {
        return managerRoleService.updateRolePermission(accountRoleR,userSessionHelper);
    }

    //endregion


}