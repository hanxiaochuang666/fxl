package com.by.blcu.course.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.CommonUtils;
import com.by.blcu.core.utils.MapAndObjectUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.dto.Course;
import com.by.blcu.course.dto.CourseModelType;
import com.by.blcu.course.model.CategoryAndModel;
import com.by.blcu.course.model.CourseViewModel;
import com.by.blcu.course.model.SyncResourcesModel;
import com.by.blcu.course.service.ICourseService;
import com.by.blcu.mall.model.CommodityInfo;
import com.by.blcu.mall.service.CommodityInfoService;
import com.by.blcu.mall.service.CourseCategoryInfoService;
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
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@CheckToken
@RequestMapping("/course")
@Api(tags = "教师课程内容管理API",description = "包含接口：\n" +
        "1、根据课程分类查询课程列表\n" +
        "2、管理员根据课程分类查询课程列表\n" +
        "3、根据条件查询课程列表分页\n" +
        "4、获取课程模块分类\n" +
        "5、保存更新/课程模块分类\n" +
        "6、删除课程\n" +
        "7、批量删除课程\n" +
        "8、课程提交审核\n" +
        "9、获取课程回显信息\n" +
        "10、校验课程是否允许编辑")
public class CourseManageController {

    @Resource
    private ICourseService courseService;
    @Resource
    private CourseCategoryInfoService courseCategoryInfoService;
    @Resource
    private CommodityInfoService commodityInfoService;

    private static Map<String, String> categoryMap = null;

    @RequestMapping(value = "/getCourseByType",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "根据课程分类查询课程列表",notes = "根据课程分类查询课程列表")
    RetResult<List<Course>> getCourseByType(@ApiParam(value = "二级分类id【categoryTwoId】") @RequestBody JSONObject object,
                                            HttpServletRequest request){

        String userId = request.getAttribute("userId").toString();
        String categoryTwoId = "";
        if(object.containsKey("categoryTwoId")) {
            categoryTwoId = object.getString("categoryTwoId");
        }else{
            return RetResponse.makeErrRsp("需要传入参数categoryTwoId！");
        }
        if(StringUtils.isEmpty(categoryTwoId)){
            return RetResponse.makeOKRsp();
        }
        Map<String,Object> parMap = new HashMap<>();
        parMap.put("categoryTwo",categoryTwoId);
        parMap.put("createUser",userId);
        return RetResponse.makeOKRsp(courseService.selectList(parMap));
    }

    @RequestMapping(value = "/selectCourseByType",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "管理员根据课程分类查询课程列表",notes = "管理员根据课程分类查询课程列表")
    RetResult<List<Course>> selectCourseByType(@ApiParam(value = "二级分类id【categoryTwoId】") @RequestBody JSONObject object){

        String categoryTwoId = "";
        if(object.containsKey("categoryTwoId")) {
            categoryTwoId = object.getString("categoryTwoId");
        }else{
            return RetResponse.makeErrRsp("需要传入参数categoryTwoId！");
        }
        if(StringUtils.isEmpty(categoryTwoId)){
            return RetResponse.makeOKRsp();
        }
        Map<String,Object> parMap = new HashMap<>();
        parMap.put("categoryTwo",categoryTwoId);
        return RetResponse.makeOKRsp(courseService.selectList(parMap));
    }

