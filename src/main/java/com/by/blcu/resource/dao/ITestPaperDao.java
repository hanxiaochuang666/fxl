package com.by.blcu.resource.dao;

import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.resource.dto.TestPaper;

import java.util.Map;

public interface ITestPaperDao extends IBaseDao {

    long selectTestPaperCount(Map<String, Object> map);
}