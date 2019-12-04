package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description= "新闻内容")
public class NewsTitleModel {
    @ApiModelProperty(value = "新闻Id")
    private String newsId;
    @ApiModelProperty(value = "课程一级分类Id")
    private String ccId1;
    @ApiModelProperty(value = "课程二级分类Id")
    private String ccId2;
    @ApiModelProperty(value = "所属分类Id")
    private String categoryId;
    @ApiModelProperty(value = "所属分类名称")
    private String categoryName;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "状态( 1 置顶+最新，2 置顶，3 最新，4 普通)")
    private Integer status;
    @ApiModelProperty(value = "机构编号")
    private String orgCode;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "点击次数")
    private Integer clicks;
    @ApiModelProperty(value = "类型（1 普通；2 推荐；）")
    private Integer type;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId == null ? null : newsId.trim();
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getClicks() {
        return clicks;
    }

    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
