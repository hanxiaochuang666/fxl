package com.by.blcu.mall.service.impl;

import com.by.blcu.core.constant.Constants;
import com.by.blcu.core.ret.ServiceException;
import com.by.blcu.core.universal.AbstractService;
import com.by.blcu.core.utils.*;
import com.by.blcu.mall.dao.MallOrderInfoMapper;
import com.by.blcu.mall.model.*;
import com.by.blcu.mall.service.*;
import com.by.blcu.mall.vo.*;
import com.by.blcu.manager.model.ManagerOrganization;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.service.ManagerOrganizationService;
import com.by.blcu.manager.service.SsoUserService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
* @Description: MallOrderInfoService接口实现类
* @author 李程
* @date 2019/09/02 17:25
*/
@Service
public class MallOrderInfoServiceImpl extends AbstractService<MallOrderInfo> implements MallOrderInfoService {

    @Resource
    private MallOrderInfoMapper mallOrderInfoMapper;

    @Resource
    private CommodityInfoService commodityInfoService;

    @Resource
    private MallCommodityOrderService mallCommodityOrderService;

    @Resource
    private ManagerOrganizationService managerOrganizationService;

    @Resource
    private IMallPaymentInvoiceService mallPaymentInvoiceService;

    @Resource
    private MallOrderInfoService mallOrderInfoService;

    @Resource
    private SsoUserService ssoUserService;

    @Resource
    private FileService fileService;

    @Resource
    private IMallOrderPaymentService mallOrderPaymentService;

    @Resource
    private MallPurchaseCommodityService mallPurchaseCommodityService;

    @Resource
    private RedisUtil redisUtil;

    //保存购物车到Redis中
    public void insertBuyerCartToRedis(BuyerCart buyerCart, String username) {
        List<BuyerItem> items = buyerCart.getItems();
        if (items.size() > 0) {
            //redis中保存的是commodityId 为key , amount 为value的Map集合
            Map<String, String> hash = new HashMap<String, String>();
            for (BuyerItem item : items) {
                //判断是否有同款
                if (!redisUtil.hExists("buyerCart:" + username, String.valueOf(item.getCommodityInfoFileVo().getCommodityId()))) {
                    hash.put(String.valueOf(item.getCommodityInfoFileVo().getCommodityId()), String.valueOf(item.getAmountNum()));
                }
                /*else {
                    redisUtil.hIncrBy("buyerCart:" + username, String.valueOf(item.getCommodityInfoFileVo().getCommodityId()), item.getAmountNum());
                }*/
            }
            if (hash.size() > 0) {
                redisUtil.hPutAll("buyerCart:" + username, hash);
            }
        }

    }

    //保存购物车到Redis中
    public void delBuyerCartToRedis(String commodityId, String username) {
        //判断是否有同款
        if (redisUtil.hExists("buyerCart:" + username, commodityId)) {
            redisUtil.hDelete("buyerCart:" + username, commodityId);
        }
    }

    //取出Redis中购物车
    public BuyerCart selectBuyerCartFromRedis(String username) {
        BuyerCart buyerCart = new BuyerCart();
        //获取所有商品, redis中保存的是skuId 为key , amount 为value的Map集合
        Map<Object, Object> hgetAll = redisUtil.hGetAll("buyerCart:" + username);
        Set<Map.Entry<Object, Object>> entrySet = hgetAll.entrySet();
        for (Map.Entry<Object, Object> entry : entrySet) {
            //entry.getKey(): skuId
            CommodityInfoFileVo commodityInfoFileVo = new CommodityInfoFileVo();
            commodityInfoFileVo.setCommodityId((String) entry.getKey());
            BuyerItem buyerItem = new BuyerItem();
            buyerItem.setCommodityInfoFileVo(commodityInfoFileVo);
            //entry.getValue(): amount
            buyerItem.setAmountNum(Integer.valueOf(entry.getValue().toString()));
            //添加到购物车中
            buyerCart.addItem(buyerItem);
        }

        return buyerCart;
    }

