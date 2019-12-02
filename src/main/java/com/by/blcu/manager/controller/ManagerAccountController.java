package com.by.blcu.manager.controller;

import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.manager.common.ReflexHelper;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.model.ManagerAccount;
import com.by.blcu.manager.model.ManagerRole;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.service.ManagerAccountService;
import com.by.blcu.manager.service.SsoUserService;
import com.by.blcu.manager.umodel.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @Description: ManagerAccountController类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@CheckToken
@RestController
@RequestMapping("/managerAccount")
@Api(tags = "Manager用户接口API", description = "包含接口：\n" +
        "1、机构用户管理-根据(用户账号/手机号/邮箱)查找用户【findAccountList】\n" +
        "2、机构用户管理-根据(用户账号/手机号/邮箱)查找一个用户【findAccount】\n" +
        "3、机构用户管理-添加用户【addAccount】\n" +
        "4、机构用户管理-修改用户【updateAccount】\n" +
        "5、机构用户管理-删除用户【deleteByUserNameList】\n" +
        "6、机构用户管理-根据用户账号查询用户【selectAccountByUserName】\n" +
        "7、机构用户管理-根据用户Id查询用户【selectAccountByUserId】\n" +
        "8、机构用户管理-查询用户列表【searchAccount】\n" +
        "9、用户角色-取当前机构下用户已有角色【getAccountRole】\n" +
        "10、用户角色-获取当前机构下未加入到当前用户的所有角色【getAccountRoleExclude】\n" +
        "11、用户角色-获取当前机构下所有角色【getRoleAllByOrgCode】\n" +
        "12、用户角色-给用户添加角色【addAccountRole】\n" +
        "13、用户角色-给用户去除角色【deleteAccountRole】\n"+
        "14、用户角色-给用户更新角色【updateAccountRole】\n")
public class ManagerAccountController extends ManagerBase {

    @Resource
    private ManagerAccountService managerAccountService;

    @Resource
    private SsoUserService ssoUserService;

    // region 后端接口: 增删改查

    @ApiOperation(tags="1",value = "机构用户管理-根据(用户账号/手机号/邮箱)查找用户", notes = "根据用户账号/手机号/邮箱，查找用户列表")
    @GetMapping("/findAccountList")
    @RequiresPermissions("system:account")
    public RetResult<List<SsoUser>> findAccountList(@ApiParam(name = "search", value = "用户账号/手机号/邮箱") @RequestParam String search) throws Exception {
        return managerAccountService.findAccountList(search,userSessionHelper);
    }

    @ApiOperation(tags="2",value = "机构用户管理-根据(用户账号/手机号/邮箱)查找一个用户", notes = "根据手机号、账号，查找用户")
    @GetMapping("/findAccount")
    @RequiresPermissions("system:account")
    public RetResult<SsoUser> findAccount(@ApiParam(name = "search", value = "用户账号/手机号/邮箱") @RequestParam String search) throws Exception {
        return managerAccountService.findAccount(search,userSessionHelper);
    }

    @ApiOperation(value = "机构用户管理-添加用户", notes = "添加用户")
    @PostMapping("/addAccount")
    @RequiresPermissions("system:account")
    public RetResult<Integer> addAccount(@RequestBody AccountUpdateModel user) throws Exception {
        return managerAccountService.addAccount(user,userSessionHelper);
    }

    @ApiOperation(value = "机构用户管理-修改用户", notes = "修改用户信息，比如教师信息等")
    @PostMapping("/updateAccount")
    @RequiresPermissions("system:account")
    public RetResult<Integer> updateAccount(@RequestBody AccountUpdateModel user) throws Exception {
        return managerAccountService.updateAccount(user,userSessionHelper);
    }

    @ApiOperation(value = "机构用户管理-删除用户", notes = "通过用户名列表，批量删除用户")
    @PostMapping("/deleteByUserNameList")
    @RequiresPermissions("system:account")
    public RetResult<Integer> deleteByUserNameList(@ApiParam(name = "userNameList", value = "用户名列表") @RequestBody List<String> userNameList) throws Exception {
        return managerAccountService.deleteByUserNameList(userNameList,userSessionHelper);
    }

    @ApiOperation(value = "机构用户管理-根据用户账号查询用户", notes = "根据用户账号，查找用户")
    @GetMapping("/selectAccountByUserName")
    @RequiresPermissions("system:account")
    public RetResult<AccountUpdateModel> selectAccountByUserName(@ApiParam(name = "userName", value = "用户账号") @RequestParam String userName,@ApiParam(name = "orgCode", value = "所属组织编码") @RequestParam String orgCode) throws Exception {
        return managerAccountService.selectAccountByUserName(userName,orgCode,userSessionHelper);
    }

