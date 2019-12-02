package com.by.blcu.course.model;

import com.by.blcu.resource.model.BaseViewModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "CourseViewModel",description = "课程管理请求参数")
public class CourseViewModel extends BaseViewModel implements Serializable{

    private static final long serialVersionUID = 94168715455478744L;

    @ApiModelProperty(value = "一级类目id")
    private String categoryOne;

    @ApiModelProperty(value = "二级类目id")
    private String categoryTwo;

    @ApiModelProperty(value = "课程名称")
    private String name;

    @ApiModelProperty(value = "审核状态")
    private Integer status;

    @ApiModelProperty(value = "课程ID 获取模块使用")
    private Integer courseId;


}
