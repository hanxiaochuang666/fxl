package com.by.blcu.manager.umodel;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel(description= "首页分块新闻")
public class CategoryNewsModel {
    @ApiModelProperty(value = "课程分类id")
    private String ccId;
    @ApiModelProperty(value = "分类名")
    private String ccName;
    @ApiModelProperty(value = "父分类id")
    private String parentId;
    @ApiModelProperty(value = "目录级别")
    private Integer level;
    @ApiModelProperty(value = "子节点")
    @JSONField(serialzeFeatures= {SerializerFeature.WriteMapNullValue})
    private List<CategoryNewsModel> children;
    @ApiModelProperty(value = "新闻列表")
    @JSONField(serialzeFeatures= {SerializerFeature.WriteMapNullValue})
    private List<NewsTitleModel> newsList;

    public String getCcId() {
        return ccId;
    }

    public void setCcId(String ccId) {
        this.ccId = ccId == null ? null : ccId.trim();
    }

    public String getCcName() {
        return ccName;
    }

    public void setCcName(String ccName) {
        this.ccName = ccName == null ? null : ccName.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<CategoryNewsModel> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryNewsModel> children) {
        this.children = children;
    }

    public List<NewsTitleModel> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<NewsTitleModel> newsList) {
        this.newsList = newsList;
    }
}
