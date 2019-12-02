package com.by.blcu.resource.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.CommonUtils;
import com.by.blcu.core.utils.DateUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.resource.dto.TestPaper;
import com.by.blcu.resource.dto.TestPaperFormat;
import com.by.blcu.resource.dto.TestResult;
import com.by.blcu.resource.model.TeacherPaperResultModel;
import com.by.blcu.resource.model.TestPaperFormatLstVo;
import com.by.blcu.resource.model.TestPaperQuestionModel;
import com.by.blcu.resource.model.TestPaperQuestionResModel;
import com.by.blcu.resource.service.ITeacherCorrectService;
import com.by.blcu.resource.service.ITestPaperFormatService;
import com.by.blcu.resource.service.ITestPaperService;
import com.by.blcu.resource.service.ITestResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teacherCorrect")
@Api(tags = "教师批改作业API",description = "包含接口：\n"+
            "1、获取作业/测试的批改列表\n"+
            "2、获取批阅列表\n"+
            "3、教师评阅试卷查询\n"+
            "4、教师评阅试卷保存\n"+
            "5、试卷批改首页查询")
@CheckToken
public class TeacherCorrectController {

    @Autowired
    private ITeacherCorrectService correctService;

    @Resource(name="testPaperService")
    private ITestPaperService testPaperService;

    @Resource(name="testPaperFormatService")
    private ITestPaperFormatService testPaperFormatService;

    @Resource(name = "testResultService")
    private ITestResultService testResultService;

