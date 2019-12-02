package com.by.blcu.manager.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.WebMessage;
import com.by.blcu.manager.model.extend.UserMessageExtend;
import com.by.blcu.manager.model.sql.InputMessage;

import java.util.List;

public interface WebMessageMapper extends Dao<WebMessage> {
    List<WebMessage> findMessageList(InputMessage message);
    Integer findMessageListCount(InputMessage message);
    Integer deleteMessageByIdList(InputMessage message);

    List<UserMessageExtend> findUserMessageList(InputMessage model);
    Integer findUserMessageListCount(InputMessage model);
}