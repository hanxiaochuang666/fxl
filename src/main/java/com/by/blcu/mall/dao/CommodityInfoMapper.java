package com.by.blcu.mall.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.mall.model.CommodityInfo;
import com.by.blcu.mall.vo.CommodityInfoCenterVo;
import com.by.blcu.mall.vo.CommodityInfoFileVo;
import com.by.blcu.mall.vo.CommodityInfoVo;
import com.by.blcu.mall.vo.CourseCategoryCommodityVo;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommodityInfoMapper extends Dao<CommodityInfo> {
    List<CommodityInfoVo> selectListAll(CommodityInfo commodityInfo);

    Integer updateComStatusByCommodityId(@Param("list")List<String> list, @Param("commodityStatus")Integer commodityStatus);

    List<CommodityInfoVo> listByCcId(CommodityInfo commodityInfo);

    List<String> selectLecturerList();

    List<CommodityInfoVo> selectByLessonType();

    List<CommodityInfoVo> listByCommodityStatus();

    List<CommodityInfoVo> selectByAudition();

    CommodityInfoVo selectByCommodityId(String commodityId /*Integer userId*/);

    List<CommodityInfoVo> listByecturer();

    List<CommodityInfoVo> selectListByCommodityInfoCenterVo(CommodityInfoCenterVo commodityInfoCenterVo);

    Integer deleteListByCommodityId(@Param("list")List<String> list);

    CommodityInfoVo selectStatusByCommodityId(String commodityId);

    CommodityInfoVo selectStatusByCommodityIdAndIsRecommend(String commodityId);

    List<CommodityInfo> selectByCourseId(@Param("courseId")String courseId);

    List<CommodityInfoVo> listByRecommend();

    Integer updateSortByCommodityId(@Param("commodityId")String commodityId,@Param("sort")Integer sort);

    Integer updateIsRecommendByCommodityId(List<String> list);

    Integer deleteIsRecommendByCommodityId(@Param("commodityId")String commodityId);

    Integer selectCountByCommodityId();

    Integer selectCountByCommodityIdType();

    Integer updateRecommendSortByCommodityId(@Param("commodityId")String commodityId, @Param("recommendSort")Integer recommendSort);

    List<String> selectcommoditydIListByIsRecommend(@Param("lessonType")Integer lessonType);

    Integer updateCourseIntroduceByCommodityId(@Param("commodityId")String commodityId, @Param("courseIntroduce")String courseIntroduce);

    Integer updateCourseIdByCommodityId(@Param("commodityId")String commodityId, @Param("courseId")String courseId);

    CommodityInfoVo selectEditByCommodityId(@Param("commodityId")String commodityId);

    Integer saveCommodity(CommodityInfo commodityInfo);

    Integer updateIsRecommendToFalse();

    CommodityInfoVo selectByCommodityIdNoStatus(String commodityId);

    List<CommodityInfoVo> selectListByCommodityIdList(List<String> commodityIdList);

    Integer updateMealTypeByCommodityId(@Param("commodityId")String commodityId, @Param("mealType")String mealType);

    CommodityInfoVo selectByCommodityIdNoCommodityStatus(String commodityId);

    List<CommodityInfoFileVo> selectCommodityInfoFileVoByOrderId(@Param("orderId")String orderId);

    List<CommodityInfoVo> listByCommodityStatusByOrgCode(@Param("orgCode")String orgCode);
    //select commdity info by my-favorite commdity id list.
    List<CommodityInfoVo> selectFavoriteListByCommodityIdList(List<String> commodityIdList);

    List<CommodityInfoVo> checkCourseName(@Param("ccId")String ccId, @Param("courseName")String courseName);

    List<CommodityInfoVo> listByCourseName(CommodityInfo commodityInfo);

}