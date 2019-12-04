package com.by.blcu.manager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.*;

@Table(name = "web_message_consum")
@ApiModel(description= "消息阅读表")
public class WebMessageConsum {
    /**
     *消息阅读表Id
     */
    @Id
    @Column(name = "consum_id")
    @ApiModelProperty(value = "消息阅读表Id")
    private String consumId;

    /**
     *消息Id
     */
    @Column(name = "message_id")
    @ApiModelProperty(value = "消息Id")
    private String messageId;

    /**
     *用户表Id
     */
    @Column(name = "user_id")
    @ApiModelProperty(value = "用户表Id")
    private String userId;

    /**
     *用户名
     */
    @Column(name = "user_name")
    @ApiModelProperty(value = "用户名")
    private String userName;

    /**
     *是否已读
     */
    @Column(name = "is_read")
    @ApiModelProperty(value = "是否已读")
    private Boolean isRead;

    /**
     *创建时间
     */
    @Column(name = "create_time")
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 获取消息阅读表Id
     *
     * @return consum_id - 消息阅读表Id
     */
    public String getConsumId() {
        return consumId;
    }

    /**
     * 设置消息阅读表Id
     *
     * @param consumId 消息阅读表Id
     */
    public void setConsumId(String consumId) {
        this.consumId = consumId == null ? null : consumId.trim();
    }

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
     * 获取用户表Id
     *
     * @return user_id - 用户表Id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户表Id
     *
     * @param userId 用户表Id
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 获取用户名
     *
     * @return user_name - 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * 获取是否已读
     *
     * @return is_read - 是否已读
     */
    public Boolean getIsRead() {
        return isRead;
    }

    /**
     * 设置是否已读
     *
     * @param isRead 是否已读
     */
    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
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
}