package com.by.blcu.mall.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
@Data
@Table(name = "mall_commodity_order")
public class MallCommodityOrder implements Serializable {
    private static final long serialVersionUID = -4758857484988459122L;
    /**
     * 关系ID
     */
    @Id
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

}