package com.by.blcu.manager.controller;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.WebMessageCategory;
import com.by.blcu.manager.model.sql.InputMessageCategory;
import com.by.blcu.manager.service.WebMessageCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: WebMessageCategoryController类
* @author 耿鹤闯
* @date 2019/09/19 10:19
*/
@RestController
@RequestMapping("/webMessageCategory")
@Api(tags = "Web消息分类接口API", description = "包含接口：\n" +
        "1、消息分类-添加消息分类【addCategory】\n" +
        "2、消息分类-删除消息分类【deleteCategory】\n" +
        "3、消息分类-修改消息分类【updateCategory】\n" +
        "4、消息分类-根据消息Id，获取消息分类【selectCategoryById】\n"+
        "5、消息分类-获取消息分类列表【list】\n")
public class WebMessageCategoryController extends ManagerBase {

    @Resource
    private WebMessageCategoryService webMessageCategoryService;
//region 后端接口

    @ApiOperation(value = "消息分类-添加消息分类", notes = "添加消息分类")
    @PostMapping("/addCategory")
    @RequiresPermissions("message:category")
    public RetResult<Integer> addCategory(@RequestBody WebMessageCategory webMessageCategory) throws Exception{
        return webMessageCategoryService.addCategory(webMessageCategory,userSessionHelper);
    }
    @ApiOperation(value = "消息分类-删除消息分类", notes = "删除消息分类")
    @PostMapping("/deleteCategory")
    @RequiresPermissions("message:category")
    public RetResult<Integer> deleteCategory(@RequestBody InputMessageCategory model) throws Exception {
        return webMessageCategoryService.deleteCategoryByIdList(model,userSessionHelper);
    }
    @ApiOperation(value = "消息分类-修改消息分类", notes = "修改消息分类")
    @PostMapping("/updateCategory")
    @RequiresPermissions("message:category")
    public RetResult<Integer> updateCategory(@RequestBody WebMessageCategory webMessageCategory) throws Exception {
        return webMessageCategoryService.updateCategory(webMessageCategory,userSessionHelper);
    }
    @ApiOperation(value = "消息分类-根据消息Id，获取消息分类", notes = "根据消息Id，获取消息分类")
    @GetMapping("/selectCategoryById")
    @RequiresPermissions("message:category")
    public RetResult<WebMessageCategory> selectCategoryById(@RequestParam String categoryId) throws Exception {
        WebMessageCategory webMessageCategory = webMessageCategoryService.selecCategoryById(categoryId,userSessionHelper);
        return RetResponse.makeOKRsp(webMessageCategory);
    }
    @ApiOperation(value = "消息分类-获取消息分类列表", notes = "获取消息分类列表")
    @GetMapping("/list")
    @RequiresPermissions("message:category")
    public RetResult<List<WebMessageCategory>> list(InputMessageCategory search) throws Exception {
        List<WebMessageCategory> list = this.webMessageCategoryService.findCategoryList(search,userSessionHelper);
        return RetResponse.makeOKRsp(list);
    }

    @ApiOperation(value = "后端新闻分类-上移分类", notes = "上移分类")
    @PostMapping("/updateUpSort")
    @RequiresPermissions("news:category")
    public RetResult<Integer> updateUpSort(@RequestBody WebMessageCategory model){
        return webMessageCategoryService.updateUpSort(model,userSessionHelper);
    }

    @ApiOperation(value = "后端新闻分类-下移分类", notes = "下移分类")
    @PostMapping("/updateDownSort")
    @RequiresPermissions("news:category")
    public RetResult<Integer>  updateDownSort(@RequestBody WebMessageCategory model, UserSessionHelper helper){
        return webMessageCategoryService.updateDownSort(model,userSessionHelper);
    }

//endregion
}