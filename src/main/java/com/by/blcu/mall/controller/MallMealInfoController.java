package com.by.blcu.mall.controller;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.mall.model.MallMealInfo;
import com.by.blcu.mall.service.MallMealInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: MallMealInfoController类
* @author 李程
* @date 2019/10/24 17:23
*/
@RestController
@RequestMapping("/mallMealInfo")
public class MallMealInfoController {

    @Resource
    private MallMealInfoService mallMealInfoService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(MallMealInfo mallMealInfo) throws Exception{
      mallMealInfo.setId(ApplicationUtils.getUUID());
       Integer state = mallMealInfoService.insert(mallMealInfo);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = mallMealInfoService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(MallMealInfo mallMealInfo) throws Exception {
        Integer state = mallMealInfoService.update(mallMealInfo);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<MallMealInfo> selectById(@RequestParam String id) throws Exception {
        MallMealInfo mallMealInfo = mallMealInfoService.selectById(id);
        return RetResponse.makeOKRsp(mallMealInfo);
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<MallMealInfo>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<MallMealInfo>> list(@RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<MallMealInfo> list = mallMealInfoService.selectAll();
        PageInfo<MallMealInfo> pageInfo = new PageInfo<MallMealInfo>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }
}