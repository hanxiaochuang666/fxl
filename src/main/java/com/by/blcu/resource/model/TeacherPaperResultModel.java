package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "TeacherPaperResultModel",description = "教师评卷结果")
public class TeacherPaperResultModel implements Serializable {

    private static final long serialVersionUID = 50585745616570153L;

    @ApiModelProperty(value = "试卷id")
    private Integer paperId;

    @ApiModelProperty(value = "学生id")
    private Integer studentId;

    @ApiModelProperty(value = "教师评论结果列表")
    private List<TeacherCorrectResultModel> resultList;
}
