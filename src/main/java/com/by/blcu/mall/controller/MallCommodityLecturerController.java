package com.by.blcu.mall.controller;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.mall.model.MallCommodityLecturer;
import com.by.blcu.mall.service.MallCommodityLecturerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: MallCommodityLecturerController类
* @author 李程
* @date 2019/09/04 19:07
*/
@RestController
@RequestMapping("/mallCommodityLecturer")
public class MallCommodityLecturerController {

    @Resource
    private MallCommodityLecturerService mallCommodityLecturerService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(MallCommodityLecturer mallCommodityLecturer) throws Exception{
      mallCommodityLecturer.setClId(ApplicationUtils.getUUID());
       Integer state = mallCommodityLecturerService.insert(mallCommodityLecturer);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = mallCommodityLecturerService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(MallCommodityLecturer mallCommodityLecturer) throws Exception {
        Integer state = mallCommodityLecturerService.update(mallCommodityLecturer);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<MallCommodityLecturer> selectById(@RequestParam String id) throws Exception {
        MallCommodityLecturer mallCommodityLecturer = mallCommodityLecturerService.selectById(id);
        return RetResponse.makeOKRsp(mallCommodityLecturer);
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<MallCommodityLecturer>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<MallCommodityLecturer>> list(@RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<MallCommodityLecturer> list = mallCommodityLecturerService.selectAll();
        PageInfo<MallCommodityLecturer> pageInfo = new PageInfo<MallCommodityLecturer>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }
}