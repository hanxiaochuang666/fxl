package com.by.blcu.course.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "PreLiveInfoModel")
public class PreLiveInfoModel implements Serializable {
    private static final long serialVersionUID = 2698987590866989533L;

    @ApiModelProperty(value = "目录id")
    private Integer catalogId;

    @ApiModelProperty(value = "目录名称")
    private String catalogName;

    @ApiModelProperty(value = "开始直播时间")
    private Date beginTime;

    @ApiModelProperty(value = "结束直播时间")
    private Date endTime;

    @ApiModelProperty(value = "是否正在直播描述")
    private String desc;

    @ApiModelProperty(value = "资源id")
    private Integer resourceId;

    @ApiModelProperty(value = "直播状态: 直播未开始 0； 正在直播 1； 直播已结束 2")
    private Integer status;



}
