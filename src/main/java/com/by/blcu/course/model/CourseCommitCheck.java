package com.by.blcu.course.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;

@Data
public class CourseCommitCheck implements Serializable {
    private static final long serialVersionUID = -3805237903739156303L;

    @ApiModelProperty(value = "课程 courseId")
    @Min(value = 1,message = "课程 courseId 非法")
    private int courseId;

    @ApiModelProperty(value = "审核意见")
    private String examineContext;
}
