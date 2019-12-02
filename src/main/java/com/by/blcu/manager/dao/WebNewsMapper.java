package com.by.blcu.manager.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.manager.model.WebNews;
import com.by.blcu.manager.model.sql.InputNews;
import com.by.blcu.manager.umodel.NewsTitleModel;

import java.util.List;

public interface WebNewsMapper extends Dao<WebNews> {
    //普通查询
    List<WebNews> findNewsList(InputNews search);
    //排序与分页查询
    List<WebNews> findNewsListPage(InputNews search);
    //统计查询
    Integer findNewsListCount(InputNews search);
    //逻辑删除
    Integer deleteNewsByIdList(InputNews search);

    List<NewsTitleModel> getNewsTitleList(InputNews search);

    //更新点击次数
    Integer updateClicks(InputNews search);
}