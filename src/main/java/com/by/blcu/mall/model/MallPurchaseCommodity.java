package com.by.blcu.mall.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "mall_purchase_commodity")
public class MallPurchaseCommodity implements Serializable {
    private static final long serialVersionUID = -3138584429691239230L;
    /**
     * 关系ID
     */
    @Id
    @Column(name = "oc_id")
    private String ocId;

    /**
     * 子商品ID
     */
    @Column(name = "child_commodity_id")
    private String childCommodityId;

    /**
     * 子商品排序
     */
    @Column(name = "child_commodity_sort")
    private Integer childCommoditySort;

    /**
     * 父商品ID
     */
    @Column(name = "commodity_id")
    private String commodityId;

    /**
     * 订单ID
     */
    @Column(name = "order_id")
    private String orderId;

    /**
     * 获取关系ID
     *
     * @return oc_id - 关系ID
     */
    public String getOcId() {
        return ocId;
    }

    /**
     * 设置关系ID
     *
     * @param ocId 关系ID
     */
    public void setOcId(String ocId) {
        this.ocId = ocId == null ? null : ocId.trim();
    }

    /**
     * 获取商品ID
     *
     * @return child_commodity_id - 商品ID
     */
    public String getChildCommodityId() {
        return childCommodityId;
    }

    /**
     * 设置商品ID
     *
     * @param childCommodityId 商品ID
     */
    public void setChildCommodityId(String childCommodityId) {
        this.childCommodityId = childCommodityId == null ? null : childCommodityId.trim();
    }

    /**
     * 获取订单ID
     *
     * @return commodity_id - 订单ID
     */
    public String getCommodityId() {
        return commodityId;
    }

    /**
     * 设置订单ID
     *
     * @param commodityId 订单ID
     */
    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId == null ? null : commodityId.trim();
    }

    /**
     * @return order_id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Integer getChildCommoditySort() {
        return childCommoditySort;
    }

    public void setChildCommoditySort(Integer childCommoditySort) {
        this.childCommoditySort = childCommoditySort;
    }
}