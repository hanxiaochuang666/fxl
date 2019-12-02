package com.by.blcu.course.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel(value = "CatalogModel",description = "新增目录的实体")
@Data
public class CatalogModel implements Serializable {
    private static final long serialVersionUID = 8595578507178253907L;

    @ApiModelProperty(value = "课程id")
    @NotNull(message = "课程id不能为空")
    private int courseId;

    @ApiModelProperty(value = "父节点id，如果是一级节点则传0")
    @NotNull(message = "节点id不能为空")
    private int parentId;

    @ApiModelProperty(value = "目录名称")
    @NotBlank(message = "目录名称不能为空")
    private String name;
}
