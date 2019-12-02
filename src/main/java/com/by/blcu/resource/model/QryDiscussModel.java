package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "QryDiscussModel",description = "讨论主题列表查询实体")
public class QryDiscussModel implements Serializable {
    private static final long serialVersionUID = 7757050034559001001L;


    @ApiModelProperty(value = "当前页码",example = "1")
    private Integer pageIndex;

    @ApiModelProperty(value = "每页条数",example = "10")
    private Integer pageSize;

    @ApiModelProperty(value = "讨论标题")
    private String title;

    @ApiModelProperty(value = "查询类型，0：查询所有 1：只查询自己的")
    private Integer type;

    @ApiModelProperty(value = "课程id")
    private Integer courseId;

    @ApiModelProperty(value = "模块分类id",example = "2")
    private Integer modelType;
}
