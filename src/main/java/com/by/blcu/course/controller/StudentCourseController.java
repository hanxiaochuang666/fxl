package com.by.blcu.course.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.course.model.CatalogAndResourceModel;
import com.by.blcu.course.model.PreLiveInfoModel;
import com.by.blcu.course.service.IStudentCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@CheckToken
@RestController
@RequestMapping(value = "/question")
@Slf4j
@Api(tags = "我的课程相关API",description = "包含接口：\n" +
        "1、在学/过期课程查询\n" +
        "2、学习进度查询\n" +
        "3、继续学习查询资源\n" +
        "4、开始学习查询资源\n" +
        "5、查询直播预告\n" +
        "6、文件预览接口\n"+
        "7、课程目录包含资源的查询")
public class StudentCourseController {

    @Autowired
    private IStudentCourseService courseService;

    @RequestMapping(value = "/selectStudentCourse",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("teacher")
    @ApiOperation(value = "在学/过期课程查询")
    public RetResult selectStudentCourse(HttpServletRequest httpServletRequest,
                                                             @ApiParam(value = "类型:1 表示在学，0 表示过期【type】;每页条数【pageSize】；" +
                                                                     "当前页码【pageIndex】；") @RequestBody JSONObject object) throws Exception{

        Integer pageSize = null;
        Integer pageIndex = null;
        if(object.containsKey("pageSize")){
            pageSize = Integer.valueOf(object.getString("pageSize"));
        }
        if(object.containsKey("pageIndex")){
            pageIndex = Integer.valueOf(object.getString("pageIndex"));
        }
        if(object.containsKey("type")) {
            Integer type = Integer.valueOf(object.getString("type"));
            return courseService.selectStudentCourse(type,httpServletRequest,pageSize,pageIndex);
        }else{
            return RetResponse.makeErrRsp("type必填！");
        }
    }


    @RequestMapping(value = "/selectStudySchedule",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("teacher")
    @ApiOperation(value = "学习进度查询")
    public RetResult<Map<String,Object>> selectStudySchedule(HttpServletRequest httpServletRequest,
                                                             @ApiParam(value = "参数课程id【courseId】") @RequestBody JSONObject object) throws Exception{

        if(object.containsKey("courseId")) {
            Integer courseId = Integer.valueOf(object.getString("courseId"));
            return RetResponse.makeOKRsp(courseService.selectStudySchedule(courseId));
        }else{
            return RetResponse.makeErrRsp("courseId必填！");
        }
    }

    @RequestMapping(value = "/keepStudy",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("teacher")
    @ApiOperation(value = "继续学习查询资源")
    public RetResult keepStudy(@ApiParam(value = "参数课程id【courseId】") @RequestBody JSONObject object,
                               HttpServletRequest request) throws Exception{

        if(object.containsKey("courseId")) {
            Integer courseId = Integer.valueOf(object.getString("courseId"));
            return RetResponse.makeOKRsp(courseService.keepStudy(courseId,request));
        }else{
            return RetResponse.makeErrRsp("courseId必填！");
        }
    }

    @RequestMapping(value = "/beginStudy",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "开始学习查询资源")
    public RetResult beginStudy(@ApiParam(value = "参数:目录id【catalogId】,类型【type】C： 学生  T: 老师") @RequestBody JSONObject object,
                               HttpServletRequest request) throws Exception{

        if(!object.containsKey("type")) {
            return RetResponse.makeErrRsp("type必填！");
        }
        if(object.containsKey("catalogId")) {
            Integer catalogId = Integer.valueOf(object.getString("catalogId"));
            String type = object.getString("type");
            return RetResponse.makeOKRsp(courseService.beginStudy(type,catalogId,request));
        }else{
            return RetResponse.makeErrRsp("catalogId必填！");
        }
    }

    @RequestMapping(value = "/selectCourseCatalog",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "课程目录包含资源的查询")
    public RetResult<List<CatalogAndResourceModel>> selectCourseCatalog(HttpServletRequest httpServletRequest,
                                                                        @ApiParam(value = "参数课程id【courseId】,用户类型【userType】C：表示普通用户，M：表示管理员,目录状态【status】 0：禁用，1：启用") @RequestBody JSONObject object) throws Exception{

        Integer status = null;
        if(!object.containsKey("userType")){
            return RetResponse.makeErrRsp("用户类型【userType】必填！");
        }
        if(object.containsKey("status")){
            status = Integer.valueOf(object.getString("status"));
        }
        if(object.containsKey("courseId")) {
            String userType = object.getString("userType");
            Integer courseId = Integer.valueOf(object.getString("courseId"));
            return RetResponse.makeOKRsp(courseService.selectCourseCatalog(status,courseId,userType,httpServletRequest));
        }else{
            return RetResponse.makeErrRsp("courseId必填！");
        }
    }

    @RequestMapping(value = "/selectPreLiveInfo",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询直播预告")
    public RetResult<List<PreLiveInfoModel>> selectPreLiveInfo(HttpServletRequest httpServletRequest,
                                                               @ApiParam(value = "参数: 课程id【courseId】") @RequestBody JSONObject object){

        if(object.containsKey("courseId")) {
            Integer courseId = Integer.valueOf(object.getString("courseId"));
            return RetResponse.makeOKRsp(courseService.selectPreLiveInfo(courseId));
        }else{
            return RetResponse.makeErrRsp("courseId必填！");
        }
    }


    @RequestMapping(value = "/filePreview",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "文件预览")
    public void filePreview(HttpServletRequest httpServletRequest,
                            HttpServletResponse response,
                            @ApiParam(value = "参数: 文件url【fileUrl】,文件名称【fileName】") @RequestBody JSONObject object) {
        if(!object.containsKey("fileName")) {
            throw new ServiceException("fileName！");
        }
        if(object.containsKey("fileUrl")) {
            String fileUrl = object.getString("fileUrl");
            String fileName = object.getString("fileName");
            try {
                courseService.filePreview(fileUrl, fileName, httpServletRequest, response);
            }catch (Exception e){
                throw new ServiceException(e);
            }
        }else{
            throw new ServiceException("fileUrl必填！");
        }

    }

}
