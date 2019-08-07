package com.by.blcu.resource.service;

import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.resource.dto.TestPaper;

public interface ITestPaperService extends IBaseService {
    /**
     * 删除试卷及试卷附属内容
     * @param id
     */
    void deleteTestPaperById(int id);

    /**
     * 批量删除试卷
     * @param testPagerIds
     */
    int deleteTestPaperBatch(String testPagerIds);

    /**
     * 创建新的试卷
     * @return
     */
    int createTestPaper(TestPaper testPaper);
}