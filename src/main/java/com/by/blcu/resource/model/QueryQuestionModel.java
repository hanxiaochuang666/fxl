package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@ApiModel(value = "查询试题的对象")
public class QueryQuestionModel implements Serializable {

    private static final long serialVersionUID = 5828244451295708074L;

    @ApiModelProperty(value = "类目一级id")
    @NotNull
    private Integer categoryOne;

    @ApiModelProperty(value = "类目二级id")
    @NotNull
    private Integer categoryTwo;

    @ApiModelProperty(value = "课程id")
    @NotNull
    private Integer courseId;

    @ApiModelProperty(value = "知识点id,多个使用;分割  -1 表示全部")
    private String knowledgePoints;

    @ApiModelProperty(value = "难度等级(0：无;1:易；2：中；3：难)  -1表示：全部")
    private Integer difficultyLevel;

    @ApiModelProperty(value = "试题类型编码  -1表示：全部")
    private String questionType;

    @ApiModelProperty(value = "题干")
    private String questionBody;
}
