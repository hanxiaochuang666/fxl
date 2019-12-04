package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description= "左侧新闻分类项右侧")
public class NewsLeftCategoryItemModel {
    @ApiModelProperty(value = "新闻分类Id")
    private String categoryId;
    @ApiModelProperty(value = "新闻分类名称")
    private String categoryName;
    @ApiModelProperty(value = "数量")
    private Long amount;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId == null ? null : categoryId.trim();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
