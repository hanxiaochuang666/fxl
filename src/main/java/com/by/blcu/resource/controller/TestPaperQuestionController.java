package com.by.blcu.resource.controller;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.resource.dto.TestPaperQuestion;
import com.by.blcu.resource.model.TestPaperQuestionResModel;
import com.by.blcu.resource.service.ITestPaperQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/testPaperQuestion")
@Api(tags = "组卷API")
public class TestPaperQuestionController {

    @Resource(name="testPaperQuestionService")
    private ITestPaperQuestionService testPaperQuestionService;

    @RequestMapping(value = "/list", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "组卷试题列表查询",notes = "组卷试题列表查询",httpMethod = "GET")
    public RetResult<List<TestPaperQuestionResModel>> list(@RequestParam int testPaperId)throws Exception{
        return RetResponse.makeOKRsp(testPaperQuestionService.queryTestPaper(testPaperId,1));
    }

    @RequestMapping(value = "/saveTestPaperQuestion", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "保存试卷试题",notes = "保存试卷试题",httpMethod = "POST")
    public RetResult saveTestPaperQuestion(@RequestBody List<TestPaperQuestion> testPaperQuestionLst)throws Exception{
        return testPaperQuestionService.saveTestPaperQuestion(testPaperQuestionLst);
    }
}
