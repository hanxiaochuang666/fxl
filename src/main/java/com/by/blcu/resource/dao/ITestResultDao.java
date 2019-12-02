package com.by.blcu.resource.dao;

import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.resource.dto.TestResult;

import java.util.List;
import java.util.Map;

public interface ITestResultDao extends IBaseDao {
    /**
     * 幂等插入
     * @param testResult
     * @return
     */
    long insertNoExists(TestResult testResult);

    List<TestResult> selectTestList(Map<String,Object> map);
}