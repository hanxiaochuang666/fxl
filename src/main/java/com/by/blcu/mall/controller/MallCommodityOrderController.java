package com.by.blcu.mall.controller;


import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.mall.model.MallCommodityOrder;
import com.by.blcu.mall.service.MallCommodityOrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: MallCommodityOrderController类
* @author 李程
* @date 2019/09/09 11:23
*/
@RestController
@RequestMapping("/mallCommodityOrder")
public class MallCommodityOrderController {

    @Resource
    private MallCommodityOrderService mallCommodityOrderService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(MallCommodityOrder mallCommodityOrder) throws Exception{
      mallCommodityOrder.setCoId(ApplicationUtils.getUUID());
       Integer state = mallCommodityOrderService.insert(mallCommodityOrder);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = mallCommodityOrderService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(MallCommodityOrder mallCommodityOrder) throws Exception {
        Integer state = mallCommodityOrderService.update(mallCommodityOrder);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<MallCommodityOrder> selectById(@RequestParam String id) throws Exception {
        MallCommodityOrder mallCommodityOrder = mallCommodityOrderService.selectById(id);
        return RetResponse.makeOKRsp(mallCommodityOrder);
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<MallCommodityOrder>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<MallCommodityOrder>> list(@RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<MallCommodityOrder> list = mallCommodityOrderService.selectAll();
        PageInfo<MallCommodityOrder> pageInfo = new PageInfo<MallCommodityOrder>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }
}