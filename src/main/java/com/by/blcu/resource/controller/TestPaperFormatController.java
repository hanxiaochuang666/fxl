package com.by.blcu.resource.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.CommonUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.resource.dto.QuestionType;
import com.by.blcu.resource.dto.TestPaper;
import com.by.blcu.resource.dto.TestPaperFormat;
import com.by.blcu.resource.model.TestPaperFormatLstVo;
import com.by.blcu.resource.model.TestPaperFormatViewModel;
import com.by.blcu.resource.service.IQuestionTypeService;
import com.by.blcu.resource.service.ITestPaperFormatService;
import com.by.blcu.resource.service.ITestPaperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/testPaperFormat")
@Api(tags = "试卷组成管理API")
@Slf4j
@CheckToken
public class TestPaperFormatController {
    @Resource(name="testPaperFormatService")
    private ITestPaperFormatService testPaperFormatService;

    @Resource(name="testPaperService")
    private ITestPaperService testPaperService;

    @Autowired
    private IQuestionTypeService typeService;

    @RequestMapping(value = "/queryTestPaperFormat", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "查询试卷组成",notes = "查询试卷组成",httpMethod = "GET")
    public RetResult<List<TestPaperFormat>> queryTestPaperFormat(@RequestParam Integer testPaperId){
        if(null==testPaperId || testPaperId<=0){
            log.info("根据试卷组成testPaperFormatId参数错误");
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"根据试卷组成testPaperFormatId参数错误");
        }
        Map<String, Object> initMap = MapUtils.initMap("testPaperId", testPaperId);
        List<TestPaperFormat> list = testPaperFormatService.selectList(initMap);
        return RetResponse.makeOKRsp(list);
    }


    @RequestMapping(value = "/queryTestPaperFormatArtificial", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "查询试卷组成(人工)",notes = "查询试卷组成",httpMethod = "GET")
    public RetResult<List<TestPaperFormatLstVo>> queryTestPaperFormatArtificial(@RequestBody JSONObject obj){
        Integer testPaperId = obj.getInteger("testPaperId");
        if(null==testPaperId || testPaperId<=0){
            log.info("根据试卷组成testPaperFormatId参数错误");
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"根据试卷组成testPaperFormatId参数错误");
        }
        List<TestPaperFormatLstVo> list1=new ArrayList<>();
        Map<String, Object> initMap = MapUtils.initMap("testPaperId", testPaperId);
        List<TestPaperFormat> list = testPaperFormatService.selectList(initMap);
        for (TestPaperFormat testPaperFormat : list) {
            if(testPaperFormat.getQuestionNum()>0){
                TestPaperFormatLstVo temp=new TestPaperFormatLstVo();
                QuestionType questionType = typeService.selectByPrimaryKey(testPaperFormat.getQuestionType());
                if(null!=questionType){
                    temp.setQuestionTypeName(questionType.getName());
                    temp.setScore(testPaperFormat.getQuestionSpec());
                    temp.setTotalNum(testPaperFormat.getQuestionNum());
                    temp.setQuestionType(testPaperFormat.getQuestionType());
                    QuestionType questionType1 = typeService.selectByPrimaryKey(testPaperFormat.getQuestionType());
                    temp.setQuestionTypeCode(questionType1.getCode());
                    list1.add(temp);
                }
            }
        }

        return RetResponse.makeOKRsp(list1);
    }
    @RequestMapping(value = "/saveTestPaperFormat", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "保存试卷组成",notes = "保存试卷组成",httpMethod = "POST")
    public RetResult saveTestPaperFormat(HttpServletRequest httpServletRequest, @Valid @RequestBody TestPaperFormatViewModel testPaperFormatViewModel)throws Exception{
        if(null==testPaperFormatViewModel.getTestPaperId()){
            log.info("参数错误："+testPaperFormatViewModel.toString());
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"参数错误："+testPaperFormatViewModel.toString());
        }
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        TestPaper testPaper = testPaperService.selectByPrimaryKey(testPaperFormatViewModel.getTestPaperId());
        if(testPaper==null)
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"试卷不存在");
        boolean operation = CommonUtils.isOperation(userId, testPaper.getTestPaperId(), testPaperService);
        if(!operation){
            log.info("只能自己修改自己的试卷");
            return RetResponse.makeRsp(RetCode.SUCCESS.code,"只能自己修改自己的试卷");
        }
        testPaperFormatViewModel.setUpdateUser(userId);
        return testPaperFormatService.syncPaperFormatInfo(testPaperFormatViewModel);
    }
}
