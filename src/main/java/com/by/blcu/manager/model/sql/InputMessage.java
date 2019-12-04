package com.by.blcu.manager.model.sql;

import com.by.blcu.manager.umodel.ManagerPagerModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@ApiModel(description= "消息类")
public class InputMessage extends ManagerPagerModel {
    @ApiModelProperty(value = "消息Id")
    private String messageId;
    @ApiModelProperty(value = "课程分类")
    private String ccId;
    @ApiModelProperty(value = "商品Id")
    private String commodityId;
    @ApiModelProperty(value = "所属分类Id")
    private String categoryId;
    @ApiModelProperty(value = "机构编号")
    private String orgCode;
    @ApiModelProperty(value = "范围( 1 所有学员，2 按课程分类，3 按课程内容)")
    private Integer scope;
    @ApiModelProperty(value = "消息内容")
    private String content;
    @ApiModelProperty(value = "消息Id列表")
    private List<String> messageIdList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;
    @ApiModelProperty(value = "修改人")
    private String modifyBy;

    @ApiModelProperty(value = "消息阅读表Id")
    private String consumId;
    @ApiModelProperty(value = "是否已读")
    private Boolean isRead;
    @ApiModelProperty(value = "用户名")
    private String userName;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId == null ? null : messageId.trim();
    }

    public String getCcId() {
        return ccId;
    }

    public void setCcId(String ccId) {
        this.ccId = ccId == null ? null : ccId.trim();
    }

    public String getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(String commodityId) {
        this.commodityId = commodityId == null ? null : commodityId.trim();
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

    public Integer getScope() {
        return scope;
    }

    public void setScope(Integer scope) {
        this.scope = scope;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public List<String> getMessageIdList() {
        return messageIdList;
    }

    public void setMessageIdList(List<String> messageIdList) {
        this.messageIdList = messageIdList;
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

    public String getConsumId() {
        return consumId;
    }

    public void setConsumId(String consumId) {
        this.consumId = consumId == null ? null : consumId.trim();
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        isRead = isRead;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }
}
