package com.by.blcu.resource.service.impl;

import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.MapAndObjectUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.dto.Course;
import com.by.blcu.course.dto.CourseDetail;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.course.service.ICourseService;
import com.by.blcu.resource.dao.IQuestionDao;
import com.by.blcu.resource.dao.ITestPaperDao;
import com.by.blcu.resource.dao.ITestPaperFormatDao;
import com.by.blcu.resource.dao.ITestResultDetailDao;
import com.by.blcu.resource.dto.*;
import com.by.blcu.resource.model.PaperFormatInfo;
import com.by.blcu.resource.model.TestPaperFormatViewModel;
import com.by.blcu.resource.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("testPaperFormatService")
@Slf4j
public class TestPaperFormatServiceImpl extends BaseServiceImpl implements ITestPaperFormatService {
    @Autowired
    private ITestPaperFormatDao testPaperFormatDao;

    @Resource(name="testPaperQuestionService")
    private ITestPaperQuestionService testPaperQuestionService;

    @Resource(name="resourcesService")
    private IResourcesService resourcesService;

    @Resource(name="testResultService")
    private ITestResultService testResultService;

    @Resource
    private ICourseService courseService;

    @Autowired
    private ITestPaperDao testPaperDao;

    @Autowired
    private ITestResultDetailDao testResultDetailDao;
    @Resource(name = "courseDetailService")
    private ICourseDetailService courseDetailService;
    @Resource(name="learnActiveService")
    private ILearnActiveService learnActiveService;
    @Override
    protected IBaseDao getDao() {
        return this.testPaperFormatDao;
    }

    @Override
    @Transactional(rollbackFor=ServiceException.class)
    public RetResult syncPaperFormatInfo(TestPaperFormatViewModel testPaperFormatViewModel) throws Exception {
        List<PaperFormatInfo> paperFormatInfoLst = testPaperFormatViewModel.getPaperFormatInfoLst();
        int totalScore=0;
        boolean isUpdate=false;
        Date date = new Date();
        Integer testPaperId = testPaperFormatViewModel.getTestPaperId();
        boolean usedMall=false;
        if(null!=testPaperId) {
            usedMall = resourcesService.isUsedMall(testPaperId, null, 0, 1);
        }
        TestPaper testPaper1 = testPaperDao.selectByPrimaryKey(testPaperFormatViewModel.getTestPaperId());
        //boolean isInsert=false;
        for (PaperFormatInfo paperFormatInfo : paperFormatInfoLst) {
            TestPaperFormat testPaperFormat = MapAndObjectUtils.ObjectClone(paperFormatInfo, TestPaperFormat.class);
            testPaperFormat.setTestPaperId(testPaperFormatViewModel.getTestPaperId());
            totalScore+=testPaperFormat.getQuestionSpec().intValue()*testPaperFormat.getQuestionNum();
            if(null==paperFormatInfo.getTestPaperFormatId()) {
                //插入
                getDao().insertSelective(testPaperFormat);
                //isInsert=true;
            } else {
                //更新
                TestPaperFormat testPaperFormat1 = getDao().selectByPrimaryKey(paperFormatInfo.getTestPaperFormatId());
                if(testPaperFormat1.getQuestionNum()==testPaperFormat.getQuestionNum()
                        && testPaperFormat1.getQuestionType()==testPaperFormat.getQuestionType()
                        && testPaperFormat1.getQuestionSpec()==testPaperFormat.getQuestionSpec()){

                }else {
                    getDao().updateByPrimaryKeySelective(testPaperFormat);
                    isUpdate=true;
                }
            }
        }
        /*if(isInsert){
            Resources resources1 = new Resources();
            resources1.setUpdateUser(testPaperFormatViewModel.getUpdateUser());
            resources1.setUpdateTime(date);
            resources1.setContent(testPaperId+"");
            resources1.setCreayeTime(date);
            resources1.setCreateUser(testPaperFormatViewModel.getUpdateUser());
            resources1.setType(testPaper1.getUseType());
            resourcesService.insertSelective(resources1);
        }*/
        TestPaper testPaper = new TestPaper();
        if(!StringUtils.isEmpty(testPaperFormatViewModel.getKonwledges()))
            testPaper.setCatalogs(testPaperFormatViewModel.getKonwledges());
        testPaper.setTestPaperId(testPaperFormatViewModel.getTestPaperId());
        testPaper.setTotalScore(totalScore);
        testPaper.setIsScore(testPaper1.getIsScore());
        if(isUpdate) {
            testPaper.setExportStuPath("");
            testPaper.setUpdateTime(date);
            testPaper.setUpdateUser(testPaperFormatViewModel.getUpdateUser());
        }
        testPaperDao.updateByPrimaryKeySelective(testPaper);
        RetResult baseModle=null;
        if(testPaper1.getFormType()==1){
            String konwledges = testPaperFormatViewModel.getKonwledges();
            if(StringUtils.isEmpty(konwledges)){
                log.info("智能组卷知识点不能为空");
                return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"智能组卷知识点不能为空");
            }
            //智能组卷
            baseModle = testPaperQuestionService.intellectPaper(testPaperFormatViewModel.getTestPaperId(), testPaperFormatViewModel.getKonwledges());
            if(baseModle.code!=200){
                //抛出运行时异常，使事务回滚
                throw new ServiceException(baseModle.getMsg());
            }else{
                isUpdate=true;
            }
        }
        if(isUpdate){
            //更新
            Map<String, Object> contentMap = MapUtils.initMap("content", testPaperId + "");
            //maxType
            contentMap.put("maxType",1);
            List<Resources> content = resourcesService.selectList(contentMap);
            if(null==content || content.size()<=0){
                log.info("试卷无资源描述信息，testPaperId:"+testPaperFormatViewModel.getTestPaperId());
                return RetResponse.makeErrRsp("试卷无资源描述信息，testPaperId:"+testPaperFormatViewModel.getTestPaperId());
            }
            Resources resources1 = new Resources();
            resources1.setUpdateUser(testPaperFormatViewModel.getUpdateUser());
            resources1.setUpdateTime(date);
            resources1.setResourcesId(content.get(0).getResourcesId());
            resourcesService.updateByPrimaryKeySelective(resources1);
            Map<String, Object> objectMap = MapUtils.initMap("resourcesId", content.get(0).getResourcesId());
            List<CourseDetail> objects1 = courseDetailService.selectList(objectMap);
            if(null!=objects1&&objects1.size()>0) {
                if(!usedMall) {
                    for (CourseDetail courseDetail : objects1) {
                        Integer courseId = courseDetail.getCourseId();
                        Course course = new Course();
                        course.setCourseId(courseId);
                        course.setStatus(0);
                        courseService.updateByPrimaryKeySelective(course);
                    }
                }
            }
        }
        if(null!=baseModle)
            return baseModle;
        return RetResponse.makeOKRsp();
    }
}