    @RequestMapping(value = "/getPaperListBycourseId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "获取作业/测试的批改列表")
    public RetResult getPaperListBycourseId(@ApiParam(value = "试卷名称【paperName】可以模糊查询；要批改的课程id【courseId】；每页条数【pageSize】；" +
            "当前页码【pageIndex】；查询类型（必传）【type】0:表示查测试 1：表示查作业；",required = true) @RequestBody JSONObject object) throws Exception {

        String paperName = "";
        String type = "";
        Integer pageSize = null;
        Integer pageIndex = null;
        Integer courseId ;
        if(object.containsKey("paperName")){
            paperName = object.getString("paperName");
        }
        if(object.containsKey("pageSize")){
            pageSize = Integer.valueOf(object.getString("pageSize"));
        }
        if(object.containsKey("pageIndex")){
            pageIndex = Integer.valueOf(object.getString("pageIndex"));
        }
        if(object.containsKey("courseId")){
            courseId = Integer.valueOf(object.getString("courseId"));
        }else{
            return RetResponse.makeErrRsp("需要传入参数要批改的课程id【courseId】！");
        }
        if(object.containsKey("type")){
            type = object.getString("type");
        }else{
            return RetResponse.makeErrRsp("需要传入参数查询类型【type】！");
        }
        return correctService.getPaperListByCourseId( courseId, type, paperName, pageIndex, pageSize);
    }

    @RequestMapping(value = "/getCorrectList", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "获取批阅列表")
    public RetResult getCorrectList(@ApiParam(value = "试卷id【paperId】，批阅状态【type】1:已批阅 0:未批阅；每页条数【pageSize】；" +
            "当前页码【pageIndex】；用户名【userName】模糊搜索时使用") @RequestBody JSONObject object) throws Exception {

        String paperId = "";
        String type = "";
        String userName = "";
        Integer pageSize = null;
        Integer pageIndex = null;
        if(object.containsKey("paperId")){
            paperId = object.getString("paperId");
        }else{
            return RetResponse.makeErrRsp("需要传入参数试卷主键【paperId】！");
        }
        if(object.containsKey("type")){
            type = object.getString("type");
        }else{
            return RetResponse.makeErrRsp("需要传入参数批阅状态【type】！");
        }
        if(object.containsKey("pageSize")){
            pageSize = Integer.valueOf(object.getString("pageSize"));
        }
        if(object.containsKey("pageIndex")){
            pageIndex = Integer.valueOf(object.getString("pageIndex"));
        }
        if(object.containsKey("userName")){
            userName = object.getString("userName");
        }
        return correctService.getCorrectList(paperId,type,pageIndex,pageSize,userName);
    }

    @RequestMapping(value = "/getTestPaperInfo", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "教师评阅试卷查询",notes = "教师评阅试卷查询",httpMethod = "POST")
    public RetResult getTestPaperInfo( @ApiParam(value = "试卷id【testPaperId】，学生id【studentId】") @RequestBody JSONObject object)throws Exception{

        Integer testPaperId ;
        Integer studentId ;
        if(object.containsKey("testPaperId")){
            testPaperId = Integer.valueOf(object.getString("testPaperId"));
        }else{
            return RetResponse.makeErrRsp("需要传入参数试卷id【testPaperId】！");
        }
        if(object.containsKey("studentId")){
            studentId = Integer.valueOf(object.getString("studentId"));
        }else{
            return RetResponse.makeErrRsp("需要传入参数学生id【studentId】！");
        }
        TestPaperQuestionModel res=new TestPaperQuestionModel();
        TestPaper testPaper = testPaperService.selectByPrimaryKey(testPaperId);
        res.setTestPaperName(testPaper.getName());
        res.setTotalScore(testPaper.getTotalScore());
        List<TestPaperFormat> testPaperFormatLst = testPaperFormatService.selectList(MapUtils.initMap("testPaperId", testPaperId));
        int totalNum=0;
        for (TestPaperFormat testPaperFormat : testPaperFormatLst) {
            totalNum+=testPaperFormat.getQuestionNum().intValue();
        }
        res.setTotalNum(totalNum);
        String date2String = DateUtils.date2String(testPaper.getStartTime(), DateUtils.DATE_FORMAT);
        String date2String1 = DateUtils.date2String(testPaper.getEndTime(), DateUtils.DATE_FORMAT);
        res.setDateTimeStr(date2String+"~"+date2String1);
        TestPaperFormatLstVo testPaperFormatLstVo = new TestPaperFormatLstVo();
        List<TestPaperQuestionResModel> testPaperQuestionResModels = correctService.getTestPaperInfo(testPaperId,studentId,testPaperFormatLstVo);
        Map<String, Object> initMap = MapUtils.initMap("studentId", studentId);
        initMap.put("testPaperId",testPaperId);
        List<TestResult> list = testResultService.selectList(initMap);
        if(!CommonUtils.listIsEmptyOrNull(list)){
            if(null != list.get(0).getObjectiveScore()){
                res.setObjective_score(""+list.get(0).getObjectiveScore());
            }
            if(null != list.get(0).getSubjectiveScore()){
                res.setSubjective_score(""+list.get(0).getSubjectiveScore());
            }
        }
        res.setHaveAnswerTotal(testPaperFormatLstVo.getTotalNum());
        res.setQuestionResModelList(testPaperQuestionResModels);
        res.setIsScore(testPaper.getIsScore());
        return RetResponse.makeOKRsp(res);
    }

    @RequestMapping(value = "/saveTeacherCorrect", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "教师评阅试卷保存",notes = "教师评阅试卷保存",httpMethod = "POST")
    public RetResult saveTeacherCorrect(@RequestBody TeacherPaperResultModel model)throws Exception{

        correctService.saveTeacherCorrect(model);
        return RetResponse.makeOKRsp();
    }


    @RequestMapping(value = "/getCourseAndTestPaper", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("teacher")
    @ApiOperation(value = "试卷批改首页查询",notes = "试卷批改首页查询",httpMethod = "POST")
    public RetResult getCourseAndTestPaper(@ApiParam(value = "模糊查询课程名称【courseName】非必填；每页条数【pageSize】；" +
            "当前页码【pageIndex】；") @RequestBody JSONObject object, HttpServletRequest request)throws Exception{

        String courseName = "";
        Integer pageSize = null;
        Integer pageIndex = null;
        if(object.containsKey("courseName")){
            courseName = object.getString("courseName");
        }
        if(object.containsKey("pageSize")){
            pageSize = Integer.valueOf(object.getString("pageSize"));
        }
        if(object.containsKey("pageIndex")){
            pageIndex = Integer.valueOf(object.getString("pageIndex"));
        }

        return correctService.getCourseAndTestPaper(courseName,pageSize,pageIndex,request);
    }
}