    @PostMapping("/list")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "查询课程列表")
    RetResult<List<Course>> queryCourseWithPage(@RequestBody CourseViewModel course, HttpServletRequest request) throws Exception {
        int userId = Integer.valueOf(request.getAttribute("userId").toString());

        //根据传入条件进行查询，支持模糊查询，注意连表查询 Course 的类目信息
        Map<String, Object> param = MapAndObjectUtils.ObjectToMap2(course);
        CommonUtils.queryParamOpt(param);
        param.put("createUser", userId);
        long count = courseService.selectCount(param);
        if(count < 1){
            return RetResponse.makeRsp(null,count);
        }
        List<Course> courseList = courseService.selectList(param);
        //获取分类信息
        categoryMap = courseCategoryInfoService.selectList();
        //类别封装需要获取
        if(courseList != null && courseList.size() > 0){
            for(Course courseTemp : courseList){
                courseTemp.setCategoryOne(categoryMapInit(courseTemp.getCategoryOne(),"一类"));
                courseTemp.setCategoryTwo(categoryMapInit(courseTemp.getCategoryTwo(),"二类"));
            }
        }

        return RetResponse.makeRsp(courseList, count);
    }

    @PostMapping("/getModelList")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "获取模块分类")
    RetResult getCourseModelList(@RequestBody CourseViewModel course) throws Exception{
        //查询全部类目 以及模块分类
        List modelList = courseService.getCourseModelInfo(course.getCourseId());
        return RetResponse.makeOKRsp(modelList);
    }


    @PostMapping("/add")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "保存/更新课程模块分类")
    RetResult addCourseInfoModelAndCategory(@Valid @RequestBody CategoryAndModel model, HttpServletRequest request) throws Exception {
        int userId = Integer.valueOf(request.getAttribute("userId").toString());
        String orgCode = request.getAttribute("orgCode").toString();
        if(!StringUtils.isBlank(model.getName()) && model.getName().length() > 30){
            throw new ServiceException("传入课程名称超长！最长30字");
        }
        //创建类目以及选择模块
        Integer courseId = courseService.addCourseInfoModelAndCategory(userId, model, orgCode);
        Map result = MapUtils.initMap("courseId", courseId);
        return RetResponse.makeOKRsp(result);
    }


    @PostMapping("/delete/{id}")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "删除课程")
    RetResult deleteCourseById(@NotBlank(message = "{required}")@PathVariable Integer id, HttpServletRequest request) throws Exception {
        //课程删除，需要确认该课程，是否上架，是否审核通过，是否有人学习
        int userId = Integer.valueOf(request.getAttribute("userId").toString());
        Course course = courseService.selectByPrimaryKey(id);
        if(course.getCreateUser().intValue() != userId){
            log.info("用户Id：" + userId + "，不可删除非本人创建课程，课程Id："+id);
            return RetResponse.makeRsp(RetCode.SUCCESS.code,"不可删除非本人创建课程");
        }
        return courseService.deleteCourseById(id);
    }


    @PostMapping("/deleteByIds")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "批量删除课程")
    RetResult batchDeleteCourseByIdList(@RequestBody JSONObject object, HttpServletRequest request) throws Exception {
        //存在一个不满足删除条件的，就不允许批量删除
        if(StringUtils.isEmpty(object.getString("ids"))){
            RetResponse.makeErrRsp("传入ID不可为空");
        }
        int userId = Integer.valueOf(request.getAttribute("userId").toString());
        String[] idsArr = object.getString("ids").split(",");
        for(String id : idsArr){
            boolean operation = CommonUtils.isOperation(userId, Integer.valueOf(id), courseService);
            if(!operation){
                log.info("用户Id：" + userId + "不可删除非本人创建课程，课程Id："+id);
                return RetResponse.makeRsp(RetCode.SUCCESS.code,"不可删除非本人创建课程");
            }
        }
        return courseService.batchDeleteCourse(idsArr);
    }

    @PostMapping("/submitReview/{courseId}")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "课程提交审核")
    RetResult submitCourseReviewById(@NotBlank(message = "{required}")@PathVariable Integer courseId) throws Exception{
        Course course = courseService.selectByPrimaryKey(courseId);
        if(StringUtils.isEmpty(course))
            throw new ServiceException("该课程不存在！");
        if(course.getStatus() != 0)
            throw new ServiceException("该课程非待审核状态");

        try {
            course.setStatus(1);
            Integer state = courseService.updateByPrimaryKeySelective(course);
        } catch (Exception e) {
            log.error("课程ID："+courseId + " 提交审核失败！",e);
            throw new ServiceException("课程ID："+courseId + " 提交审核失败！");
        }
        return RetResponse.makeOKRsp();
    }

    @PostMapping("/syncCourseResources")
    //@RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "同步购买课程的资源")
    RetResult syncCourseResources(@RequestBody SyncResourcesModel syncResourcesModel){
        List<Integer> courseIdLst = syncResourcesModel.getCourseIdLst();
        if(null==courseIdLst || courseIdLst.size()<=0){
            log.info("课程id 的集合不能为空");
            return RetResponse.makeErrRsp("请求课程id 的集合不能为空");
        }
        Integer student = syncResourcesModel.getStudent();
        if(null==student || student.intValue()<=0){
            log.info("学生id不能为空");
            return RetResponse.makeErrRsp("学生id不能为空");
        }
        return courseService.syncCourseResources(syncResourcesModel.getCourseIdLst(),syncResourcesModel.getStudent());
    }


    private String categoryMapInit(String key, String category){
        //课程分类信息更新 定义时间段来更新 比如整点更新
        return StringUtils.isEmpty(categoryMap.get(key)) ? category : categoryMap.get(key);
    }

    @PostMapping("/get")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "获取课程模块分类回显")
    RetResult getCourseInfoModelAndCategory(@RequestBody CategoryAndModel model, HttpServletRequest request) throws Exception {
        int userId = Integer.valueOf(request.getAttribute("userId").toString());
        CategoryAndModel categoryAndModel = new CategoryAndModel();
        if(StringUtils.isEmpty(model.getCourseId())){
            throw new ServiceException("课程ID不能为空");
        }
        Map param = MapUtils.initMap("courseId", model.getCourseId());
        List<Course> courses = courseService.selectList(param);
        if(courses != null && courses.size() > 0){
            Course course = courses.get(0);
            categoryAndModel.setIsRelated(false);
            //获取课程是否关联商品情况
            List<CommodityInfo> commodityInfos = commodityInfoService.selectByCourseId(String.valueOf(course.getCourseId()));
            if(commodityInfos != null && commodityInfos.size() > 0){
                categoryAndModel.setIsRelated(true);
            }
            categoryAndModel.setCourseId(course.getCourseId());
            categoryAndModel.setName(course.getName());
            categoryAndModel.setCategoryOne(course.getCategoryOne());
            categoryAndModel.setCategoryTwo(course.getCategoryTwo());
            List<CourseModelType> modelList = courseService.getCourseModelInfo(course.getCourseId());
            List<Integer> modelTypes = new ArrayList<>();
            modelList.forEach(modelType -> modelTypes.add(modelType.getCourseModelTypeId()));
            categoryAndModel.setModelList(modelTypes);

        }else{
            throw new ServiceException("该课程不存在！课程ID：" + model.getCourseId());
        }

        return RetResponse.makeOKRsp(categoryAndModel);
    }

    @PostMapping("/verify")
    @RequiresPermissions("teacher")
    @ApiOperation(httpMethod = "POST", value = "校验课程是否允许编辑")
    RetResult verifyCourseAllowsEditing(@RequestBody CourseViewModel course) throws Exception{
        return courseService.verifyCourseAllowsEditing(course);
    }

}
