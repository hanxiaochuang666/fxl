package com.by.blcu.manager.controller;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.WebNews;
import com.by.blcu.manager.model.WebNewsCategory;
import com.by.blcu.manager.model.extend.NewsCategoryTree;
import com.by.blcu.manager.model.sql.InputNewsCategory;
import com.by.blcu.manager.service.WebNewsCategoryService;
import com.by.blcu.manager.umodel.NewsLeftCategoryModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: WebNewsCategoryController类
* @author 耿鹤闯
* @date 2019/10/28 17:13
*/
@RestController
@RequestMapping("/webNewsCategory")
@Api(tags = "Web新闻分类接口API", description = "包含接口：\n" +
        "1、后端新闻分类-添加新闻分类【addCategory】\n" +
        "2、后端新闻分类-删除新闻分类【deleteCategory】\n" +
        "3、后端新闻分类-修改新闻分类【updateCategory】\n" +
        "4、后端新闻分类-根据新闻分类Id，获取新闻分类【selectCategoryById】\n"+
        "5、后端新闻分类-获取新闻分类列表【list】\n"+
        "6、后端新闻分类-获取新闻分类树【selectCategoryTree】\n"+

        "7、前端新闻-根据新闻Id，获取新闻【getNewsById】\n"+
        "8、前端新闻-分页获取新闻列表【getNewsList】\n")
public class WebNewsCategoryController extends ManagerBase {

    @Resource
    private WebNewsCategoryService webNewsCategoryService;

    //region 后端接口API

    @ApiOperation(value = "后端新闻分类-添加新闻分类", notes = "添加新闻分类")
    @PostMapping("/addCategory")
    @RequiresPermissions("news:category")
    public RetResult<Integer> addCategory(@RequestBody WebNewsCategory webNewsCategory) throws Exception{
        return webNewsCategoryService.addCategory(webNewsCategory,userSessionHelper);
    }
    @ApiOperation(value = "后端新闻分类-删除新闻分类", notes = "删除新闻分类")
    @PostMapping("/deleteCategory")
    @RequiresPermissions("news:category")
    public RetResult<Integer> deleteCategory(@RequestBody InputNewsCategory model) throws Exception {
        return webNewsCategoryService.deleteCategoryByIdList(model,userSessionHelper);
    }
    @ApiOperation(value = "后端新闻分类-修改新闻分类", notes = "修改新闻分类")
    @PostMapping("/updateCategory")
    @RequiresPermissions("news:category")
    public RetResult<Integer> updateCategory(@RequestBody WebNewsCategory webNewsCategory) throws Exception {
        return webNewsCategoryService.updateCategory(webNewsCategory,userSessionHelper);
    }
    @ApiOperation(value = "后端新闻分类-根据新闻分类Id，获取新闻分类", notes = "根据新闻Id，获取新闻分类")
    @GetMapping("/selectCategoryById")
    @RequiresPermissions("news:category")
    public RetResult<WebNewsCategory> selectCategoryById(@RequestParam String categoryId) throws Exception {
        WebNewsCategory model =null;
        InputNewsCategory search =new InputNewsCategory();
        search.setCategoryId(categoryId);
        List<WebNewsCategory> webNewsCategory = webNewsCategoryService.findCategoryList(search,userSessionHelper);
        if(!StringHelper.IsNullOrEmpty(webNewsCategory)){
            model=webNewsCategory.get(0);
        }
        return RetResponse.makeOKRsp(model);
    }
    @ApiOperation(value = "后端新闻分类-获取新闻分类列表", notes = "获取新闻分类列表")
    @GetMapping("/list")
    @RequiresPermissions("news:category")
    public RetResult<List<WebNewsCategory>> list(InputNewsCategory search) throws Exception {
        List<WebNewsCategory> list = this.webNewsCategoryService.findCategoryList(search,userSessionHelper);
        return RetResponse.makeOKRsp(list);
    }

    @ApiOperation(value = "后端新闻分类-获取新闻分类树", notes = "获取新闻分类树")
    @GetMapping("/selectCategoryTree")
    @RequiresPermissions("news:category")
    public RetResult<List<NewsCategoryTree>> selectCategoryTree(InputNewsCategory search) throws Exception {
        List<NewsCategoryTree> list = this.webNewsCategoryService.selectNewsCategoryTree(search,userSessionHelper);
        return RetResponse.makeOKRsp(list);
    }

    @ApiOperation(value = "后端新闻分类-上移分类", notes = "上移分类")
    @PostMapping("/updateUpSort")
    @RequiresPermissions("news:category")
    public RetResult<Integer> updateUpSort(@RequestBody WebNewsCategory model){
        return webNewsCategoryService.updateUpSort(model,userSessionHelper);
    }

    @ApiOperation(value = "后端新闻分类-下移分类", notes = "下移分类")
    @PostMapping("/updateDownSort")
    @RequiresPermissions("news:category")
    public RetResult<Integer>  updateDownSort(@RequestBody WebNewsCategory model, UserSessionHelper helper){
        return webNewsCategoryService.updateDownSort(model,userSessionHelper);
    }

    //endregion

    //region 前端接口API

    @ApiOperation(value = "前端新闻分类-根据分类父级Id，获取新闻分类树", notes = "根据分类父级Id，获取新闻分类树")
    @GetMapping("/getCategoryTree")
    public RetResult<List<NewsCategoryTree>> getCategoryTree(InputNewsCategory search) throws Exception {
        List<NewsCategoryTree>  webNewsCategory = webNewsCategoryService.selectNewsCategoryTree(search,userSessionHelper);
        return RetResponse.makeOKRsp(webNewsCategory);
    }

    @ApiOperation(value = "前端新闻分类-获取左侧分类", notes = "获取左侧分类")
    @GetMapping("/getLeftCategory")
    public RetResult<List<NewsLeftCategoryModel>> getLeftCategory(InputNewsCategory search) throws Exception {
        List<NewsLeftCategoryModel> list =webNewsCategoryService.getLeftCategory(search,userSessionHelper);
        return RetResponse.makeOKRsp(list);
    }

    //endregion

}