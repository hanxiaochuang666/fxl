package com.by.blcu.resource.controller;


import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.mall.vo.MallCommodityOrderVo;
import com.by.blcu.mall.vo.MallOrderInfoVo;
import com.by.blcu.resource.model.StudentCourseModel;
import com.by.blcu.resource.model.SyncTestPaperModel;
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
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/testResult")
@Api(tags = "学生作业操作API")
@Slf4j
@CheckToken
public class TestResultController {

    @Resource(name="testResultService")
    private ITestResultService testResultService;

    @RequestMapping(value = "/getStudentCourse", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("teacher")
    @ApiOperation(value = "学生作业/测试列表查询",notes = "学生作业/测试列表查询",httpMethod = "GET")
    public RetResult<List<StudentCourseModel>> getStudentCourse(HttpServletRequest httpServletRequest,
            @ApiParam(name = "useType", value = "0 测试, 1 作业") @RequestParam Integer useType,
            @RequestParam(required = false) Integer courseId)throws Exception{
        int userId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString()).intValue();
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        String userName = StringUtils.obj2Str(httpServletRequest.getAttribute("username"));
        return RetResponse.makeOKRsp(testResultService.getStudentCourseLst(useType, userId, userName,courseId,false));
    }

    @RequestMapping(value = "/getCourseTest", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "我的课程查询课程下的作业/测试列表",notes = "我的课程查询课程下的作业/测试列表",httpMethod = "POST")
    public RetResult<List<StudentCourseModel>> getCourseTest(@ApiParam(value = "查询类型【searchType】0 测试, 1 作业;课程id【courseId】") @RequestBody JSONObject object,
                                                             HttpServletRequest request)throws Exception{

        Integer courseId;
        Integer searchType;
        if(object.containsKey("courseId")){
           courseId = Integer.valueOf(object.getString("courseId"));
        }else{
            return RetResponse.makeErrRsp("需要传入参数【courseId】！");
        }
        if(object.containsKey("searchType")){
            searchType = Integer.valueOf(object.getString("searchType"));
        }else{
            return RetResponse.makeErrRsp("需要传入参数【searchType】！");
        }
        String userName = StringUtils.obj2Str(request.getAttribute("username"));
        int userId = Integer.valueOf(request.getAttribute("userId").toString()).intValue();
        return RetResponse.makeOKRsp(testResultService.getStudentCourseLst(searchType, userId, userName,courseId,true));
    }

}
