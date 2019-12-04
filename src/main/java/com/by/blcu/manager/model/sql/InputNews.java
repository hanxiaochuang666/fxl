package com.by.blcu.manager.model.sql;

import com.by.blcu.manager.umodel.ManagerPagerModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@ApiModel(description= "新闻内容类")
public class InputNews extends ManagerPagerModel {
    @ApiModelProperty(value = "新闻Id")
    private String newsId;
    @ApiModelProperty(value = "课程一级分类Id")
    private String ccId1;
    @ApiModelProperty(value = "课程二级分类Id")
    private String ccId2;
    @ApiModelProperty(value = "所属分类Id")
    private String categoryId;
    @ApiModelProperty(value = "机构编号")
    private String orgCode;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "状态( 1 置顶+最新，2 置顶，3 最新，4 普通)")
    private Integer status;
    @ApiModelProperty(value = "点击次数")
    private Integer clicks;
    @ApiModelProperty(value = "类型（1 普通；2 推荐；）")
    private Integer type;
    @ApiModelProperty(value = "是否删除")
    private Boolean isDeleted;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;
    @ApiModelProperty(value = "新闻Id列表")
    private List<String> newsIdList;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;
    @ApiModelProperty(value = "修改人")
    private String modifyBy;

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

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
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

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        isDeleted = isDeleted;
    }

    public Date getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(Date createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public List<String> getNewsIdList() {
        return newsIdList;
    }

    public void setNewsIdList(List<String> newsIdList) {
        this.newsIdList = newsIdList;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
    }

}