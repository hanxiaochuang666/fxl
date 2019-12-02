package com.by.blcu.mall.controller;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.mall.model.MallPurchaseCommodity;
import com.by.blcu.mall.service.MallPurchaseCommodityService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: MallPurchaseCommodityController类
* @author 李程
* @date 2019/11/05 18:43
*/
@RestController
@RequestMapping("/mallPurchaseCommodity")
public class MallPurchaseCommodityController {

    @Resource
    private MallPurchaseCommodityService mallPurchaseCommodityService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(MallPurchaseCommodity mallPurchaseCommodity) throws Exception{
      mallPurchaseCommodity.setOcId(ApplicationUtils.getUUID());
       Integer state = mallPurchaseCommodityService.insert(mallPurchaseCommodity);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = mallPurchaseCommodityService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(MallPurchaseCommodity mallPurchaseCommodity) throws Exception {
        Integer state = mallPurchaseCommodityService.update(mallPurchaseCommodity);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<MallPurchaseCommodity> selectById(@RequestParam String id) throws Exception {
        MallPurchaseCommodity mallPurchaseCommodity = mallPurchaseCommodityService.selectById(id);
        return RetResponse.makeOKRsp(mallPurchaseCommodity);
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<MallPurchaseCommodity>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<MallPurchaseCommodity>> list(@RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<MallPurchaseCommodity> list = mallPurchaseCommodityService.selectAll();
        PageInfo<MallPurchaseCommodity> pageInfo = new PageInfo<MallPurchaseCommodity>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }
}