package com.by.blcu.mall.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.constant.Constants;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.*;
import com.by.blcu.mall.model.BuyerCart;
import com.by.blcu.mall.model.BuyerItem;
import com.by.blcu.mall.model.MallOrderInfo;
import com.by.blcu.mall.model.MallPaymentInvoice;
import com.by.blcu.mall.service.CommodityInfoService;
import com.by.blcu.mall.service.MallOrderInfoService;
import com.by.blcu.mall.vo.*;
import com.by.blcu.manager.model.ManagerOrganization;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.service.ManagerOrganizationService;
import com.by.blcu.manager.service.SsoUserService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 李程
 * @Description: MallOrderInfoController类
 * @date 2019/09/02 17:25
 */
@Controller
@CheckToken
@RequestMapping("/mallOrderInfo")
public class MallOrderInfoController {

    @Resource
    private MallOrderInfoService mallOrderInfoService;

    @Resource
    private CommodityInfoService commodityInfoService;

    @Resource
    private SsoUserService ssoUserService;

    @Resource
    private ManagerOrganizationService managerOrganizationService;

    @PostMapping("/insert")
    public RetResult<Integer> insert(MallOrderInfo mallOrderInfo) throws Exception {
        mallOrderInfo.setOrderId(ApplicationUtils.getUUID());
        Integer state = mallOrderInfoService.insert(mallOrderInfo);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/deleteById")
    public RetResult<Integer> deleteById(@RequestParam String id) throws Exception {
        Integer state = mallOrderInfoService.deleteById(id);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/update")
    public RetResult<Integer> update(MallOrderInfo mallOrderInfo) throws Exception {
        Integer state = mallOrderInfoService.update(mallOrderInfo);
        return RetResponse.makeOKRsp(state);
    }

    @PostMapping("/selectById")
    public RetResult<MallOrderInfo> selectById(@RequestParam String id) throws Exception {
        MallOrderInfo mallOrderInfo = mallOrderInfoService.selectById(id);
        return RetResponse.makeOKRsp(mallOrderInfo);
    }

    /**
     * @param page 页码
     * @param size 每页条数
     * @Description: 分页查询
     * @Reutrn RetResult<PageInfo   <   MallOrderInfo>>
     */
    @PostMapping("/list")
    public RetResult<PageInfo<MallOrderInfo>> list(@RequestParam(defaultValue = "0") Integer page,
                                                   @RequestParam(defaultValue = "0") Integer size) throws Exception {
        PageHelper.startPage(page, size);
        List<MallOrderInfo> list = mallOrderInfoService.selectAll();
        PageInfo<MallOrderInfo> pageInfo = new PageInfo<MallOrderInfo>(list);
        return RetResponse.makeOKRsp(pageInfo);
    }

    /**
     * @Description: 创建发票
     * @Reutrn
     */
    @PostMapping("/creatPaymentInvoiceUpdateMallOrderInfo")
    @CheckToken
    @ResponseBody
    public RetResult<Integer> creatPaymentInvoiceUpdateMallOrderInfo(@RequestBody MallPaymentInvoiceVo mallPaymentInvoiceVo, HttpServletRequest httpServletRequest) throws Exception {
        if(!StringUtils.isBlank(mallPaymentInvoiceVo.getOrderId())){
            Integer state = mallOrderInfoService.creatPaymentInvoiceUpdateMallOrderInfo(mallPaymentInvoiceVo);
            return RetResponse.makeOKRsp(state);
        }else{
            return RetResponse.makeErrRsp("没有订单");
        }
    }

//    /**
//     * @Description: 创建订单
//     * @Reutrn
//     */
//    @PostMapping("/creatMallOrderInfo")
//    @CheckToken
//    @ResponseBody
//    public RetResult<Object> creatMallOrderInfo(@RequestBody JSONObject obj, HttpServletRequest httpServletRequest) throws Exception {
//        if (obj.containsKey("commodityIdString") && !StringUtils.isBlank("commodityIdString")) {
//            String ids = obj.getString("commodityIdString");
//            MallOrderInfo mallOrderInfo = new MallOrderInfo();
//            Double amount = 0.0;
//            Double costPrice = 0.0;
//            List<String> commodityIdList = Arrays.asList(ids.split(","));//根据逗号分隔转化为list
//            if (null != commodityIdList && commodityIdList.size() > 0) {
//                for (String s : commodityIdList) {
//                    CommodityInfoFileVo commodityInfoFileVo = commodityInfoService.selectByCommodityId(s);
//                    if (null != commodityInfoFileVo) {
//                        if (null != commodityInfoFileVo.getPreferential()) {
//                            amount += commodityInfoFileVo.getPreferential();
//                        }
//                        if (null != commodityInfoFileVo.getPrice()) {
//                            costPrice += commodityInfoFileVo.getPrice();
//                        }
//                    }
//                }
//            }
//            Object username = httpServletRequest.getAttribute("username");
//            if (null != username) {
//                mallOrderInfo.setUserName((String) username);
//                mallOrderInfo.setOrderId(ApplicationUtils.getUUID());
//                mallOrderInfo.setCreateTime(new Date());
//                mallOrderInfo.setOrderStatus(1);
//                mallOrderInfo.setIsBill(0);
//                mallOrderInfo.setIsdrawBill(0);
//                mallOrderInfo.setPaymentStatus(Constants.READY_TO_PAY);
//                mallOrderInfo.setOrg("1234");
//                mallOrderInfo.setOrderNo("px" + ApplicationUtils.getNumStringRandom(18));
//                mallOrderInfo.setAmount(amount);
//                mallOrderInfo.setCostPrice(costPrice);
//                Integer state = mallOrderInfoService.creatMallOrderInfo(mallOrderInfo, commodityIdList);
//                if (null != state) {
//                    List<MallOrderInfoVo> mallOrderInfoVos = mallOrderInfoService.selectMallOrderInfoVoListByOrderId(mallOrderInfo.getOrderId());
//                    if (null != mallOrderInfoVos && mallOrderInfoVos.size() > 0) {
//                        return RetResponse.makeOKRsp(mallOrderInfoVos.get(0));
//                    }
//                }
//                return RetResponse.makeErrRsp("订单创建失败");
//
//            } else {
//                return RetResponse.makeErrRsp("用户未登录！");
//            }
//        } else {
//            return RetResponse.makeErrRsp("请选择结算商品");
//        }
//    }

    /**
     * @Description: 创建订单
     * @Reutrn
     */
    @PostMapping("/creatMallOrderInfo")
    @CheckToken
    @ResponseBody
    public RetResult<Object> creatMallOrderInfo(@RequestBody MallOrderPaymentInvoiceVo mallOrderPaymentInvoiceVo, HttpServletRequest httpServletRequest) throws Exception {
        Object userName = httpServletRequest.getAttribute("username");
        String s = "";
        if (null != userName) {
            if(null != mallOrderPaymentInvoiceVo.getOrderId()){
                s = mallOrderInfoService.updateMallOrderInfoVo(mallOrderPaymentInvoiceVo, userName.toString());
            }else {
                s = mallOrderInfoService.creatOrderAndInvoice(mallOrderPaymentInvoiceVo, userName.toString());
            }
            if("1".equals(s)){
                return RetResponse.makeErrRsp("订单创建或更新失败");
            }
            if("2".equals(s)){
                return RetResponse.makeErrRsp("发票创建失败");
            }
            if("3".equals(s)){
                return RetResponse.makeErrRsp("关联表创建失败");
            }
            if("4".equals(s)){
                return RetResponse.makeErrRsp("发票更新失败");
            }
            if("5".equals(s)){
                return RetResponse.makeErrRsp("订单已有情况下发票创建失败");
            }
            List<MallOrderInfoVo> mallOrderInfoVos = mallOrderInfoService.selectMallOrderInfoVoListByOrderId(s);
            if (null != mallOrderInfoVos && mallOrderInfoVos.size() > 0) {
                return RetResponse.makeOKRsp(mallOrderInfoVos.get(0));
            }
            return RetResponse.makeErrRsp("订单失败");
        } else {
            return RetResponse.makeErrRsp("用户未登录！");
        }
    }

    /**
     * @Description: 订单更改状态
     * @Reutrn
     */
    @ResponseBody
    @PostMapping("/updateMallOrderInfoVoByOrderId")
    public RetResult<Integer> updateMallOrderInfoVoByOrderId(@RequestBody MallOrderInfo mallOrderInfo) throws Exception {
        if(!StringUtils.isBlank(mallOrderInfo.getOrderId())){
            Integer state = mallOrderInfoService.updateMallOrderInfoVoByOrderId(mallOrderInfo);
            return RetResponse.makeOKRsp(state);
        }else {
            return RetResponse.makeErrRsp("未找到该订单！");
        }
    }

    /**
     * @Description: 前台订单更改状态
     * @Reutrn
     */
    @ResponseBody
    @PostMapping("/updateMallOrderInfoVoByOrderIdAndPay")
    public RetResult<Integer> updateMallOrderInfoVoByOrderIdAndPay(@RequestBody MallOrderInfo mallOrderInfo) throws Exception {
        if(!StringUtils.isBlank(mallOrderInfo.getOrderId())){
            Integer state = mallOrderInfoService.updateMallOrderInfoVoByOrderIdAndPay(mallOrderInfo);
            if(null != state && state > 0){
                return RetResponse.makeOKRsp(state);
            }
            return RetResponse.makeErrRsp("删除失败！");
        }else {
            return RetResponse.makeErrRsp("未找到该订单！");
        }
    }

    /**
     * @Description: 订单导出
     * @Reutrn
     */
    @ResponseBody
    @GetMapping("/excelByMallOrderInfo")
    public void excelByMallOrderInfo(MallOrderInfoExcel mallOrderInfoExcel,HttpServletRequest request,HttpServletResponse response) throws Exception {
        MallOrderInfo mallOrderInfo = MapAndObjectUtils.ObjectClone(mallOrderInfoExcel, MallOrderInfo.class);
        if(!StringUtils.isBlank(mallOrderInfoExcel.getExcelCreateTimeStart())){
            mallOrderInfo.setCreateTimeStart(DateUtils.string2Date(mallOrderInfoExcel.getExcelCreateTimeStart(),"yyyy-MM-dd"));
        }
        if(!StringUtils.isBlank(mallOrderInfoExcel.getExcelCreateTimeEnd())){
            mallOrderInfo.setCreateTimeEnd(DateUtils.string2Date(mallOrderInfoExcel.getExcelCreateTimeEnd(),"yyyy-MM-dd"));
        }
        if(mallOrderInfo.getPaymentStatus() == 1){
            Integer i = 1;
            List<MallCommodityOrderPayVo> list = mallOrderInfoService.excelByMallOrderInfo(mallOrderInfo);
            for(MallCommodityOrderPayVo mallCommodityOrderPayVo : list){
                SsoUser userByUserNameInter = ssoUserService.getUserByUserNameInter(mallCommodityOrderPayVo.getUserName());
                if(null != userByUserNameInter){
                    mallCommodityOrderPayVo.setName(userByUserNameInter.getRealName());
                }
                if(null != mallCommodityOrderPayVo.getStandard()){
                    String format = new DecimalFormat("0.00").format(mallCommodityOrderPayVo.getStandard());
                    mallCommodityOrderPayVo.setStandardView(format);
                }
                if(null != mallCommodityOrderPayVo.getCostPrice() && null != mallCommodityOrderPayVo.getAmount()){
                    mallCommodityOrderPayVo.setPaidTuition(DoubleUtil.sub(mallCommodityOrderPayVo.getCostPrice(),mallCommodityOrderPayVo.getAmount()));
                }
                if(!StringUtils.isBlank(mallCommodityOrderPayVo.getOrg())){
                    ManagerOrganization managerOrganization = managerOrganizationService.selectOrganizationByOrgCode(mallCommodityOrderPayVo.getOrg());
                    if(null != managerOrganization){
                        mallCommodityOrderPayVo.setOrgName(managerOrganization.getOrganizationName());
                    }
                }
                mallCommodityOrderPayVo.setNum(i);
                i++;
            }
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("非学历交费确认表","交费确认表"), MallCommodityOrderPayVo.class, list);
            // 设置excel的文件名称
            String excelName = "交费确认表" ;
            excelName = new String(excelName.getBytes("UTF-8"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateStr = excelName+"-"+sdf.format(new Date());
            // 指定下载的文件名--设置响应头
            //response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Access-Control-Expose-Headers","filename");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(dateStr,"UTF-8") +".xls");
            response.setHeader("filename",URLEncoder.encode(dateStr,"UTF-8") +".xls");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            try{
//                File savefile = new File("D:/excel/");
//                if (!savefile.exists()) {
//                    savefile.mkdirs();
//                }
                OutputStream output = response.getOutputStream();
                BufferedOutputStream bufferedOutPut = new BufferedOutputStream(output);
                workbook.write(bufferedOutPut);
                bufferedOutPut.flush();
                bufferedOutPut.close();
                output.close();
//                FileOutputStream fos = new FileOutputStream("D:/excel/交费确认表.xls");
//                workbook.write(fos);
//                fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * @Description: 订单查询
     * @Reutrn RetResult<PageInfo < CommodityInfo>>
     */
    @ResponseBody
    @PostMapping("/selectMallOrderInfoVoByMallOrderInfo")
    public RetResult<PageInfo<MallOrderInfoVo>> selectMallOrderInfoVoByMallOrderInfo(@RequestBody JSONObject obj) throws Exception {
        Integer page = 1;
        Integer size = 10;
        if (obj.containsKey("page") && obj.getInteger("page") != null) {
            page = obj.getInteger("page");
        }
        if (obj.containsKey("size") && obj.getInteger("size") != null) {
            size = obj.getInteger("size");
        }
        MallOrderInfo mallOrderInfo = obj.toJavaObject(MallOrderInfo.class);
        PageInfo<MallOrderInfoVo> pageInfo = mallOrderInfoService.selectMallOrderInfoVoByMallOrderInfo(page, size, mallOrderInfo);
        return RetResponse.makeOKRsp(pageInfo);
    }

    /**
     * @Description: 根据课程Id查询购买的用户
     * @Reutrn RetResult<PageInfo < CommodityInfo>>
     */
    @ResponseBody
    @PostMapping("/selectUserListByCourseId")
    public RetResult<Set<String>> selectUserListByCourseId(@RequestBody JSONObject obj) throws Exception {
        if(obj.containsKey("courseId") && !StringUtils.isBlank(obj.getString("courseId"))){
            String courseId = obj.getString("courseId");
            Set<String> set = mallOrderInfoService.selectUserListByCourseId(courseId);
            if(null!=set && !set.isEmpty())
                return RetResponse.makeOKRsp(set);
        }
        return RetResponse.makeOKRsp();
    }
    /**
     * @Description: 根据用户查询已经购买的商品
     * @Reutrn RetResult<PageInfo < CommodityInfo>>
     */
    @ResponseBody
    @PostMapping("/selectMallOrderInfoVoByUserName")
    public RetResult<List<MallOrderInfoVo>> selectMallOrderInfoVoByUserName(HttpServletRequest httpServletRequest) throws Exception {
        String userName = (String) httpServletRequest.getAttribute("username");
        if(!StringUtils.isBlank(userName)){
            List<MallOrderInfoVo> mallOrderInfoVos = mallOrderInfoService.selectMallOrderInfoVoByUserName(userName);
            return RetResponse.makeOKRsp(mallOrderInfoVos);
        }else {
            return RetResponse.makeOKRsp();
        }
    }

    /**
     * @Description: 查询已经购买的商品
     * @Reutrn RetResult<PageInfo < CommodityInfo>>
     */
    @ResponseBody
    @PostMapping("/selectMallOrderInfoVoList")
    public RetResult<List<MallOrderInfoVo>> selectMallOrderInfoVoList() throws Exception {
            List<MallOrderInfoVo> mallOrderInfoVos = mallOrderInfoService.selectMallOrderInfoVoList();
            return RetResponse.makeOKRsp(mallOrderInfoVos);
    }

    /**
     * @Description: 根据用户和课程ID查询已经购买的商品
     * @Reutrn RetResult<PageInfo < CommodityInfo>>
     */
    @ResponseBody
    @PostMapping("/selectMallOrderInfoVoByIdAndCourseId")
    public RetResult<Boolean> selectMallOrderInfoVoByIdAndCourseId(@RequestBody JSONObject obj,HttpServletRequest httpServletRequest) throws Exception {
        boolean b = false;
        if(obj.containsKey("id") && null != obj.getInteger("id") && obj.containsKey("courseId") && !StringUtils.isBlank("courseId")){
            String courseId = obj.getString("courseId");
            Integer id = obj.getInteger("id");
            b = mallOrderInfoService.selectMallOrderInfoVoByIdAndCourseId(id, courseId);
            return RetResponse.makeOKRsp(b);
        }else {
            return RetResponse.makeOKRsp(b);
        }
    }

    /**
     * @Description: 根据商品ID已经购买的商品
     * @Reutrn RetResult<PageInfo < CommodityInfo>>
     */
    @ResponseBody
    @PostMapping("/selectMallOrderInfoVoByCommodityId")
    public RetResult<List<MallOrderInfoVo>> selectMallOrderInfoVoByCommodityId(@RequestBody JSONObject obj) throws Exception {
        if(obj.containsKey("commodityId") && !StringUtils.isBlank("commodityId")){
            String commodityId = obj.getString("commodityId");
            List<MallOrderInfoVo> list = mallOrderInfoService.selectMallOrderInfoVoByCommodityId(commodityId);
            return RetResponse.makeOKRsp(list);
        }else {
            return RetResponse.makeOKRsp();
        }
    }

    /**
     * @Description: 前台订单查询
     * @Reutrn RetResult<PageInfo < CommodityInfo>>
     */
    @ResponseBody
    @CheckToken
    @PostMapping("/selectMallOrderInfoVo")
    public RetResult<PageInfo<MallOrderInfoVo>> selectMallOrderInfoVo(@RequestBody JSONObject obj, HttpServletRequest httpServletRequest) throws Exception {
        Object username = httpServletRequest.getAttribute("username");
        if(!StringUtils.isBlank(username.toString())){
            Integer page = 1;
            Integer size = 10;
            if (obj.containsKey("page") && obj.getInteger("page") != null) {
                page = obj.getInteger("page");
            }
            if (obj.containsKey("size") && obj.getInteger("size") != null) {
                size = obj.getInteger("size");
            }
            MallOrderInfo mallOrderInfo = obj.toJavaObject(MallOrderInfo.class);
            mallOrderInfo.setUserName((String) username);
            PageInfo<MallOrderInfoVo> pageInfo = mallOrderInfoService.selectMallOrderInfoVoByMallOrderInfo(page, size, mallOrderInfo);
            return RetResponse.makeOKRsp(pageInfo);
        }else{
            return RetResponse.makeErrRsp("未登录，请先登录！");
        }
    }

    @ResponseBody
    @PostMapping(value = "/shopping/buyerCart")
    public RetResult<BuyerCart> buyerCart(@RequestBody JSONObject obj, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
        if (obj.containsKey("commodityIdString") && !StringUtils.isBlank(obj.getString("commodityIdString"))) {
            String ids = obj.getString("commodityIdString");
            List<String> commodityIdList = Arrays.asList(ids.split(","));
            String username = (String) request.getAttribute("username");
            if (!StringUtils.isBlank(username)) {
                BuyerCart buyerCartView = new BuyerCart();
                for(String commodityId : commodityIdList){
                    BuyerCart buyerCart = new BuyerCart();
                    CommodityInfoFileVo commodityInfoFileVo = new CommodityInfoFileVo();
                    commodityInfoFileVo.setCommodityId(commodityId);
                    BuyerItem buyerItem = new BuyerItem();
                    buyerItem.setCommodityInfoFileVo(commodityInfoFileVo);
                    //设置数量
                    //buyerItem.setAmount(amount);
                    //添加购物项到购物车
                    buyerCart.addItem(buyerItem);
                    //排序  倒序
                    List<BuyerItem> items = buyerCart.getItems();
                    Collections.sort(items, new Comparator<BuyerItem>() {
                        @Override
                        public int compare(BuyerItem o1, BuyerItem o2) {
                            return -1;
                        }
                    });
                    //将购物车追加到Redis中
                    mallOrderInfoService.insertBuyerCartToRedis(buyerCart, username);
                }
                //取出Redis中的购物车
                buyerCartView = mallOrderInfoService.selectBuyerCartFromRedis(username);
                //将购物车装满, 前面只是将skuId装进购物车, 这里还需要查出详情
                List<BuyerItem> items1 = buyerCartView.getItems();
                if (items1.size() > 0) {
                    //只有购物车中有购物项, 才可以将相关信息加入到购物项中
                    for (BuyerItem buyerItem1 : items1) {
                        CommodityInfoFileVo commodityInfoFileVo = commodityInfoService.selectCarByCommodityId(buyerItem1.getCommodityInfoFileVo().getCommodityId());
                        if(null != commodityInfoFileVo){
                            buyerItem1.setCommodityInfoFileVo(commodityInfoFileVo);
                        }
                    }
                }
                return RetResponse.makeOKRsp(buyerCartView);
            }else{
                return RetResponse.makeErrRsp("未登录，请先登录");
            }
        } else {
            return RetResponse.makeErrRsp("未找到对应商品");
        }
    }

    @ResponseBody
    @PostMapping(value = "/shopping/delBuyerCart")
    public RetResult<BuyerCart> delBuyerCart(@RequestBody JSONObject obj, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
        if (obj.containsKey("commodityId") && !StringUtils.isBlank(obj.getString("commodityId"))) {
            String commodityId = obj.getString("commodityId");
            String username = (String) request.getAttribute("username");
            BuyerCart buyerCart = new BuyerCart();
            if (!StringUtils.isBlank(username)) {
                //删除购物车Redis
                mallOrderInfoService.delBuyerCartToRedis(commodityId, username);
                //取出Redis中的购物车
                buyerCart = mallOrderInfoService.selectBuyerCartFromRedis(username);
                if(null != buyerCart){
                    List<BuyerItem> items1 = buyerCart.getItems();
                    if (items1.size() > 0) {
                        //只有购物车中有购物项, 才可以将sku相关信息加入到购物项中
                        for (BuyerItem buyerItem1 : items1) {
                            buyerItem1.setCommodityInfoFileVo(commodityInfoService.selectByCommodityId(buyerItem1.getCommodityInfoFileVo().getCommodityId()));
                        }
                    }
                }
                //5, 将购物车装满, 前面只是将skuId装进购物车, 这里还需要查出sku详情
                return RetResponse.makeOKRsp(buyerCart);
            }else{
                return RetResponse.makeErrRsp("未登录，请先登录");
            }
        } else {
            return RetResponse.makeErrRsp("未找到对应商品");
        }
    }

    @ResponseBody
    @PostMapping(value = "/shopping/delCart")
    public RetResult<Boolean> delCart(@RequestBody JSONObject obj, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
        if (obj.containsKey("commodityId") && !StringUtils.isBlank(obj.getString("commodityId"))) {
            String commodityId = obj.getString("commodityId");
            String username = (String) request.getAttribute("username");
            BuyerCart buyerCart = new BuyerCart();
            if (!StringUtils.isBlank(username)) {
                //删除购物车Redis
                mallOrderInfoService.delBuyerCartToRedis(commodityId, username);
                //取出Redis中的购物车
                Boolean b = mallOrderInfoService.selectBuyerCartFromRedisById(username, commodityId);
                return RetResponse.makeOKRsp(b);
            } else {
                return RetResponse.makeErrRsp("未登录，请先登录");
            }
        } else {
            return RetResponse.makeErrRsp("未找到对应商品");
        }
    }

    @ResponseBody
    @PostMapping(value = "/shopping/selectBuyerCart")
    public RetResult<BuyerCart> selectBuyerCart(HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException {
            String username = (String) request.getAttribute("username");
            BuyerCart buyerCart = new BuyerCart();
            if (!StringUtils.isBlank(username)) {
                //取出Redis中的购物车
                buyerCart = mallOrderInfoService.selectBuyerCartFromRedis(username);
                if(null != buyerCart){
                    List<BuyerItem> items1 = buyerCart.getItems();
                    if (items1.size() > 0) {
                        //只有购物车中有购物项, 才可以将sku相关信息加入到购物项中
                        for (BuyerItem buyerItem1 : items1) {
                            buyerItem1.setCommodityInfoFileVo(commodityInfoService.selectByCommodityId(buyerItem1.getCommodityInfoFileVo().getCommodityId()));
                        }
                    }
                }
                //5, 将购物车装满, 前面只是将skuId装进购物车, 这里还需要查出sku详情
                return RetResponse.makeOKRsp(buyerCart);
            }else{
                return RetResponse.makeErrRsp("未登录，请先登录");
            }
    }
//    @ResponseBody
//    @PostMapping(value = "/shopping/buyerCart")
//    public RetResult<String> buyerCart(@RequestBody JSONObject obj, HttpServletRequest request,
//                                       HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException {
//        if (obj.containsKey("commodityId") && !StringUtils.isBlank(obj.getString("commodityId"))) {
//            String commodityId = obj.getString("commodityId");
//            //将对象转换成json字符串/json字符串转成对象
//            ObjectMapper om = new ObjectMapper();
//            om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//            BuyerCart buyerCart = null;
//            //1,获取Cookie中的购物车
//            Cookie[] cookies = request.getCookies();
//            if (null != cookies && cookies.length > 0) {
//                for (Cookie cookie : cookies) {
//                    //
//                    if (Constants.BUYER_CART.equals(cookie.getName())) {
//                        //购物车 对象 与json字符串互转
//                        String decode = URLDecoder.decode(cookie.getValue(), "utf-8");
//                        buyerCart = om.readValue(decode, BuyerCart.class);
//                        break;
//                    }
//                }
//            }
//
//            //2,Cookie中没有购物车, 创建购物车对象
//            if (null == buyerCart) {
//                buyerCart = new BuyerCart();
//            }
//
//            //3, 将当前款商品追加到购物车
//            if (null != commodityId) {
//                CommodityInfoFileVo commodityInfoFileVo = new CommodityInfoFileVo();
//                commodityInfoFileVo.setCommodityId(commodityId);
//                BuyerItem buyerItem = new BuyerItem();
//                buyerItem.setCommodityInfoFileVo(commodityInfoFileVo);
//                //设置数量
//                //buyerItem.setAmount(amount);
//                //添加购物项到购物车
//                buyerCart.addItem(buyerItem);
//            }
//
//            //排序  倒序
//            List<BuyerItem> items = buyerCart.getItems();
//            Collections.sort(items, new Comparator<BuyerItem>() {
//
//                @Override
//                public int compare(BuyerItem o1, BuyerItem o2) {
//                    return -1;
//                }
//
//            });
//
//            //前三点 登录和非登录做的是一样的操作, 在第四点需要判断
//            String username = (String) request.getAttribute("username");
//            if (!StringUtils.isBlank(username)) {
//                //登录了
//                //4, 将购物车追加到Redis中
//                mallOrderInfoService.insertBuyerCartToRedis(buyerCart, username);
//                //5, 清空Cookie 设置存活时间为0, 立马销毁
//                Cookie cookie = new Cookie(Constants.BUYER_CART, null);
//                cookie.setPath("/");
//                cookie.setMaxAge(-0);
//                response.addCookie(cookie);
//            } else {
//                //未登录
//                //4, 保存购物车到Cookie中
//                //将对象转换成json格式
//                Writer w = new StringWriter();
//                om.writeValue(w, buyerCart);
//                String encode = URLEncoder.encode(w.toString(), "utf-8");
//                Cookie cookie = new Cookie(Constants.BUYER_CART, encode);
//                //设置path是可以共享cookie
//                cookie.setPath("/");
//                //设置Cookie过期时间: -1 表示关闭浏览器失效  0: 立即失效  >0: 单位是秒, 多少秒后失效
//                cookie.setMaxAge(24 * 60 * 60);
//                //5,Cookie写会浏览器
//                response.addCookie(cookie);
//            }
//            return RetResponse.makeOKRsp();
//        }else {
//            return RetResponse.makeErrRsp("未找到对应商品");
//        }
//    }

    //去购物车结算, 这里有两个地方可以直达: 1,在商品详情页 中点击加入购物车按钮  2, 直接点击购物车按钮
//    @ResponseBody
//    @RequestMapping(value = "/shopping/toCart")
//    public RetResult<BuyerCart> toCart(HttpServletRequest request,
//                         HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException {
//        //将对象转换成json字符串/json字符串转成对象
//        ObjectMapper om = new ObjectMapper();
//        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        BuyerCart buyerCart = null;
//        //1,获取Cookie中的购物车
//        Cookie[] cookies = request.getCookies();
//        if (null != cookies && cookies.length > 0) {
//            for (Cookie cookie : cookies) {
//                //
//                if (Constants.BUYER_CART.equals(cookie.getName())) {
//                    //购物车 对象 与json字符串互转
//                    String decode = URLDecoder.decode(cookie.getValue(), "utf-8");
//                    buyerCart = om.readValue(decode, BuyerCart.class);
//                    break;
//                }
//            }
//        }
//
//        //判断是否登录
//        String username = (String) request.getAttribute("username");
//        if (!StringUtils.isBlank(username)) {
//            //登录了
//            //2, 购物车 有东西, 则将购物车的东西保存到Redis中
//            if (null != buyerCart) {
//                mallOrderInfoService.insertBuyerCartToRedis(buyerCart, username);
//                //清空Cookie 设置存活时间为0, 立马销毁
//                Cookie cookie = new Cookie(Constants.BUYER_CART, null);
//                cookie.setPath("/");
//                cookie.setMaxAge(-0);
//                response.addCookie(cookie);
//            }
//            //3, 取出Redis中的购物车
//            buyerCart = mallOrderInfoService.selectBuyerCartFromRedis(username);
//        }
//
//
//        //4, 没有 则创建购物车
//        if (null == buyerCart) {
//            buyerCart = new BuyerCart();
//        }
//
//        //5, 将购物车装满, 前面只是将skuId装进购物车, 这里还需要查出sku详情
//        List<BuyerItem> items = buyerCart.getItems();
//        if (items.size() > 0) {
//            //只有购物车中有购物项, 才可以将sku相关信息加入到购物项中
//            for (BuyerItem buyerItem : items) {
//                buyerItem.setCommodityInfoFileVo(commodityInfoService.selectByCommodityId(buyerItem.getCommodityInfoFileVo().getCommodityId()));
//            }
//        }
//
//        //跳转购物页面
//        return RetResponse.makeOKRsp(buyerCart);
//    }
}