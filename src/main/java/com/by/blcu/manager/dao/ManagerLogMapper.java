package com.by.blcu.manager.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.manager.model.ManagerLog;
import com.by.blcu.manager.model.sql.InputLog;

import java.util.List;

public interface ManagerLogMapper extends Dao<ManagerLog> {
    List<ManagerLog> selectLogList(InputLog search);
    Integer selectLogListCount(InputLog search);
}