package com.by.blcu.course.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.course.model.CatalogAndResourceModel;
import com.by.blcu.course.model.CatalogModel;
import com.by.blcu.course.model.KnowledgePointNode;
import com.by.blcu.course.service.ICatalogService;
import com.by.blcu.course.service.IStudentCourseService;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CheckToken
@RestController
@RequestMapping("/catalog")
@Api(tags = "目录管理API",description = "包含接口：\n" +
        "1、课程目录导入\n" +
        "2、查询课程目录结构\n" +
        "3、修改课程目录结构\n" +
        "4、添加课程目录结构\n" +
        "5、删除课程目录结构\n"+
        "6、管理端课程目录包含资源的查询\n"+
        "7、修改目录状态为启用或禁用\n"+
        "8、点下一步，整体保存目录结构")
public class CatalogController {

    @Autowired
    private ICatalogService catalogService;
    @Autowired
    private IStudentCourseService courseService;

    @RequestMapping(value = "/importExcel", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "课程目录导入",notes = "课程目录导入",httpMethod = "POST")
    public RetResult<KnowledgePointNode> importExcel(@ApiParam(value = "file文件",required = true) @RequestParam(value = "file") MultipartFile file,
                                 @ApiParam(value = "课程id",required = true) @RequestParam int courseId,
                                 @ApiParam(value = "机构id",required = true) @RequestParam String orgCode,
                                 HttpServletRequest request) throws Exception {

        int userId = Integer.valueOf(request.getAttribute("userId").toString());
        Map<String,Object> paraMap = new HashMap<>();
        paraMap.put("courseId", courseId);
        paraMap.put("orgCode", orgCode);
        paraMap.put("createUserId", userId);
        return RetResponse.makeOKRsp(catalogService.importKnowledgePoints(file,paraMap));
    }

    @RequestMapping(value = "/getKnowledgePoints", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "查询课程目录结构",notes = "查询课程目录结构")
    public RetResult<KnowledgePointNode> getKnowledgePoints(@ApiParam(value = "课程id【courseId】；status(状态：启用 1； 禁用 0)") @RequestBody JSONObject obj) throws ServiceException {

        Integer status = 1;
        if(obj.containsKey("status")){
            status = Integer.valueOf(obj.getString("status"));
        }
        if(obj.containsKey("courseId")){
            int courseId = Integer.parseInt(obj.getString("courseId"));
            return RetResponse.makeOKRsp(catalogService.getKnowledgePoints(courseId,status));
        }else {
            throw new ServiceException("参数：【courseId】必传！");
        }
    }

    @RequestMapping(value = "/editKnowledgePoints", method = RequestMethod.PUT ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "修改课程目录结构",notes = "修改课程目录结构")
    public RetResult editKnowledgePoints(@ApiParam(value = "修改目录的实体:包含两个参数：catalogId（目录主键）；name(目录名称)") @RequestBody JSONObject obj,
                                         HttpServletRequest request) throws ServiceException {

        if(obj.containsKey("catalogId") && obj.containsKey("name")){
            int catalogId = Integer.parseInt(obj.getString("catalogId"));
            String name = obj.getString("name");
            return catalogService.editKnowledgePoints(catalogId,name,request,new CourseCheckModel());
        }else {
            throw new ServiceException("参数：【catalogId】，【name】必传！");
        }

    }

    @RequestMapping(value = "/changeStatus", method = RequestMethod.PUT ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "修改目录状态为启用或禁用",notes = "修改目录状态为启用或禁用")
    public RetResult changeStatus(@ApiParam(value = "修改目录的实体:包含两个参数：catalogId（目录主键）；status(状态：启用 1； 禁用 0)") @RequestBody JSONObject obj,
                                         HttpServletRequest request) throws ServiceException {

        if(obj.containsKey("catalogId") && obj.containsKey("status")){
            int catalogId = Integer.parseInt(obj.getString("catalogId"));
            Integer status = Integer.parseInt(obj.getString("status"));
            return catalogService.changeStatus(catalogId,status,request);
        }else {
            throw new ServiceException("参数：【catalogId】，【status】必传！");
        }

    }

    @RequestMapping(value = "/addKnowledgePoints", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "添加课程目录结构",notes = "添加课程目录结构")
    public RetResult addKnowledgePoints(@RequestBody CatalogModel catalogModel,HttpServletRequest request) throws ServiceException {

        return catalogService.addKnowledgePoints(catalogModel,request,new CourseCheckModel());
    }

    @RequestMapping(value = "/deleteKnowledgePoints", method = RequestMethod.DELETE ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "删除课程目录结构",notes = "删除课程目录结构")
    public RetResult deleteKnowledgePoints(@ApiParam(value = "目录主键id:pointDetailId") @RequestBody JSONObject object) throws Exception {

        if(object.containsKey("pointDetailId")){
            String pointDetailId = object.getString("pointDetailId");
            return catalogService.deleteKnowledgePoints(Integer.parseInt(pointDetailId));
        }else{
            return RetResponse.makeErrRsp("需要传入参数pointDetailId！");
        }
    }

    @RequestMapping(value = "/saveKnowledgePoints", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "保存整体课程目录结构",notes = "点下一步，整体保存课程目录结构")
    public RetResult saveKnowledgePoints(@RequestBody List<KnowledgePointNode> knowledgePointNode) throws ServiceException {

        catalogService.saveKnowledgePoints(knowledgePointNode);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/getCourseCatalog",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "管理端课程目录包含资源的查询")
    public RetResult<List<CatalogAndResourceModel>> getCourseCatalog(HttpServletRequest httpServletRequest,
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
}
