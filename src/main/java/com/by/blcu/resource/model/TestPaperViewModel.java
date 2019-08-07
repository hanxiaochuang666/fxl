package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel(value = "试卷查询请求参数")
public class TestPaperViewModel extends BaseViewModel implements Serializable {
    private static final long serialVersionUID = 2867045842060996669L;
    @ApiModelProperty(value = "一级类目id")
    private Integer categoryOne;
    @ApiModelProperty(value = "二级类目id")
    private Integer categoryTwo;
    @ApiModelProperty(value = "课程id")
    private Integer courseId;
    @ApiModelProperty(value = "用途:0 测试，1作业")
    private Integer useType;
    @ApiModelProperty(value = "课程名称")
    private String name;
}
