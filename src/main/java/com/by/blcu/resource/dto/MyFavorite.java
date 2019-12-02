package com.by.blcu.resource.dto;

import java.util.Date;

public class MyFavorite {
    /**
	 *INTEGER
	 *
	 */
    private Integer id;

    /**
	 *INTEGER
	 *用户Id
	 */
    private Integer userId;

    /**
	 *VARCHAR
	 *商品id
	 */
    private String commodityId;

    /**
	 *TIMESTAMP
	 *收藏时间
	 */
    private Date createTime;

    /**
	 *VARCHAR
	 *备用
	 */
    private String back;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId == null ? null : commodityId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back == null ? null : back.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", commodityId=").append(commodityId);
        sb.append(", createTime=").append(createTime);
        sb.append(", back=").append(back);
        sb.append("]");
        return sb.toString();
    }
}