package com.by.blcu.resource.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.filter.SensitiveWordsFilter;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.RedisUtil;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.course.service.ICourseDetailService;
import com.by.blcu.resource.model.ResourcesViewModel;
import com.by.blcu.resource.service.IResourcesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/resources")
@Slf4j
@Api(tags = "目录资源操作API",description = "包含接口：\n"
        +"1、查询目录下资源详情\n"
        +"2、删除目录下资源")
public class ResourcesController {

    @Resource
    private IResourcesService resourcesService;

    @Resource
    SensitiveWordsFilter sensitiveWordsFilter;

    @Resource
    private RedisUtil redisUtil;

    //最大的单条讨论长度
    public static final Integer MAX_DISCUSS_LEN = 1024;

    @PostMapping("/get")
    @RequiresPermissions("teacher")
    @CheckToken
    @ApiOperation(httpMethod = "POST", value = "查询目录下资源详情")
    RetResult queryResourcesDetailByCatalogId(@RequestBody ResourcesViewModel resourcesViewModel, HttpServletRequest request) throws Exception{
        int userId = Integer.valueOf(request.getAttribute("userId").toString());
        if(StringUtils.isBlank(resourcesViewModel.getCatalogId())){
            throw new ServiceException("目录ID不能为空！");
        }else if("0".equals(resourcesViewModel.getCatalogId())){
            throw new ServiceException("当前目录为根目录，不可查看");
        }
        boolean flag = resourcesService.getResourcesByCatalogId(resourcesViewModel, userId);
        return RetResponse.makeOKRsp(resourcesViewModel);
    }

    @PostMapping("/delete")
    @RequiresPermissions("teacher")
    @CheckToken
    @ApiOperation(httpMethod = "POST", value = "删除目录下资源")
    RetResult deleteResourcesFromCatalog(@RequestBody ResourcesViewModel resourcesViewModel, HttpServletRequest request) throws Exception{
        int userId = Integer.valueOf(request.getAttribute("userId").toString());
        if(StringUtils.isBlank(resourcesViewModel.getCatalogId())){
            throw new ServiceException("目录ID不能为空！");
        }else if("0".equals(resourcesViewModel.getCatalogId())){
            throw new ServiceException("当前目录为根目录，不可移除资源");
        }
        boolean flag = resourcesService.delResourcesByCatalogId(resourcesViewModel, userId);
        return RetResponse.makeOKRsp(flag);
    }

    @RequestMapping(value = "/checkSensitive",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询是否敏感词")
    public RetResult checkSensitive(@ApiParam(value = "讨论【discuss】") @RequestBody JSONObject object,
                                    HttpServletRequest request) throws Exception{


        if(object.containsKey("discuss")) {

            Long starttime = System.currentTimeMillis();
            String dis = object.getString("discuss");
            if (dis.isEmpty()) {
                return RetResponse.makeErrRsp("讨论内容空!");
            } else if (MAX_DISCUSS_LEN < dis.length()) {
                return RetResponse.makeErrRsp("讨论内容过长!");
            }

            boolean ret = sensitiveWordsFilter.hasSensitiveWord(dis);
            Long finishtime = System.currentTimeMillis();
            log.info("======== check SensitiveWord use: " + (finishtime - starttime) + "ms ==========");

            return RetResponse.makeOKRsp(ret);
        } else {
            return RetResponse.makeErrRsp("缺少discuss！");
        }

    }

    @RequestMapping(value = "/buildSensitiveSet",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public RetResult buildSensitiveSet(
            @RequestBody JSONObject object,
            HttpServletRequest request) throws Exception{

        String setn = null;
        String pathn = null;
        if(object.containsKey("setName")) {
            setn = object.getString("setName");
        } else {
            return RetResponse.makeErrRsp("缺少参数！");
        }
        if(object.containsKey("path")) {
            pathn = object.getString("path");
        }else{
            return RetResponse.makeErrRsp("缺少参数！");
        }


        File file = new File(pathn);
        List<String> list = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String nString = bufferedReader.readLine();

        while (nString != null) {
            list.add(nString);
            //long ret = redisUtil.sAdd(setn, nString);
            nString = bufferedReader.readLine();
        }
        bufferedReader.close();
        redisUtil.sAdd(setn, (String[]) list.toArray(new String[0]) );

        return RetResponse.makeOKRsp(true);

    }
}
