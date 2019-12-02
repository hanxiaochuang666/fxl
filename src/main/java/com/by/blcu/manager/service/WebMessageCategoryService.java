package com.by.blcu.manager.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.Service;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.WebMessageCategory;
import com.by.blcu.manager.model.WebNewsCategory;
import com.by.blcu.manager.model.sql.InputMessageCategory;

import java.util.List;

/**
* @Description: WebMessageCategoryService接口
* @author 耿鹤闯
* @date 2019/09/19 10:19
*/
public interface WebMessageCategoryService extends Service<WebMessageCategory> {
    List<WebMessageCategory> findCategoryList(InputMessageCategory message, UserSessionHelper helper);
    Integer findCategoryListCount(InputMessageCategory message, UserSessionHelper helper);
    RetResult<Integer> deleteCategoryByIdList(InputMessageCategory message, UserSessionHelper helper);

    RetResult<Integer> addCategory(WebMessageCategory model, UserSessionHelper helper);
    RetResult<Integer> updateCategory(WebMessageCategory model, UserSessionHelper helper);
    WebMessageCategory selecCategoryById(String categoryId,UserSessionHelper helper);

    RetResult<Integer> updateUpSort(WebMessageCategory model, UserSessionHelper helper);
    RetResult<Integer>  updateDownSort(WebMessageCategory model, UserSessionHelper helper);
}