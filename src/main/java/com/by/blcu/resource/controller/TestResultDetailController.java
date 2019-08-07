package com.by.blcu.resource.controller;

import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.GetUserInfoUtils;
import com.by.blcu.resource.model.TestPaperAnswerViewModel;
import com.by.blcu.resource.model.TestPaperQuestionResModel;
import com.by.blcu.resource.service.ITestResultDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/testResultDetail")
@Api(tags = "学生作业详情操作API")
@CheckToken
@Slf4j
public class TestResultDetailController {
    @Resource(name="testResultDetailService")
    private ITestResultDetailService testResultDetailService;

    @RequestMapping(value = "/selectTestPaperInfo", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "学生作业/测试查询",notes = "学生作业/测试查询",httpMethod = "GET")
    //optType 0 开始答题；1继续答题；2：查看详情
    public RetResult<List<TestPaperQuestionResModel>> selectTestPaperInfo(HttpServletRequest httpServletRequest,
                      @ApiParam(value = "试卷id",required = true)@RequestParam int testPaperId,
                      @ApiParam(value = "0 开始答题；1继续答题；2：查看详情",required = true)@RequestParam int optType)throws Exception{
        int userId = GetUserInfoUtils.getUserIdByRequest(httpServletRequest);
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        return RetResponse.makeOKRsp(testResultDetailService.selectTestPaperInfo(userId,testPaperId,optType));
    }

    @RequestMapping(value = "/saveTestResultDetailInfo", method = RequestMethod.PUT ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "学生提交作业/测试",notes = "学生提交作业/测试",httpMethod = "PUT")
    public RetResult saveTestResultDetailInfo(HttpServletRequest httpServletRequest, @RequestBody TestPaperAnswerViewModel testPaperAnswerViewModel)throws Exception{
        int userId = GetUserInfoUtils.getUserIdByRequest(httpServletRequest);
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        return testResultDetailService.saveTestResultDetailInfo(userId, testPaperAnswerViewModel);
    }
}
