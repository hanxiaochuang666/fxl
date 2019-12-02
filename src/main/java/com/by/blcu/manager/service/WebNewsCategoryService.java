package com.by.blcu.manager.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.WebNews;
import com.by.blcu.manager.model.WebNewsCategory;
import com.by.blcu.core.universal.Service;
import com.by.blcu.manager.model.extend.NewsCategoryTree;
import com.by.blcu.manager.model.sql.InputMessageCategory;
import com.by.blcu.manager.model.sql.InputNewsCategory;
import com.by.blcu.manager.umodel.NewsLeftCategoryModel;

import java.util.List;

/**
* @Description: WebNewsCategoryService接口
* @author 耿鹤闯
* @date 2019/10/28 17:13
*/
public interface WebNewsCategoryService extends Service<WebNewsCategory> {
    List<WebNewsCategory> findCategoryList(InputNewsCategory model, UserSessionHelper helper);
    Integer findCategoryListCount(InputNewsCategory model, UserSessionHelper helper);
    RetResult<Integer> deleteCategoryByIdList(InputNewsCategory model, UserSessionHelper helper);

    RetResult<Integer> addCategory(WebNewsCategory model, UserSessionHelper helper);
    RetResult<Integer> updateCategory(WebNewsCategory model, UserSessionHelper helper);
    List<NewsCategoryTree> selectNewsCategoryTree(InputNewsCategory model, UserSessionHelper helper);

    List<NewsLeftCategoryModel> getLeftCategory(InputNewsCategory model, UserSessionHelper helper);

    RetResult<Integer> updateUpSort(WebNewsCategory model, UserSessionHelper helper);
    RetResult<Integer>  updateDownSort(WebNewsCategory model, UserSessionHelper helper);
}