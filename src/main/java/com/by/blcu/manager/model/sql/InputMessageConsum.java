package com.by.blcu.manager.model.sql;

import com.by.blcu.manager.umodel.ManagerPagerModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Data
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
}