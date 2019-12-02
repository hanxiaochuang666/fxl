package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "QueryQuestionModel",description = "查询试题的对象")
public class QueryQuestionModel implements Serializable {

    private static final long serialVersionUID = 5828244451295708074L;

    @ApiModelProperty(value = "类目一级id",example = "007122f868ce425a8fc2f32047adc026")
    private String categoryOne;

    @ApiModelProperty(value = "类目二级id",example = "007122f868ce425a8fc2f32047adc028")
    private String categoryTwo;

    @ApiModelProperty(value = "课程id",example = "1")
    private Integer courseId;

    @ApiModelProperty(value = "知识点id,多个使用;分割  -1 表示全部")
    private String knowledgePoints;

    @ApiModelProperty(value = "难度等级(0：无;1:易；2：中；3：难)  -1表示：全部")
    private Integer difficultyLevel;

    @ApiModelProperty(value = "试题类型多个则使用;分隔")
    private String questionType;

    @ApiModelProperty(value = "题干")
    private String questionBody;

    @ApiModelProperty(value = "每页条数")
    private String pageSize;

    @ApiModelProperty(value = "当前页码")
    private String page;
}
