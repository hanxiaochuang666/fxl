package com.by.blcu.manager.umodel;


import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(description= "左侧新闻分类")
public class NewsLeftCategoryModel {
    @ApiModelProperty(value = "一级分类id")
    private String ccId1;
    @ApiModelProperty(value = "一级分类名称")
    private String ccName1;
    @ApiModelProperty(value = "二级分类id")
    private String ccId2;
    @ApiModelProperty(value = "二级分类名称")
    private String ccName2;
    @ApiModelProperty(value = "新闻分类列表")
    @JSONField(serialzeFeatures= {SerializerFeature.WriteMapNullValue})
    private List<NewsLeftCategoryItemModel> categoryList;

    public String getCcId1() {
        return ccId1;
    }

    public void setCcId1(String ccId1) {
        this.ccId1 = ccId1 == null ? null : ccId1.trim();
    }

    public String getCcName1() {
        return ccName1;
    }

    public void setCcName1(String ccName1) {
        this.ccName1 = ccName1 == null ? null : ccName1.trim();
    }

    public String getCcId2() {
        return ccId2;
    }

    public void setCcId2(String ccId2) {
        this.ccId2 = ccId2 == null ? null : ccId2.trim();
    }

    public String getCcName2() {
        return ccName2;
    }

    public void setCcName2(String ccName2) {
        this.ccName2 = ccName2 == null ? null : ccName2.trim();
    }

    public List<NewsLeftCategoryItemModel> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<NewsLeftCategoryItemModel> categoryList) {
        this.categoryList = categoryList;
    }
}
