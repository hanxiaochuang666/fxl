package com.by.blcu.manager.dao;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.Dao;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.WebMessage;
import com.by.blcu.manager.model.WebMessageConsum;
import com.by.blcu.manager.model.sql.InputMessage;
import com.by.blcu.manager.model.sql.InputMessageConsum;

import java.util.List;

public interface WebMessageConsumMapper extends Dao<WebMessageConsum> {
    List<WebMessageConsum> findConsumList(InputMessageConsum message);
    Integer findConsumListCount(InputMessageConsum message);
    Integer readConsum(WebMessageConsum model);
    Integer insertConsumList(List<WebMessageConsum> list);
    //批量删除
    Integer deleteConsum(InputMessageConsum message);
}