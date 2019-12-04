package com.by.blcu.manager.model.sql;

import com.by.blcu.manager.umodel.ManagerPagerModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@ApiModel(description= "消息阅读类")
public class InputMessageConsum extends ManagerPagerModel {
    @ApiModelProperty(value = "消息阅读表Id")
    private String consumId;
    @ApiModelProperty(value = "消息Id")
    private String messageId;
    @ApiModelProperty(value = "用户表Id")
    private String userId;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "是否已读")
    private Boolean isRead;

    @ApiModelProperty(value = "消息阅读表Id列表")
    private List<String> consumIdList;

    public String getConsumId() {
        return consumId;
    }

    public void setConsumId(String consumId) {
        this.consumId = consumId == null ? null : consumId.trim();
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId == null ? null : messageId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        isRead = isRead;
    }

    public List<String> getConsumIdList() {
        return consumIdList;
    }

    public void setConsumIdList(List<String> consumIdList) {
        this.consumIdList = consumIdList;
    }
}