package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import javax.persistence.*;

@Table(name = "web_news")
@ApiModel(description= "用户")
public class WebNews {
    /**
     *新闻Id
     */
    @Id
    @Column(name = "news_id")
    @ApiModelProperty(value = "新闻Id")
    private String newsId;

    /**
     *课程一级分类Id
     */
    @Column(name = "cc_id1")
    @ApiModelProperty(value = "课程一级分类Id")
    private String ccId1;

    /**
     *课程二级分类Id
     */
    @Column(name = "cc_id2")
    @ApiModelProperty(value = "课程二级分类Id")
    private String ccId2;

    /**
     *所属分类Id
     */
    @Column(name = "category_id")
    @ApiModelProperty(value = "所属分类Id")
    private String categoryId;

    /**
     *机构编号
     */
    @Column(name = "org_code")
    @ApiModelProperty(value = "机构编号")
    private String orgCode;

    /**
     *标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     *作者
     */
    @ApiModelProperty(value = "作者")
    private String author;

    /**
     *状态( 1 置顶+最新，2 置顶，3 最新，4 普通)
     */
    @ApiModelProperty(value = "状态( 1 置顶+最新，2 置顶，3 最新，4 普通)")
    private Integer status;

    /**
     *附件地址（多个用逗号隔开）
     */
    @Column(name = "file_url")
    @ApiModelProperty(value = "附件地址（多个用逗号隔开）")
    private String fileUrl;

    /**
     *描述
     */
    @ApiModelProperty(value = "描述")
    private String description;

    /**
     *排序
     */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
     *是否删除
     */
    @Column(name = "is_deleted")
    @ApiModelProperty(value = "是否删除")
    private Boolean isDeleted;

    /**
     *添加时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    /**
     *添加人
     */
    @Column(name = "create_by")
    @ApiModelProperty(value = "添加人")
    private String createBy;

    /**
     *修改时间
     */
    @Column(name = "modify_time")
    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    /**
     *点击次数
     */
    @ApiModelProperty(value = "点击次数")
    private Integer clicks;

    /**
     *修改人
     */
    @Column(name = "modify_by")
    @ApiModelProperty(value = "修改人")
    private String modifyBy;

    /**
     *类型（1 普通；2 推荐；）
     */
    @ApiModelProperty(value = "类型（1 普通；2 推荐；）")
    private Integer type;

    /**
     *新闻内容
     */
    @ApiModelProperty(value = "新闻内容")
    private String content;

    /**
     * 获取新闻Id
     *
     * @return news_id - 新闻Id
     */
    public String getNewsId() {
        return newsId;
    }

    /**
     * 设置新闻Id
     *
     * @param newsId 新闻Id
     */
    public void setNewsId(String newsId) {
        this.newsId = newsId == null ? null : newsId.trim();
    }

    /**
     * @return cc_id1
     */
    public String getCcId1() {
        return ccId1;
    }

    /**
     * @param ccId1
     */
    public void setCcId1(String ccId1) {
        this.ccId1 = ccId1 == null ? null : ccId1.trim();
    }

    /**
     * 获取课程二级分类Id
     *
     * @return cc_id2 - 课程二级分类Id
     */
    public String getCcId2() {
        return ccId2;
    }

    /**
     * 设置课程二级分类Id
     *
     * @param ccId2 课程二级分类Id
     */
    public void setCcId2(String ccId2) {
        this.ccId2 = ccId2 == null ? null : ccId2.trim();
    }

    /**
     * 获取所属分类Id
     *
     * @return category_id - 所属分类Id
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * 设置所属分类Id
     *
     * @param categoryId 所属分类Id
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId == null ? null : categoryId.trim();
    }

    /**
     * 获取机构编号
     *
     * @return org_code - 机构编号
     */
    public String getOrgCode() {
        return orgCode;
    }

    /**
     * 设置机构编号
     *
     * @param orgCode 机构编号
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取作者
     *
     * @return author - 作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置作者
     *
     * @param author 作者
     */
    public void setAuthor(String author) {
        this.author = author == null ? null : author.trim();
    }

    /**
     * 获取状态( 1 置顶+最新，2 置顶，3 最新，4 普通)
     *
     * @return status - 状态( 1 置顶+最新，2 置顶，3 最新，4 普通)
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态( 1 置顶+最新，2 置顶，3 最新，4 普通)
     *
     * @param status 状态( 1 置顶+最新，2 置顶，3 最新，4 普通)
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取附件地址（多个用逗号隔开）
     *
     * @return file_url - 附件地址（多个用逗号隔开）
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * 设置附件地址（多个用逗号隔开）
     *
     * @param fileUrl 附件地址（多个用逗号隔开）
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    /**
     * 获取描述
     *
     * @return description - 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置描述
     *
     * @param description 描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取是否删除
     *
     * @return is_deleted - 是否删除
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置是否删除
     *
     * @param isDeleted 是否删除
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取添加时间
     *
     * @return create_time - 添加时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置添加时间
     *
     * @param createTime 添加时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取添加人
     *
     * @return create_by - 添加人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置添加人
     *
     * @param createBy 添加人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取点击次数
     *
     * @return clicks - 点击次数
     */
    public Integer getClicks() {
        return clicks;
    }

    /**
     * 设置点击次数
     *
     * @param clicks 点击次数
     */
    public void setClicks(Integer clicks) {
        this.clicks = clicks;
    }

    /**
     * 获取修改人
     *
     * @return modify_by - 修改人
     */
    public String getModifyBy() {
        return modifyBy;
    }

    /**
     * 设置修改人
     *
     * @param modifyBy 修改人
     */
    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
    }

    /**
     * 获取类型（1 普通；2 推荐；）
     *
     * @return type - 类型（1 普通；2 推荐；）
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型（1 普通；2 推荐；）
     *
     * @param type 类型（1 普通；2 推荐；）
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 获取新闻内容
     *
     * @return content - 新闻内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置新闻内容
     *
     * @param content 新闻内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}