package com.by.blcu.mall.controller;

import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.utils.ApplicationUtils;
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
import java.util.Date;
import java.util.List;

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
    public RetResult<Integer> insertByParentId(CourseCategoryInfo courseCategoryInfo) throws Exception{
        courseCategoryInfo.setCcId(ApplicationUtils.getUUID());
        String parentId = courseCategoryInfo.getParentId();
        if(parentId != null && !"".equals(parentId)){
            Integer sort = courseCategoryInfoService.selectMaxSortByParentId(parentId);
            if(sort == null){
                courseCategoryInfo.setCcSort(0);
            }else{
                courseCategoryInfo.setCcSort(sort+1);
            }
            courseCategoryInfo.setCcStatus(1);
            courseCategoryInfo.setCcCreateTime(new Date());
            Integer state = courseCategoryInfoService.insert(courseCategoryInfo);
            return RetResponse.makeOKRsp(state);
        } else {
            return RetResponse.makeErrRsp("parentId不能为空！");
        }
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = courseCategoryInfoService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteByCcId")
    @ApiOperation(value = "递归删除接口")
    public RetResult<Integer> deleteByCcId(@RequestParam String ccId) throws Exception {
        Integer state = courseCategoryInfoService.deleteByCcId(ccId);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(CourseCategoryInfo courseCategoryInfo) throws Exception {
        Integer state = courseCategoryInfoService.update(courseCategoryInfo);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/updateNameByCcId")
    @ApiOperation(value = "编辑名字接口")
    public RetResult<Integer> updateNameByCcId(@RequestParam String ccId,@RequestParam String ccName) throws Exception {
        Integer state = courseCategoryInfoService.updateNameByCcId(ccId,ccName);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/updateUpSort")
    @ApiOperation(value = "课程分类上移接口")
    public RetResult<Integer> updateSort(String ccId) throws Exception {
        Integer state = courseCategoryInfoService.updateUpSort(ccId);
        if (state == 1){
            return RetResponse.makeOKRsp(state);
        } else{
            return RetResponse.makeErrRsp("已经置顶！");
        }
    }

    @PostMapping("/updateDownSort")
    @ApiOperation(value = "课程分类下移接口")
    public RetResult<Integer> updateDownSort(String ccId) throws Exception {
        Integer state = courseCategoryInfoService.updateDownSort(ccId);
        if (state == 1){
            return RetResponse.makeOKRsp(state);
        } else{
            return RetResponse.makeErrRsp("已经到底！");
        }
    }

    @PostMapping("/selectById")
    public RetResult<CourseCategoryInfo> selectById(@RequestParam String id) throws Exception {
        CourseCategoryInfo courseCategoryInfo = courseCategoryInfoService.selectById(id);
        return RetResponse.makeOKRsp(courseCategoryInfo);
    }

    @PostMapping("/selectByParentId")
    @ApiOperation(value = "根据父ID查询接口")
    public RetResult<PageInfo<CourseCategoryInfo>> selectByParentId(@RequestParam String parentId) throws Exception {
        PageHelper.startPage(0, 0);
        List<CourseCategoryInfo> list = courseCategoryInfoService.selectByParentId(parentId);
        PageInfo<CourseCategoryInfo> pageInfo = new PageInfo<CourseCategoryInfo>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }

    /**
     * @Description: 分页查询
     * @param page 页码
     * @param size 每页条数
     * @Reutrn RetResult<PageInfo<CourseCategoryInfo>>
     */
    @GetMapping("/list")
    public RetResult<PageInfo<CourseCategoryInfo>> list(@RequestParam(defaultValue = "0") Integer page,
               @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<CourseCategoryInfo> list = courseCategoryInfoService.selectAll();
        PageInfo<CourseCategoryInfo> pageInfo = new PageInfo<CourseCategoryInfo>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }

    /**
     * @Description: 递归查询
     * @Reutrn RetResult<PageInfo<CourseCategoryInfo>>
     */
    @PostMapping("/selectListRecursion")
    @ApiOperation(value = "递归查询所有接口")
    public RetResult<PageInfo<CourseCategoryInfoVo>> selectListRecursion(@RequestParam(defaultValue = "0") Integer page,
                @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<CourseCategoryInfoVo> list = courseCategoryInfoService.selectListRecursion();
        PageInfo<CourseCategoryInfoVo> pageInfo = new PageInfo<CourseCategoryInfoVo>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }
}