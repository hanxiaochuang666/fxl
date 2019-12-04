package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import javax.persistence.*;

@Table(name = "web_news_category")
@ApiModel(description= "新闻分类表")
public class WebNewsCategory {
    /**
     *新闻分类Id
     */
    @Id
    @Column(name = "category_id")
    @ApiModelProperty(value = "新闻分类Id")
    private String categoryId;

    /**
     *课程分类
     */
    @Column(name = "cc_id")
    @ApiModelProperty(value = "课程分类")
    private String ccId;

    /**
     *机构编号
     */
    @Column(name = "org_code")
    @ApiModelProperty(value = "机构编号")
    private String orgCode;

    /**
     *分类名称
     */
    @Column(name = "category_name")
    @ApiModelProperty(value = "分类名称")
    private String categoryName;

    /**
     *分类编码
     */
    @ApiModelProperty(value = "分类编码")
    private String code;

    /**
     *所属父ID(顶级为0)
     */
    @Column(name = "parent_id")
    @ApiModelProperty(value = "所属父ID(顶级为0)")
    private String parentId;

    /**
     *分类深度
     */
    @Column(name = "class_layer")
    @ApiModelProperty(value = "分类深度")
    private Integer classLayer;

    /**
     *状态（1 正常；2 停用）
     */
    @ApiModelProperty(value = "状态（1 正常；2 停用）")
    private Integer status;

    /**
     *分类图片地址
     */
    @Column(name = "image_url")
    @ApiModelProperty(value = "分类图片地址")
    private String imageUrl;

    /**
     *链接地址
     */
    @Column(name = "link_url")
    @ApiModelProperty(value = "链接地址")
    private String linkUrl;

    /**
     *排序数字
     */
    @ApiModelProperty(value = "排序数字")
    private Integer sort;

    /**
     *分类介绍
     */
    @ApiModelProperty(value = "分类介绍")
    private String description;

    /**
     *备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     *是否删除
     */
    @Column(name = "is_deleted")
    @ApiModelProperty(value = "是否删除")
    private Boolean isDeleted;

    /**
     *创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     *创建人
     */
    @Column(name = "create_by")
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     *修改时间
     */
    @Column(name = "modify_time")
    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    /**
     *修改人
     */
    @Column(name = "modify_by")
    @ApiModelProperty(value = "修改人")
    private String modifyBy;

    /**
     * 获取新闻分类Id
     *
     * @return category_id - 新闻分类Id
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * 设置新闻分类Id
     *
     * @param categoryId 新闻分类Id
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId == null ? null : categoryId.trim();
    }

    /**
     * 获取课程分类
     *
     * @return cc_id - 课程分类
     */
    public String getCcId() {
        return ccId;
    }

    /**
     * 设置课程分类
     *
     * @param ccId 课程分类
     */
    public void setCcId(String ccId) {
        this.ccId = ccId == null ? null : ccId.trim();
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
     * 获取分类名称
     *
     * @return category_name - 分类名称
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 设置分类名称
     *
     * @param categoryName 分类名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    /**
     * 获取分类编码
     *
     * @return code - 分类编码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置分类编码
     *
     * @param code 分类编码
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 获取所属父ID(顶级为0)
     *
     * @return parent_id - 所属父ID(顶级为0)
     */
    public String getParentId() {
        return parentId;
    }

    /**
     * 设置所属父ID(顶级为0)
     *
     * @param parentId 所属父ID(顶级为0)
     */
    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    /**
     * 获取分类深度
     *
     * @return class_layer - 分类深度
     */
    public Integer getClassLayer() {
        return classLayer;
    }

    /**
     * 设置分类深度
     *
     * @param classLayer 分类深度
     */
    public void setClassLayer(Integer classLayer) {
        this.classLayer = classLayer;
    }

    /**
     * 获取状态（1 正常；2 停用）
     *
     * @return status - 状态（1 正常；2 停用）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态（1 正常；2 停用）
     *
     * @param status 状态（1 正常；2 停用）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取分类图片地址
     *
     * @return image_url - 分类图片地址
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 设置分类图片地址
     *
     * @param imageUrl 分类图片地址
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    /**
     * 获取链接地址
     *
     * @return link_url - 链接地址
     */
    public String getLinkUrl() {
        return linkUrl;
    }

    /**
     * 设置链接地址
     *
     * @param linkUrl 链接地址
     */
    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl == null ? null : linkUrl.trim();
    }

    /**
     * 获取排序数字
     *
     * @return sort - 排序数字
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序数字
     *
     * @param sort 排序数字
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取分类介绍
     *
     * @return description - 分类介绍
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置分类介绍
     *
     * @param description 分类介绍
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取创建人
     *
     * @return create_by - 创建人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人
     *
     * @param createBy 创建人
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
}