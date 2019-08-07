package com.by.blcu.course.controller;

import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.GetUserInfoUtils;
import com.by.blcu.course.model.KnowledgePointNode;
import com.by.blcu.course.service.ICatalogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
        "6、点下一步，整体保存目录结构")
public class CatalogController {

    @Autowired
    private ICatalogService catalogService;

    @RequestMapping(value = "/importExcel", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "课程目录导入",notes = "课程目录导入",httpMethod = "POST")
    public RetResult<List<KnowledgePointNode>> importExcel(@ApiParam(value = "file文件",required = true) @RequestParam(value = "file") MultipartFile file,
                                 @ApiParam(value = "一级目录",required = true) @RequestParam int categoryOne,
                                 @ApiParam(value = "二级目录",required = true) @RequestParam int categoryTwo,
                                 @ApiParam(value = "课程id",required = true) @RequestParam int courseId,
                                 @ApiParam(value = "机构id",required = true) @RequestParam int orgId,
                                 HttpServletRequest request) throws Exception {

        int userId = GetUserInfoUtils.getUserIdByRequest(request);
        Map<String,Object> paraMap = new HashMap<>();
        paraMap.put("categoryOne", categoryOne);
        paraMap.put("categoryTwo", categoryTwo);
        paraMap.put("courseId", courseId);
        paraMap.put("orgId", orgId);
        paraMap.put("createUserId", userId);
        return RetResponse.makeOKRsp(catalogService.importKnowledgePoints(file,paraMap));
    }

    @RequestMapping(value = "/getKnowledgePoints", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询课程目录结构",notes = "查询课程目录结构")
    public RetResult<List<KnowledgePointNode>> getKnowledgePoints(@ApiParam(value = "课程id") @RequestParam int courseId) throws ServiceException {

        return RetResponse.makeOKRsp(catalogService.getKnowledgePoints(courseId));
    }

    @RequestMapping(value = "/editKnowledgePoints", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "修改课程目录结构",notes = "修改课程目录结构")
    public RetResult editKnowledgePoints(@ApiParam(value = "目录主键id") @RequestParam int pointDetailId,
                                         @ApiParam(value = "修改后名称") @RequestParam String name) throws ServiceException {

        return catalogService.editKnowledgePoints(pointDetailId,name);
    }

    @RequestMapping(value = "/addKnowledgePoints", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "添加课程目录结构",notes = "添加课程目录结构")
    public RetResult addKnowledgePoints(@ApiParam(value = "课程id") @RequestParam int courseId,
                                        @ApiParam(value = "父节点id") @RequestParam int parentId,
                                        @ApiParam(value = "章节名称") @RequestParam String name) throws ServiceException {

        return catalogService.addKnowledgePoints(courseId, parentId,name);
    }

    @RequestMapping(value = "/deleteKnowledgePoints", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "删除课程目录结构",notes = "删除课程目录结构")
    public RetResult deleteKnowledgePoints(@ApiParam(value = "目录主键id") @RequestParam int pointDetailId) throws ServiceException {

        return catalogService.deleteKnowledgePoints(pointDetailId);
    }

    @RequestMapping(value = "/saveKnowledgePoints", method = RequestMethod.GET ,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "保存整体课程目录结构",notes = "点下一步，整体保存课程目录结构")
    public RetResult saveKnowledgePoints() throws ServiceException {

        return RetResponse.makeOKRsp();
    }
}
