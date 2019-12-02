package com.by.blcu.mall.model;

import com.by.blcu.mall.vo.CommodityInfoFileVo;

import java.io.Serializable;

public class BuyerItem implements Serializable {

    private static final long serialVersionUID = 3111897581409538942L;
    //SKu对象
    private CommodityInfoFileVo commodityInfoFileVo;

    //购买的数量
    private Integer amountNum = 1;

    public CommodityInfoFileVo getCommodityInfoFileVo() {
        return commodityInfoFileVo;
    }

    public void setCommodityInfoFileVo(CommodityInfoFileVo commodityInfoFileVo) {
        this.commodityInfoFileVo = commodityInfoFileVo;
    }

    public Integer getAmountNum() {
        return amountNum;
    }

    public void setAmountNum(Integer amountNum) {
        this.amountNum = amountNum;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((commodityInfoFileVo == null) ? 0 : commodityInfoFileVo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) //比较地址
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BuyerItem other = (BuyerItem) obj;
        if (commodityInfoFileVo == null) {
            if (other.commodityInfoFileVo != null)
                return false;
        } else if (!commodityInfoFileVo.getCommodityId().equals(other.commodityInfoFileVo.getCommodityId()))
            return false;
        return true;
    }
}
