package com.by.blcu.manager.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.Service;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.WebMessage;
import com.by.blcu.manager.model.extend.UserMessageExtend;
import com.by.blcu.manager.model.sql.InputMessage;

import java.util.List;

/**
* @Description: WebMessageService接口
* @author 耿鹤闯
* @date 2019/09/19 10:19
*/
public interface WebMessageService extends Service<WebMessage> {
    List<WebMessage> findMessageList(InputMessage model, UserSessionHelper helper);
    Integer findMessageListCount(InputMessage model, UserSessionHelper helper);
    RetResult<Integer> deleteMessageByIdList(InputMessage model, UserSessionHelper helper);

    RetResult<Integer> addMessage(WebMessage model, UserSessionHelper helper);
    RetResult<Integer> updateMessage(WebMessage model, UserSessionHelper helper);
    WebMessage selectMessageById(String messageId,UserSessionHelper helper);

    List<UserMessageExtend> findUserMessageList(InputMessage model, UserSessionHelper helper);
    Integer findUserMessageListCount(InputMessage model, UserSessionHelper helper);

}