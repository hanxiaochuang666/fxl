package com.by.blcu.manager.controller;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.manager.model.ManagerOrgApply;
import com.by.blcu.manager.service.ManagerOrgApplyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: ManagerOrgApplyController类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@RestController
@RequestMapping("/managerOrgApply")
public class ManagerOrgApplyController {

    @Resource
    private ManagerOrgApplyService managerOrgApplyService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(ManagerOrgApply managerOrgApply) throws Exception{
      managerOrgApply.setOrgApplyId(ApplicationUtils.getUUID());
       Integer state = managerOrgApplyService.insert(managerOrgApply);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = managerOrgApplyService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(ManagerOrgApply managerOrgApply) throws Exception {
        Integer state = managerOrgApplyService.update(managerOrgApply);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<ManagerOrgApply> selectById(@RequestParam String id) throws Exception {
        ManagerOrgApply managerOrgApply = managerOrgApplyService.selectById(id);
        return RetResponse.makeOKRsp(managerOrgApply);
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<ManagerOrgApply>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<ManagerOrgApply>> list(@RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<ManagerOrgApply> list = managerOrgApplyService.selectAll();
        PageInfo<ManagerOrgApply> pageInfo = new PageInfo<ManagerOrgApply>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }
}