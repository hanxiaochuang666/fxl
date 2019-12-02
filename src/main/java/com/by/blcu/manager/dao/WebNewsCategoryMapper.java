package com.by.blcu.manager.dao;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.Dao;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.WebNewsCategory;
import com.by.blcu.manager.model.extend.NewsCategoryAmount;
import com.by.blcu.manager.model.sql.InputNewsCategory;

import java.util.List;

public interface WebNewsCategoryMapper extends Dao<WebNewsCategory> {
    //普通查询
    List<WebNewsCategory> findCategoryList(InputNewsCategory search);
    //添加或修改时的查重
    List<WebNewsCategory> checkCategoryList(InputNewsCategory search);
    //排序查询
    List<WebNewsCategory> findCategoryListPage(InputNewsCategory search);
    //统计查询
    Integer findCategoryListCount(InputNewsCategory search);
    //逻辑删除
    Integer deleteCategoryByIdList(InputNewsCategory search);

    //获取新闻分类与数量
    List<NewsCategoryAmount> getCategoryAmount(InputNewsCategory search);

    //获取当前层的所有新闻分类
    List<WebNewsCategory> getCategoryThisLevel(InputNewsCategory search);

    //检查分类是否已使用
    Integer checkNewsR(InputNewsCategory search);
}