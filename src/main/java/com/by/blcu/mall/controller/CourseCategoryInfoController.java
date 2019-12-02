package com.by.blcu.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.mall.model.CourseCategoryInfo;
import com.by.blcu.mall.service.CourseCategoryInfoService;
import com.by.blcu.mall.vo.CourseCategoryInfoVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @Description: CourseCategoryInfoController类
* @author 李程
* @date 2019/07/25 15:20
*/
@RestController
@RequestMapping("/courseCategoryInfo")
@Api(tags = "课程分类API",description = "包含接口：\n" +
        "1、添加课程分类接口\n" +
        "2、递归删除接口\n" +
        "3、编辑名字接口\n" +
        "4、课程分类上移接口\n" +
        "5、课程分类下移接口\n" +
        "6、根据父ID查询接口\n" +
        "7、递归查询所有接口")
@CheckToken
public class CourseCategoryInfoController {
    @Resource
    private CourseCategoryInfoService courseCategoryInfoService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(CourseCategoryInfo courseCategoryInfo) throws Exception{
        courseCategoryInfo.setCcId(ApplicationUtils.getUUID());
        Integer state = courseCategoryInfoService.insert(courseCategoryInfo);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/insertByParentId")
    @ApiOperation(value = "添加课程分类接口")
    public RetResult<Integer> insertByParentId(@RequestBody CourseCategoryInfo courseCategoryInfo,HttpServletRequest httpServletRequest) throws Exception{
        courseCategoryInfo.setCcId(ApplicationUtils.getUUID());
        String parentId = courseCategoryInfo.getParentId();
        String ccName = courseCategoryInfo.getCcName();
        List<CourseCategoryInfo> courseCategoryInfos = courseCategoryInfoService.selectByParentIdAndName(parentId,ccName.trim());
        if(null != courseCategoryInfos && courseCategoryInfos.size()>0){
            return RetResponse.makeErrRsp("该分类名已存在！");
        }
        if(parentId != null && !"".equals(parentId) && ccName != null && !"".equals(ccName)){
            Integer sort = courseCategoryInfoService.selectMaxSortByParentId(parentId);
            if(sort == null){
                courseCategoryInfo.setCcSort(0);
            }else{
                courseCategoryInfo.setCcSort(sort+1);
            }
            courseCategoryInfo.setCcCreator((String) httpServletRequest.getAttribute("username"));
            courseCategoryInfo.setCcStatus(1);
            courseCategoryInfo.setCcCreateTime(new Date());
            Integer state = courseCategoryInfoService.insert(courseCategoryInfo);
            return RetResponse.makeOKRsp(state);
        } else {
            return RetResponse.makeErrRsp("parentId和ccName不能为空！");
        }
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = courseCategoryInfoService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteByCcId")
    @ApiOperation(value = "递归删除接口")
    public RetResult<Integer> deleteByCcId(@RequestBody JSONObject obj) throws Exception {
        if(obj.containsKey("ccId") && !StringUtils.isBlank(obj.getString("ccId"))){
            String ccId = obj.getString("ccId");
            Integer state = courseCategoryInfoService.deleteByCcId(ccId);
            return RetResponse.makeOKRsp(state);
        }else{
            return RetResponse.makeErrRsp("需要选择要删除的分类！");
        }
    }

    @PostMapping("/update")
    public RetResult<Integer> update(CourseCategoryInfo courseCategoryInfo) throws Exception {
        Integer state = courseCategoryInfoService.update(courseCategoryInfo);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/updateNameByCcId")
    @ApiOperation(value = "编辑名字接口")
    public RetResult<Integer> updateNameByCcId(@RequestBody JSONObject obj) throws Exception {
        if(obj.containsKey("ccId") && obj.containsKey("ccName") && !StringUtils.isBlank(obj.getString("ccId")) && !StringUtils.isBlank(obj.getString("ccName"))){
            String ccId = obj.getString("ccId");
            String ccName = obj.getString("ccName");
            CourseCategoryInfo courseCategoryInfo = courseCategoryInfoService.selectByCcId(ccId);
            if(null == courseCategoryInfo){
                return RetResponse.makeErrRsp("不存在该分类！");
            }
            List<CourseCategoryInfo> courseCategoryInfos = courseCategoryInfoService.selectByParentIdAndName(courseCategoryInfo.getParentId(),ccName);
            if(null != courseCategoryInfos && courseCategoryInfos.size()>0){
                return RetResponse.makeErrRsp("该分类名已存在！");
            }
            Integer state = courseCategoryInfoService.updateNameByCcId(ccId,ccName);
            return RetResponse.makeOKRsp(state);
        }else{
            return RetResponse.makeErrRsp("需要传入参数ccId和ccName！");
        }
    }

    @PostMapping("/updateUpSort")
    @ApiOperation(value = "课程分类上移接口")
    public RetResult<Integer> updateSort(@RequestBody JSONObject obj) throws Exception {
        if(obj.containsKey("ccId")){
            String ccId = obj.getString("ccId");
            Integer state = courseCategoryInfoService.updateUpSort(ccId);
            if (state == 1){
                return RetResponse.makeOKRsp(state);
            } else{
                return RetResponse.makeErrRsp("已经置顶！");
            }
        }else{
            return RetResponse.makeErrRsp("需要传入参数ccId！");
        }
    }

    @PostMapping("/updateDownSort")
    @ApiOperation(value = "课程分类下移接口")
    public RetResult<Integer> updateDownSort(@RequestBody JSONObject obj) throws Exception {
        if(obj.containsKey("ccId")){
            String ccId = obj.getString("ccId");
            Integer state = courseCategoryInfoService.updateDownSort(ccId);
            if (state == 1){
                return RetResponse.makeOKRsp(state);
            } else{
                return RetResponse.makeErrRsp("已经到底！");
            }
        }else{
            return RetResponse.makeErrRsp("需要传入参数ccId！");
        }
    }

    @PostMapping("/selectById")
    public RetResult<CourseCategoryInfo> selectById(@RequestParam String id) throws Exception {
        CourseCategoryInfo courseCategoryInfo = courseCategoryInfoService.selectById(id);
        return RetResponse.makeOKRsp(courseCategoryInfo);
    }

    @PostMapping("/selectCcNameByCcId")
    public RetResult<String> selectCcNameByCcId(@RequestBody JSONObject obj) throws Exception {
        if(obj.containsKey("ccId")){
            String ccId = obj.getString("ccId");
            String ccName = courseCategoryInfoService.selectCcNameByCcId(ccId);
            return RetResponse.makeOKRsp(ccName);
        }else{
            return RetResponse.makeErrRsp("需要传入参数ccId！");
        }
    }


    @PostMapping("/selectByParentId")
    @ApiOperation(value = "根据父ID查询接口")
    public RetResult<List> selectByParentId(@RequestBody JSONObject obj) throws Exception {
        if(obj.containsKey("parentId")){
            String parentId = obj.getString("parentId");
            List<CourseCategoryInfo> list = courseCategoryInfoService.selectByParentId(parentId);
            return RetResponse.makeOKRsp(list);
        }else{
            return RetResponse.makeErrRsp("需要传入参数parentId！");
        }
    }

    @GetMapping("/getByParentId")
    @ApiOperation(value = "根据父ID查询接口")
    public RetResult<List> getByParentId(@RequestParam(required=true) String parentId) throws Exception {
        if(!parentId.isEmpty()){
            List<CourseCategoryInfo> list = courseCategoryInfoService.selectByParentId(parentId);
            return RetResponse.makeOKRsp(list);
        }else{
            return RetResponse.makeErrRsp("入参parentId不能为空！");
        }
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<CourseCategoryInfo>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<CourseCategoryInfo>> list(@RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<CourseCategoryInfo> list = courseCategoryInfoService.selectAll();
        PageInfo<CourseCategoryInfo> pageInfo = new PageInfo<CourseCategoryInfo>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }

    /**
     * @Description: 递归查询
     * @Reutrn RetResult<List>
     */
    @GetMapping("/selectListRecursion")
    @ApiOperation(value = "递归查询所有接口")
    public RetResult<List> selectListRecursion() throws Exception {
        List<CourseCategoryInfoVo> list = courseCategoryInfoService.selectListRecursion();
        return RetResponse.makeOKRsp(list);
    }

    /**
     * @Description: 查询所有分类
     * @Reutrn RetResult<Map>
     */
    @GetMapping("/selectList")
    @ApiOperation(value = "查询所有接口")
    public RetResult<Map> selectList() throws Exception {
        Map<String,String> map = courseCategoryInfoService.selectList();
        return RetResponse.makeOKRsp(map);
    }
}