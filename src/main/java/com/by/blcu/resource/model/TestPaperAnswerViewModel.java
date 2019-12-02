package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "TestPaperAnswerViewModel",description = "试卷结果保存参数")
public class TestPaperAnswerViewModel implements Serializable {
    private static final long serialVersionUID = -6613536427333166321L;
    @ApiModelProperty(value = "试卷id")
    @Min(value = 1,message = "试卷id")
    private Integer testPaperId;

    @ApiModelProperty(value = "操作选项 0 保存；1 提交")
    @Range(min = 0,max=1,message = "操作选项 0 保存；1 提交")
    private Integer optFlag;    //操作选项 0 保存；1 提交

    @ApiModelProperty(value = "课程ID")
    @Min(value = 1,message = "课程ID")
    private Integer courseId;

    @ApiModelProperty(value = "答案集合")
    private List<TestPaperAnswerModel> TestPaperAnswerLst;
}
