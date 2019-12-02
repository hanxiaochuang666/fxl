package com.by.blcu.manager.controller;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.mall.service.CourseCategoryInfoService;
import com.by.blcu.mall.vo.CourseCategoryInfoVo;
import com.by.blcu.manager.common.StringHelper;
import com.by.blcu.manager.model.WebNews;
import com.by.blcu.manager.model.WebNews;
import com.by.blcu.manager.model.sql.InputNews;
import com.by.blcu.manager.model.sql.InputNewsCategory;
import com.by.blcu.manager.service.WebNewsService;
import com.by.blcu.manager.umodel.CategoryNewsModel;
import com.by.blcu.manager.umodel.NewsTitleModel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
* @Description: WebNewsController类
* @author 耿鹤闯
* @date 2019/10/28 17:13
*/
@RestController
@RequestMapping("/webNews")
@Api(tags = "Web新闻接口API", description = "包含接口：\n" +
        "1、后端新闻管理-添加新闻【addNews】\n" +
        "2、后端新闻管理-删除新闻【deleteNews】\n" +
        "3、后端新闻管理-修改新闻【updateNews】\n" +
        "4、后端新闻管理-根据新闻Id，获取新闻【selectNewsById】\n"+
        "5、后端新闻管理-分页获取新闻列表【list】\n"+

        "6、前端新闻-根据新闻Id，获取新闻【getNewsById】\n"+
        "7、前端新闻-分页获取新闻列表【getNewsList】\n")
public class WebNewsController extends ManagerBase {

    //region 后端接口API

    @Resource
    private WebNewsService webNewsService;

    @ApiOperation(value = "后端新闻管理-添加新闻", notes = "添加新闻")
    @PostMapping("/addNews")
    @RequiresPermissions("news:news")
    public RetResult<Integer> addNews(@RequestBody WebNews webNews) throws Exception{
        return webNewsService.addNews(webNews,userSessionHelper);
    }
    @ApiOperation(value = "后端新闻管理-删除新闻", notes = "删除新闻")
    @PostMapping("/deleteNews")
    @RequiresPermissions("news:news")
    public RetResult<Integer> deleteNews(@RequestBody InputNews model) throws Exception {
        return webNewsService.deleteNewsByIdList(model,userSessionHelper);
    }
    @ApiOperation(value = "后端新闻管理-修改新闻", notes = "修改新闻")
    @PostMapping("/updateNews")
    @RequiresPermissions("news:news")
    public RetResult<Integer> updateNews(@RequestBody WebNews webNews) throws Exception {
        return webNewsService.updateNews(webNews,userSessionHelper);
    }
    @ApiOperation(value = "后端新闻管理-根据新闻Id，获取新闻", notes = "根据新闻Id，获取新闻")
    @GetMapping("/selectNewsById")
    @RequiresPermissions("news:news")
    public RetResult<WebNews> selectNewsById(@RequestParam String newsId) throws Exception {
        InputNews search =new InputNews();
        search.setNewsId(newsId);
        List<WebNews> webNews = webNewsService.findNewsList(search,userSessionHelper);
        if(webNews==null){
            return RetResponse.makeOKRsp(null);
        }
        return RetResponse.makeOKRsp(webNews.get(0));
    }
    @ApiOperation(value = "后端新闻管理-分页获取新闻列表", notes = "分页获取新闻列表")
    @GetMapping("/list")
    @RequiresPermissions("news:news")
    public RetResult<List<WebNews>> list(InputNews search) throws Exception {
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

        search.setPage(page);
        search.setSize(size);

        Integer count = this.webNewsService.findNewsListCount(search,userSessionHelper);
        if (count.intValue() < 1) {
            return RetResponse.makeRsp(null,0);
        } else {
            List<WebNews> list = this.webNewsService.findNewsListPage(search,userSessionHelper);
            return RetResponse.makeRsp(list,count);
        }
    }

    //endregion

    //region 前端接口API

    @ApiOperation(value = "首页-获取首块新闻", notes = "获取首块新闻")
    @GetMapping("/getNewsIndex")
    public RetResult<List<CategoryNewsModel>> getNewsIndex() throws Exception {
      List<CategoryNewsModel> list =  webNewsService.getNewsIndex(null,userSessionHelper);
      return RetResponse.makeOKRsp(list);
    }

    @ApiOperation(value = "前端新闻-根据新闻Id，获取新闻", notes = "根据新闻Id，获取新闻")
    @GetMapping("/getNewsById")
    public RetResult<WebNews> getNewsById(@RequestParam String newsId) throws Exception {
        InputNews search =new InputNews();
        search.setNewsId(newsId);
        List<WebNews> webNews = webNewsService.findNewsList(search,userSessionHelper);
        if(StringHelper.IsNullOrEmpty(webNews) || webNews.get(0)==null){
            return RetResponse.makeOKRsp(null);
        }
        Integer oldClick =webNews.get((0)).getClicks();
        oldClick = oldClick==null?0:oldClick;
        search.setClicks(oldClick+1);
        webNewsService.updateClicks(search,userSessionHelper);

        return RetResponse.makeOKRsp(webNews.get(0));
    }

    @ApiOperation(value = "前端新闻-分页获取新闻列表", notes = "分页获取新闻列表")
    @GetMapping("/getNewsList")
    public RetResult<List<WebNews>> getNewsList(InputNews search) throws Exception {
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
        search.setPage(page);
        search.setSize(size);

        Integer count = this.webNewsService.findNewsListCount(search,userSessionHelper);
        if (count.intValue() < 1) {
            return RetResponse.makeRsp(null,0);
        } else {
            List<WebNews> list = this.webNewsService.findNewsListPage(search,userSessionHelper);
            return RetResponse.makeRsp(list,count);
        }
    }
    //endregion

}