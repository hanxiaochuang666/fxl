package com.by.blcu.manager.controller;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.manager.model.ManagerTeacherApply;
import com.by.blcu.manager.service.ManagerTeacherApplyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: ManagerTeacherApplyController类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@RestController
@RequestMapping("/managerTeacherApply")
public class ManagerTeacherApplyController {

    @Resource
    private ManagerTeacherApplyService managerTeacherApplyService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(ManagerTeacherApply managerTeacherApply) throws Exception{
      managerTeacherApply.setTeacherApplyId(ApplicationUtils.getUUID());
       Integer state = managerTeacherApplyService.insert(managerTeacherApply);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = managerTeacherApplyService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(ManagerTeacherApply managerTeacherApply) throws Exception {
        Integer state = managerTeacherApplyService.update(managerTeacherApply);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<ManagerTeacherApply> selectById(@RequestParam String id) throws Exception {
        ManagerTeacherApply managerTeacherApply = managerTeacherApplyService.selectById(id);
        return RetResponse.makeOKRsp(managerTeacherApply);
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<ManagerTeacherApply>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<ManagerTeacherApply>> list(@RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<ManagerTeacherApply> list = managerTeacherApplyService.selectAll();
        PageInfo<ManagerTeacherApply> pageInfo = new PageInfo<ManagerTeacherApply>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }
}