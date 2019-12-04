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

/**
 * 日志查询类
 */
@ApiModel(description= "日志查询类")
public class InputLog extends ManagerPagerModel {
    @ApiModelProperty(value = "表Id")
    private String logId;
    @ApiModelProperty(value = "操作人")
    private String optName;
    @ApiModelProperty(value = "操作类型")
    private String optType;
    @ApiModelProperty(value = "操作描述")
    private String optDescription;
    @ApiModelProperty(value = "操作时间-开始")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date optTimeBegin;
    @ApiModelProperty(value = "操作时间-结束")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date optTimeEnd;
    @ApiModelProperty(value = "操作的IP地址")
    private String optIP;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId == null ? null : logId.trim();
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName == null ? null : optName.trim();
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType == null ? null : optType.trim();
    }

    public String getOptDescription() {
        return optDescription;
    }

    public void setOptDescription(String optDescription) {
        this.optDescription = optDescription == null ? null : optDescription.trim();
    }

    public Date getOptTimeBegin() {
        return optTimeBegin;
    }

    public void setOptTimeBegin(Date optTimeBegin) {
        this.optTimeBegin = optTimeBegin;
    }

    public Date getOptTimeEnd() {
        return optTimeEnd;
    }

    public void setOptTimeEnd(Date optTimeEnd) {
        this.optTimeEnd = optTimeEnd;
    }

    public String getOptIP() {
        return optIP;
    }

    public void setOptIP(String optIP) {
        this.optIP = optIP == null ? null : optIP.trim();
    }
}
