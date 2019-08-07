package com.by.blcu.resource.controller;

import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.*;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.resource.dto.Resources;
import com.by.blcu.resource.dto.TestPaper;
import com.by.blcu.resource.model.TestPaperCreateModel;
import com.by.blcu.resource.model.TestPaperViewModel;
import com.by.blcu.resource.model.TestPaperVo;
import com.by.blcu.resource.service.IResourcesService;
import com.by.blcu.resource.service.ITestPaperService;
import com.by.blcu.resource.service.ITestResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/testPaper")
@Api(tags = "试卷管理API")
@Slf4j
@CheckToken
public class TestPaperController {
    @Resource(name="testPaperService")
    private ITestPaperService testPaperService;

    @Resource(name="resourcesService")
    private IResourcesService resourcesService;

    @Resource(name="courseDetailService")
    private ICourseDetailService courseDetailService;

    @Autowired
    private FastDFSClientWrapper dfsClient;

    @RequestMapping(value = "/list", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询试卷列表",notes = "查询试卷L列表",httpMethod = "POST")
    public RetResult<List<TestPaperVo>> list(HttpServletRequest httpServletRequest,@RequestBody TestPaperViewModel testPaperViewModel)throws Exception{
        int userId = GetUserInfoUtils.getUserIdByRequest(httpServletRequest);
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        Map<String, Object> param = MapAndObjectUtils.ObjectToMap2(testPaperViewModel);
        if(!StringUtils.isEmpty(param.get("page")) && !StringUtils.isEmpty(param.get("limit"))){
            int page = Integer.valueOf(param.get("page").toString()).intValue();
            int pagesize=Integer.valueOf(param.get("limit").toString()).intValue();
            int __currentIndex__=(page-1)*pagesize;
            param.put("__currentIndex__",__currentIndex__);
            param.put("__pageSize__",pagesize);
        }
        param.put("createUser",userId);
        log.info("list请求数据:"+param.toString());
        long count = testPaperService.selectCount(param);
        if (count<=0)
            return RetResponse.makeRsp(null,count);
        List<TestPaperVo> testPaperList = testPaperService.selectList(param);

        return RetResponse.makeRsp(testPaperList,count);
    }

    @RequestMapping(value = "/saveTestPaper", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "保存试卷",notes = "创建试卷",httpMethod = "POST")
    public RetResult<Integer> saveTestPaper(HttpServletRequest httpServletRequest,@Valid @RequestBody TestPaperCreateModel testPaperCreateModel)throws Exception{
        //1.检验参数
        int userId = GetUserInfoUtils.getUserIdByRequest(httpServletRequest);
        if(userId==-1){
            log.info("无效的用户，请注册或登录");
            return RetResponse.makeErrRsp("无效的用户，请注册或登录");
        }
        //2.VO  BO转换
        TestPaper testPaper = MapAndObjectUtils.ObjectClone(testPaperCreateModel, TestPaper.class);
        String startTime = testPaperCreateModel.getStartTimeStr();
        Date startDate = DateUtils.string2Date(startTime, "yyyy-MM-dd");
        String endTime = testPaperCreateModel.getEndTimeStr();
        Date endDate = DateUtils.string2Date(endTime, "yyyy-MM-dd");
        testPaper.setStartTime(startDate);
        testPaper.setEndTime(endDate);
        testPaper.setCreateTime(new Date());
        testPaper.setCreateUser(userId);
        int count=0;
        //3.保存
        if(testPaper.getTestPaperId()==null) {
            //判断重名试卷
            String name = testPaper.getName();
            if(!StringUtils.isEmpty(name)){
                Map<String, Object> initMap = MapUtils.initMap("name", name);
                initMap.put("createUser",userId);
                long selectCount = testPaperService.selectCount(initMap);
                if(selectCount>0){
                    log.info("试卷名称为:"+name+"的试卷已经存在");
                    return RetResponse.makeRsp(RetCode.SUCCESS.code,"试卷名称为:"+name+"的试卷已经存在");
                }
            }
            count= testPaperService.createTestPaper(testPaper);
        }else {
            count=testPaperService.updateByPrimaryKeySelective(testPaper);
        }
        return count>0?RetResponse.makeOKRsp(testPaper.getTestPaperId()):RetResponse.makeErrRsp("创建失败");
    }

    @RequestMapping(value = "/selectById", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "根据主键查询试卷",notes = "根据主键查询试卷",httpMethod = "GET")
    public RetResult<TestPaperCreateModel> selectById(@RequestParam int testPagerId)throws Exception{
        TestPaper testPaper = testPaperService.selectByPrimaryKey(testPagerId);
        if(null==testPaper){
            log.info("试卷id为:"+testPagerId+"的试卷不存在");
            return RetResponse.makeRsp(RetCode.NOT_FOUND.code,"试卷id为:"+testPagerId+"的试卷不存在");
        }
        TestPaperCreateModel testPaperCreateModel = MapAndObjectUtils.ObjectClone2(testPaper, TestPaperCreateModel.class);
        testPaperCreateModel.setStartTimeStr(DateUtils.date2String(testPaper.getStartTime(),"yyyy-MM-dd"));
        testPaperCreateModel.setEndTimeStr(DateUtils.date2String(testPaper.getEndTime(),"yyyy-MM-dd"));
        return RetResponse.makeOKRsp(testPaperCreateModel);
    }

    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "根据主键删除试卷",notes = "根据主键删除试卷",httpMethod = "DELETE")
    public RetResult deleteById(@RequestParam int testPagerId){
        //添加是否绑定课程判定
        Map<String, Object> objectMap = MapUtils.initMap("content", testPagerId + "");
        objectMap.put("maxType",1);
        List<Resources> objects = resourcesService.selectList(objectMap);
        if(objects.size()>0){
            Integer resourcesId = objects.get(0).getResourcesId();
            objectMap.clear();
            objectMap.put("resourcesId",resourcesId.intValue());
            long count = courseDetailService.selectCount(objectMap);
            if(count>0){
                log.info("试卷id为"+testPagerId+"的试卷正在被课程使用，不能删除");
                return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"试卷id为"+testPagerId+"的试卷正在被课程使用，不能删除");
            }
        }

        testPaperService.deleteTestPaperById(testPagerId);
        return RetResponse.makeOKRsp();
    }

    @RequestMapping(value = "/deleteBatch", method = RequestMethod.DELETE ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "批量删除试卷",notes = "批量删除试卷",httpMethod = "DELETE")
    public RetResult deleteBatch(@RequestParam String testPagerIds){
        if(StringUtils.isEmpty(testPagerIds)){
            log.info("删除请求参数不能为空");
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"删除请求参数不能为空");
        }
        return RetResponse.makeOKRsp(testPaperService.deleteTestPaperBatch(testPagerIds));
    }

    @RequestMapping(value = "/exporTestPaperById", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "导出试卷",notes = "导出试卷",httpMethod = "GET")
    public RetResult exporTestPaperById(@RequestParam int testPagerId, HttpServletResponse response){
        if(testPagerId>0){
            TestPaper testPaper= testPaperService.selectByPrimaryKey(testPagerId);
            String exportPath = testPaper.getExportPath();
            if(!StringUtils.isEmpty(exportPath)){
                dfsClient.webDownFile(response,exportPath,testPaper.getName());
            }
        }
        return RetResponse.makeOKRsp();
    }
}
