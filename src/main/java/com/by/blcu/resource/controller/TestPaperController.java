package com.by.blcu.resource.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.*;
import com.by.blcu.course.dto.Course;
import com.by.blcu.course.service.ICourseService;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.mall.service.CourseCategoryInfoService;
import com.by.blcu.resource.dto.TestPaper;
import com.by.blcu.resource.dto.TestResult;
import com.by.blcu.resource.model.TestPaperCreateModel;
import com.by.blcu.resource.model.TestPaperViewModel;
import com.by.blcu.resource.model.TestPaperVoModel;
import com.by.blcu.resource.service.IResourcesService;
import com.by.blcu.resource.service.ITestPaperService;
import com.by.blcu.resource.service.ITestResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/testPaper")
@Api(tags = "试卷管理API")
@Slf4j
@CheckToken
public class TestPaperController {
    @Resource(name="testPaperService")
    private ITestPaperService testPaperService;

    @Resource(name="resourcesService")
    private IResourcesService resourcesService;

    @Resource(name="courseService")
    private ICourseService courseService;

    @Resource
    private CourseCategoryInfoService courseCategoryInfoService;

    @Resource(name="testResultService")
    private ITestResultService testResultService;
    @Autowired
    private FastDFSClientWrapper dfsClient;

    @RequestMapping(value = "/list", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "查询试卷列表",notes = "查询试卷L列表",httpMethod = "POST")
    public RetResult<List<TestPaperVoModel>> list(HttpServletRequest httpServletRequest, @RequestBody TestPaperViewModel testPaperViewModel)throws Exception{
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        Map<String, Object> param = MapAndObjectUtils.ObjectToMap2(testPaperViewModel);
        CommonUtils.queryParamOpt(param);
        param.put("createUser",userId);
        log.info("list请求数据:"+param.toString());
        long count = testPaperService.selectCount(param);
        if (count<=0)
            return RetResponse.makeRsp(null,count);
        List<TestPaperVoModel> resList=new ArrayList<>();
        List<TestPaper> testPaperList =testPaperService.selectList(param);
        for (TestPaper testPaper : testPaperList) {
            Map<String, Object> testPaperMap = MapAndObjectUtils.ObjectToMap2(testPaper);
            TestPaperVoModel testPaperVoModel = MapAndObjectUtils.MapToObject(testPaperMap, TestPaperVoModel.class);
            Integer courseId = testPaperVoModel.getCourseId();
            Course course = courseService.selectByPrimaryKey(courseId);
            if(StringUtils.isEmpty(course)){
                testPaperVoModel.setCourseName("备用名称");
            }else {
                testPaperVoModel.setCourseName(course.getName());
            }
            resList.add(testPaperVoModel);
        }
        return RetResponse.makeRsp(resList,count);
    }

