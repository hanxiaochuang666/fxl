package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "LiveRetModel")
@Data
public class LiveRetModel implements Serializable {

    private static final long serialVersionUID = -8597139080056929313L;

    @ApiModelProperty(value = "资源类型")
    private String type;

    @ApiModelProperty(value = "资源id")
    private String resourceId;

    @ApiModelProperty(value = "直播间号")
    private String content;

    @ApiModelProperty(value = "目录id")
    private String catalogId;

    @ApiModelProperty(value = "课程id")
    private String courseId;

    @ApiModelProperty("直播开始时间")
    private Date liveStartTime;

    @ApiModelProperty("直播结束时间")
    private Date liveEndTime;

    @ApiModelProperty("直播相关描述")
    private String liveResult;

    @ApiModelProperty(value = "直播状态: 直播未开始 0； 正在直播 1； 直播已结束 2")
    private Integer liveStatus;

    @ApiModelProperty(value = "老师直播间url")
    private String tecUrl;

}
