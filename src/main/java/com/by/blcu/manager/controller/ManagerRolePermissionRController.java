package com.by.blcu.manager.controller;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.manager.model.ManagerRolePermissionR;
import com.by.blcu.manager.service.ManagerRolePermissionRService;
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
* @Description: ManagerRolePermissionRController类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@RestController
@RequestMapping("/managerRolePermissionR")
@ApiIgnore
public class ManagerRolePermissionRController {

    @Resource
    private ManagerRolePermissionRService managerRolePermissionRService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(ManagerRolePermissionR managerRolePermissionR) throws Exception{
      managerRolePermissionR.setRolePermissionRId(ApplicationUtils.getUUID());
       Integer state = managerRolePermissionRService.insert(managerRolePermissionR);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = managerRolePermissionRService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(ManagerRolePermissionR managerRolePermissionR) throws Exception {
        Integer state = managerRolePermissionRService.update(managerRolePermissionR);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<ManagerRolePermissionR> selectById(@RequestParam String id) throws Exception {
        ManagerRolePermissionR managerRolePermissionR = managerRolePermissionRService.selectById(id);
        return RetResponse.makeOKRsp(managerRolePermissionR);
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<ManagerRolePermissionR>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<ManagerRolePermissionR>> list(@RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<ManagerRolePermissionR> list = managerRolePermissionRService.selectAll();
        PageInfo<ManagerRolePermissionR> pageInfo = new PageInfo<ManagerRolePermissionR>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }
}