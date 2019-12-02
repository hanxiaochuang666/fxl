package com.by.blcu.resource.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.resource.dto.QuestionType;
import com.by.blcu.resource.service.IQuestionTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
@Api(tags = "试题类型管理API",description = "包含接口：\n"+
        "1、新增试题类型\n"+
        "2、删除试题类型\n"+
        "3、修改试题类型\n"+
        "4、查询试题类型列表\n")
public class QuestionTypeController {


    @Autowired
    private IQuestionTypeService typeService;

    @RequestMapping(value = "/addQuestionType",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "新增试题类型")
    public RetResult addQuestionType(@RequestBody QuestionType model, HttpServletRequest request){

        typeService.insertSelective(model);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/deleteQuestionType",method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "删除试题类型")
    public RetResult deleteQuestionType(@ApiParam(value = "试题类型主键【id】") @RequestBody JSONObject object, HttpServletRequest request){

        Integer id = null;
        if(object.containsKey("id")){
            id = Integer.valueOf(object.getString("id"));
        }else{
            return RetResponse.makeErrRsp("需要传入参数试题类型主键【id】！");
        }

        QuestionType type = new QuestionType();
        type.setQuestionTypeId(id);
        type.setStatus((byte) 0);
        typeService.updateByPrimaryKeySelective(type);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/updateQuestionType",method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "修改试题类型")
    public RetResult updateQuestionType(@RequestBody QuestionType model, HttpServletRequest request){

        typeService.updateByPrimaryKeySelective(model);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/getQuestionTypeList",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "查询试题类型列表")
    public RetResult getQuestionTypeList(@ApiParam(value = "int类型【status】")@RequestBody JSONObject object, HttpServletRequest request){

        Integer status ;
        if(object.containsKey("status")){
            status = Integer.valueOf(object.getString("status"));
        }else{
            return RetResponse.makeErrRsp("需要传入参数试题类型主键【status】！");
        }
        Map<String,Object> map = new HashMap<>();
        if(-1 != status){
            map.put("status",status);
        }
        return RetResponse.makeOKRsp(typeService.selectList(map));
    }
}
