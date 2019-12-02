package com.by.blcu.course.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "TaskViewModel",description = "课程作业查询请求参数")
public class TaskViewModel implements Serializable {

    private static final long serialVersionUID = 8110961966010159879L;

    @NotNull(message = "课程ID不能为空")
    @ApiModelProperty(value = "课程ID")
    private Integer courseId;

    @NotNull(message = "模块ID不能为空")
    @ApiModelProperty(value = "模块ID")
    private Integer modelType;

    @ApiModelProperty(value = "目录下资源类别")
    private Integer resourceType;

    @ApiModelProperty(value = "目录ID，0 表示根目录下资源")
    private Integer catalogId = 0;

    @ApiModelProperty(value = "试卷ID 传多个以,号隔开")
    private String testPaperId;

    @ApiModelProperty(value = "作业名称，传入则表示更新作业名称操作")
    private String taskName;

    @ApiModelProperty(value = "资源ID 回显使用")
    private Integer resourcesId;

    @ApiModelProperty(value = "模块ID集合：用于作业试卷查询")
    private List modelTypes;


}
