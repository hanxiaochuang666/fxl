package com.by.blcu.manager.model.extend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description= "新闻分类统计")
public class NewsCategoryAmount {
    @ApiModelProperty(value = "一级分类id")
    private String ccId1;
    @ApiModelProperty(value = "二级分类id")
    private String ccId2;
    @ApiModelProperty(value = "所属分类Id")
    private String categoryId;
    @ApiModelProperty(value = "所属分类名称")
    private String categoryName;
    @ApiModelProperty(value = "分类下新闻数")
    private Long amount;

    public String getCcId1() {
        return ccId1;
    }

    public void setCcId1(String ccId1) {
        this.ccId1 = ccId1 == null ? null : ccId1.trim();
    }

    public String getCcId2() {
        return ccId2;
    }

    public void setCcId2(String ccId2) {
        this.ccId2 = ccId2 == null ? null : ccId2.trim();
    }

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
