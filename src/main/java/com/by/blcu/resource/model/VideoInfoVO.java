package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ToString
public class VideoInfoVO implements Serializable {

    private static final long serialVersionUID = -8460350945039127237L;

    @NotNull(message = "课程ID不能为空")
    @ApiModelProperty(value = "课程ID")
    private Integer courseId;

    @NotNull(message = "视频资源ID不能为空")
    @ApiModelProperty(value = "视频ID")
    private Integer videoInfoId;

    @ApiModelProperty(value = "目录ID，0 表示根模块下资源")
    private Integer catalogId = 0;

    @ApiModelProperty(value = "资源名称")
    private String title;

    @ApiModelProperty(value = "模块ID 目录默认为 1")
    private Integer modelType = 1;

    @ApiModelProperty(value = "资源ID 目录下获取资源")
    private Integer resourcesId;


}
