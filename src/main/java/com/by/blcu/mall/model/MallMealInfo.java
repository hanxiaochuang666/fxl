package com.by.blcu.mall.model;

import javax.persistence.*;

@Table(name = "mall_meal_info")
public class MallMealInfo {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 套餐id
     */
    @Column(name = "com_commodity_id")
    private String comCommodityId;

    /**
     * 商品id
     */
    @Column(name = "commodity_id")
    private String commodityId;

    /**
     * 子商品排序
     */
    @Column(name = "child_commodity_sort")
    private Integer childCommoditySort;

    /**
     * 获取id
     *
     * @return id - id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * 获取套餐id
     *
     * @return com_commodity_id - 套餐id
     */
    public String getComCommodityId() {
        return comCommodityId;
    }

    /**
     * 设置套餐id
     *
     * @param comCommodityId 套餐id
     */
    public void setComCommodityId(String comCommodityId) {
        this.comCommodityId = comCommodityId == null ? null : comCommodityId.trim();
    }

    /**
     * 获取商品id
     *
     * @return commodity_id - 商品id
     */
    public String getCommodityId() {
        return commodityId;
    }

    /**
     * 设置商品id
     *
     * @param commodityId 商品id
     */
    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId == null ? null : commodityId.trim();
    }

    public Integer getChildCommoditySort() {
        return childCommoditySort;
    }

    public void setChildCommoditySort(Integer childCommoditySort) {
        this.childCommoditySort = childCommoditySort;
    }
}