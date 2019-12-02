package com.by.blcu.manager.controller;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.manager.model.ManagerAccountRoleR;
import com.by.blcu.manager.service.ManagerAccountRoleRService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: ManagerAccountRoleRController类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@RestController
@RequestMapping("/managerAccountRoleR")
@ApiIgnore
public class ManagerAccountRoleRController {

    @Resource
    private ManagerAccountRoleRService managerAccountRoleRService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(ManagerAccountRoleR managerAccountRoleR) throws Exception{
      managerAccountRoleR.setAccountRoleRId(ApplicationUtils.getUUID());
       Integer state = managerAccountRoleRService.insert(managerAccountRoleR);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = managerAccountRoleRService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(ManagerAccountRoleR managerAccountRoleR) throws Exception {
        Integer state = managerAccountRoleRService.update(managerAccountRoleR);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<ManagerAccountRoleR> selectById(@RequestParam String id) throws Exception {
        ManagerAccountRoleR managerAccountRoleR = managerAccountRoleRService.selectById(id);
        return RetResponse.makeOKRsp(managerAccountRoleR);
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<ManagerAccountRoleR>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<ManagerAccountRoleR>> list(@RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<ManagerAccountRoleR> list = managerAccountRoleRService.selectAll();
        PageInfo<ManagerAccountRoleR> pageInfo = new PageInfo<ManagerAccountRoleR>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }
}