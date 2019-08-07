package com.by.blcu.mall.service;

import com.by.blcu.mall.model.SystemLog;

import java.util.List;

import com.by.blcu.core.universal.Service;

/**
* @Description: SystemLogService接口
* @author 李程
* @date 2019/06/28 09:11
*/
public interface SystemLogService extends Service<SystemLog> {
	
	Integer insertByBatch(List<SystemLog> list);

}