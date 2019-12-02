package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QuestionCountModel {

    private static final long serialVersionUID = -5819802355634475230L;
    @ApiModelProperty(value = "总数")
    private Integer count;

    /*试题类型id*/
    @ApiModelProperty(value = "试题类型")
    private Integer questionTypeId;

    /*类型名称*/
    @ApiModelProperty(value = "类型名称")
    private String name;

    /*是否是客观题（0:是;1:非）*/
    @ApiModelProperty(value = "是否是客观题（0:是;1:非）")
    private Integer isObjective;

    /*编码*/
    @ApiModelProperty(value = "编码")
    private String code;

    /*状态：0：启用；1：禁用*/
    @ApiModelProperty(value = "状态：0：启用；1：禁用")
    private Byte status;
}
