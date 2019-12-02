package com.by.blcu.mall.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.utils.ApplicationUtils;
import com.by.blcu.core.utils.MapAndObjectUtils;
import com.by.blcu.core.utils.MapUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.mall.model.CommodityInfo;
import com.by.blcu.mall.model.CommodityLecturer;
import com.by.blcu.mall.model.CommodityPackage;
import com.by.blcu.mall.model.MallMealInfo;
import com.by.blcu.mall.service.CommodityInfoService;
import com.by.blcu.mall.service.MallMealInfoService;
import com.by.blcu.mall.vo.*;
import com.by.blcu.mall.service.IMyFavoriteService;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.star.uno.Exception;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author 李程
 * @Description: CommodityInfoController类
 * @date 2019/07/29 11:03
 */
@Slf4j
@RestController
@CheckToken
@RequestMapping("/commodityInfo")
@Api(tags = "产品信息API", description = "包含接口：\n" +
        "1、首页直播排序查询接口\n" +
        "2、首页试听排序查询接口\n" +
        "3、首页递归查询接口\n" +
        "4、商品信息列表查询接口\n" +
        "5、根据分类查询接口\n" +
        "6、根据父ID查询接口\n" +
        "7、递归查询所有接口")
public class CommodityInfoController {

    @Resource
    private CommodityInfoService commodityInfoService;

    @Resource
    private IMyFavoriteService myFavoriteService;

    @Resource
    private MallMealInfoService mallMealInfoService;

    @PostMapping("/saveCommodity")
    @ApiOperation(value = "创建商品接口")
    public RetResult<String> saveCommodity(@RequestBody CommodityInfoVo commodityInfoVo, HttpServletRequest httpServletRequest) throws Exception {
        Integer state = 0;
        List<CommodityLecturer> commodityLecturerList = commodityInfoVo.getCommodityLecturerList();
        CommodityInfo commodityInfo = MapAndObjectUtils.ObjectClone(commodityInfoVo, CommodityInfo.class);
        if(StringUtils.isBlank(commodityInfoVo.getCommodityId())){
            commodityInfo.setCommodityId(ApplicationUtils.getUUID());
            commodityInfo.setCommodityStatus(2);
            commodityInfo.setStatus(1);
            commodityInfo.setCreateTime(new Date());
            commodityInfo.setCreator((String) httpServletRequest.getAttribute("username"));
            commodityInfo.setIsRecommend(0);
            if(commodityInfo.getSort() == null){
                commodityInfo.setSort(9999);
            }
            commodityInfo.setRecommendSort(999);
            state = commodityInfoService.saveCommodity(commodityInfo,commodityLecturerList);
            if(state == 1){
                return RetResponse.makeOKRsp(commodityInfo.getCommodityId());
            }else{
                return RetResponse.makeErrRsp("保存失败，重新保存！");
            }
        }else{
            CommodityInfoVo commodityInfoVo1 = commodityInfoService.selectEditByCommodityId(commodityInfoVo.getCommodityId());
            if(commodityInfoVo1 != null){
                state = commodityInfoService.updateCommodity(commodityInfo,commodityLecturerList,commodityInfoVo.getCommodityId());
                if(state == 1){
                    return RetResponse.makeOKRsp(state.toString());
                }else{
                    return RetResponse.makeErrRsp("更新失败，重新保存！");
                }
            }else{
                return RetResponse.makeErrRsp("未找到对应商品！");
            }
        }
    }

    @PostMapping("/updateCourseIntroduceByCommodityId")
    @ApiOperation(value = "商城管理更新保存课程简介接口")
    public RetResult<Integer> updateCourseIntroduceByCommodityId(@RequestBody JSONObject obj) throws Exception {
        if (obj.containsKey("commodityId") && !StringUtils.isBlank(obj.getString("commodityId"))) {
            String courseIntroduce = "";
            String commodityId = obj.getString("commodityId");
            if(obj.containsKey("courseIntroduce") && !StringUtils.isBlank(obj.getString("courseIntroduce"))){
                courseIntroduce = obj.getString("courseIntroduce");
            }
            Integer state = commodityInfoService.updateCourseIntroduceByCommodityId(commodityId, courseIntroduce);
            if (state == 0) {
                return RetResponse.makeErrRsp("更新失败！");
            }
            return RetResponse.makeOKRsp(state);
        } else {
            return RetResponse.makeErrRsp("未找到对应商品");
        }
    }

