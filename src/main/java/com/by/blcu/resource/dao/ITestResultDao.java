package com.by.blcu.resource.dao;

import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.resource.dto.TestResult;

public interface ITestResultDao extends IBaseDao {
    /**
     * 幂等插入
     * @param testResult
     * @return
     */
    long insertNoExists(TestResult testResult);
}