package com.by.blcu.resource.controller;


import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.GetUserInfoUtils;
import com.by.blcu.resource.model.QueryQuestionModel;
import com.by.blcu.resource.model.QuestionModel;
import com.by.blcu.resource.service.IQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CheckToken
@RestController
@RequestMapping(value = "/question")
@Slf4j
@Api(tags = "试题管理API",description = "包含接口：\n" +
        "1、试题录入接口\n" +
        "2、试题批量导入接口\n" +
        "3、试题编辑接口\n" +
        "4、试题删除接口\n" +
        "5、试题批量删除接口\n" +
        "6、查询试题列表\n" +
        "7、查询试题列表总数")
public class QuestionController {

    @Autowired
    private IQuestionService questionService;

    @RequestMapping(value = "/insertQuestion",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "试题录入接口")
    public RetResult insertQuestion(HttpServletRequest httpServletRequest, @RequestBody QuestionModel questionModel) throws Exception{

        questionService.insertQuestion(questionModel,httpServletRequest);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/importQuestion",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "试题批量导入接口")
    public RetResult importQuestion(@ApiParam(value = "试题导入模板",required = true) @RequestParam(value = "file") MultipartFile file,
                                    @ApiParam(value = "一级目录",required = true) @RequestParam int categoryOne,
                                    @ApiParam(value = "二级目录",required = true) @RequestParam int categoryTwo,
                                    @ApiParam(value = "课程id",required = true) @RequestParam int courseId,
                                    @ApiParam(value = "机构id",required = true) @RequestParam int orgId,
                                    HttpServletRequest request) throws Exception{

        int userId = GetUserInfoUtils.getUserIdByRequest(request);
        Map<String,Object> paraMap = new HashMap<>();
        paraMap.put("categoryOne", categoryOne);
        paraMap.put("categoryTwo", categoryTwo);
        paraMap.put("courseId", courseId);
        paraMap.put("orgId", orgId);
        paraMap.put("createUserId", userId);
        questionService.importQuestion(paraMap,file, request);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/editQuestion",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "试题编辑接口")
    public RetResult editQuestion(HttpServletRequest httpServletRequest,@RequestBody QuestionModel questionModel) throws Exception{

        questionService.editQuestion(questionModel,httpServletRequest);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/deleteQuestion",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "试题删除接口")
    public RetResult deleteQuestion(@ApiParam(value = "试题主键id") @RequestParam int questionId,
                                    HttpServletRequest httpServletRequest) throws Exception{

        questionService.deleteQuestion(httpServletRequest,questionId);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/deleteQuestionList",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "试题批量删除接口")
    public RetResult deleteQuestionList(@ApiParam(value = "试题主键id字符串，多个以;分割") @RequestParam String questionIdStr,
                                        HttpServletRequest httpServletRequest) throws Exception{

        questionService.deleteQuestionList(httpServletRequest, questionIdStr);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/selectQuestionListCount",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询试题列表总数")
    public RetResult selectQuestionListCount(HttpServletRequest httpServletRequest,
                                             @ApiParam(value = "一级目录id") @RequestParam int categoryOne,
                                             @ApiParam(value = "二级目录id") @RequestParam int categoryTwo,
                                             @ApiParam(value = "课程id") @RequestParam int courseId) throws Exception{

        Map<String,Object> map = new HashMap<>();
        map.put("categoryOne", categoryOne);
        map.put("categoryTwo", categoryTwo);
        map.put("courseId", courseId);
        return RetResponse.makeOKRsp(questionService.selectQuestionListCount(httpServletRequest,map));
    }

    @RequestMapping(value = "/selectQuestionList",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询试题列表")
    public RetResult selectQuestionList(HttpServletRequest httpServletRequest,
                                        @RequestBody @Valid QueryQuestionModel model) throws Exception{

        Map<String,Object> map = new HashMap<>();
        map.put("categoryOne", model.getCategoryOne());
        map.put("categoryTwo", model.getCategoryTwo());
        map.put("courseId", model.getCourseId());
        map.put("questionType", model.getQuestionType());
        map.put("questionBody", model.getQuestionBody());
        map.put("difficultyLevel", model.getDifficultyLevel());
        map.put("points", model.getKnowledgePoints());
        return RetResponse.makeOKRsp(questionService.selectQuestionList(httpServletRequest,map));
    }

}
