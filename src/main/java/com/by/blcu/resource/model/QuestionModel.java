package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "QuestionModel",description = "试题对象")
public class QuestionModel implements Serializable {
    
    private static final long serialVersionUID = 8933558368212829442L;

    // 试题序号
    private String order;

    @ApiModelProperty(value = "主键id，试题编辑的时候必传")
    private Integer questionId;

    @ApiModelProperty(value = "类目一级id")
    private String categoryOne;

    @ApiModelProperty(value = "类目二级id")
    private String categoryTwo;

    @ApiModelProperty(value = "课程id")
    private Integer courseId;

    @ApiModelProperty(value = "课程名称",example = "公共英语三级")
    private String courseName;

    @ApiModelProperty(value = "知识点id,多个使用;分割")
    private String knowledgePoints;

    @ApiModelProperty(value = "难度等级(0：无;1:易；2：中；3：难)")
    private Integer difficultyLevel;

    @ApiModelProperty(value = "试题类型编码",example = "DANXUA")
    @NotBlank
    private String questionTypeCode;

    @ApiModelProperty(value = "题干")
    private String questionBody;

    @ApiModelProperty(value = "音频fileId")
    private String questionSound;

    @ApiModelProperty(value = "选项(复用于综合题，表示子题id，使用;分割)",example = "1222;3546;222")
    private String questionOpt;

    @ApiModelProperty(value = "答案")
    private String questionAnswer;

    @ApiModelProperty(value = "解析")
    private String questionResolve;

    @ApiModelProperty(value = "机构id")
    private String orgCode;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建日期的时间戳")
    private String createTimeStr;

    @ApiModelProperty(value = "试题操作类型：-1：表示删除 0：表示更新；1：表示添加")
    private String status;

    @ApiModelProperty(value = "子题列表，综合题使用")
    private List<QuestionModel> modelList;

}
