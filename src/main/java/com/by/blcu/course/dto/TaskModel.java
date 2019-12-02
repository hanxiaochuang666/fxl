package com.by.blcu.course.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel(value = "TaskModel",description = "作业测试回显实体")
@Data
public class TaskModel {

    @ApiModelProperty(value = "课程详情ID")
    private Integer courseDetailId;

    @ApiModelProperty(value = "资源ID")
    private Integer resourcesId;

    @ApiModelProperty(value = "作业标题")
    private String title;

    @ApiModelProperty(value = "试卷名称")
    private String name;

    @ApiModelProperty(value = "试卷ID")
    private String testPaperId;

    @ApiModelProperty(value = "审核状态（0：待审核；1::机审不通过；2：机审通过；3：人审不通过；4：人审通过）")
    private Integer checkStatus;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty( value = "开始时间" )
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @ApiModelProperty( value = "结束时间" )
    private Date endTime;





}
