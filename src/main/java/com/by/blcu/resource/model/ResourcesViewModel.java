package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "ResourcesViewModel",description = "目录资源回显")
public class ResourcesViewModel<T> implements Serializable {

    private static final long serialVersionUID = 298268272159881262L;

    @ApiModelProperty(value = "目录ID")
    private String catalogId;

    @ApiModelProperty(value = "资源ID")
    private String resourcesId;

    @ApiModelProperty(value = "资源类型")
    private String type;

    @ApiModelProperty(value = "资源内容（富文本）")
    private String content;

    @ApiModelProperty(value = "资源详情")
    private T data;



}
