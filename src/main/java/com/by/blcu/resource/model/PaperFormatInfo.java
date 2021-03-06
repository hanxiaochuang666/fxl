package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.io.Serializable;


@Data
public class PaperFormatInfo implements Serializable {
    private static final long serialVersionUID = -4863899546470091392L;
    @ApiModelProperty(value = "testPaperFormatId")
    private Integer testPaperFormatId;

    @Min(value = 1,message = "试题类型 questionType 非法")
    @ApiModelProperty(value = "试题类型type")
    private int questionType;

    @Min(value = 0,message = "试题数量 questionNum 非法")
    @ApiModelProperty(value = "试题数量")
    private int questionNum;

    @Min(value = 1,message = "试题分数 questionSpec 非法")
    @ApiModelProperty(value = "试题分数")
    private int questionSpec;
}
