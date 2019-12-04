package com.by.blcu.course.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.CommonUtils;
import com.by.blcu.core.utils.DateUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.model.CatalogAndResourceModel;
import com.by.blcu.course.model.PreLiveInfoModel;
import com.by.blcu.course.service.IStudentCourseService;
import com.by.blcu.mall.service.CourseCategoryInfoService;
import com.by.blcu.mall.service.MallOrderInfoService;
import com.by.blcu.resource.dto.TestPaper;
import com.by.blcu.resource.dto.TestPaperFormat;
import com.by.blcu.resource.dto.TestResult;
import com.by.blcu.resource.model.*;
import com.by.blcu.resource.service.ITestPaperFormatService;
import com.by.blcu.resource.service.ITestPaperService;
import com.by.blcu.resource.service.ITestResultDetailService;
import com.by.blcu.resource.service.ITestResultService;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        "7、查询作业测试试卷信息\n"+
        "8、课程目录包含资源的查询")
public class StudentCourseController {

    @Autowired
    private IStudentCourseService courseService;

    @Resource(name="testResultService")
    private ITestResultService testResultService;

    @Resource(name="testPaperService")
    private ITestPaperService testPaperService;

    @Resource
    private MallOrderInfoService mallOrderInfoService;

    @Resource
    private CourseCategoryInfoService courseCategoryInfoService;

    @Resource(name="testPaperFormatService")
    private ITestPaperFormatService testPaperFormatService;

    @Resource(name="testResultDetailService")
    private ITestResultDetailService testResultDetailService;

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

        int studentId = Integer.valueOf(httpServletRequest.getAttribute("userId").toString());
        if(object.containsKey("courseId")) {
            Integer courseId = Integer.valueOf(object.getString("courseId"));
            return RetResponse.makeOKRsp(courseService.selectStudySchedule(courseId,studentId));
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

    @RequestMapping(value = "/getCatalogTestPaperInfo", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询作业/测试试卷信息",notes = "查询作业/测试试卷信息",httpMethod = "POST")
    public RetResult<TestPaperQuestionModel> getCatalogTestPaperInfo(@ApiParam(value = "查询类型【searchType】0:测试,1:作业；课程id【courseId】；试卷id【testPaperId】" +
            "用户类型【userType】学生：C；老师：T") @RequestBody JSONObject object,
                                                             HttpServletRequest request)throws Exception{

        String userType = "";
        Integer courseId;
        Integer searchType;
        Integer testPaperId;
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

        if(!object.containsKey("testPaperId")){
            log.info("参数错误");
            return RetResponse.makeErrRsp("需要传入参数【testPaperId】");
        }else{
            testPaperId = object.getInteger("testPaperId");
        }
        if(!object.containsKey("userType")){
            log.info("参数错误，userType是空");
            return RetResponse.makeErrRsp("需要传入参数【userType】！");
        }else {
            userType = object.getString("userType");
        }

        String userName = StringUtils.obj2Str(request.getAttribute("username"));
        int userId = Integer.valueOf(request.getAttribute("userId").toString());
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        if (!"T".equals(userType.toUpperCase())) {
            //判斷學生是否购买试卷对应的课程
            boolean isBuy = isBuy(userId, testPaperId);
            if (!isBuy) {
                log.info("未购买相关课程,试卷id:" + testPaperId);
                return RetResponse.makeErrRsp("未购买相关课程,试卷id:" + testPaperId);
            }
        }
        List<StudentCourseModel> courseLst = testResultService.getStudentCourseLst(searchType, userId, userName,courseId,false);

        if (!CommonUtils.listIsEmptyOrNull(courseLst)){
            StudentCourseModel courseModel = courseLst.get(0);
            List<StudentCourseInfoModel> studentCourseInfoModels = courseModel.getStudentCourseInfoLst();
            if (!CommonUtils.listIsEmptyOrNull(studentCourseInfoModels)){
                for (StudentCourseInfoModel infoModel : studentCourseInfoModels) {
                    if (testPaperId.equals(infoModel.getTestPaperId())){
                        TestPaperQuestionModel res=new TestPaperQuestionModel();
                        TestPaper testPaper = testPaperService.selectByPrimaryKey(testPaperId);
                        res.setTestPaperName(testPaper.getName());
                        res.setTotalScore(testPaper.getTotalScore());
                        res.setCategoryTwoName(courseCategoryInfoService.selectCcNameByCcId(testPaper.getCategoryTwo()));
                        List<TestPaperFormat> testPaperFormatLst = testPaperFormatService.selectList(MapUtils.initMap("testPaperId", testPaperId));
                        int totalNum=0;
                        for (TestPaperFormat testPaperFormat : testPaperFormatLst) {
                            totalNum+=testPaperFormat.getQuestionNum().intValue();
                        }
                        res.setTotalNum(totalNum);
                        res.setTime(testPaper.getTime());
                        String date2String = DateUtils.date2String(testPaper.getStartTime(), DateUtils.DATE_FORMAT);
                        String date2String1 = DateUtils.date2String(testPaper.getEndTime(), DateUtils.DATE_FORMAT);
                        res.setDateTimeStr(date2String+"~"+date2String1);
                        Integer optType;
                        if(infoModel.getStatus() >= 2){
                            optType = 2;
                        }else{
                            optType = infoModel.getStatus();
                        }
                        res.setOptType(optType);
                        // 用一个对象来封装一个总数
                        TestPaperFormatLstVo testPaperFormatLstVo=new TestPaperFormatLstVo();
                        List<TestPaperQuestionResModel> testPaperQuestionResModels = testResultDetailService.selectTestPaperInfo(userId, testPaperId, optType,testPaperFormatLstVo);
                        res.setHaveAnswerTotal(testPaperFormatLstVo.getTotalNum());
                        Map<String, Object> initMap = MapUtils.initMap("studentId", userId);
                        initMap.put("testPaperId",testPaperId);
                        List<TestResult> list = testResultService.selectList(initMap);
                        if(null!=list && list.size()>0){
                            TestResult testResult = list.get(0);
                            res.setUserScore(testResult.getTotalScore()!=null?testResult.getTotalScore():0);
                            Integer useTime = testResult.getUseTime();
                            if(null!=useTime){
                                int hour=useTime.intValue()/60;
                                String hourStr=null;
                                String minStr=null;
                                if(hour>=0&&hour<10)
                                    hourStr="0"+hour;
                                else
                                    hourStr=""+hour;

                                int mintinue=useTime.intValue()%60;
                                if(mintinue>=0&&mintinue<10)
                                    minStr="0"+mintinue;
                                else
                                    minStr=""+mintinue;
                                res.setUseTimeStr(hourStr+":"+minStr);
                            }
                        }
                        res.setQuestionResModelList(testPaperQuestionResModels);

                        return RetResponse.makeOKRsp(res);
                    }
                }
                return RetResponse.makeErrRsp("未查询到试卷信息！");
            }else{
                return RetResponse.makeErrRsp("未查询到试卷信息！");
            }
        }else{
            return RetResponse.makeErrRsp("未查询到试卷信息！");
        }
    }

    private boolean isBuy(int student,int testPaperId){
        Set<Integer> courseLstByTestPaperId = testPaperService.getCourseLstByTestPaperId(testPaperId);
        if(null!=courseLstByTestPaperId){
            for (Integer integer : courseLstByTestPaperId) {
                boolean flag = mallOrderInfoService.selectMallOrderInfoVoByIdAndCourseId(student, integer + "");
                if (flag)
                    return true;
            }
        }
        return false;
    }
}