    @PostMapping("/updateCourseIdByCommodityId")
    @ApiOperation(value = "商城管理更新保存关联课程接口")
    public RetResult<Integer> updateCourseIdByCommodityId(@RequestBody JSONObject obj) throws Exception {
        if (obj.containsKey("commodityId") && !StringUtils.isBlank(obj.getString("commodityId"))) {
            String courseId = "";
            String commodityId = obj.getString("commodityId");
            if(obj.containsKey("courseId") && !StringUtils.isBlank(obj.getString("courseId"))){
                courseId = obj.getString("courseId");
            }
            CommodityInfo commodityInfo = new CommodityInfo();
            commodityInfo.setCommodityId(commodityId);
            List<CommodityInfo> select = commodityInfoService.select(commodityInfo);
            if(null == select || select.size() <= 0){
                return RetResponse.makeErrRsp("未找到该商品！");
            }
//            if(select.get(0).getCommodityStatus() != 2){
//                return RetResponse.makeErrRsp("此商品不能更换课程！");
//            }
            Integer state = commodityInfoService.updateCourseIdByCommodityId(commodityId, courseId);
            if (state < 0) {
                return RetResponse.makeErrRsp("更新失败！");
            }
            return RetResponse.makeOKRsp(state);
        } else {
            return RetResponse.makeErrRsp("未找到对应商品");
        }
    }

    @PostMapping("/updateCommodityByCommodityId")
    @ApiOperation(value = "商城管理套餐更新保存关联商品")
    public RetResult<List<MallMealInfo>> updateCommodityByCommodityId(@RequestBody CommodityPackage commodityPackage) throws Exception {
        String comCommodityId = commodityPackage.getComCommodityId();
        List<ChildCommoditySort> list = commodityPackage.getCommodityList();
        String mealType = commodityPackage.getMealType();
        CommodityInfo commodityInfo = new CommodityInfo();
        commodityInfo.setCommodityId(comCommodityId);
        List<CommodityInfo> select = commodityInfoService.select(commodityInfo);
        if(null == select || select.size() <= 0){
            return RetResponse.makeErrRsp("未找到该商品！");
        }
        //套餐下架时才能更新商品
//        if(select.get(0).getCommodityStatus() != 2){
//            return RetResponse.makeErrRsp("此套餐不能更换商品！");
//        }
        if(null == list || list.size() <= 0){
            return RetResponse.makeErrRsp("此套餐未选择商品！");
        }
        MallMealInfo mallMealInfo = new MallMealInfo();
        mallMealInfo.setComCommodityId(comCommodityId);
        List<MallMealInfo> select1 = mallMealInfoService.select(mallMealInfo);
        //更新商品
        if(null != select1 && select1.size() > 0){
            List<MallMealInfo> mallMealInfos = mallMealInfoService.updateMallMealInfo(comCommodityId, list, mealType);
            return RetResponse.makeOKRsp(mallMealInfos);
        }
        //添加商品
        List<MallMealInfo> mallMealInfos = mallMealInfoService.insertMallMealInfo(comCommodityId, list, mealType);
        return RetResponse.makeOKRsp(mallMealInfos);
    }

    @PostMapping("/childCommodityUpdateUpSort")
    @ApiOperation(value = "商品上移接口")
    public RetResult<Integer> childCommodityUpdateUpSort(@RequestBody JSONObject obj) throws java.lang.Exception {
        if(obj.containsKey("comCommodityId") && obj.containsKey("commodityId") && !StringUtils.isBlank("comCommodityId") && !StringUtils.isBlank("commodityId")){
            String comCommodityId = obj.getString("comCommodityId");
            String commodityId = obj.getString("commodityId");
            Integer state = commodityInfoService.childCommodityUpdateUpSort(comCommodityId,commodityId);
            if (state == 1){
                return RetResponse.makeOKRsp(state);
            } else{
                return RetResponse.makeErrRsp("已经置顶！");
            }
        }else{
            return RetResponse.makeErrRsp("未找到商品！");
        }
    }

