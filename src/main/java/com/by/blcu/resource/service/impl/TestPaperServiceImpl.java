package com.by.blcu.resource.service.impl;

import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.universal.BaseServiceImpl;
import com.by.blcu.core.universal.IBaseDao;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.resource.dao.*;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.dto.TestPaper;
import com.by.blcu.resource.service.IResourcesService;
import com.by.blcu.resource.service.ITestPaperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("testPaperService")
@Slf4j
public class TestPaperServiceImpl extends BaseServiceImpl implements ITestPaperService {
    @Autowired
    private ITestPaperDao testPaperDao;

    @Autowired
    private ITestPaperFormatDao testPaperFormatDao;

    @Autowired
    private ITestPaperQuestionDao testPaperQuestionDao;

    @Resource(name="resourcesService")
    private IResourcesService resourcesService;

    @Resource(name="courseDetailService")
    private ICourseDetailService courseDetailService;

    @Autowired
    private ITestResultDao testResultDao;

    @Autowired
    private IResourcesDao resourcesDao;

    @Override
    protected IBaseDao getDao() {
        return this.testPaperDao;
    }

    @Override
    @Transactional
    public void deleteTestPaperById(int id) {
        //1.删除试卷
        getDao().deleteByPrimaryKey(id);
        //2.删除试卷组成
        Map<String, Object> initMap = MapUtils.initMap("testPaperId", id);
        testPaperFormatDao.deleteByParams(initMap);
        //3.删除试卷的题目关联
        testPaperQuestionDao.deleteByParams(initMap);
    }

    @Override
    public int deleteTestPaperBatch(String testPagerIds) {
        boolean contains = testPagerIds.contains(";");
        if(!contains) {
            Integer testPagerId = Integer.valueOf(testPagerIds).intValue();
            //添加是否绑定课程判定
            Map<String, Object> objectMap = MapUtils.initMap("content", testPagerId + "");
            objectMap.put("maxType",1);
            List<Resources> objects = resourcesService.selectList(objectMap);
            if(objects.size()>0){
                Integer resourcesId = objects.get(0).getResourcesId();
                objectMap.clear();
                objectMap.put("resourcesId",resourcesId.intValue());
                long count = courseDetailService.selectCount(objectMap);
                if(count>0){
                    log.info("试卷id为"+testPagerId+"的试卷正在被课程使用，不能删除");
                    return 0;
                }
            }
            deleteTestPaperById(Integer.valueOf(testPagerIds));
            return 1;
        }
        String[] split = testPagerIds.split(";");
        int count=0;
        for (String s : split) {
            int testPagerId=Integer.valueOf(s).intValue();
            //添加是否绑定课程判定
            Map<String, Object> objectMap = MapUtils.initMap("content", testPagerId + "");
            objectMap.put("maxType",1);
            List<Resources> objects = resourcesService.selectList(objectMap);
            if(objects.size()>0){
                Integer resourcesId = objects.get(0).getResourcesId();
                objectMap.clear();
                objectMap.put("resourcesId",resourcesId.intValue());
                long count1 = courseDetailService.selectCount(objectMap);
                if(count1>0){
                    log.info("试卷id为"+testPagerId+"的试卷正在被课程使用，不能删除");
                    continue;
                }
            }
            count++;
            deleteTestPaperById(Integer.valueOf(s));
        }
        return count;
    }

    @Override
    @Transactional
    public int createTestPaper(TestPaper testPaper) {
        int i = testPaperDao.insertSelective(testPaper);
        Integer testPaperId = testPaper.getTestPaperId();
        Resources resources = new Resources();
        resources.setContent(testPaperId.toString());
        resources.setType(testPaper.getUseType());
        resources.setOrgId(testPaper.getOrgId());
        resources.setCreateUser(testPaper.getCreateUser());
        resources.setCreayeTime(testPaper.getCreateTime());
        int insert = resourcesDao.insert(resources);
        return insert;
    }
}