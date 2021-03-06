package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class SaveVideoPlayModel implements Serializable {
    private static final long serialVersionUID = -2699457307509682426L;
    @NotNull
    @ApiModelProperty(value = "视频id")
    private Integer videoInfoId;
    @ApiModelProperty(value = "视频名称")
    private String videoInfoName;
    @ApiModelProperty(value = "视频大小,视频上传回调时，必传；视频编辑时，不传")
    private Integer bak2;
    @ApiModelProperty(value = "视频时长(秒),视频上传回调时，必传；视频编辑时，不传")
    private Integer duration;
}
