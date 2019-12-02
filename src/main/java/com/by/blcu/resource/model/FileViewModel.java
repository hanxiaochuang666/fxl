package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "FileViewModel",description = "课程资料请求参数")
public class FileViewModel extends BaseViewModel implements Serializable {

    private static final long serialVersionUID = -3435938546585407404L;

    @ApiModelProperty(value = "课程ID")
    private String courseId;

    @ApiModelProperty(value = "模块ID")
    private String modelType;

    @ApiModelProperty(value = "目录ID 默认为0表示根目录",example = "0")
    private String catalogId = "0";

    @ApiModelProperty(value = "资源名称")
    private String fileName;

    @ApiModelProperty(value = "资源ID")
    private String resourcesId;

    @ApiModelProperty(value = "文件ID")
    private String fileId;

    @ApiModelProperty(value = "富文本内容")
    private String richText;

}
