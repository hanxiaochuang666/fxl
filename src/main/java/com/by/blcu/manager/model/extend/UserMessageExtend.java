package com.by.blcu.manager.model.extend;

import com.by.blcu.manager.model.WebMessage;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@ApiModel(description= "用户消息列表")
public class UserMessageExtend extends WebMessage {
    @ApiModelProperty(value = "消息阅读表Id")
    private String consumId;
    @ApiModelProperty(value = "是否已读")
    private Boolean isRead;

    public String getConsumId() {
        return consumId;
    }

    public void setConsumId(String consumId) {
        this.consumId = consumId == null ? null : consumId.trim();
    }

    public Boolean getisRead() {
        return isRead;
    }

    public void setisRead(Boolean isRead) {
        isRead = isRead;
    }
}
