package com.by.blcu.manager.controller;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.manager.model.ManagerLog;
import com.by.blcu.manager.model.sql.InputLog;
import com.by.blcu.manager.service.ManagerLogService;
import com.by.blcu.manager.umodel.TeacherSearchModel;
import com.by.blcu.manager.umodel.TeacherShowModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
* @Description: ManagerLogController类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@RestController
@RequestMapping("/managerLog")
@Api(tags = "Manager系统日志", description = "包含接口：\n" +
        "1、系统日志-添加【addLog】\n" +
        "2、系统日志-查询【searchLog】")
public class ManagerLogController extends ManagerBase {

    @Resource
    private ManagerLogService managerLogService;

    @ApiOperation(value = "系统日志-添加", notes = "添加系统日志")
    @PostMapping("/addLog")
    @RequiresPermissions("manager:log")
    public RetResult<Integer> addLog(ManagerLog managerLog) throws Exception{
        return managerLogService.addLog(managerLog,userSessionHelper);
    }

    @ApiOperation(value = "系统日志-查询", notes = "查询系统日志，多条件分页查询")
    @GetMapping({"/searchLog"})
    @RequiresPermissions("manager:log")
    public RetResult<PageInfo<ManagerLog>> searchLog(InputLog search) throws Exception {
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

        Integer count = this.managerLogService.selectLogListCount(search);
        PageInfo<ManagerLog> pageInfo = null;
        if (count.intValue() < 1) {
            pageInfo = new PageInfo<ManagerLog>();
        } else {

            PageHelper.startPage(page.intValue(), size.intValue());
            List<ManagerLog> list = this.managerLogService.selectLogList(search);
            pageInfo = new PageInfo<ManagerLog>(list);
            pageInfo.setTotal(count.intValue());
        }
        return RetResponse.makeOKRsp(pageInfo);
    }
}