package com.by.blcu.course.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "CategoryAndModel",description = "初步保存课程数据")
public class CategoryAndModel implements Serializable {

    private static final long serialVersionUID = -2947290314381457330L;

    @NotBlank(message = "课程名称不能为空")
    @ApiModelProperty(value = "课程名称")
    private String name;

    @NotBlank(message = "类目不能为空")
    @ApiModelProperty(value = "一级类目")
    private String categoryOne;

    @NotBlank(message = "二级类目不能为空")
    @ApiModelProperty(value = "二级类目")
    private String categoryTwo;

    @NotNull(message = "关联模块不能为空")
    @ApiModelProperty(value = "关联模块")
    private List<Integer> modelList;

    @ApiModelProperty(value = "课程ID 更新课程模块使用")
    private Integer courseId;

    @ApiModelProperty(value = "课程是否关联商品：用于课程目录编辑判断")
    private Boolean isRelated;

}
