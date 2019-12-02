package com.by.blcu.manager.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.manager.common.UserSessionHelper;
import com.by.blcu.manager.model.ManagerLog;
import com.by.blcu.core.universal.Service;
import com.by.blcu.manager.model.sql.InputLog;

import java.util.List;

/**
* @Description: ManagerLogService接口
* @author 耿鹤闯
* @date 2019/08/05 14:37
*/
public interface ManagerLogService extends Service<ManagerLog> {
    RetResult<Integer> addLog(ManagerLog log, UserSessionHelper userSessionHelper);
    List<ManagerLog> selectLogList(InputLog search);
    Integer selectLogListCount(InputLog search);
    Integer addLogAsync(String optType,String optDescription, UserSessionHelper helper);
}