    @PostMapping("/deleteChildCommodity")
    @ApiOperation(value = "子商品删除接口")
    public RetResult<Integer> deleteChildCommodity(@RequestBody JSONObject obj) throws java.lang.Exception {
        if(obj.containsKey("comCommodityId") && obj.containsKey("commodityId") && !StringUtils.isBlank("comCommodityId") && !StringUtils.isBlank("commodityId")){
            String comCommodityId = obj.getString("comCommodityId");
            String commodityId = obj.getString("commodityId");
            CommodityInfoVo commodityInfoVo = commodityInfoService.selectByCommodityIdNoCommodityStatus(comCommodityId);
            if(commodityInfoVo != null && commodityInfoVo.getCommodityStatus() != null && commodityInfoVo.getCommodityStatus() == 2){
                Integer state = commodityInfoService.deleteChildCommodity(comCommodityId,commodityId);
                if (state == 1){
                    return RetResponse.makeOKRsp(state);
                } else{
                    return RetResponse.makeErrRsp("删除失败！");
                }
            }else {
                return RetResponse.makeErrRsp("非待上架状态商品不能删除！");
            }
        }else{
            return RetResponse.makeErrRsp("未找到商品！");
        }
    }

    @PostMapping("/childCommodityUpdateDownSort")
    @ApiOperation(value = "商品下移接口")
    public RetResult<Integer> childCommodityUpdateDownSort(@RequestBody JSONObject obj) throws java.lang.Exception {
        if(obj.containsKey("comCommodityId") && obj.containsKey("commodityId") && !StringUtils.isBlank("comCommodityId") && !StringUtils.isBlank("commodityId")){
            String comCommodityId = obj.getString("comCommodityId");
            String commodityId = obj.getString("commodityId");
            Integer state = commodityInfoService.childCommodityUpdateDownSort(comCommodityId,commodityId);
            if (state == 1){
                return RetResponse.makeOKRsp(state);
            } else{
                return RetResponse.makeErrRsp("已经到底！");
            }
        }else{
            return RetResponse.makeErrRsp("未找到商品！");
        }
    }

