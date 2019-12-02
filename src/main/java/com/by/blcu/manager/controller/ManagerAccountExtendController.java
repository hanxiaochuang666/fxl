package com.by.blcu.manager.controller;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.manager.model.ManagerAccountExtend;
import com.by.blcu.manager.service.ManagerAccountExtendService;
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
* @Description: ManagerAccountExtendController类
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
@RestController
@RequestMapping("/managerAccountExtend")
@ApiIgnore
public class ManagerAccountExtendController {

    @Resource
    private ManagerAccountExtendService managerAccountExtendService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(ManagerAccountExtend managerAccountExtend) throws Exception{
      managerAccountExtend.setAccountId(ApplicationUtils.getUUID());
       Integer state = managerAccountExtendService.insert(managerAccountExtend);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = managerAccountExtendService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(ManagerAccountExtend managerAccountExtend) throws Exception {
        Integer state = managerAccountExtendService.update(managerAccountExtend);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<ManagerAccountExtend> selectById(@RequestParam String id) throws Exception {
        ManagerAccountExtend managerAccountExtend = managerAccountExtendService.selectById(id);
        return RetResponse.makeOKRsp(managerAccountExtend);
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<ManagerAccountExtend>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<ManagerAccountExtend>> list(@RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<ManagerAccountExtend> list = managerAccountExtendService.selectAll();
        PageInfo<ManagerAccountExtend> pageInfo = new PageInfo<ManagerAccountExtend>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }
}