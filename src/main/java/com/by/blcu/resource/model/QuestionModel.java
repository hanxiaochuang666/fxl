package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "试题对象")
public class QuestionModel implements Serializable {
    
    private static final long serialVersionUID = 8933558368212829442L;

    @ApiModelProperty(value = "主键id，试题编辑的时候必传")
    private Integer questionId;

    @ApiModelProperty(value = "类目一级id")
    private Integer categoryOne;

    @ApiModelProperty(value = "类目二级id")
    private Integer categoryTwo;

    @ApiModelProperty(value = "课程id")
    private Integer courseId;

    @ApiModelProperty(value = "知识点id,多个使用;分割")
    private String knowledgePoints;

    @ApiModelProperty(value = "难度等级(0：无;1:易；2：中；3：难)")
    private Integer difficultyLevel;

    @ApiModelProperty(value = "试题类型编码")
    private String questionType;

    @ApiModelProperty(value = "题干")
    private String questionBody;

    @ApiModelProperty(value = "音频fileId")
    private String questionSound;

    @ApiModelProperty(value = "选项(复用于综合题，表示子题id，使用;分割)")
    private String questionOpt;

    @ApiModelProperty(value = "答案")
    private String questionAnswer;

    @ApiModelProperty(value = "解析")
    private String questionResolve;

    @ApiModelProperty(value = "机构id")
    private Integer orgId;

    @ApiModelProperty(value = "子题列表，综合题使用")
    private List<QuestionModel> modelList;

}
