package com.by.blcu.manager.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import javax.persistence.*;

@Data
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
     *用户名
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

}