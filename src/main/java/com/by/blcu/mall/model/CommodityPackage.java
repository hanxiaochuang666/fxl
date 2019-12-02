package com.by.blcu.mall.model;

import com.by.blcu.mall.vo.ChildCommoditySort;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CommodityPackage implements Serializable {

    private static final long serialVersionUID = 7233994436482492416L;
    /**
     * 套餐商品Id
     */
    private String comCommodityId;

    /**
     * 套餐包含商品List
     */
    private List<ChildCommoditySort> commodityList;

    /**
     * 套餐类型（固定套餐：0，可选套餐：1）
     */
    private String mealType;



}
