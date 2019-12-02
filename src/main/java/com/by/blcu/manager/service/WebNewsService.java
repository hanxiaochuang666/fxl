package com.by.blcu.manager.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.WebNews;
import com.by.blcu.core.universal.Service;
import com.by.blcu.manager.model.sql.InputNews;
import com.by.blcu.manager.umodel.CategoryNewsModel;

import java.util.List;

/**
* @Description: WebNewsService接口
* @author 耿鹤闯
* @date 2019/10/28 17:13
*/
public interface WebNewsService extends Service<WebNews> {
    List<WebNews> findNewsList(InputNews model, UserSessionHelper helper);
    List<WebNews> findNewsListPage(InputNews model, UserSessionHelper helper);
    Integer findNewsListCount(InputNews model, UserSessionHelper helper);
    RetResult<Integer> deleteNewsByIdList(InputNews model, UserSessionHelper helper);

    RetResult<Integer> addNews(WebNews model, UserSessionHelper helper);
    RetResult<Integer> updateNews(WebNews model, UserSessionHelper helper);
    List<CategoryNewsModel> getNewsIndex(WebNews model, UserSessionHelper helper);

    Integer updateClicks(InputNews search, UserSessionHelper helper);
}