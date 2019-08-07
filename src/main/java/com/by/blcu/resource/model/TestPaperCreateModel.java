package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel(value = "保存试卷请求参数")
public class TestPaperCreateModel implements Serializable {
    private static final long serialVersionUID = 3563176697295766191L;
    @ApiModelProperty(value = "试卷id")
    private Integer testPaperId;

    @NotBlank(message = "试卷名称name不能为空")
    @ApiModelProperty(value = "试卷名称")
    private String name;

    @Min(value = 1,message = "类目1 categoryOne 非法")
    @ApiModelProperty(value = "类目1主键标识")
    private int categoryOne;

    @Min(value = 1,message = "类目2 categoryTwo 非法")
    @ApiModelProperty(value = "类目1主键标识")
    private int categoryTwo;

    @ApiModelProperty(value = "课程 courseId")
    @Min(value = 1,message = "课程 courseId 非法")
    private int courseId;

    @Range(min = 0,max=1,message = "使用类型useType非法")
    @ApiModelProperty(value = "使用类型0 测试，1作业")
    private int useType;

    @ApiModelProperty(value = "时长(分)")
    private int time;

    @NotBlank(message = "开始时间startTime不能为空")
    @ApiModelProperty(value = "开始日期字符串")
    private String startTimeStr;

    @NotBlank(message = "结束时间endTime不能为空")
    @ApiModelProperty(value = "结束日期字符串")
    private String endTimeStr;

    @Range(min = 0,max=1,message = "是否计分isScore非法")
    @ApiModelProperty(value = "是否计分0计分，1不计分")
    private int isScore;

    @Range(min = 0,max=1,message = "组卷类型formType非法")
    @ApiModelProperty(value = "组卷类型0:人工组卷；1:智能组卷")
    private int formType;

    @Min(value = 1,message = "机构Id orgId 非法")
    @ApiModelProperty(value = "机构Id")
    private int orgId;
}
