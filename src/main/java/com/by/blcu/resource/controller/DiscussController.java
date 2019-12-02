package com.by.blcu.resource.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.resource.model.DiscussModel;
import com.by.blcu.resource.model.QryDiscussModel;
import com.by.blcu.resource.model.ReplyInfoModel;
import com.by.blcu.resource.service.IDiscussService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Slf4j
@CheckToken
@RequestMapping("/discuss")
@Api(tags = "讨论管理API",description = "包含接口：\n" +
        "1、目录下保存或更新讨论主题\n" +
        "2、创建讨论主题\n" +
        "3、添加回复\n" +
        "4、编辑讨论主题\n" +
        "5、编辑回复\n" +
        "6、查询讨论主题列表\n" +
        "7、查询讨论主题下的回复列表\n" +
        "8、删除讨论主题\n" +
        "9、删除回复")
public class DiscussController  {

    @Autowired
    private IDiscussService discussService;

    @RequestMapping(value = "/saveDiscuss",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "目录下创建讨论主题")
    public RetResult saveDiscuss(HttpServletRequest httpServletRequest,
                                @RequestBody @Valid DiscussModel model) throws Exception {

        return RetResponse.makeOKRsp(discussService.saveDiscuss(model,httpServletRequest));
    }

    @RequestMapping(value = "/addDiscuss",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "创建讨论主题")
    public RetResult addDiscuss(HttpServletRequest httpServletRequest,
                                @RequestBody @Valid DiscussModel model) throws Exception{

        return RetResponse.makeOKRsp(discussService.addDiscuss(model,httpServletRequest));
    }

    @RequestMapping(value = "/addReply",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "添加回复")
    public RetResult addReply(HttpServletRequest httpServletRequest,
                                @RequestBody @Valid ReplyInfoModel model){

        return RetResponse.makeOKRsp(discussService.addReply(model,httpServletRequest));
    }

    @RequestMapping(value = "/editDiscuss",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "编辑讨论主题")
    public RetResult editDiscuss(HttpServletRequest httpServletRequest,
                                @RequestBody @Valid DiscussModel model) throws Exception{

        return RetResponse.makeOKRsp(discussService.editDiscuss(model,httpServletRequest));
    }

    @RequestMapping(value = "/editReply",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "编辑回复")
    public RetResult editReply(HttpServletRequest httpServletRequest,
                                 @RequestBody @Valid ReplyInfoModel model) throws Exception{

        return RetResponse.makeOKRsp(discussService.editReply(model,httpServletRequest));
    }

    @RequestMapping(value = "/deleteDiscuss",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "删除讨论主题")
    public RetResult<String> deleteDiscuss(HttpServletRequest httpServletRequest,
                                           @ApiParam(value = "讨论主题id【resourceId】,") @RequestBody JSONObject object){

        if(object.containsKey("resourceId")) {
            Integer resourceId = Integer.parseInt(object.getString("resourceId"));
            discussService.deleteDiscuss(resourceId,httpServletRequest);
        }else{
            return RetResponse.makeErrRsp("参数resourceId必传！");
        }

        return RetResponse.makeOKRsp("删除成功！");
    }

    @RequestMapping(value = "/deleteReply",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "删除回复内容")
    public RetResult<String> deleteReply(HttpServletRequest httpServletRequest,
                                         @ApiParam(value = "回复id【discussId】") @RequestBody JSONObject object){

        if(object.containsKey("discussId")) {
            Integer discussId = Integer.parseInt(object.getString("discussId"));
            discussService.deleteReply(discussId,httpServletRequest);
        }else{
            return RetResponse.makeErrRsp("需要传入参数discussId！");
        }

        return RetResponse.makeOKRsp("删除成功！");
    }

    @RequestMapping(value = "/getDiscussList",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询讨论主题列表")
    public RetResult getDiscussList(HttpServletRequest httpServletRequest,
                                                        @RequestBody @Valid QryDiscussModel model){
        return discussService.getDiscussList(model,httpServletRequest);
    }

    @RequestMapping(value = "/getReplyList",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询主题下面的回复列表")
    public RetResult getReplyList(HttpServletRequest httpServletRequest,
                                  @ApiParam(value = "讨论主题id【resourceId】") @RequestBody JSONObject object){

        if(object.containsKey("resourceId")) {
            Integer resourceId = Integer.parseInt(object.getString("resourceId"));
            return discussService.getReplyList(resourceId,httpServletRequest);
        }else{
            return RetResponse.makeErrRsp("需要传入参数resourceId！");
        }

    }

}
