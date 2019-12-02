package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(value = "CourseAndTestModel",description = "")
public class CourseAndTestModel implements Serializable {
    private static final long serialVersionUID = 5216919984477714465L;

    @ApiModelProperty(value = "已经提交的数量")
    private Long haveCommit;

    @ApiModelProperty(value = "应该提交的数量")
    private Integer shouldCommit;

    @ApiModelProperty(value = "已经批阅的数量")
    private Long haveCorrect;

    @ApiModelProperty(value = "未批阅的数量")
    private Long haveNotCorrect;

    @ApiModelProperty(value = "试卷名称")
    private String paperName;

    @ApiModelProperty(value = "有效开始日期")
    private Date startTime;

    @ApiModelProperty(value = "截止日期")
    private Date endTime;

    @ApiModelProperty(value = "试卷id")
    private Integer paperId;

}
