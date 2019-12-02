package com.by.blcu.course.controller;


import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.AutoCheckRetResponse;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.model.CheckPassModel;
import com.by.blcu.course.model.CourseCheckQueryModel;
import com.by.blcu.course.model.CourseCommitCheck;
import com.by.blcu.course.service.IAutomaticCheckService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@CheckToken
@RequestMapping("/courseCheck")
@Api(tags = "课程审核API",description = "包含接口：\n" +
        "1、课程审核列表查询\n" +
        "2、课程提交审核接口\n" +
        "3、课程审核回调\n" +
        "4、课程审核通过接口\n" +
        "5、课程审核一键通过接口\n" +
        "6、人工课程审核提交")
public class CourseCheckController {

    @Resource(name="automaticCheckService")
    private IAutomaticCheckService automaticCheckService;



    @RequestMapping(value = "/list", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "课程审核列表查询")
    public RetResult list(@RequestBody CourseCheckQueryModel courseCheckQueryModel)throws Exception{
        return automaticCheckService.selectList(courseCheckQueryModel);
    }

    @RequestMapping(value = "/commitCheck", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "课程提交审核接口")
    public RetResult commitCheck(@RequestBody CourseCommitCheck courseCommitCheck)throws Exception{
        if(StringUtils.isEmpty(courseCommitCheck) || StringUtils.isEmpty(courseCommitCheck.getCourseId())){
            return RetResponse.makeErrRsp("请求错误");
        }
        return automaticCheckService.commitCheck(courseCommitCheck.getCourseId());
    }


    @RequestMapping(value = "/checkCallback", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "课程审核回调")
    public AutoCheckRetResponse checkCallback(@RequestParam String checksum, @RequestParam String content){
        boolean back = automaticCheckService.checkCallBack(checksum, content);
        log.info("checksum=["+checksum+"]");
        log.info("content=["+content+"]");
        return back ? RetResponse.makeAutoCheckRetResponse(200,"OK","","",null):RetResponse.makeAutoCheckRetResponse(500,"error","","",null);
    }

    @RequestMapping(value = "/checkPass", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "课程审核通过接口")
    public RetResult checkPass(@RequestBody CheckPassModel checkPassModel){
        return automaticCheckService.checkPass(checkPassModel);
    }

    @RequestMapping(value = "/checkPassAll", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "课程审核一键通过接口")
    public RetResult checkPassAll( HttpServletRequest httpServletRequest, @RequestBody JSONObject jsonObject){
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        if(!jsonObject.containsKey("courseId")|| jsonObject.getInteger("courseId").intValue()<=0){
            log.info("课程id参数错误");
            return RetResponse.makeErrRsp("课程id参数错误");
        }
        return automaticCheckService.checkPassAll(jsonObject.getInteger("courseId"),userId);
    }


    @RequestMapping(value = "/checkCommit", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "人工课程审核提交")
    public RetResult checkCommit( HttpServletRequest httpServletRequest, @RequestBody CourseCommitCheck courseCommitCheck){
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        return automaticCheckService.checkCommit(courseCommitCheck,userId);
    }
}
