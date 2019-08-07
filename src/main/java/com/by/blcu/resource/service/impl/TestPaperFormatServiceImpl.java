package com.by.blcu.resource.service.impl;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.MapAndObjectUtils;
import com.by.blcu.resource.dao.ITestPaperDao;
import com.by.blcu.resource.dao.ITestPaperFormatDao;
import com.by.blcu.resource.dto.TestPaper;
import com.by.blcu.resource.dto.TestPaperFormat;
import com.by.blcu.resource.model.PaperFormatInfo;
import com.by.blcu.resource.model.TestPaperFormatViewModel;
import com.by.blcu.resource.service.ITestPaperFormatService;
import com.by.blcu.resource.service.ITestPaperQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("testPaperFormatService")
public class TestPaperFormatServiceImpl extends BaseServiceImpl implements ITestPaperFormatService {
    @Autowired
    private ITestPaperFormatDao testPaperFormatDao;

    @Resource(name="testPaperQuestionService")
    private ITestPaperQuestionService testPaperQuestionService;
    @Autowired
    private ITestPaperDao testPaperDao;

    @Override
    protected IBaseDao getDao() {
        return this.testPaperFormatDao;
    }

    @Override
    @Transactional(rollbackFor=ServiceException.class)
    public RetResult syncPaperFormatInfo(TestPaperFormatViewModel testPaperFormatViewModel) throws Exception {
        List<PaperFormatInfo> paperFormatInfoLst = testPaperFormatViewModel.getPaperFormatInfoLst();
        int totalScore=0;
        for (PaperFormatInfo paperFormatInfo : paperFormatInfoLst) {
            TestPaperFormat testPaperFormat = MapAndObjectUtils.ObjectClone(paperFormatInfo, TestPaperFormat.class);
            testPaperFormat.setTestPaperId(testPaperFormatViewModel.getTestPaperId());
            totalScore+=testPaperFormat.getQuestionSpec().intValue()*testPaperFormat.getQuestionNum();
            if(null==paperFormatInfo.getTestPaperFormatId())
                //插入
                getDao().insertSelective(testPaperFormat);
            else
                //更新
                getDao().updateByPrimaryKeySelective(testPaperFormat);
        }
        TestPaper testPaper = new TestPaper();
        testPaper.setTestPaperId(testPaperFormatViewModel.getTestPaperId());
        testPaper.setTotalScore(totalScore);
        testPaperDao.updateByPrimaryKeySelective(testPaper);
        testPaper = testPaperDao.selectByPrimaryKey(testPaperFormatViewModel.getTestPaperId());
        if(testPaper.getFormType()==1){
            //智能组卷
            RetResult baseModle = testPaperQuestionService.intellectPaper(testPaperFormatViewModel.getTestPaperId(), testPaperFormatViewModel.getKonwledges());
            if(baseModle.code!=200){
                //抛出运行时异常，使事务回滚
                throw new ServiceException(baseModle.getMsg());
            }else{
                return baseModle;
            }
        }
        return RetResponse.makeOKRsp();
    }
}