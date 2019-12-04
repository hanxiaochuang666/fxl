package com.by.blcu.resource.controller;


import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.CommonUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.resource.model.QueryQuestionModel;
import com.by.blcu.resource.model.QuestionModel;
import com.by.blcu.resource.service.IQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
        "6、根据id查询试题\n" +
        "7、查询试题列表\n" +
        "8、查询试题列表总数")
public class QuestionController {

    @Autowired
    private IQuestionService questionService;

    @RequestMapping(value = "/insertQuestion",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "试题录入接口")
    public RetResult<Integer> insertQuestion(HttpServletRequest httpServletRequest,@Valid @RequestBody QuestionModel questionModel) throws Exception{

        return RetResponse.makeOKRsp(questionService.insertQuestion(questionModel,httpServletRequest));
    }

    @RequestMapping(value = "/importQuestion",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "试题批量导入接口")
    public RetResult<String> importQuestion(@ApiParam(value = "试题导入模板",required = true) @RequestParam(value = "file") MultipartFile file,
                                    @ApiParam(value = "一级目录",required = true) @RequestParam String categoryOne,
                                    @ApiParam(value = "二级目录",required = true) @RequestParam String categoryTwo,
                                    @ApiParam(value = "课程id",required = true) @RequestParam int courseId,
                                    @ApiParam(value = "机构id",required = true) @RequestParam String orgCode,
                                    HttpServletRequest request) throws Exception{

        int userId = Integer.valueOf(request.getAttribute("userId").toString());
        Map<String,Object> paraMap = new HashMap<>();
        paraMap.put("categoryOne", categoryOne);
        paraMap.put("categoryTwo", categoryTwo);
        paraMap.put("courseId", courseId);
        paraMap.put("orgCode", orgCode);
        paraMap.put("createUserId", userId);
        questionService.importQuestion(paraMap,file, request);
        return RetResponse.makeOKRsp("导入成功!");
    }

    @RequestMapping(value = "/editQuestion",method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "试题编辑接口")
    public RetResult editQuestion(HttpServletRequest httpServletRequest,@RequestBody QuestionModel questionModel) throws Exception{
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        boolean operation = CommonUtils.isOperation(userId, questionModel.getQuestionId(), questionService);
        if(!operation){
            log.info("自己只能操作自己的试题");
            return RetResponse.makeRsp(RetCode.SUCCESS.code,"自己只能操作自己的试题");
        }
        questionService.editQuestion(questionModel,httpServletRequest,new CourseCheckModel());
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/deleteQuestion",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "试题删除接口")
    public RetResult deleteQuestion(@ApiParam(value = "试题主键id【questionId】;父试题id【fatherId】如果没有父题则添：-1") @RequestBody JSONObject object,
                                    HttpServletRequest httpServletRequest) throws Exception{
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        Integer questionId;
        Integer fatherId;
        if(object.containsKey("questionId")) {
            questionId = Integer.valueOf(object.getString("questionId"));
        }else{
            return RetResponse.makeErrRsp("需要传入参数【questionId】！");
        }
        if(object.containsKey("fatherId")) {
            fatherId = Integer.valueOf(object.getString("fatherId"));
        }else{
            return RetResponse.makeErrRsp("需要传入参数【fatherId】！");
        }
        boolean operation = CommonUtils.isOperation(userId, questionId, questionService);
        if(!operation){
            log.info("自己只能操作自己的试题");
            return RetResponse.makeRsp(RetCode.SUCCESS.code,"自己只能操作自己的试题");
        }
        questionService.deleteQuestion(httpServletRequest,questionId,fatherId);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/deleteQuestionList",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "试题批量删除接口")
    public RetResult deleteQuestionList(@ApiParam(value = "试题主键id字符串【questionIdStr】，多个以;分割") @RequestBody JSONObject object,
                                        HttpServletRequest httpServletRequest) throws Exception{

        if(object.containsKey("questionIdStr")) {
            String questionIdStr = object.getString("questionIdStr");
            questionService.deleteQuestionList(httpServletRequest, questionIdStr);
        }else{
            return RetResponse.makeErrRsp("需要传入参数questionIdStr！");
        }
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/selectQuestionListCount",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "查询试题列表总数")
    public RetResult selectQuestionListCount(HttpServletRequest httpServletRequest,
                                             @ApiParam(value = "包含参数：一级目录id【categoryOne】，二级目录id【categoryTwo】，" +
                                                     "知识点id【knowledgePoints】,多个使用;分割  -1 表示全部，课程id【courseId】") @RequestBody JSONObject object) throws Exception{

        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        String courseId = "";
        String categoryOne="";
        String categoryTwo="";
        String knowledgePoints="";
        if(object.containsKey("courseId")) {
            courseId = object.getString("courseId");
        }
        if(object.containsKey("categoryOne")) {
            categoryOne = object.getString("categoryOne");
        }
        if(object.containsKey("categoryTwo")) {
            categoryTwo = object.getString("categoryTwo");
        }
        if(object.containsKey("knowledgePoints")) {
            knowledgePoints = object.getString("knowledgePoints");
        }
        Map<String,Object> map = new HashMap<>();
        if(StringUtils.isEmpty(courseId)){
            map.put("courseId", "-1");
        }else{
            map.put("courseId", courseId);
        }
        map.put("categoryOne", categoryOne);
        map.put("categoryTwo", categoryTwo);
        map.put("createUserId", userId);
        map.put("points", knowledgePoints);
        return RetResponse.makeOKRsp(questionService.selectQuestionListCount(httpServletRequest,map));
    }

    @RequestMapping(value = "/selectQuestion",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "根据id查询试题")
    public RetResult<QuestionModel> selectQuestion(HttpServletRequest httpServletRequest,
                                                   @ApiParam(value = "试题主键id【questionId】")@RequestBody JSONObject object) throws Exception{

        if(object.containsKey("questionId")) {
            Integer questionId = Integer.valueOf(object.getString("questionId"));
            return RetResponse.makeOKRsp(questionService.selectQuestion(questionId));
        }else{
            return RetResponse.makeErrRsp("questionId必填！");
        }
    }

    @RequestMapping(value = "/selectQuestionList",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "查询试题列表")
    public RetResult selectQuestionList(HttpServletRequest httpServletRequest,
                                        @RequestBody @Valid QueryQuestionModel model) throws Exception{

        Map<String,Object> map = new HashMap<>();
        map.put("categoryOne", model.getCategoryOne());
        map.put("categoryTwo", model.getCategoryTwo());
        map.put("courseId", model.getCourseId());
        if("null".equals(model.getCourseId())){
            map.put("courseId", "-1");
        }else{
            map.put("courseId", model.getCourseId());
        }
        if("null".equals(model.getDifficultyLevel()) || StringUtils.isEmpty(model.getDifficultyLevel())){
            map.put("difficultyLevel", "-1");
        }else{
            map.put("difficultyLevel", model.getDifficultyLevel());
        }
        map.put("questionBody", StringUtils.isEmpty(model.getQuestionBody())?"":model.getQuestionBody().trim());
        map.put("points", model.getKnowledgePoints());
        map.put("pageSize", model.getPageSize());
        map.put("page", model.getPage());
        map.put("questionType", model.getQuestionType());
        Map retMap = questionService.selectQuestionList(httpServletRequest,map);
        List<QuestionModel> questions = (List<QuestionModel>)retMap.get("questions");
        Long total = (Long) retMap.get("total");
        return RetResponse.makeRsp(questions,total);
    }
}
