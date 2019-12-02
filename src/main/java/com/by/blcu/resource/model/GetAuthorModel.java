package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class GetAuthorModel implements Serializable {
    private static final long serialVersionUID = 49163776092513448L;
    @ApiModelProperty(value = "视频文件后缀名")
    private  String extendName;

    @ApiModelProperty(value = "视频文件名字")
    private  String videoName;
}
