package com.by.blcu.manager.controller;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.manager.model.WebMessage;
import com.by.blcu.manager.model.WebMessageConsum;
import com.by.blcu.manager.model.sql.InputMessageConsum;
import com.by.blcu.manager.service.WebMessageConsumService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: WebMessageConsumController类
* @author 耿鹤闯
* @date 2019/09/19 10:19
*/
@RestController
@RequestMapping("/webMessageConsum")
@Api(tags = "内部接口", description = "包含接口：\n" +
        "1、消息阅读-添加阅读【addConsum】\n" +
        "2、消息阅读-分页获取消息列表【list】\n")
public class WebMessageConsumController  extends ManagerBase{

    @Resource
    private WebMessageConsumService webMessageConsumService;

    @ApiOperation(value = "消息阅读-添加阅读", notes = "添加阅读")
    @PostMapping("/addConsum")
    public RetResult<Integer> addConsum(@RequestBody WebMessageConsum webMessageConsum) throws Exception{
        return webMessageConsumService.addConsum(webMessageConsum,userSessionHelper);
    }
    @ApiOperation(value = "消息阅读-分页获取读消息列表", notes = "分页获取已阅读消息列表")
    @GetMapping("/list")
    public RetResult<PageInfo<WebMessageConsum>> list(InputMessageConsum search) throws Exception {
        if (search == null) {
            return RetResponse.makeErrRsp("参数不能为空");
        }
        Integer page = Integer.valueOf(1);
        Integer size = Integer.valueOf(1);
        if (search.getPage() == null || search.getPage().intValue() < 0) {
            page =0;
        } else {
            page = search.getPage();
        }
        if (search.getSize() == null || search.getSize().intValue() < 0) {
            size = 0;
        } else {
            size = search.getSize();
        }

        Integer count = this.webMessageConsumService.findConsumListCount(search,userSessionHelper);
        PageInfo<WebMessageConsum> pageInfo = null;
        if (count.intValue() < 1) {
            pageInfo = new PageInfo<WebMessageConsum>();
        } else {

            PageHelper.startPage(page.intValue(), size.intValue());
            List<WebMessageConsum> list = this.webMessageConsumService.findConsumList(search,userSessionHelper);
            pageInfo = new PageInfo<WebMessageConsum>(list);
            pageInfo.setTotal(count.intValue());
        }
        return RetResponse.makeOKRsp(pageInfo);
    }
}