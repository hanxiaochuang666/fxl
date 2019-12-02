package com.by.blcu.manager.controller;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.manager.model.ManagerTeacherExp;
import com.by.blcu.manager.service.ManagerTeacherExpService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: ManagerTeacherExpController类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@RestController
@RequestMapping("/managerTeacherExp")
public class ManagerTeacherExpController {

    @Resource
    private ManagerTeacherExpService managerTeacherExpService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(ManagerTeacherExp managerTeacherExp) throws Exception{
      managerTeacherExp.setTeacherExpId(ApplicationUtils.getUUID());
       Integer state = managerTeacherExpService.insert(managerTeacherExp);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = managerTeacherExpService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(ManagerTeacherExp managerTeacherExp) throws Exception {
        Integer state = managerTeacherExpService.update(managerTeacherExp);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<ManagerTeacherExp> selectById(@RequestParam String id) throws Exception {
        ManagerTeacherExp managerTeacherExp = managerTeacherExpService.selectById(id);
        return RetResponse.makeOKRsp(managerTeacherExp);
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<ManagerTeacherExp>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<ManagerTeacherExp>> list(@RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<ManagerTeacherExp> list = managerTeacherExpService.selectAll();
        PageInfo<ManagerTeacherExp> pageInfo = new PageInfo<ManagerTeacherExp>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }
}