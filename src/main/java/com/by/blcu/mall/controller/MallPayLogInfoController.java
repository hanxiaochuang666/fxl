package com.by.blcu.mall.controller;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.mall.model.MallPayLogInfo;
import com.by.blcu.mall.service.MallPayLogInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: MallPayLogInfoController类
* @author 李程
* @date 2019/10/08 19:44
*/
@RestController
@RequestMapping("/mallPayLogInfo")
public class MallPayLogInfoController {

    @Resource
    private MallPayLogInfoService mallPayLogInfoService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(MallPayLogInfo mallPayLogInfo) throws Exception{
      mallPayLogInfo.setPayResponId(ApplicationUtils.getUUID());
       Integer state = mallPayLogInfoService.insert(mallPayLogInfo);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = mallPayLogInfoService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(MallPayLogInfo mallPayLogInfo) throws Exception {
        Integer state = mallPayLogInfoService.update(mallPayLogInfo);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<MallPayLogInfo> selectById(@RequestParam String id) throws Exception {
        MallPayLogInfo mallPayLogInfo = mallPayLogInfoService.selectById(id);
        return RetResponse.makeOKRsp(mallPayLogInfo);
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<MallPayLogInfo>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<MallPayLogInfo>> list(@RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<MallPayLogInfo> list = mallPayLogInfoService.selectAll();
        PageInfo<MallPayLogInfo> pageInfo = new PageInfo<MallPayLogInfo>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }
}