package com.by.blcu.mall.model.payment;

import com.by.blcu.core.utils.MapAndObjectUtils;
import com.by.blcu.core.utils.StringUtils;
import com.by.blcu.core.utils.YeePayUtils;
import lombok.Data;
import lombok.ToString;

import java.lang.reflect.Field;

@Data
@ToString
public class YeePayOrderReq {

    //业务类型
    private String p0_Cmd = "QueryOrdDetail";
    //商户编号
    private String p1_MerId;
    //商户订单号
    private String p2_Order;
    //版本号
    private String pv_Ver = "3.0";
    //查询类型
    private String p3_ServiceType = "2";

    private String hmac_safe;

    private String hmac;


}
