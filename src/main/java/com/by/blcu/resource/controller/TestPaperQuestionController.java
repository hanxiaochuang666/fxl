package com.by.blcu.resource.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.DateUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.dto.TestPaper;
import com.by.blcu.resource.dto.TestPaperFormat;
import com.by.blcu.resource.dto.TestPaperQuestion;
import com.by.blcu.resource.model.TestPaperQuestionModel;
import com.by.blcu.resource.model.TestPaperQuestionResModel;
import com.by.blcu.resource.service.IResourcesService;
import com.by.blcu.resource.service.ITestPaperFormatService;
import com.by.blcu.resource.service.ITestPaperQuestionService;
import com.by.blcu.resource.service.ITestPaperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/testPaperQuestion")
@Api(tags = "组卷API")
@Slf4j
@CheckToken
public class TestPaperQuestionController {

    @Resource(name="testPaperQuestionService")
    private ITestPaperQuestionService testPaperQuestionService;

    @Resource(name="testPaperService")
    private ITestPaperService testPaperService;

    @Resource(name="testPaperFormatService")
    private ITestPaperFormatService testPaperFormatService;

    @Resource
    private IResourcesService resourcesService;

    @RequestMapping(value = "/list", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "组卷试题列表查询",notes = "组卷试题列表查询",httpMethod = "POST")
    public RetResult<List<TestPaperQuestionResModel>> list(@RequestBody JSONObject obj)throws Exception{
        if(!obj.containsKey("testPaperId")|| obj.getInteger("testPaperId").intValue()<=0){
            log.info("testPaperId 错误");
            return RetResponse.makeErrRsp("testPaperId 错误");
        }
        return RetResponse.makeOKRsp(testPaperQuestionService.queryTestPaper(obj.getInteger("testPaperId"),1));
    }

    @RequestMapping(value = "/saveTestPaperQuestion", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "保存试卷试题",notes = "保存试卷试题",httpMethod = "POST")
    public RetResult saveTestPaperQuestion(HttpServletRequest httpServletRequest, @RequestBody List<TestPaperQuestion> testPaperQuestionLst)throws Exception{
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        return testPaperQuestionService.saveTestPaperQuestion(testPaperQuestionLst,userId,new CourseCheckModel());
    }


    @RequestMapping(value = "/preview", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "试卷预览",notes = "试卷预览",httpMethod = "POST")
    public RetResult<TestPaperQuestionModel> preview(@RequestBody JSONObject obj)throws Exception{
        if(!obj.containsKey("testPaperId")|| obj.getInteger("testPaperId").intValue()<=0){
            log.info("testPaperId 错误");
            return RetResponse.makeErrRsp("testPaperId 错误");
        }
        Integer testPaperId = obj.getInteger("testPaperId");
        TestPaper testPaper = testPaperService.selectByPrimaryKey(testPaperId);
        if(null==testPaper){
            log.info("testPaperId为"+testPaperId+"的试卷不存在");
            return RetResponse.makeErrRsp("testPaperId为"+testPaperId+"的试卷不存在");
        }
        TestPaperQuestionModel res=new TestPaperQuestionModel();
        if(obj.containsKey("isChecked") && obj.getBooleanValue("isChecked")){
            Map<String, Object> contentMap = MapUtils.initMap("content", testPaperId);
            contentMap.put("maxType",1);
            List<Resources> objects = resourcesService.selectList(contentMap);
            if(null!=objects && objects.size()>0){
                Resources resources = objects.get(0);
                res.setCheckStatus(resources.getCheckStatus());
            }else {
                log.info("试卷数据异常"+testPaperId);
                throw new ServiceException("试卷数据异常"+testPaperId);
            }
        }
        res.setIsScore(testPaper.getIsScore());
        res.setTestPaperName(testPaper.getName());
        res.setTotalScore(testPaper.getTotalScore());
        res.setTime(testPaper.getTime()==null?0:testPaper.getTime().intValue());
        List<TestPaperFormat> testPaperFormatLst = testPaperFormatService.selectList(MapUtils.initMap("testPaperId", testPaperId));
        int totalNum=0;
        for (TestPaperFormat testPaperFormat : testPaperFormatLst) {
            totalNum+=testPaperFormat.getQuestionNum().intValue();
        }
        res.setTotalNum(totalNum);
        String date2String = DateUtils.date2String(testPaper.getStartTime(), DateUtils.DATE_FORMAT);
        String date2String1 = DateUtils.date2String(testPaper.getEndTime(), DateUtils.DATE_FORMAT);
        res.setDateTimeStr(date2String+"~"+date2String1);
        List<TestPaperQuestionResModel> testPaperQuestionResModelList = testPaperQuestionService.queryTestPaper(obj.getInteger("testPaperId"), 1);
        res.setQuestionResModelList(testPaperQuestionResModelList);
        return RetResponse.makeOKRsp(res);
    }

    /*@RequestMapping(value = "/createWord", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "创建word测试",notes = "创建word测试",httpMethod = "GET")
    public void createWord(@RequestParam int testPaperId)throws Exception{
        testPaperQuestionService.createNewWord(testPaperId);
    }*/
}
