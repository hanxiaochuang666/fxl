package com.by.blcu.mall.vo;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
@Data
@Table(name = "mall_commodity_order")
public class MallCommodityOrderVo implements Serializable {
    private static final long serialVersionUID = 6429381032419911232L;
    /**
     * 关系ID
     */
    @Column(name = "co_id")
    private String coId;

    /**
     * 商品ID
     */
    @Column(name = "commodity_id")
    private String commodityId;

    /**
     * 订单ID
     */
    @Column(name = "order_id")
    private String orderId;

    private CommodityInfoFileVo commodityInfoFileVo;

}