package com.by.blcu.course.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ResourceModel implements Serializable {
    private static final long serialVersionUID = 4131936548145554818L;

    @ApiModelProperty(value = "资源id")
    private Integer resourcesId;

    @ApiModelProperty(value = "资源类型：（0：测试；1：作业；2：视频；3：直播；4：讨论；5：问答；6：文档；7：文本；8：资料）")
    private Integer resourceType;

    @ApiModelProperty(value = "审核状态")
    private String checkStatus;

    @ApiModelProperty("审核结果描述")
    private String checkResult;

    @ApiModelProperty("资源关联的标题")
    private String title;

    @ApiModelProperty("资源关联的内容")
    private String content;

    @ApiModelProperty("直播开始时间")
    private Date liveStartTime;

    @ApiModelProperty("直播结束时间")
    private Date liveEndTime;

    @ApiModelProperty("直播相关描述")
    private String liveResult;

    @ApiModelProperty(value = "直播状态: 直播未开始 0； 正在直播 1； 直播已结束 2")
    private Integer liveStatus;


}
