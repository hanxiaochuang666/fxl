package com.by.blcu.resource.controller;


import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.resource.model.StudentCourseModel;
import com.by.blcu.resource.service.ITestResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/testResult")
@Api(tags = "学生作业操作API")
@Slf4j
public class TestResultController {

    @Resource(name="testResultService")
    private ITestResultService testResultService;

    @RequestMapping(value = "/syncTestPaper", method = RequestMethod.PUT ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "同步创建学生试卷",notes = "同步创建学生试卷",httpMethod = "PUT")
    public RetResult syncTestPaper(@RequestParam Integer userId, @RequestParam Integer courseId, @RequestParam Integer studentId){
        return testResultService.syncTestPaper(userId,courseId,studentId);
    }

    @RequestMapping(value = "/getStudentCourse", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "学生作业/测试列表查询",notes = "学生作业/测试列表查询",httpMethod = "GET")
    public RetResult<List<StudentCourseModel>> getStudentCourse(@RequestParam Integer useType, @RequestParam Integer studentId, @RequestParam(required = false) Integer courseId)throws Exception{
        return RetResponse.makeOKRsp(testResultService.getStudentCourseLst(useType,studentId,courseId));
    }

}
