package com.by.blcu.mall.model;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "mall_commodity_lecturer")
public class MallCommodityLecturer implements Serializable {
    private static final long serialVersionUID = -4053291851339064201L;
    /**
     * 表ID
     */
    @Id
    @Column(name = "cl_id")
    private String clId;

    /**
     * 商品ID
     */
    @Column(name = "commodity_id")
    private String commodityId;

    /**
     * 教师编号
     */
    private String lecturer;

    /**
     * 获取表ID
     *
     * @return cl_id - 表ID
     */
    public String getClId() {
        return clId;
    }

    /**
     * 设置表ID
     *
     * @param clId 表ID
     */
    public void setClId(String clId) {
        this.clId = clId == null ? null : clId.trim();
    }

    /**
     * 获取商品ID
     *
     * @return commodity_id - 商品ID
     */
    public String getCommodityId() {
        return commodityId;
    }

    /**
     * 设置商品ID
     *
     * @param commodityId 商品ID
     */
    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId == null ? null : commodityId.trim();
    }

    /**
     * 获取教师编号
     *
     * @return lecturer - 教师编号
     */
    public String getLecturer() {
        return lecturer;
    }

    /**
     * 设置教师编号
     *
     * @param lecturer 教师编号
     */
    public void setLecturer(String lecturer) {
        this.lecturer = lecturer == null ? null : lecturer.trim();
    }
}