    @RequestMapping(value = "/saveTestPaper", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "保存试卷",notes = "创建试卷",httpMethod = "POST")
    public RetResult<Integer> saveTestPaper(HttpServletRequest httpServletRequest,@Valid @RequestBody TestPaperCreateModel testPaperCreateModel)throws Exception{
        //1.检验参数
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }/*
        List<String> roleListCode = RoleListConfigurer.ROLE_LIST_CODE;
        log.info(roleListCode.toString());*/
        //2.VO  BO转换
        Date date = new Date();
        TestPaper testPaper = MapAndObjectUtils.ObjectClone(testPaperCreateModel, TestPaper.class);
        String startTime = testPaperCreateModel.getStartTimeStr();
        Date startDate = DateUtils.string2Date(startTime, "yyyy-MM-dd");
        String endTime = testPaperCreateModel.getEndTimeStr();
        Date endDate = DateUtils.string2Date(endTime, "yyyy-MM-dd");
        testPaper.setStartTime(startDate);
        testPaper.setEndTime(endDate);
        int count=0;
        //3.保存
        if(testPaper.getTestPaperId()==null) {
            //判断重名试卷
            String name = testPaper.getName();
            if(!StringUtils.isEmpty(name)){
                Map<String, Object> initMap = MapUtils.initMap("nameAll", name);
                initMap.put("createUser",userId);
                long selectCount = testPaperService.selectCount(initMap);
                if(selectCount>0){
                    log.info("试卷名称为:"+name+"的试卷已经存在");
                    return RetResponse.makeRsp(RetCode.SUCCESS.code,"试卷名称为:"+name+"的试卷已经存在");
                }
            }
            testPaper.setCreateTime(date);
            testPaper.setCreateUser(userId);
            testPaper.setUpdateUser(userId);
            testPaper.setUpdateTime(date);
            testPaper.setStatus(0);
            count= testPaperService.createTestPaper(testPaper);
        }else {
            /**
             *判断试卷创建者是不是自己
             */
            boolean operation = CommonUtils.isOperation(userId, testPaper.getTestPaperId(), testPaperService);
            if(!operation){
                log.info("只能自己修改自己的试卷");
                return RetResponse.makeRsp(RetCode.SUCCESS.code,"只能自己修改自己的试卷");
            }
            /*boolean usedMall = resourcesService.isUsedMall(testPaper.getTestPaperId(), null, 0, 1);
            if(usedMall){
                log.info("试卷id为"+testPaper.getTestPaperId()+"的试卷关联的课程已经上架，不能编辑");
                return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"试卷名称为"+testPaper.getName()+"的试卷关联的课程已经上架，不能编辑");
            }*/
            /**
             * 如果试卷已经被购买，则不能修改
             */

            /*List<TestResult> testResults = testResultService.selectList(MapUtils.initMap("testPaperId", testPaper.getTestPaperId()));
            if(null!=testResults && testResults.size()>0){
                log.info("试卷id为"+testPaper.getTestPaperId()+"的试卷已经被购买,不能编辑");
                return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"试卷id为"+testPaper.getTestPaperId()+"的试卷关联的课程已经上架，不能编辑");
            }*/
            testPaper.setUpdateUser(userId);
            testPaper.setUpdateTime(date);
            testPaper.setExportStuPath("");
            testPaper.setExportPath("");
            count=testPaperService.editTestPaper(testPaper,new CourseCheckModel());
        }
        return count>0?RetResponse.makeOKRsp(testPaper.getTestPaperId()):RetResponse.makeErrRsp("操作失败");
    }

    @PostMapping("/selectById")
    @RequiresPermissions("teacher")
    @ApiOperation(value = "根据主键查询试卷",notes = "根据主键查询试卷",httpMethod = "POST")
    public RetResult<TestPaperCreateModel> selectById(@RequestBody TestPaperCreateModel testPaperModel)throws Exception{
        Integer testPagerId = testPaperModel.getTestPaperId();
        if(null == testPagerId){
            log.info("传入试卷id为空:"+testPagerId);
            return RetResponse.makeRsp(RetCode.NOT_FOUND.code,"传入试卷id为空"+testPagerId);
        }
        TestPaper testPaper = testPaperService.selectByPrimaryKey(testPagerId);
        if(null==testPaper){
            log.info("试卷id为:"+testPagerId+"的试卷不存在");
            return RetResponse.makeRsp(RetCode.NOT_FOUND.code,"试卷不存在");
        }
        TestPaperCreateModel testPaperCreateModel = MapAndObjectUtils.ObjectClone2(testPaper, TestPaperCreateModel.class);
        testPaperCreateModel.setStartTimeStr(DateUtils.date2String(testPaper.getStartTime(),"yyyy-MM-dd"));
        testPaperCreateModel.setEndTimeStr(DateUtils.date2String(testPaper.getEndTime(),"yyyy-MM-dd"));
        testPaperCreateModel.setCategoryOneName(courseCategoryInfoService.selectCcNameByCcId(testPaperCreateModel.getCategoryOne()));
        testPaperCreateModel.setCategoryTwoName(courseCategoryInfoService.selectCcNameByCcId(testPaperCreateModel.getCategoryTwo()));
        Course course = courseService.selectByPrimaryKey(testPaperCreateModel.getCourseId());
        if(!StringUtils.isEmpty(course)){
            testPaperCreateModel.setCourseName(course.getName());
        }else {
            testPaperCreateModel.setCourseName("");
        }

        return RetResponse.makeOKRsp(testPaperCreateModel);
    }

    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "根据主键删除试卷",notes = "根据主键删除试卷",httpMethod = "DELETE")
    public RetResult deleteById(HttpServletRequest httpServletRequest,@RequestParam int testPagerId)throws Exception{
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        boolean operation = CommonUtils.isOperation(userId, testPagerId, testPaperService);
        if(!operation){
            log.info("只能自己修改自己的试卷");
            return RetResponse.makeRsp(RetCode.SUCCESS.code,"只能自己修改自己的试卷");
        }
        //添加是否绑定课程判定
        boolean usedResources = resourcesService.isUsedResources(testPagerId, null, 0, 1);
        if(usedResources){
            log.info("试卷id为"+testPagerId+"的试卷正在被课程使用，不能删除");
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"试卷正在被课程使用，不能删除");
        }
        testPaperService.deleteTestPaperById(testPagerId);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "批量删除试卷",notes = "批量删除试卷",httpMethod = "DELETE")
    public RetResult deleteBatch(HttpServletRequest httpServletRequest,@RequestParam String testPagerIds)throws Exception{
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(StringUtils.isEmpty(testPagerIds)){
            log.info("删除请求参数不能为空");
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"删除请求参数不能为空");
        }
        return RetResponse.makeOKRsp(testPaperService.deleteTestPaperBatch(testPagerIds,userId));
    }

    @RequestMapping(value = "/exporTestPaperById", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "导出试卷",notes = "导出试卷",httpMethod = "POST")
    public void exporTestPaperById(@RequestBody JSONObject obj, HttpServletRequest httpServletRequest,HttpServletResponse response){
        if(!obj.containsKey("testPagerId")){
            log.info("试卷id没传");
            throw new ServiceException("试卷id没传");
        }
        int testPagerId=obj.getInteger("testPagerId");
        if(testPagerId>0){
            testPaperService.exporTestPaperById(httpServletRequest,response,testPagerId);
        }else {
            log.info("试卷id无效"+testPagerId);
            throw new ServiceException("试卷id无效"+testPagerId);
        }
    }

}
