package com.by.blcu.mall.dao;

import com.by.blcu.core.universal.Dao;
import com.by.blcu.mall.model.MallOrderInfo;
import com.by.blcu.mall.vo.MallCommodityOrderPayVo;
import com.by.blcu.mall.vo.MallOrderInfoVo;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MallOrderInfoMapper extends Dao<MallOrderInfo> {

    List<MallOrderInfoVo> selectMallOrderInfoVoByMallOrderInfo(MallOrderInfo mallOrderInfo);

    Integer updateMallOrderInfoVoByOrderId(MallOrderInfo mallOrderInfo);

    Integer creatMallOrderInfo(MallOrderInfo mallOrderInfo);

    List<MallOrderInfoVo> selectMallOrderInfoVoByUserName(@Param("userName")String userName);

    List<MallOrderInfoVo> selectMallOrderInfoVoByUserIdAndCourseId(@Param("userName")String userName, @Param("courseId")String courseId);

    List<MallOrderInfoVo> selectMallOrderInfoVoByCommodityId(@Param("commodityId")String commodityId);

    List<MallCommodityOrderPayVo> excelByMallOrderInfo(MallOrderInfo mallOrderInfo);

    Integer updateMallOrderInfoVoByOrderIdAndPay(MallOrderInfo mallOrderInfo);

    List<MallOrderInfoVo> selectMallOrderInfoVoList();

    Integer updateMallOrderInfo(MallOrderInfo mallOrderInfo);

}