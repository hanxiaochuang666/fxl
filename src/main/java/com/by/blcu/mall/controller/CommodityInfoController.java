package com.by.blcu.mall.controller;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.mall.model.CommodityInfo;
import com.by.blcu.mall.service.CommodityInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: CommodityInfoController类
* @author 李程
* @date 2019/07/29 11:03
*/
@RestController
@RequestMapping("/commodityInfo")
public class CommodityInfoController {

    @Resource
    private CommodityInfoService commodityInfoService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(CommodityInfo commodityInfo) throws Exception{
      commodityInfo.setCommodityId(ApplicationUtils.getUUID());
       Integer state = commodityInfoService.insert(commodityInfo);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = commodityInfoService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(CommodityInfo commodityInfo) throws Exception {
        Integer state = commodityInfoService.update(commodityInfo);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/updateComStatusById")
    public RetResult<Integer> updateComStatusById(String commodityId,int commodityStatus) throws Exception {
        Integer state = commodityInfoService.updateComStatusById(commodityId,commodityStatus);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<CommodityInfo> selectById(@RequestParam String id) throws Exception {
        CommodityInfo commodityInfo = commodityInfoService.selectById(id);
        return RetResponse.makeOKRsp(commodityInfo);
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<CommodityInfo>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<CommodityInfo>> list(@RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<CommodityInfo> list = commodityInfoService.selectAll();
        PageInfo<CommodityInfo> pageInfo = new PageInfo<CommodityInfo>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<CommodityInfo>>
     */
    @PostMapping("/listByCourseName")
    public RetResult<PageInfo<CommodityInfo>> listByCourseName(@RequestParam(defaultValue = "0") Integer page,
                                                   @RequestParam(defaultValue = "0") Integer size,CommodityInfo commodityInfo) throws Exception {
        PageHelper.startPage(page, size);
        List<CommodityInfo> list = commodityInfoService.selectAllByCourseName(commodityInfo);
        PageInfo<CommodityInfo> pageInfo = new PageInfo<CommodityInfo>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }
}