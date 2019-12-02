package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SyncTestPaperModel implements Serializable {
    private static final long serialVersionUID = 6818860546245376403L;
    @NotNull
    @ApiModelProperty(value = "课程id")
    private Integer courseId;
    @NotNull
    @ApiModelProperty(value = "学生id")
    private Integer studentId;
}
