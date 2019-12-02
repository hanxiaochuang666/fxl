package com.by.blcu.mall.service;

import com.by.blcu.mall.model.CommodityInfo;
import com.by.blcu.core.universal.Service;
import com.by.blcu.mall.model.CommodityLecturer;
import com.by.blcu.mall.vo.CommodityInfoCenterVo;
import com.by.blcu.mall.vo.CommodityInfoFileVo;
import com.by.blcu.mall.vo.CommodityInfoVo;
import com.by.blcu.mall.vo.CourseCategoryCommodityVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* @Description: CommodityInfoService接口
* @author 李程
* @date 2019/07/29 11:03
*/
public interface CommodityInfoService extends Service<CommodityInfo> {

    PageInfo<CommodityInfoFileVo> selectListAll(Integer page, Integer size,CommodityInfo commodityInfo);

    String updateComStatusByCommodityId(List<String> commodityIdList,Integer commodityStatus);

    List<CourseCategoryCommodityVo> selectListRecursion();

    PageInfo<CommodityInfoFileVo> listByCcId(Integer page, Integer size, CommodityInfo commodityInfo);

    List<String> selectLecturerList();

    List<CommodityInfoFileVo> selectByLessonType();

    List<CommodityInfoFileVo> selectByAudition();

    CommodityInfoFileVo selectByCommodityId(String commodityId /*Integer userId*/);

    CommodityInfoFileVo selectByCommodityIdNoStatus(String commodityId);

    CommodityInfoVo selectByCommodityIdNoCommodityStatus(String commodityId);

    PageInfo<CommodityInfoFileVo> listByecturer(Integer page, Integer size, String lecturer);

    PageInfo<CommodityInfoFileVo> selectListByCommodityInfoCenterVo(Integer page, Integer size,CommodityInfoCenterVo commodityInfoCenterVo);

    String deleteListByCommodityId(List<String> commodityIdList);

    CommodityInfoVo selectStatusByCommodityId(String commodityId);

    List<CommodityInfo> selectByCourseId(String courseId);

    PageInfo<CommodityInfoVo> listByRecommend(Integer page, Integer size);

    Integer updateSortByCommodityId(String commodityId,Integer page);

    Integer updateIsRecommendByCommodityId(List<String> commodityIdList);

    Integer deleteIsRecommendByCommodityId(String commodityId);

    Integer selectCountByCommodityId();

    Integer selectCountByCommodityIdType();

    Integer updateRecommendSortByCommodityId(String commodityId, Integer recommendSort);

    List<String> selectcommoditydIListByIsRecommend(Integer lessonType);

    Integer updateCourseIntroduceByCommodityId(String commodityId, String courseIntroduce);

    Integer updateCourseIdByCommodityId(String commodityId, String courseId);

    CommodityInfoVo selectEditByCommodityId(String commodityId);

    Integer saveCommodity(CommodityInfo commodityInfo, List<CommodityLecturer> commodityLecturerList);

    Integer updateCommodity(CommodityInfo commodityInfo, List<CommodityLecturer> commodityLecturerList,String commodityId);

    public Integer updateIsRecommendToFalse();

    public CommodityInfoFileVo selectCarByCommodityId(String commodityId);

    Integer updateMealTypeByCommodityId(String commodityId, String mealType);

    List<CourseCategoryCommodityVo> selectListRecursionByOrgCode(String orgCode);

    Integer childCommodityUpdateUpSort(String comCommodityId, String commodityId);

    Integer childCommodityUpdateDownSort(String comCommodityId, String commodityId);

    PageInfo<CommodityInfoFileVo> listByCourseName(Integer page, Integer size, CommodityInfo commodityInfo);

    Integer deleteChildCommodity(String comCommodityId, String commodityId);

}