    @PostMapping("/selectEditByCommodityId")
    @ApiOperation(value = "商品编辑页查询接口")
    public RetResult<CommodityInfoVo> selectEditByCommodityId(@RequestBody JSONObject obj) throws Exception {
        if (obj.containsKey("commodityId") && !StringUtils.isBlank(obj.getString("commodityId"))) {
            String commodityId = obj.getString("commodityId");
            CommodityInfoVo commodityInfo = commodityInfoService.selectEditByCommodityId(commodityId);
            return RetResponse.makeOKRsp(commodityInfo);
        } else {
            return RetResponse.makeErrRsp("未选择商品！");
        }
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = commodityInfoService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping(value = "/deleteListByCommodityId")
    @ApiOperation(value = "商城管理页批量删除接口")
    public RetResult<Integer> deleteListByCommodityIdList(@RequestBody JSONObject obj) throws Exception {
        if (obj.containsKey("commodityIdString") && !StringUtils.isBlank(obj.getString("commodityIdString"))) {
            String ids = obj.getString("commodityIdString");
            List<String> commodityIdList = Arrays.asList(ids.split(","));//根据逗号分隔转化为list
            String state = commodityInfoService.deleteListByCommodityId(commodityIdList);
            if("2".equals(state) || "0".equals(state)){
                return RetResponse.makeErrRsp("选择商品中包含推荐商品或上架状态商品，不能删除！");
            }
            return RetResponse.makeOKRsp();
        } else {
            return RetResponse.makeErrRsp("未选择商品！");
        }
    }

    @PostMapping("/update")
    public RetResult<Integer> update(CommodityInfo commodityInfo) throws Exception {
        Integer state = commodityInfoService.update(commodityInfo);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/updateSortByCommodityId")
    @ApiOperation(value = "商城管理更改排序接口")
    public RetResult<Integer> updateSortByCommodityId(@RequestBody JSONObject obj) throws Exception {
        if (obj.containsKey("commodityId")) {
            Integer sort = 9999;
            String commodityId = obj.getString("commodityId");
            if (obj.containsKey("sort") && obj.getInteger("sort") != null) {
                sort = obj.getInteger("sort");
            }
            Integer state = commodityInfoService.updateSortByCommodityId(commodityId, sort);
            if (state == 0) {
                return RetResponse.makeErrRsp("该商品不存在或已删除！");
            }
            return RetResponse.makeOKRsp(state);
        } else {
            return RetResponse.makeErrRsp("未选择商品");
        }
    }

    @PostMapping("/updateRecommendSortByCommodityId")
    @ApiOperation(value = "商城管理推荐页更改排序接口")
    public RetResult<Integer> updateRecommendSortByCommodityId(@RequestBody JSONObject obj) throws Exception {
        if (obj.containsKey("commodityId")) {
            Integer recommendSort = 999;
            String commodityId = obj.getString("commodityId");
            if (obj.containsKey("recommendSort") && obj.getInteger("recommendSort") != null) {
                recommendSort = obj.getInteger("recommendSort");
            }
            Integer state = commodityInfoService.updateRecommendSortByCommodityId(commodityId, recommendSort);
            if (state == 0) {
                return RetResponse.makeErrRsp("该商品不存在或未被推荐！");
            }
            return RetResponse.makeOKRsp(state);
        } else {
            return RetResponse.makeErrRsp("未选择商品！");
        }
    }

    @PostMapping("/updateComStatusByCommodityId")
    @ApiOperation(value = "商城管理批量上下架接口")
    public RetResult<Integer> updateComStatusByCommodityId(@RequestBody JSONObject obj) throws Exception {
        if (obj.containsKey("commodityIdString") && obj.containsKey("commodityStatus") && !StringUtils.isBlank(obj.getString("commodityIdString"))) {
            String ids = obj.getString("commodityIdString");
            Integer commodityStatus = obj.getInteger("commodityStatus");
            List<String> commodityIdList = Arrays.asList(ids.split(","));//根据逗号分隔转化为list
            String state = commodityInfoService.updateComStatusByCommodityId(commodityIdList, commodityStatus);
            if (state == "0") {
                return RetResponse.makeErrRsp("未找到更新商品！");
            }
//            if (state == "1") {
//                return RetResponse.makeErrRsp("同步课程失败！");
//            }
            if (state == "2") {
                return RetResponse.makeErrRsp("商品被推荐，无法操作！");
            }
            return RetResponse.makeOKRsp();
        } else {
            return RetResponse.makeErrRsp("未选择商品！");
        }
    }

    @PostMapping("/updateIsRecommendByCommodityId")
    @ApiOperation(value = "商城管理批量推荐接口")
    public RetResult<Integer> updateIsRecommendByCommodityId(@RequestBody JSONObject obj) throws Exception {
        //Integer count = commodityInfoService.selectCountByCommodityId();
        if (obj.containsKey("commodityIdString") && !StringUtils.isBlank(obj.getString("commodityIdString"))) {
            String ids = obj.getString("commodityIdString");
            List<String> commodityIdList = Arrays.asList(ids.split(","));//根据逗号分隔转化为list
            //Integer countType = count + commodityIdList.size();
            Integer state = commodityInfoService.updateIsRecommendByCommodityId(commodityIdList);
            if (state == 0) {
                return RetResponse.makeErrRsp("未找到可以推荐的商品！");
            }
            return RetResponse.makeOKRsp(state);
        } else {
            return RetResponse.makeErrRsp("未选择商品！");
        }
    }

//    @PostMapping("/updateIsRecommendByCommodityIdType")
//    @ApiOperation(value = "商城管理试听批量推荐接口")
//    public RetResult<Integer> updateIsRecommendByCommodityIdType(@RequestBody JSONObject obj) throws Exception {
//        //Integer count = commodityInfoService.selectCountByCommodityIdType();
//        if (obj.containsKey("commodityIdString") && !StringUtils.isBlank(obj.getString("commodityIdString"))) {
//            String ids = obj.getString("commodityIdString");
//            Integer lessonType = obj.getInteger("lessonType");
//            List<String> commodityIdList = Arrays.asList(ids.split(","));//根据逗号分隔转化为list
//            //Integer countType = count + commodityIdList.size();
//            if (commodityIdList.size() <= 4) {
//                Integer state = commodityInfoService.updateIsRecommendByCommodityId(commodityIdList,lessonType);
//                if (state == 0) {
//                    return RetResponse.makeErrRsp("未找到可以推荐的商品！");
//                }
//                return RetResponse.makeOKRsp(state);
//            } else {
//                return RetResponse.makeErrRsp("试听推荐不能超过4个！");
//            }
//        } else {
//            return RetResponse.makeErrRsp("未选择商品！");
//        }
//    }

    @PostMapping("/selectById")
    public RetResult<CommodityInfo> selectById(@RequestParam String id) throws Exception {
        CommodityInfo commodityInfo = commodityInfoService.selectById(id);
        return RetResponse.makeOKRsp(commodityInfo);
    }

    @PostMapping("/selectByCourseId")
    @ApiOperation(value = "根据课程查商品接口")
    public RetResult<List> selectByCourseId(@RequestBody JSONObject obj) throws Exception {
        if (obj.containsKey("courseId") && !StringUtils.isBlank(obj.getString("courseId"))) {
            String courseId = obj.getString("courseId");
            List<CommodityInfo> commodityInfo = commodityInfoService.selectByCourseId(courseId);
            return RetResponse.makeOKRsp(commodityInfo);
        } else {
            return RetResponse.makeErrRsp("需要传入参数courseId！");
        }
    }

    @PostMapping("/selectcommoditydIListByIsRecommend")
    @ApiOperation(value = "根据推荐查商品Id集合接口")
    public RetResult<List> selectcommoditydIListByIsRecommend(@RequestBody JSONObject obj) throws Exception {
        if (obj.containsKey("lessonType") && obj.getInteger("lessonType") != null) {
            Integer lessonType = obj.getInteger("lessonType");
            List<String> list = commodityInfoService.selectcommoditydIListByIsRecommend(lessonType);
            return RetResponse.makeOKRsp(list);
        } else {
            return RetResponse.makeErrRsp("未输入lessonType！");
        }
    }

    @PostMapping("/selectByCommodityId")
    @ApiOperation(value = "商品详情页接口")
    public RetResult<CommodityInfoFileVo> selectByCommodityId(@RequestBody JSONObject obj, HttpServletRequest req) throws Exception {
        if (obj.containsKey("commodityId") && !StringUtils.isBlank(obj.getString("commodityId"))) {
            String commodityId = obj.getString("commodityId");
            CommodityInfoFileVo commodityInfoFileVo = commodityInfoService.selectByCommodityId(commodityId);
            Integer userId =Integer.valueOf(req.getAttribute("userId").toString());
            Map<String, Object> param = MapUtils.initMap("userId", String.valueOf(userId));
            param.put("commodityId", commodityId);
            commodityInfoFileVo.setIsFavorite(Integer.valueOf((int) myFavoriteService.selectCount(param)));
            return RetResponse.makeOKRsp(commodityInfoFileVo);
        } else {
            return RetResponse.makeOKRsp();
        }
    }

    @GetMapping("/selectByLessonType")
    @ApiOperation(value = "首页直播排序查询接口")
    public RetResult<List> selectByLessonType() throws Exception {
        List<CommodityInfoFileVo> list = commodityInfoService.selectByLessonType();
        if(null != list && list.size() > 0){
            return RetResponse.makeOKRsp(list);
        }
        return RetResponse.makeOKRsp();
    }

//    @GetMapping("/selectByAudition")
//    @ApiOperation(value = "首页试听排序查询接口")
//    public RetResult<List> selectByAudition() throws Exception {
//        List<CommodityInfoFileVo> list = commodityInfoService.selectByAudition();
//        return RetResponse.makeOKRsp(list);
//    }

    @GetMapping("/selectLecturerList")
    public RetResult<List> selectLecturerList() throws Exception {
        List list = commodityInfoService.selectLecturerList();
        return RetResponse.makeOKRsp(list);
    }

    /**
     * @param page 页码
     * @param size 每页条数
     * @Description: 分页查询
     * @Reutrn RetResult<PageInfo   <   CommodityInfo>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<CommodityInfo>> list(@RequestParam(defaultValue = "0") Integer page,
                                                   @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<CommodityInfo> list = commodityInfoService.selectAll();
        PageInfo<CommodityInfo> pageInfo = new PageInfo<CommodityInfo>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }

    /**
     * @Description: 选课中心条件查询
     * @Reutrn RetResult<PageInfo < CommodityInfo>>
     */
    @PostMapping("/selectListByCommodityInfoCenterVo")
    @ApiOperation(value = "选课中心查询接口")
    public RetResult<PageInfo<CommodityInfoFileVo>> selectListByCommodityInfoCenterVo(@RequestBody JSONObject obj, HttpServletRequest httpServletRequest) throws Exception {
        Integer page = 0;
        Integer size = 0;
        if (obj.containsKey("page") && obj.getInteger("page") != null) {
            page = obj.getInteger("page");
        }
        if (obj.containsKey("size") && obj.getInteger("size") != null) {
            size = obj.getInteger("size");
        }
        CommodityInfoCenterVo commodityInfoCenterVo = obj.toJavaObject(CommodityInfoCenterVo.class);
        String orgCode = (String) httpServletRequest.getAttribute("orgCode");
        String orgType = (String) httpServletRequest.getAttribute("orgType");
        if(!StringUtils.isBlank(orgType) && !"1".equals(orgType)){
            commodityInfoCenterVo.setOrgCode(orgCode);
        }
        PageInfo<CommodityInfoFileVo> pageInfo = commodityInfoService.selectListByCommodityInfoCenterVo(page, size, commodityInfoCenterVo);
        return RetResponse.makeOKRsp(pageInfo);
    }

    /**
     * @Description: 递归查询
     * @Reutrn RetResult<List>
     */
    @PostMapping("/selectListRecursion")
    @ApiOperation(value = "首页递归查询接口")
    public RetResult<PageInfo<CourseCategoryCommodityVo>> selectListRecursion(HttpServletRequest httpServletRequest) throws Exception {
        PageHelper.startPage(0, 0);
        String orgCode = (String) httpServletRequest.getAttribute("orgCode");
        String orgType = (String) httpServletRequest.getAttribute("orgType");
        if(StringUtils.isBlank(orgType) || "1".equals(orgType)){
            List<CourseCategoryCommodityVo> list = commodityInfoService.selectListRecursion();
            PageInfo<CourseCategoryCommodityVo> pageInfo = new PageInfo<CourseCategoryCommodityVo>(list);
            return RetResponse.makeOKRsp(pageInfo);
        }
        List<CourseCategoryCommodityVo> list = commodityInfoService.selectListRecursionByOrgCode(orgCode);
        PageInfo<CourseCategoryCommodityVo> pageInfo = new PageInfo<CourseCategoryCommodityVo>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }

    /**
     * @Description: 分页查询
     * @Reutrn RetResult<PageInfo < CommodityInfo>>
     */
    @PostMapping("/selectListAll")
    @ApiOperation(value = "商品信息列表查询接口")
    public RetResult<PageInfo<CommodityInfoFileVo>> selectListAll(@RequestBody JSONObject obj) throws Exception {
        Integer page = 1;
        Integer size = 10;
        if (obj.containsKey("page") && obj.getInteger("page") != null) {
            page = obj.getInteger("page");
        }
        if (obj.containsKey("size") && obj.getInteger("size") != null) {
            size = obj.getInteger("size");
        }
        CommodityInfo commodityInfo = obj.toJavaObject(CommodityInfo.class);
        PageInfo<CommodityInfoFileVo> pageInfo = commodityInfoService.selectListAll(page, size, commodityInfo);
        return RetResponse.makeOKRsp(pageInfo);
    }

    /**
     * @Description: 根据商品分类查询
     * @Reutrn RetResult<PageInfo < CommodityInfo>>
     */
    @PostMapping("/listByCcId")
    @ApiOperation(value = "根据分类查询接口")
    public RetResult<PageInfo<CommodityInfoFileVo>> listByCcId(@RequestBody JSONObject obj,HttpServletRequest httpServletRequest) throws Exception {
        Integer page = 0;
        Integer size = 0;
        if (obj.containsKey("page") && obj.getInteger("page") != null) {
            page = obj.getInteger("page");
        }
        if (obj.containsKey("size") && obj.getInteger("size") != null) {
            size = obj.getInteger("size");
        }
        CommodityInfo commodityInfo = obj.toJavaObject(CommodityInfo.class);
        String orgCode = (String) httpServletRequest.getAttribute("orgCode");
        String orgType = (String) httpServletRequest.getAttribute("orgType");
        if(!StringUtils.isBlank(orgType) && !"1".equals(orgType)){
            commodityInfo.setOrgCode(orgCode);
        }
        PageInfo<CommodityInfoFileVo> pageInfo = commodityInfoService.listByCcId(page, size, commodityInfo);
        return RetResponse.makeOKRsp(pageInfo);
    }

    @PostMapping("/listByCourseName")
    @ApiOperation(value = "根据分类查询接口")
    public RetResult<PageInfo<CommodityInfoFileVo>> listByCourseName(@RequestBody JSONObject obj,HttpServletRequest httpServletRequest) throws Exception {
        Integer page = 0;
        Integer size = 0;
        if (obj.containsKey("page") && obj.getInteger("page") != null) {
            page = obj.getInteger("page");
        }
        if (obj.containsKey("size") && obj.getInteger("size") != null) {
            size = obj.getInteger("size");
        }
        CommodityInfo commodityInfo = obj.toJavaObject(CommodityInfo.class);
        String orgCode = (String) httpServletRequest.getAttribute("orgCode");
        String orgType = (String) httpServletRequest.getAttribute("orgType");
        if(!StringUtils.isBlank(orgType) && !"1".equals(orgType)){
            commodityInfo.setOrgCode(orgCode);
        }
        PageInfo<CommodityInfoFileVo> pageInfo = commodityInfoService.listByCourseName(page, size, commodityInfo);
        return RetResponse.makeOKRsp(pageInfo);
    }

    /**
     * @Description: 推荐商品查询
     * @Reutrn RetResult<PageInfo < CommodityInfo>>
     */
    @PostMapping("/listByRecommend")
    @ApiOperation(value = "商城管理推荐商品查询接口")
    public RetResult<PageInfo<CommodityInfoVo>> listByRecommend(@RequestBody JSONObject obj) throws Exception {
        Integer page = 0;
        Integer size = 0;
        if (obj.containsKey("page") && obj.getInteger("page") != null) {
            page = obj.getInteger("page");
        }
        if (obj.containsKey("size") && obj.getInteger("size") != null) {
            size = obj.getInteger("size");
        }
        PageInfo<CommodityInfoVo> pageInfo = commodityInfoService.listByRecommend(page, size);
        return RetResponse.makeOKRsp(pageInfo);
    }

    /**
     * @Description: 根据教师查询对应商品
     * @Reutrn RetResult<PageInfo < CommodityInfo>>
     */
    @PostMapping("/listByecturer")
    @ApiOperation(value = "根据教师查询对应商品接口")
    public RetResult<PageInfo<CommodityInfoFileVo>> listByecturer(@RequestBody JSONObject obj) throws Exception {
        Integer page = 1;
        Integer size = 10;
        if (obj.containsKey("page") && obj.getInteger("page") != null) {
            page = obj.getInteger("page");
        }
        if (obj.containsKey("size") && obj.getInteger("size") != null) {
            size = obj.getInteger("size");
        }
        if (obj.containsKey("lecturer") && !StringUtils.isBlank(obj.getString("lecturer"))) {
            String lecturer = obj.getString("lecturer");
            PageInfo<CommodityInfoFileVo> pageInfo = commodityInfoService.listByecturer(page, size, lecturer);
            return RetResponse.makeOKRsp(pageInfo);
        } else {
            return RetResponse.makeErrRsp("没有选教师！");
        }
    }

    @PostMapping("/deleteIsRecommendByCommodityId")
    @ApiOperation(value = "删除推荐接口")
    public RetResult<Integer> deleteIsRecommendByCommodityId(@RequestBody JSONObject obj) throws Exception {
        if (obj.containsKey("commodityId") && !StringUtils.isBlank(obj.getString("commodityId"))) {
            String commodityId = obj.getString("commodityId");
            Integer state = commodityInfoService.deleteIsRecommendByCommodityId(commodityId);
            return RetResponse.makeOKRsp(state);
        } else {
            return RetResponse.makeErrRsp("未选择商品！");
        }
    }

    /**
     * @Description: 添加我的收藏
     * @Reutrn RetResult<String>
     */
    @PostMapping("/addMyFavorite")
    @ApiOperation(value = "添加我的收藏")
    @CheckToken
    public RetResult<String> addMyFavorite(@ApiParam(value = "参数: commodityId(商品id))") @Valid @RequestBody JSONObject obj, HttpServletRequest req) throws java.lang.Exception {
        Integer state = 0;
        String commodityId = null;
        Object uid = req.getAttribute("userId");
        if (null == uid){
            log.info("未登陆，请先登录");
            throw new ServiceException("未登陆，请先登录");
        }
        Integer userId = Integer.valueOf(uid.toString());//userId = 1;
        if(-1 == userId){
            log.info("无效的用户，请注册或登录");
            throw new ServiceException("无效的用户，请注册或登录");
        }
        if (obj.containsKey("commodityId") && obj.getString("commodityId") != null) {
            commodityId = obj.getString("commodityId");
        }

        state = myFavoriteService.addMyFavorite(userId, commodityId);
        if(state == 1){
            return RetResponse.makeOKRsp(commodityId);
        }else{
            return RetResponse.makeErrRsp("收藏失败！");
        }

    }

    /**
     * @Description: 删除我的收藏
     * @Reutrn RetResult<String>
     */
    @PostMapping("/deleteMyFavorite")
    @ApiOperation(value = "删除我的收藏")
    @CheckToken
    public RetResult<String> deleteMyFavorite(@ApiParam(value = "参数: commodityId(商品id))") @Valid @RequestBody JSONObject obj, HttpServletRequest req) throws java.lang.Exception {
        Integer state = 0;
        String commodityId = null;
        Object uid = req.getAttribute("userId");
        if (null == uid){
            log.info("未登陆，请先登录");
            throw new ServiceException("未登陆，请先登录");
        }
        Integer userId = Integer.valueOf(uid.toString());
        if(-1 == userId){
            log.info("无效的用户，请注册或登录");
            throw new ServiceException("无效的用户，请注册或登录");
        }
        if (obj.containsKey("commodityId") && obj.getString("commodityId") != null) {
            commodityId = obj.getString("commodityId");
        }

        state = myFavoriteService.deleteMyFavorite(userId, commodityId);
        if(state == 1){
            return RetResponse.makeOKRsp(commodityId);
        }else{
            return RetResponse.makeErrRsp("收藏失败！");
        }

    }

    /**
     * @Description: 我的收藏
     * @Reutrn RetResult<PageInfo <CommodityInfoFileVo>>
     */
    @PostMapping("/selectMyFavorite")
    @ApiOperation(value = "我的收藏")
    @CheckToken
    public RetResult selectMyFavorite(@ApiParam(value = "两个参数: page(分页数); size(分页大小)") @RequestBody JSONObject obj, HttpServletRequest req) throws java.lang.Exception {
        Integer page = 1;
        Integer size = 8;
        if (obj.containsKey("page") && obj.getInteger("page") != null) {
            page = obj.getInteger("page");
        }
        if (obj.containsKey("size") && obj.getInteger("size") != null) {
            size = obj.getInteger("size");
        }

        Object uid = req.getAttribute("userId");
        if (null == uid){
            log.info("未登陆，请先登录");
            throw new ServiceException("未登陆，请先登录");
        }
        Integer userId = Integer.valueOf(uid.toString());//userId = 1;
        List<CommodityInfoFileVo> pageInfo = myFavoriteService.selectMyFavorite(page, size, userId);
        return RetResponse.makeRsp(pageInfo,pageInfo.size());
    }
}