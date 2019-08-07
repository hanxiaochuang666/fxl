package com.by.blcu.resource.controller;

import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.resource.dto.QuestionType;
import com.by.blcu.resource.service.IQuestionTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@CheckToken
@RestController
@RequestMapping(value = "/questionType")
@Slf4j
@Api(tags = "试题类型管理API")
public class QuestionTypeController {


    @Autowired
    private IQuestionTypeService typeService;

    @RequestMapping(value = "/addQuestionType",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "新增试题类型")
    public RetResult addQuestionType(@RequestBody QuestionType model, HttpServletRequest request){

        typeService.insertSelective(model);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/deleteQuestionType",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "删除试题类型")
    public RetResult deleteQuestionType(@RequestParam Integer id, HttpServletRequest request){

        QuestionType type = new QuestionType();
        type.setQuestionTypeId(id);
        type.setStatus((byte) 0);
        typeService.updateByPrimaryKeySelective(type);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/updateQuestionType",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "修改试题类型")
    public RetResult updateQuestionType(@RequestBody QuestionType model, HttpServletRequest request){

        typeService.updateByPrimaryKeySelective(model);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/getQuestionTypeList",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询试题类型列表")
    public RetResult getQuestionTypeList(@RequestParam Integer status, HttpServletRequest request){

        Map<String,Object> map = new HashMap<>();
        if(-1 != status){
            map.put("status",status);
        }
        return RetResponse.makeOKRsp(typeService.selectList(map));
    }
}