    //根据商品ID取出Redis中购物车
    public Boolean selectBuyerCartFromRedisById(String username,String commodityId) {
        BuyerCart buyerCart = new BuyerCart();
        //获取所有商品, redis中保存的是skuId 为key , amount 为value的Map集合
        Object o = redisUtil.hGet("buyerCart:" + username, commodityId);
        if(null != o){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public PageInfo<MallOrderInfoVo> selectMallOrderInfoVoByMallOrderInfo(Integer page, Integer size, MallOrderInfo mallOrderInfo) {
        String commodityId = "";
        Date createTimeEnd = mallOrderInfo.getCreateTimeEnd();
        if(null != createTimeEnd){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfNew = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                mallOrderInfo.setCreateTimeEnd(sdfNew.parse(sdf.format(createTimeEnd) + " 23:59:59"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //根据条件查询
        List<MallOrderInfoVo> mallOrderInfoVos = mallOrderInfoMapper.selectMallOrderInfoVoByMallOrderInfo(mallOrderInfo);
        if(null != mallOrderInfoVos && mallOrderInfoVos.size() > 0){
            for(MallOrderInfoVo mallOrderInfoVo : mallOrderInfoVos){
                //查询机构
                ManagerOrganization managerOrganization = managerOrganizationService.selectOrganizationByOrgCode(mallOrderInfoVo.getOrg());
                if(null != managerOrganization){
                    mallOrderInfoVo.setOrgName(managerOrganization.getOrganizationName());
                }
                String s = mallOrderInfoVo.getScreenshot();
                if(!StringUtils.isBlank(s)){
                    File file = fileService.selectByFileId(s);
                    if(null != file){
                        mallOrderInfoVo.setScreenshotView(file.getFilePath());
                    }
                }
                List<MallCommodityOrderVo> mallCommodityOrderVoList = mallOrderInfoVo.getMallCommodityOrderVoList();
                if(null != mallCommodityOrderVoList && mallCommodityOrderVoList.size() > 0){
                    //订单查询注入商品
                    for(MallCommodityOrderVo mallCommodityOrderVo : mallCommodityOrderVoList){
                        commodityId = mallCommodityOrderVo.getCommodityId();
                        if(!StringUtils.isBlank(commodityId)){
                            CommodityInfoFileVo commodityInfoFileVo = commodityInfoService.selectByCommodityId(commodityId);
                            mallCommodityOrderVo.setCommodityInfoFileVo(commodityInfoFileVo);
                        }
                        //判断是否套餐，如果是套餐注入子商品
                        if(!StringUtils.isBlank(commodityId) && null != mallCommodityOrderVo.getCommodityInfoFileVo().getCourseType() && mallCommodityOrderVo.getCommodityInfoFileVo().getCourseType() == 1){
                            List<CommodityInfoFileVo> commodityInfoFileVos = fillChildCommodity(commodityId,mallCommodityOrderVo.getOrderId());
                            if(null != commodityInfoFileVos && commodityInfoFileVos.size() > 0){
                                mallCommodityOrderVo.getCommodityInfoFileVo().setCommodityInfoFileVoList(commodityInfoFileVos);
                            }
                        }
                    }
                }
            }
        }
        SubListUtil<MallOrderInfoVo> sListUtil = new SubListUtil<>(page, size, mallOrderInfoVos);
        PageInfo<MallOrderInfoVo> pageInfo = new PageInfo<MallOrderInfoVo>(sListUtil.getList());
        pageInfo.setTotal(sListUtil.getTotal());
        pageInfo.setPages(sListUtil.getPage());
        pageInfo.setPageSize(sListUtil.getPageSize());
        return pageInfo;
    }
    //根据订单号注入子商品
    public List<CommodityInfoFileVo> fillChildCommodity(String commodityId,String orderId){
        List<CommodityInfoFileVo> list = new ArrayList<CommodityInfoFileVo>();
        MallPurchaseCommodity mallPurchaseCommodity = new MallPurchaseCommodity();
        mallPurchaseCommodity.setCommodityId(commodityId);
        mallPurchaseCommodity.setOrderId(orderId);
        List<MallPurchaseCommodity> select = mallPurchaseCommodityService.selectByChildCommoditySort(orderId,commodityId);
        if(null != select && select.size() > 0){
            for(MallPurchaseCommodity mallPurchaseCommodityNew : select){
                CommodityInfoFileVo commodityInfoFileVo = commodityInfoService.selectByCommodityId(mallPurchaseCommodityNew.getChildCommodityId());
                list.add(commodityInfoFileVo);
            }
        }
        return list;
    }

    @Override
    public List<MallOrderInfoVo> selectMallOrderInfoVoListByOrderId(String orderId) {
        MallOrderInfo mallOrderInfo = new MallOrderInfo();
        mallOrderInfo.setOrderId(orderId);
        String commodityId = "";
        List<MallOrderInfoVo> mallOrderInfoVos = mallOrderInfoMapper.selectMallOrderInfoVoByMallOrderInfo(mallOrderInfo);
        if(null != mallOrderInfoVos && mallOrderInfoVos.size() > 0){
            for(MallOrderInfoVo mallOrderInfoVo : mallOrderInfoVos){
                ManagerOrganization managerOrganization = managerOrganizationService.selectOrganizationByOrgCode(mallOrderInfoVo.getOrg());
                if(null != managerOrganization){
                    mallOrderInfoVo.setOrgName(managerOrganization.getOrganizationName());
                }
                String s = mallOrderInfoVo.getScreenshot();
                if(!StringUtils.isBlank(s)){
                    File file = fileService.selectByFileId(s);
                    if(null != file){
                        mallOrderInfoVo.setScreenshotView(file.getFilePath());
                    }
                }
                List<MallCommodityOrderVo> mallCommodityOrderVoList = mallOrderInfoVo.getMallCommodityOrderVoList();
                if(null != mallCommodityOrderVoList && mallCommodityOrderVoList.size() > 0){
                    for(MallCommodityOrderVo mallCommodityOrderVo : mallCommodityOrderVoList){
                        commodityId = mallCommodityOrderVo.getCommodityId();
                        if(!StringUtils.isBlank(commodityId)){
                            CommodityInfoFileVo commodityInfoFileVo = commodityInfoService.selectByCommodityIdNoStatus(commodityId);
                            mallCommodityOrderVo.setCommodityInfoFileVo(commodityInfoFileVo);
                        }
                        //判断是否套餐，如果是套餐注入子商品
                        if(mallCommodityOrderVo.getCommodityInfoFileVo().getCourseType() == 1){
                            List<CommodityInfoFileVo> commodityInfoFileVos = fillChildCommodity(commodityId,mallCommodityOrderVo.getOrderId());
                            if(null != commodityInfoFileVos && commodityInfoFileVos.size() > 0){
                                mallCommodityOrderVo.getCommodityInfoFileVo().setCommodityInfoFileVoList(commodityInfoFileVos);
                            }
                        }
                    }
                }
            }
        }
        return mallOrderInfoVos;
    }

    @Override
    public Integer updateMallOrderInfoVoByOrderIdAndPay(MallOrderInfo mallOrderInfo) {
        return mallOrderInfoMapper.updateMallOrderInfoVoByOrderIdAndPay(mallOrderInfo);
    }

    @Override
    public List<MallOrderInfoVo> selectMallOrderInfoVoByUserName(String userName) {
        String commodityId = "";
        List<MallOrderInfoVo> mallOrderInfoVos = mallOrderInfoMapper.selectMallOrderInfoVoByUserName(userName);
        if(null != mallOrderInfoVos && mallOrderInfoVos.size() > 0){
            for(MallOrderInfoVo mallOrderInfoVo : mallOrderInfoVos){
                ManagerOrganization managerOrganization = managerOrganizationService.selectOrganizationByOrgCode(mallOrderInfoVo.getOrg());
                if(null != managerOrganization){
                    mallOrderInfoVo.setOrgName(managerOrganization.getOrganizationName());
                }
                List<MallCommodityOrderVo> mallCommodityOrderVoList = mallOrderInfoVo.getMallCommodityOrderVoList();
                if(null != mallCommodityOrderVoList && mallCommodityOrderVoList.size() > 0){
                    for(MallCommodityOrderVo mallCommodityOrderVo : mallCommodityOrderVoList){
                        commodityId = mallCommodityOrderVo.getCommodityId();
                        if(!StringUtils.isBlank(commodityId)){
                            CommodityInfoFileVo commodityInfoFileVo = commodityInfoService.selectByCommodityIdNoStatus(commodityId);
                            mallCommodityOrderVo.setCommodityInfoFileVo(commodityInfoFileVo);
                        }
                        //判断是否套餐，如果是套餐注入子商品
                        if(mallCommodityOrderVo.getCommodityInfoFileVo().getCourseType() == 1){
                            List<CommodityInfoFileVo> commodityInfoFileVos = fillChildCommodity(commodityId,mallCommodityOrderVo.getOrderId());
                            if(null != commodityInfoFileVos && commodityInfoFileVos.size() > 0){
                                mallCommodityOrderVo.getCommodityInfoFileVo().setCommodityInfoFileVoList(commodityInfoFileVos);
                            }
                        }
                    }
                }
            }
        }
        return mallOrderInfoVos;
    }

    @Override
    public List<MallOrderInfoVo> selectMallOrderInfoVoList() {
        String commodityId = "";
        List<MallOrderInfoVo> mallOrderInfoVos = mallOrderInfoMapper.selectMallOrderInfoVoList();
        if(null != mallOrderInfoVos && mallOrderInfoVos.size() > 0){
            for(MallOrderInfoVo mallOrderInfoVo : mallOrderInfoVos){
                ManagerOrganization managerOrganization = managerOrganizationService.selectOrganizationByOrgCode(mallOrderInfoVo.getOrg());
                if(null != managerOrganization){
                    mallOrderInfoVo.setOrgName(managerOrganization.getOrganizationName());
                }
                List<MallCommodityOrderVo> mallCommodityOrderVoList = mallOrderInfoVo.getMallCommodityOrderVoList();
                if(null != mallCommodityOrderVoList && mallCommodityOrderVoList.size() > 0){
                    for(MallCommodityOrderVo mallCommodityOrderVo : mallCommodityOrderVoList){
                        commodityId = mallCommodityOrderVo.getCommodityId();
                        if(!StringUtils.isBlank(commodityId)){
                            CommodityInfoFileVo commodityInfoFileVo = commodityInfoService.selectByCommodityId(commodityId);
                            mallCommodityOrderVo.setCommodityInfoFileVo(commodityInfoFileVo);
                        }
                        //判断是否套餐，如果是套餐注入子商品
                        if(mallCommodityOrderVo.getCommodityInfoFileVo().getCourseType() == 1){
                            List<CommodityInfoFileVo> commodityInfoFileVos = fillChildCommodity(commodityId,mallCommodityOrderVo.getOrderId());
                            if(null != commodityInfoFileVos && commodityInfoFileVos.size() > 0){
                                mallCommodityOrderVo.getCommodityInfoFileVo().setCommodityInfoFileVoList(commodityInfoFileVos);
                            }
                        }
                    }
                }
            }
        }
        return mallOrderInfoVos;
    }

    @Override
    @Transactional
    public String creatOrderAndInvoice(MallOrderPaymentInvoiceVo mallOrderPaymentInvoiceVo,String userName) {
        if (!StringUtils.isBlank(mallOrderPaymentInvoiceVo.getCommodityId())) {
            //订单
            MallOrderInfo mallOrderInfo = new MallOrderInfo();
            mallOrderInfo.setOrderId(ApplicationUtils.getUUID());
            CommodityInfoFileVo commodityInfoFileVo = commodityInfoService.selectByCommodityId(mallOrderPaymentInvoiceVo.getCommodityId());
            //存到商品订单关系表
            MallCommodityOrder mallCommodityOrder = new MallCommodityOrder();
            mallCommodityOrder.setCommodityId(commodityInfoFileVo.getCommodityId());
            mallCommodityOrder.setOrderId(mallOrderInfo.getOrderId());
            mallCommodityOrder.setCoId(ApplicationUtils.getUUID());
            Integer insert = mallCommodityOrderService.insert(mallCommodityOrder);
            if(null == insert && insert <= 0 ){
                return "3";
            }
            //判断是否套餐
            if(commodityInfoFileVo.getCourseType() == 1){
                List<ChildCommoditySort> commodityList = mallOrderPaymentInvoiceVo.getCommodityList();
                //存到商品套餐购买关系表
                MallPurchaseCommodity mallPurchaseCommodity = new MallPurchaseCommodity();
                mallPurchaseCommodity.setCommodityId(commodityInfoFileVo.getCommodityId());
                mallPurchaseCommodity.setOrderId(mallOrderInfo.getOrderId());
                //是否可选套餐
                if(commodityInfoFileVo.getMealType() == 1){
                    Double SumAmount = 0.0;
                    Double SumCostPrice = 0.0;
                    for (ChildCommoditySort childCommoditySort : commodityList){
                        mallPurchaseCommodity.setOcId(ApplicationUtils.getUUID());
                        mallPurchaseCommodity.setChildCommodityId(childCommoditySort.getCommodityId());
                        mallPurchaseCommodity.setChildCommoditySort(childCommoditySort.getChildCommoditySort());
                        Integer insert1 = mallPurchaseCommodityService.insert(mallPurchaseCommodity);
                        if(null == insert1 && insert1 <= 0 ){
                            return "3";
                        }
                        CommodityInfoFileVo commodityInfoFileVo1 = commodityInfoService.selectByCommodityId(childCommoditySort.getCommodityId());
                        //计算价格
                        SumAmount += commodityInfoFileVo1.getPreferential();
                        SumCostPrice += commodityInfoFileVo1.getPrice();
                    }
                    mallOrderInfo.setAmount(SumAmount);
                    mallOrderInfo.setCostPrice(SumCostPrice);
                }
                if(commodityInfoFileVo.getMealType() == 0){
                    //直接取商品套餐价格
                    mallOrderInfo.setAmount(commodityInfoFileVo.getPreferential());
                    mallOrderInfo.setCostPrice(commodityInfoFileVo.getPrice());
                    for (ChildCommoditySort childCommoditySort : commodityList){
                        mallPurchaseCommodity.setOcId(ApplicationUtils.getUUID());
                        mallPurchaseCommodity.setChildCommodityId(childCommoditySort.getCommodityId());
                        mallPurchaseCommodity.setChildCommoditySort(childCommoditySort.getChildCommoditySort());
                        Integer insert1 = mallPurchaseCommodityService.insert(mallPurchaseCommodity);
                        if(null == insert1 && insert1 <= 0 ){
                            return "3";
                        }
                    }
                }
            }else {
                mallOrderInfo.setAmount(commodityInfoFileVo.getPreferential());
                mallOrderInfo.setCostPrice(commodityInfoFileVo.getPrice());
            }
            String s = "px" + ChineseCharacterUtil.convertHanzi2Pinyin(commodityInfoFileVo.getCcName(),false);
            mallOrderInfo.setUserName((String) userName);
            mallOrderInfo.setCreateTime(new Date());
            mallOrderInfo.setOrderStatus(1);
            mallOrderInfo.setIsBill(mallOrderPaymentInvoiceVo.getIsBill());
            mallOrderInfo.setIsdrawBill(0);
            mallOrderInfo.setReceiver(mallOrderPaymentInvoiceVo.getReceiver());
            mallOrderInfo.setTelNum(mallOrderPaymentInvoiceVo.getTelNum());
            if("2".equals(mallOrderPaymentInvoiceVo.getPayType()) && null != mallOrderPaymentInvoiceVo.getRemittance()){
                mallOrderInfo.setRemittance(mallOrderPaymentInvoiceVo.getRemittance());
            }
            if("2".equals(mallOrderPaymentInvoiceVo.getPayType()) && null != mallOrderPaymentInvoiceVo.getScreenshot()){
                mallOrderInfo.setScreenshot(mallOrderPaymentInvoiceVo.getScreenshot());
            }
            mallOrderInfo.setPaymentStatus(Constants.READY_TO_PAY);
            mallOrderInfo.setOrg("1234");
            mallOrderInfo.setOrderNo(ApplicationUtils.formatStr(s,20));
            Integer integer = mallOrderInfoMapper.creatMallOrderInfo(mallOrderInfo);
            if(null == integer && integer <= 0 ){
                return "1";
            }
            //发票
            if(null != mallOrderInfo.getIsBill() && mallOrderInfo.getIsBill() == 1){
                MallPaymentInvoice mallPaymentInvoice = new MallPaymentInvoice();
                mallPaymentInvoice.setPiId(ApplicationUtils.getUUID());
                mallPaymentInvoice.setOrderId(mallOrderInfo.getOrderId());
                mallPaymentInvoice.setAddress(mallOrderPaymentInvoiceVo.getAddress());
                mallPaymentInvoice.setZipCode(mallOrderPaymentInvoiceVo.getZipCode());
                mallPaymentInvoice.setInvoice(mallOrderPaymentInvoiceVo.getInvoice());
                mallPaymentInvoice.setCompanyName(mallOrderPaymentInvoiceVo.getCompanyName());
                mallPaymentInvoice.setTaxpayerNumber(mallOrderPaymentInvoiceVo.getTaxpayerNumber());
                mallPaymentInvoice.setType(mallOrderPaymentInvoiceVo.getType());
                mallPaymentInvoice.setRegisteredAddress(mallOrderPaymentInvoiceVo.getRegisteredAddress());
                mallPaymentInvoice.setRegisteredPhone(mallOrderPaymentInvoiceVo.getRegisteredPhone());
                mallPaymentInvoice.setBank(mallOrderPaymentInvoiceVo.getBank());
                mallPaymentInvoice.setCompanyAccount(mallOrderPaymentInvoiceVo.getCompanyAccount());
                mallPaymentInvoice.setCreateUser(userName);
                mallPaymentInvoice.setCreateTime(new Date());
                Integer i = mallPaymentInvoiceService.insertSelective(mallPaymentInvoice);
                if(null == i && i <= 0 ){
                    return "2";
                }
            }
            return mallOrderInfo.getOrderId();
        }else {
            throw new ServiceException("未找到购买商品！");
        }
    }

    @Override
    @Transactional
    public String updateMallOrderInfoVo(MallOrderPaymentInvoiceVo mallOrderPaymentInvoiceVo,String userName) {
        //订单
        MallOrderInfo mallOrderInfo = new MallOrderInfo();
        mallOrderInfo.setOrderId(mallOrderPaymentInvoiceVo.getOrderId());
        mallOrderInfo.setPayType(mallOrderPaymentInvoiceVo.getPayType());
        mallOrderInfo.setReceiver(mallOrderPaymentInvoiceVo.getReceiver());
        mallOrderInfo.setTelNum(mallOrderPaymentInvoiceVo.getTelNum());
        mallOrderInfo.setRemittance(mallOrderPaymentInvoiceVo.getRemittance());
        mallOrderInfo.setScreenshot(mallOrderPaymentInvoiceVo.getScreenshot());
        mallOrderInfo.setIsBill(mallOrderPaymentInvoiceVo.getIsBill());
        Integer integer = mallOrderInfoService.updateMallOrderInfoVoByOrderId(mallOrderInfo);
        if(null == integer || integer < 0 ){
            return "1";
        }
        if(null != mallOrderInfo.getIsBill() && mallOrderInfo.getIsBill() == 1){
            MallPaymentInvoice mallPaymentInvoice = new MallPaymentInvoice();
            if(!StringUtils.isBlank(mallOrderPaymentInvoiceVo.getPiId())){
                mallPaymentInvoice.setPiId(mallOrderPaymentInvoiceVo.getPiId());
                mallPaymentInvoice.setOrderId(mallOrderInfo.getOrderId());
                mallPaymentInvoice.setAddress(mallOrderPaymentInvoiceVo.getAddress());
                mallPaymentInvoice.setZipCode(mallOrderPaymentInvoiceVo.getZipCode());
                mallPaymentInvoice.setInvoice(mallOrderPaymentInvoiceVo.getInvoice());
                mallPaymentInvoice.setCompanyName(mallOrderPaymentInvoiceVo.getCompanyName());
                mallPaymentInvoice.setTaxpayerNumber(mallOrderPaymentInvoiceVo.getTaxpayerNumber());
                mallPaymentInvoice.setType(mallOrderPaymentInvoiceVo.getType());
                mallPaymentInvoice.setRegisteredAddress(mallOrderPaymentInvoiceVo.getRegisteredAddress());
                mallPaymentInvoice.setRegisteredPhone(mallOrderPaymentInvoiceVo.getRegisteredPhone());
                mallPaymentInvoice.setBank(mallOrderPaymentInvoiceVo.getBank());
                mallPaymentInvoice.setCompanyAccount(mallOrderPaymentInvoiceVo.getCompanyAccount());
                mallPaymentInvoice.setUpdateUser(userName);
                mallPaymentInvoice.setUpdateTime(new Date());
                Integer i = mallPaymentInvoiceService.updateByPrimaryKeySelective(mallPaymentInvoice);
                if(null == i || i < 0 ){
                    return "4";
                }
            }else{
                mallPaymentInvoice.setPiId(ApplicationUtils.getUUID());
                mallPaymentInvoice.setOrderId(mallOrderInfo.getOrderId());
                mallPaymentInvoice.setAddress(mallOrderPaymentInvoiceVo.getAddress());
                mallPaymentInvoice.setZipCode(mallOrderPaymentInvoiceVo.getZipCode());
                mallPaymentInvoice.setInvoice(mallOrderPaymentInvoiceVo.getInvoice());
                mallPaymentInvoice.setCompanyName(mallOrderPaymentInvoiceVo.getCompanyName());
                mallPaymentInvoice.setTaxpayerNumber(mallOrderPaymentInvoiceVo.getTaxpayerNumber());
                mallPaymentInvoice.setType(mallOrderPaymentInvoiceVo.getType());
                mallPaymentInvoice.setRegisteredAddress(mallOrderPaymentInvoiceVo.getRegisteredAddress());
                mallPaymentInvoice.setRegisteredPhone(mallOrderPaymentInvoiceVo.getRegisteredPhone());
                mallPaymentInvoice.setBank(mallOrderPaymentInvoiceVo.getBank());
                mallPaymentInvoice.setCompanyAccount(mallOrderPaymentInvoiceVo.getCompanyAccount());
                mallPaymentInvoice.setCreateUser(userName);
                mallPaymentInvoice.setCreateTime(new Date());
                Integer ii = mallPaymentInvoiceService.insertSelective(mallPaymentInvoice);
                if(null == ii || ii < 0 ){
                    return "5";
                }
            }
        }
        return mallOrderInfo.getOrderId();
    }

    @Override
    public Set<String> selectUserListByCourseId(String courseId) {
        List<MallOrderInfoVo> mallOrderInfoVos = mallOrderInfoService.selectMallOrderInfoVoList();
        if(null != mallOrderInfoVos && mallOrderInfoVos.size() > 0){
            List<String> list = new ArrayList<String>();
            for(MallOrderInfoVo mallOrderInfoVo : mallOrderInfoVos){
                List<String> userNameList = selectUserListByMallOrderInfoVo(mallOrderInfoVo, courseId);
                list.addAll(userNameList);
            }
            Set<String> userNameSet = new HashSet<String>(list);
            return userNameSet;
        }

        return null;
    }

    public List<String> selectUserListByMallOrderInfoVo(MallOrderInfoVo mallOrderInfoVo,String courseId){
        List<String> list = new ArrayList<String>();
        List<MallCommodityOrderVo> mallCommodityOrderVoList = mallOrderInfoVo.getMallCommodityOrderVoList();
        if(null != mallCommodityOrderVoList && mallCommodityOrderVoList.size() > 0){
            for(MallCommodityOrderVo mallCommodityOrderVo : mallCommodityOrderVoList){
                CommodityInfoFileVo commodityInfoFileVo = mallCommodityOrderVo.getCommodityInfoFileVo();
                //单个商品
                if(commodityInfoFileVo.getCourseType() == 0 && null != commodityInfoFileVo.getCourseId() && courseId.equals(commodityInfoFileVo.getCourseId())){
                    list.add(mallOrderInfoVo.getUserName());
                    break;
                }
                //套餐商品
                if(commodityInfoFileVo.getCourseType() == 1){
                    List<CommodityInfoFileVo> commodityInfoFileVoList = commodityInfoFileVo.getCommodityInfoFileVoList();
                    if(null != commodityInfoFileVoList && commodityInfoFileVoList.size() > 0){
                        for(CommodityInfoFileVo commodityInfoFileVoNew : commodityInfoFileVoList){
                            String courseId1 = commodityInfoFileVoNew.getCourseId();
                            if(null != courseId1 && courseId.equals(courseId1)){
                                list.add(mallOrderInfoVo.getUserName());
                                break;
                            }
                        }
                    }
                }
            }
        }
        return list;
    }



    @Override
    public boolean selectMallOrderInfoVoByIdAndCourseId(Integer id, String courseId) {
        SsoUser userByUserIdInter = ssoUserService.getUserByIdInter(id);
        if(null != userByUserIdInter){
            String userName = userByUserIdInter.getUserName();
            List<MallOrderInfoVo> mallOrderInfoVos = mallOrderInfoMapper.selectMallOrderInfoVoByUserIdAndCourseId(userName,courseId);
            if(null != mallOrderInfoVos && mallOrderInfoVos.size()>0){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    @Override
    public List<MallOrderInfoVo> selectMallOrderInfoVoByCommodityId(String commodityId) {
        List<MallOrderInfoVo> mallOrderInfoVos = mallOrderInfoMapper.selectMallOrderInfoVoByCommodityId(commodityId);
        return mallOrderInfoVos;
    }

    @Override
    public List<MallCommodityOrderPayVo> excelByMallOrderInfo(MallOrderInfo mallOrderInfo) {
        return mallOrderInfoMapper.excelByMallOrderInfo(mallOrderInfo);
    }

    @Override
    @Transactional
    public Integer updateMallOrderInfoVoByOrderId(MallOrderInfo mallOrderInfo) {
        Integer integer = mallOrderInfoMapper.updateMallOrderInfoVoByOrderId(mallOrderInfo);
        if(null != mallOrderInfo.getPaymentStatus() && mallOrderInfo.getPaymentStatus() == 1 && !StringUtils.isBlank(mallOrderInfo.getUserName())){
            try {
                Boolean aBoolean = mallOrderPaymentService.syncCourseResourcesForShelf(mallOrderInfo.getOrderId(), mallOrderInfo.getUserName());
                if(!aBoolean){
                    throw new ServiceException("同步课程失败！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return integer;
    }

    @Override
    @Transactional
    public Integer creatMallOrderInfo(MallOrderInfo mallOrderInfo,List<String> commodityIdList) {
        Integer insert = 0;
        Integer integer = mallOrderInfoMapper.creatMallOrderInfo(mallOrderInfo);
        for(String s : commodityIdList){
            MallCommodityOrder mallCommodityOrder = new MallCommodityOrder();
            mallCommodityOrder.setCoId(ApplicationUtils.getUUID());
            mallCommodityOrder.setCommodityId(s);
            mallCommodityOrder.setOrderId(mallOrderInfo.getOrderId());
            insert = mallCommodityOrderService.insert(mallCommodityOrder);
        }
        if(integer != null && insert > 0){
            return integer;
        }else{
            throw new ServiceException( "保存失败！");
        }
    }

    @Override
    @Transactional
    public Integer creatPaymentInvoiceUpdateMallOrderInfo(MallPaymentInvoiceVo mallPaymentInvoiceVo) {
        String receiver = mallPaymentInvoiceVo.getReceiver();
        String telNum = mallPaymentInvoiceVo.getTelNum();
        String orderId = mallPaymentInvoiceVo.getOrderId();
        Integer payType = mallPaymentInvoiceVo.getPayType();
        MallOrderInfo mallOrderInfo = new MallOrderInfo();
        if(!StringUtils.isBlank(orderId)){
            mallOrderInfo.setOrderId(orderId);
            mallOrderInfo.setReceiver(receiver);
            mallOrderInfo.setTelNum(telNum);
            mallOrderInfo.setPayType(payType);
            Integer integer = updateMallOrderInfoVoByOrderId(mallOrderInfo);
            MallPaymentInvoice mallPaymentInvoice = MapAndObjectUtils.ObjectClone(mallPaymentInvoiceVo, MallPaymentInvoice.class);
            mallPaymentInvoice.setPiId(ApplicationUtils.getUUID());
            Integer i = mallPaymentInvoiceService.insertSelective(mallPaymentInvoice);
            if(null != integer && null != i){
                return i;
            }else{
                throw new ServiceException( "保存失败！");
            }
        }else{
            throw new ServiceException( "未传入订单！");
        }
    }
}