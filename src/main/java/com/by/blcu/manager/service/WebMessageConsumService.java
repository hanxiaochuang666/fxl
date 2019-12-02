package com.by.blcu.manager.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.Service;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.WebMessage;
import com.by.blcu.manager.model.WebMessageConsum;
import com.by.blcu.manager.model.sql.InputMessageConsum;

import java.util.List;

/**
* @Description: WebMessageConsumService接口
* @author 耿鹤闯
* @date 2019/09/19 10:19
*/
public interface WebMessageConsumService extends Service<WebMessageConsum> {
    List<WebMessageConsum> findConsumList(InputMessageConsum model, UserSessionHelper helper);
    Integer findConsumListCount(InputMessageConsum model, UserSessionHelper helper);

    RetResult<Integer> addConsum(WebMessageConsum model, UserSessionHelper helper);
    RetResult<Integer> readConsum(WebMessageConsum model,UserSessionHelper helper);
    WebMessageConsum selectConsumById(String consumId,UserSessionHelper helper);

    Boolean sendMessageAll(String messageId, UserSessionHelper helper);
    Boolean sendMessgaeBuyer(String messageId, String ccId, String commodityId, UserSessionHelper helper);

}