    @ApiOperation(value = "机构用户管理-根据用户Id查询用户", notes = "根据用户Id，查找用户")
    @GetMapping("/selectAccountByUserId")
    @RequiresPermissions("system:account")
    public RetResult<AccountUpdateModel> selectAccountByUserId(@ApiParam(name = "userId", value = "用户Id") @RequestParam String userId,@ApiParam(name = "orgCode", value = "所属组织编码") @RequestParam String orgCode) throws Exception {
        return managerAccountService.selectAccountByUserId(userId,orgCode,userSessionHelper);
    }

    @ApiOperation(value = "机构用户管理-查询用户列表", notes = "查询用户列表，多条件分页查询")
    @GetMapping("/searchAccount")
    @RequiresPermissions("system:account")
    public RetResult<PageInfo<AccountUpdateModel>> searchAccount(AccountSearchModel search) throws Exception {
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

        Integer count = managerAccountService.selectAccountListCount(search,userSessionHelper);
        PageHelper.startPage(page, size);
        List<AccountUpdateModel> list = managerAccountService.selectAccountList(search,userSessionHelper);
        if(StringHelper.IsNullOrEmpty(list)){
            list=new ArrayList<>();
        }
        PageInfo<AccountUpdateModel> pageInfo = new PageInfo<AccountUpdateModel>(list);
        pageInfo.setTotal(count);
        return RetResponse.makeOKRsp(pageInfo);
    }

    //endregion

    //region 用户角色

    @ApiOperation(value = "用户角色-取当前机构下用户已有角色", notes = "取当前机构下用户已有角色")
    @GetMapping("/getAccountRole")
    @RequiresPermissions("system:account")
    public RetResult<List<ManagerRole>> getAccountRole(@ApiParam(name = "orgCode", value = "所属组织编码") @RequestParam String orgCode, @ApiParam(name = "userId", value = "用户Id")  @RequestParam String userId) throws Exception {
        return managerAccountService.getAccountRole(orgCode,userId,userSessionHelper);
    }

    @ApiOperation(value = "用户角色-获取当前机构下未加入到当前用户的所有角色", notes = "获取当前机构下未加入到当前用户的所有角色")
    @GetMapping("/getAccountRoleExclude")
    @RequiresPermissions("system:account")
    public RetResult<List<ManagerRole>> getAccountRoleExclude(@ApiParam(name = "orgCode", value = "所属组织编码")  @RequestParam String orgCode,@ApiParam(name = "userId", value = "用户Id") @RequestParam String userId) throws Exception {
        return managerAccountService.getAccountRoleExclude(orgCode,userId,userSessionHelper);
    }

    @ApiOperation(value = "用户角色-获取当前机构下所有角色", notes = "获取当前机构下所有角色")
    @GetMapping("/getRoleAllByOrgCode")
    @RequiresPermissions("system:account")
    public RetResult<List<ManagerRole>> getRoleAllByOrgCode(@ApiParam(name = "orgCode", value = "所属组织编码")  @RequestParam String orgCode) throws Exception {
        return managerAccountService.getRoleAllByOrgCode(orgCode,userSessionHelper);
    }

    @ApiOperation(value = "用户角色-给用户添加角色", notes = "给用户添加角色")
    @PostMapping("/addAccountRole")
    @RequiresPermissions("system:account")
    public RetResult<Integer> addAccountRole(@RequestBody AccountRoleRModel accountRoleR) throws Exception {
        return managerAccountService.addAccountRole(accountRoleR,userSessionHelper);
    }

    @ApiOperation(value = "用户角色-给用户去除角色", notes = "删除用户的角色")
    @PostMapping("/deleteAccountRole")
    @RequiresPermissions("system:account")
    public RetResult<Integer> deleteAccountRole(@RequestBody AccountRoleRModel accountRoleR) throws Exception {
        return managerAccountService.deleteAccountRole(accountRoleR,userSessionHelper);
    }

    @ApiOperation(value = "用户角色-给用户更新角色", notes = "给用户更新角色，包括新增以及删除")
    @PostMapping("/updateAccountRole")
    @RequiresPermissions("system:account")
    public RetResult<Integer> updateAccountRole(@RequestBody AccountRoleRModel accountRoleR) throws Exception {
        return managerAccountService.updateAccountRole(accountRoleR,userSessionHelper);
    }
    //endregion




}