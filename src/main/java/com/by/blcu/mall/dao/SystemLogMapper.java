package com.by.blcu.mall.dao;


import com.by.blcu.core.universal.Dao;
import com.by.blcu.mall.model.SystemLog;

import java.util.List;

public interface SystemLogMapper extends Dao<SystemLog> {
	Integer insertByBatch(List<SystemLog> list);
}