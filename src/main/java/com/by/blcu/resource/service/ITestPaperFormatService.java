package com.by.blcu.resource.service;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.universal.IBaseService;
import com.by.blcu.resource.model.TestPaperFormatViewModel;

public interface ITestPaperFormatService extends IBaseService {
    /**
     * 同步试卷组成
     * @param testPaperFormatViewModel
     */
    RetResult syncPaperFormatInfo(TestPaperFormatViewModel testPaperFormatViewModel)throws Exception;
}