package com.by.blcu.mall.service;

import com.by.blcu.core.universal.Service;
import com.by.blcu.mall.model.BuyerCart;
import com.by.blcu.mall.model.MallOrderInfo;
import com.by.blcu.mall.vo.*;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Set;

/**
* @Description: MallOrderInfoService接口
* @author 李程
* @date 2019/09/02 17:25
*/
public interface MallOrderInfoService extends Service<MallOrderInfo> {

    public void insertBuyerCartToRedis(BuyerCart buyerCart, String username);

    public BuyerCart selectBuyerCartFromRedis(String username);

    void delBuyerCartToRedis(String commodityId, String username);

    public Boolean selectBuyerCartFromRedisById(String username,String commodityId);

    PageInfo<MallOrderInfoVo> selectMallOrderInfoVoByMallOrderInfo(Integer page, Integer size, MallOrderInfo mallOrderInfo);

    Integer updateMallOrderInfoVoByOrderId(MallOrderInfo mallOrderInfo);

    Integer creatMallOrderInfo(MallOrderInfo mallOrderInfo,List<String> commodityIdList);

    Integer creatPaymentInvoiceUpdateMallOrderInfo(MallPaymentInvoiceVo mallPaymentInvoiceVo);

    public List<MallOrderInfoVo> selectMallOrderInfoVoByUserName(String userName);

    public boolean selectMallOrderInfoVoByIdAndCourseId(Integer id,String courseId);

    public List<MallOrderInfoVo> selectMallOrderInfoVoByCommodityId(String commodityId);

    List<MallCommodityOrderPayVo> excelByMallOrderInfo(MallOrderInfo mallOrderInfo);

    public List<MallOrderInfoVo> selectMallOrderInfoVoListByOrderId(String orderId);

    Integer updateMallOrderInfoVoByOrderIdAndPay(MallOrderInfo mallOrderInfo);

    public List<MallOrderInfoVo> selectMallOrderInfoVoList();

    String creatOrderAndInvoice(MallOrderPaymentInvoiceVo mallOrderPaymentInvoiceVo, String userName);

    String updateMallOrderInfoVo(MallOrderPaymentInvoiceVo mallOrderPaymentInvoiceVo, String userName);

    Set<String> selectUserListByCourseId(String courseId);

}