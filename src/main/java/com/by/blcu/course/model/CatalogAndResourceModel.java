package com.by.blcu.course.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "CatalogAndResourceModel",description = "目录以及资源的实体参数")
public class CatalogAndResourceModel implements Serializable {

    private static final long serialVersionUID = -7010895563646580834L;

    @ApiModelProperty(value = "排序字段")
    private Integer sort;

    @ApiModelProperty(value = "目录名称")
    private String name;

    @ApiModelProperty(value = "目录主键id")
    private Integer id;

    @ApiModelProperty("审核结果描述")
    private String checkResult;

    @ApiModelProperty("审核状态")
    private String checkStatus;

    @ApiModelProperty("目录状态")
    private Integer catalogStatus;

    @ApiModelProperty("学习标识，N: 未学习  Y: 已学习")
    private String learnFlag;

    @ApiModelProperty(value = "资源")
    private ResourceModel resourceModel;

    @ApiModelProperty(value = "是否为已审核目录：用于课程目录编辑校验")
    private Boolean isOriginal;

    @ApiModelProperty(value = "子节点")
    private List<CatalogAndResourceModel> nodes;

}
