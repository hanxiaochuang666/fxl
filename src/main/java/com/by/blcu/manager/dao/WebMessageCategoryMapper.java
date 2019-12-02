package com.by.blcu.manager.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.manager.model.WebMessageCategory;
import com.by.blcu.manager.model.sql.InputMessageCategory;

import java.util.List;

public interface WebMessageCategoryMapper extends Dao<WebMessageCategory> {
    List<WebMessageCategory> findCategoryList(InputMessageCategory message);
    Integer findCategoryListCount(InputMessageCategory message);
    Integer deleteCategoryByIdList(InputMessageCategory message);
    //添加或修改时的查重
    List<WebMessageCategory> checkCategoryList(InputMessageCategory search);

    //获取当前层的所有新闻分类
    List<WebMessageCategory> getCategoryThisLevel(InputMessageCategory search);

    //检查分类是否已使用
    Integer checkMessageR(InputMessageCategory message);
}