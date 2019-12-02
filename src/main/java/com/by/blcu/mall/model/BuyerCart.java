package com.by.blcu.mall.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BuyerCart implements Serializable {

    private static final long serialVersionUID = 903818974249434550L;
    /**
     * 购物车
     */

    //商品结果集
    private List<BuyerItem> items = new ArrayList<BuyerItem>();

    //添加购物项到购物车
    public void addItem(BuyerItem item) {
        //判断是否包含同款
        if (!items.contains(item)) {
            items.add(item);
        }
    }

    public List<BuyerItem> getItems() {
        return items;
    }

    public void setItems(List<BuyerItem> items) {
        this.items = items;
    }


    //小计
    //商品数量
    @JsonIgnore
    public Integer getProductAmount() {
        Integer result = 0;
        //计算
        for (BuyerItem buyerItem : items) {
            result += buyerItem.getAmountNum();
        }
        return result;
    }

    //商品金额
    @JsonIgnore
    public Double getProductPrice() {
        double result = 0;
        //计算
        for (BuyerItem buyerItem : items) {
            if(null != buyerItem.getCommodityInfoFileVo().getPrice()){
                result += buyerItem.getAmountNum() * buyerItem.getCommodityInfoFileVo().getPrice();
            }
        }
        return result;
    }

}
