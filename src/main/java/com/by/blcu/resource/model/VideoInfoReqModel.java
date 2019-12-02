package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class VideoInfoReqModel extends BaseViewModel implements Serializable {
    private static final long serialVersionUID = -3149057828184471954L;
    @ApiModelProperty(value = "视频名称")
    private String videoNameLike;
}
