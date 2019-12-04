package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import javax.persistence.*;

@Table(name = "web_message")
@ApiModel(description= "消息提醒表")
public class WebMessage {
    /**
     *消息Id
     */
    @Id
    @Column(name = "message_id")
    @ApiModelProperty(value = "消息Id")
    private String messageId;

    /**
     *课程分类
     */
    @Column(name = "cc_id")
    @ApiModelProperty(value = "课程分类")
    private String ccId;

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
     *范围( 1 所有学员，2 按课程分类，3 按课程内容)
     */
    @ApiModelProperty(value = "范围( 1 所有学员，2 按课程分类，3 按课程内容)")
    private Integer scope;

    /**
     *消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String content;

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
     *修改人
     */
    @Column(name = "modify_by")
    @ApiModelProperty(value = "修改人")
    private String modifyBy;

    /**
     *商品Id
     */
    @Column(name = "commodity_id")
    @ApiModelProperty(value = "商品Id")
    private String commodityId;

    /**
     * 获取消息Id
     *
     * @return message_id - 消息Id
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * 设置消息Id
     *
     * @param messageId 消息Id
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId == null ? null : messageId.trim();
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
     * 获取范围( 1 所有学员，2 按课程分类，3 按课程内容)
     *
     * @return scope - 范围( 1 所有学员，2 按课程分类，3 按课程内容)
     */
    public Integer getScope() {
        return scope;
    }

    /**
     * 设置范围( 1 所有学员，2 按课程分类，3 按课程内容)
     *
     * @param scope 范围( 1 所有学员，2 按课程分类，3 按课程内容)
     */
    public void setScope(Integer scope) {
        this.scope = scope;
    }

    /**
     * 获取消息内容
     *
     * @return content - 消息内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置消息内容
     *
     * @param content 消息内容
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
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
     * 获取商品Id
     *
     * @return commodity_id - 商品Id
     */
    public String getCommodityId() {
        return commodityId;
    }

    /**
     * 设置商品Id
     *
     * @param commodityId 商品Id
     */
    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId == null ? null : commodityId.trim();
    }
}