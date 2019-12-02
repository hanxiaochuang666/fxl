package com.by.blcu.resource.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.DateUtils;
import com.by.blcu.core.utils.GetUserInfoUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.mall.service.CourseCategoryInfoService;
import com.by.blcu.mall.service.MallOrderInfoService;
import com.by.blcu.resource.dto.TestPaper;
import com.by.blcu.resource.dto.TestPaperFormat;
import com.by.blcu.resource.dto.TestResult;
import com.by.blcu.resource.model.TestPaperAnswerViewModel;
import com.by.blcu.resource.model.TestPaperFormatLstVo;
import com.by.blcu.resource.model.TestPaperQuestionModel;
import com.by.blcu.resource.model.TestPaperQuestionResModel;
import com.by.blcu.resource.service.ITestPaperFormatService;
import com.by.blcu.resource.service.ITestPaperService;
import com.by.blcu.resource.service.ITestResultDetailService;
import com.by.blcu.resource.service.ITestResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/testResultDetail")
@Api(tags = "学生作业详情操作API")
@CheckToken
@Slf4j
public class TestResultDetailController {
    @Resource(name="testResultDetailService")
    private ITestResultDetailService testResultDetailService;

    @Resource(name="testPaperService")
    private ITestPaperService testPaperService;

    @Resource(name="testPaperFormatService")
    private ITestPaperFormatService testPaperFormatService;

    @Resource(name = "testResultService")
    private ITestResultService testResultService;

    @Resource
    private MallOrderInfoService mallOrderInfoService;

    @Resource
    private CourseCategoryInfoService courseCategoryInfoService;

