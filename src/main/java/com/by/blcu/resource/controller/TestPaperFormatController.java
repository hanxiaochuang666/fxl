package com.by.blcu.resource.controller;

import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.resource.dto.TestPaper;
import com.by.blcu.resource.dto.TestPaperFormat;
import com.by.blcu.resource.model.TestPaperFormatViewModel;
import com.by.blcu.resource.service.ITestPaperFormatService;
import com.by.blcu.resource.service.ITestPaperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/testPaperFormat")
@Api(tags = "试卷组成管理API")
@Slf4j
public class TestPaperFormatController {
    @Resource(name="testPaperFormatService")
    private ITestPaperFormatService testPaperFormatService;

    @Resource(name="testPaperService")
    private ITestPaperService testPaperService;

    @RequestMapping(value = "/queryTestPaperFormat", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询试卷组成",notes = "查询试卷组成",httpMethod = "GET")
    public RetResult<List<TestPaperFormat>> queryTestPaperFormat(@RequestParam Integer testPaperId){
        if(null==testPaperId || testPaperId<=0){
            log.info("根据试卷组成testPaperFormatId参数错误");
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"根据试卷组成testPaperFormatId参数错误");
        }
        Map<String, Object> initMap = MapUtils.initMap("testPaperId", testPaperId);
        List<TestPaperFormat> list = testPaperFormatService.selectList(initMap);
        return RetResponse.makeOKRsp(list);
    }

    @RequestMapping(value = "/saveTestPaperFormat", method = RequestMethod.POST ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "保存试卷组成",notes = "保存试卷组成",httpMethod = "POST")
    public RetResult saveTestPaperFormat(@Valid @RequestBody TestPaperFormatViewModel testPaperFormatViewModel)throws Exception{
        if(null==testPaperFormatViewModel.getTestPaperId()){
            log.info("参数错误："+testPaperFormatViewModel.toString());
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"参数错误："+testPaperFormatViewModel.toString());
        }
        TestPaper testPaper = testPaperService.selectByPrimaryKey(testPaperFormatViewModel.getTestPaperId());
        if(testPaper==null)
            return RetResponse.makeRsp(RetCode.BUSINESS_ERROR.code,"试卷不存在");

        return testPaperFormatService.syncPaperFormatInfo(testPaperFormatViewModel);
    }
}
