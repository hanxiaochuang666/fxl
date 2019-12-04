package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 消息统计详情
 */
@ApiModel(description= "未读消息统计")
public class MessageUnReadDetail {
    @ApiModelProperty(value = "消息分类Id")
    private String categoryId;
    @ApiModelProperty(value = "未读消息数")
    private Long total;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId == null ? null : categoryId.trim();
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
