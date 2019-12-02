package com.by.blcu.mall.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ChildCommoditySort implements Serializable {

    private static final long serialVersionUID = -6732290022944972607L;
    /**
     * 套餐商品Id
     */
    private String commodityId;

    /**
     * 套餐类型(单一课程：0，套餐：1)
     */
    private Integer childCommoditySort;



}