    @RequestMapping(value = "/selectTestPaperInfo", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("teacher")
    @ApiOperation(value = "学生作业/测试查询",notes = "学生作业/测试查询",httpMethod = "POST")
    //optType 0 开始答题；1继续答题；2：查看详情
    public RetResult<TestPaperQuestionModel> selectTestPaperInfo(HttpServletRequest httpServletRequest,@RequestBody JSONObject obj)throws Exception{
        String userType = "";
        if(obj.size()>4){
            log.info("参数长度越界");
            return RetResponse.makeErrRsp("参数长度越界");
        }
        if(!obj.containsKey("testPaperId") || !obj.containsKey("optType")){
            log.info("参数错误");
            return RetResponse.makeErrRsp("参数错误");
        }
        if(!obj.containsKey("userType")){
            log.info("参数错误，userType是空");
            return RetResponse.makeErrRsp("userType必传！");
        }else {
            userType = obj.getString("userType");
        }
        int testPaperId=obj.getInteger("testPaperId").intValue();
        int optType=obj.getInteger("optType").intValue();
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        if (!"T".equals(userType.toUpperCase())) {
            //判斷學生是否购买试卷对应的课程
            boolean isBuy = isBuy(userId, testPaperId);
            if (!isBuy) {
                log.info("未购买相关课程,试卷id:" + testPaperId);
                return RetResponse.makeErrRsp("未购买相关课程,试卷id:" + testPaperId);
            }
        }
        TestPaperQuestionModel res=new TestPaperQuestionModel();
        TestPaper testPaper = testPaperService.selectByPrimaryKey(testPaperId);
        res.setTestPaperName(testPaper.getName());
        res.setTotalScore(testPaper.getTotalScore());
        res.setCategoryTwoName(courseCategoryInfoService.selectCcNameByCcId(testPaper.getCategoryTwo()));
        List<TestPaperFormat> testPaperFormatLst = testPaperFormatService.selectList(MapUtils.initMap("testPaperId", testPaperId));
        int totalNum=0;
        for (TestPaperFormat testPaperFormat : testPaperFormatLst) {
            totalNum+=testPaperFormat.getQuestionNum().intValue();
        }
        res.setTotalNum(totalNum);
        res.setTime(testPaper.getTime());
        String date2String = DateUtils.date2String(testPaper.getStartTime(), DateUtils.DATE_FORMAT);
        String date2String1 = DateUtils.date2String(testPaper.getEndTime(), DateUtils.DATE_FORMAT);
        res.setDateTimeStr(date2String+"~"+date2String1);
        // 教师端这里应该取学生的id，不能取request里面的
        if(obj.containsKey("studentId")){
            userId = obj.getInteger("studentId");
        }
        // 用一个对象来封装一个总数
        TestPaperFormatLstVo testPaperFormatLstVo=new TestPaperFormatLstVo();
        List<TestPaperQuestionResModel> testPaperQuestionResModels = testResultDetailService.selectTestPaperInfo(userId, testPaperId, optType,testPaperFormatLstVo);
        res.setHaveAnswerTotal(testPaperFormatLstVo.getTotalNum());
        Map<String, Object> initMap = MapUtils.initMap("studentId", userId);
        initMap.put("testPaperId",testPaperId);
        List<TestResult> list = testResultService.selectList(initMap);
        if(null!=list && list.size()>0){
            TestResult testResult = list.get(0);
            res.setUserScore(testResult.getTotalScore()!=null?testResult.getTotalScore():0);
            Integer useTime = testResult.getUseTime();
            if(null!=useTime){
                int hour=useTime.intValue()/60;
                String hourStr=null;
                String minStr=null;
                if(hour>=0&&hour<10)
                    hourStr="0"+hour;
                else
                    hourStr=""+hour;

                int mintinue=useTime.intValue()%60;
                if(mintinue>=0&&mintinue<10)
                    minStr="0"+mintinue;
                else
                    minStr=""+mintinue;
                res.setUseTimeStr(hourStr+":"+minStr);
            }
        }
        res.setQuestionResModelList(testPaperQuestionResModels);
        return RetResponse.makeOKRsp(res);
    }

    @RequestMapping(value = "/saveTestResultDetailInfo", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("teacher")
    @ApiOperation(value = "学生提交作业/测试",notes = "学生提交作业/测试",httpMethod = "POST")
    public RetResult saveTestResultDetailInfo(HttpServletRequest httpServletRequest, @Valid @RequestBody TestPaperAnswerViewModel testPaperAnswerViewModel)throws Exception{
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        //判斷學生是否购买试卷对应的课程
        boolean isBuy = isBuy(userId, testPaperAnswerViewModel.getTestPaperId());
        if(!isBuy){
            log.info("未购买相关课程,试卷id:"+testPaperAnswerViewModel.getTestPaperId());
            return RetResponse.makeErrRsp("未购买相关课程,试卷id:"+testPaperAnswerViewModel.getTestPaperId());
        }
        return testResultDetailService.saveTestResultDetailInfo(userId, testPaperAnswerViewModel);
    }

    private boolean isBuy(int student,int testPaperId){
        Set<Integer> courseLstByTestPaperId = testPaperService.getCourseLstByTestPaperId(testPaperId);
        if(null!=courseLstByTestPaperId){
            for (Integer integer : courseLstByTestPaperId) {
                boolean flag = mallOrderInfoService.selectMallOrderInfoVoByIdAndCourseId(student, integer + "");
                if (flag)
                    return true;
            }
        }
        return false;
    }

    @RequestMapping(value = "/downTestPaper", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("teacher")
    @ApiOperation(value = "学生端下载试卷",notes = "学生端下载试卷",httpMethod = "POST")
    public void downTestPaper(@RequestBody JSONObject obj, HttpServletRequest httpServletRequest,HttpServletResponse response){
        if(!obj.containsKey("testPaperId")){
            log.info("试卷id没传");
            throw new ServiceException("试卷id没传");
        }
        int testPagerId=obj.getInteger("testPaperId");
        if(testPagerId>0){
            testResultService.exporTestPaperById(httpServletRequest,response,testPagerId);
        }else {
            log.info("试卷id无效"+testPagerId);
            throw new ServiceException("试卷id无效"+testPagerId);
        }
    }
}
