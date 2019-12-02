package com.by.blcu.course.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CourseCheckQueryModel implements Serializable {
    private static final long serialVersionUID = -4804652574350845060L;

    @ApiModelProperty(value = "课程名称")
    private String name;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "课程类目1")
    private String categoryOne;

    @ApiModelProperty(value = "课程类目2")
    private String categoryTwo;

    @ApiModelProperty(value = "审核状态：1 审核中，2 审核通过， 3 审核未通过")
    private Integer status;

    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty(value = "每页显示数量")
    private Integer limit;

}
