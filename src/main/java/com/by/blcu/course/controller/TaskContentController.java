package com.by.blcu.course.controller;

import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.MapAndObjectUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.dto.TaskModel;
import com.by.blcu.course.model.TaskViewModel;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.course.service.courseCheck.CourseCheckModel;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.dto.TestPaper;
import com.by.blcu.resource.service.IResourcesService;
import com.by.blcu.resource.service.ITestPaperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@CheckToken
@RequestMapping("/task")
@Api(tags = "教师课程作业测试API",description = "包含接口：\n" +
        "1、根据课程查询作业测试\n" +
        "2、新增课程关联作业测试\n" +
        "3、删除课程关联作业测试\n" +
        "4、获取作业试卷列表\n" +
        "5、获取测试试卷列表\n" +
        "6、批量删除关联作业测试\n" +
        "7、根据资源ID获取作业测试名称")
public class TaskContentController {

    private String message;
    @Resource
    private ICourseDetailService courseDetailService;
    @Resource
    private IResourcesService resourcesService;
    @Resource
    private ITestPaperService testPaperService;

    @PostMapping("/taskList")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "查询作业测试列表")
    RetResult<List<TaskModel>> queryTaskList(@Valid @RequestBody TaskViewModel taskVO) throws ServiceException{
        List<TaskModel> taskModels = null;
        try{
            //按照原型，暂不加分页
            taskModels = courseDetailService.getTaskList(taskVO);
        }catch (Exception e){
            message = "查询测试列表失败";
            log.error(message, e);
            throw new ServiceException(message);
        }
        return RetResponse.makeOKRsp(taskModels).setTotal(taskModels.size());
    }

    @PostMapping("/addTask")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "新增关联作业测试")
    RetResult addTask(@RequestBody TaskViewModel taskVO, HttpServletRequest request) throws ServiceException{
        if(StringUtils.isBlank(taskVO.getTestPaperId())){
            throw new ServiceException("作业测试ID不可为空！");
        }
        if(!StringUtils.isBlank(taskVO.getTaskName()) && taskVO.getTaskName().length() > 30){
            throw new ServiceException("传入试卷名称超长！最长30字");
        }
        try{
            //新增 前提需要查询试题列表（test_paper）
            Integer rid = courseDetailService.addTask(taskVO, request,new CourseCheckModel());
            Map map = MapUtils.initMap("resourcesId",rid);
            map.put("content", taskVO.getTestPaperId());
            return RetResponse.makeOKRsp(map);
        }catch (Exception e){
            message = "新增作业测试失败";
            log.error(message, e);
            throw new ServiceException(e.getMessage());
        }
    }

    @PostMapping("/taskPapers")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "获取作业列表")
    RetResult getTaskPaperList(@RequestBody TaskViewModel taskVO, HttpServletRequest request) throws ServiceException{
        List<TestPaper> taskPapers = null;
        if(StringUtils.isEmpty(taskVO.getModelType())){
            throw new ServiceException("传入模块ID不能为空！");
        }
        try {
            //uesType 0测试 1作业
            taskPapers = courseDetailService.getTestPaperList(taskVO, 1, request);
        }catch (Exception e){
            message = "获取作业列表失败";
            log.error(message, e);
            throw new ServiceException(message);
        }
        return  RetResponse.makeOKRsp(taskPapers).setTotal(taskPapers.size());
    }

    @PostMapping("/testPapers")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "获取测试列表")
    RetResult getTestPaperList(@RequestBody TaskViewModel taskVO, HttpServletRequest request) throws ServiceException{
        List<TestPaper> testPapers = null;
        if(StringUtils.isEmpty(taskVO.getModelType())){
            throw new ServiceException("传入模块ID不能为空！");
        }
        try {
            testPapers = courseDetailService.getTestPaperList(taskVO, 0, request);
        }catch (Exception e){
            message = "获取测试列表失败";
            log.error(message, e);
            throw new ServiceException(message);
        }
        return  RetResponse.makeOKRsp(testPapers).setTotal(testPapers.size());
    }

    @PostMapping("/deleteTask")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "删除关联作业测试")
    RetResult deleteTask(@RequestBody TaskViewModel taskVO, HttpServletRequest request) throws ServiceException{
        if(StringUtils.isEmpty(taskVO.getCourseId())){
            throw new ServiceException("传入课程ID不能为空！");
        }else if(StringUtils.isEmpty(taskVO.getResourcesId())){
            throw new ServiceException("传入资源ID不能为空！");
        }
        try{
            //删除关联资源关系
           Integer state = courseDetailService.deleteTask(taskVO, request);
           return RetResponse.makeOKRsp(state);
        }catch (Exception e){
            message = "删除作业测试失败";
            log.error(message, e);
            throw new ServiceException(message);
        }
    }

    @PostMapping
    @ApiOperation(httpMethod = "POST", value = "根据资源ID获取作业测试名称")
    RetResult getTaskByResourcesId(@RequestBody TaskViewModel taskVO) throws Exception{
        //获取资源关联作业的名字 需要联查？
        if(StringUtils.isEmpty(taskVO.getResourcesId())){
            throw new ServiceException("资源ID不能为空");
        }
        Resources resources = null;
        Map param = MapUtils.initMap("resourcesId", taskVO.getResourcesId());
        List<Resources> resourcesList = resourcesService.selectList(param);
        if(resourcesList != null && resourcesList.size() > 0){
            resources = resourcesList.get(0);
            if(org.apache.commons.lang3.StringUtils.isNumeric(resources.getContent())){
                TestPaper testPaper = testPaperService.selectByPrimaryKey(Integer.valueOf(resources.getContent()));
                resources.setContent(testPaper.getName());
            }
        }else{
            throw new ServiceException("该资源不存在，资源ID：" + taskVO.getResourcesId());
        }

        return RetResponse.makeOKRsp(resources);
    }

}
