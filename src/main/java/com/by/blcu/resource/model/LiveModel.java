package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@ApiModel(value = "LiveModel",description = "直播实体对象")
@Getter
@Setter
@ToString
public class LiveModel extends LiveBaseModel implements Serializable {

    private static final long serialVersionUID = -3665842615042063271L;

    @ApiModelProperty(value = "直播间名称",required = true,example = "测试直播")
    private String roomName;

    @ApiModelProperty(value = "直播间描述",required = true,example = "描述测试")
    private String roomDesc;

    @ApiModelProperty(value = "直播开始时间",required = true,example = "2019-08-26 14:00:00")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String startTime;

    @ApiModelProperty(value = "直播截止时间",required = true,example = "2019-08-26 16:00:00")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private String endTime;

    @ApiModelProperty(value = "房间类型，取默认值:2",example = "2",hidden = true)
    private String roomType = "2";

//    @ApiModelProperty(value = "模板类型，取默认值:2（视频直播+聊天互动+直播问答）",example = "2",hidden = true)
//    private String templateType = "2";

    @ApiModelProperty(value = "直播平台：取默认值：cc",example = "cc",hidden = true)
    private String plat = "cc";

    @ApiModelProperty(value = "验证地址",hidden = true)
    private String checkUrl = "https://www.baidu.com/";

    @ApiModelProperty(value = "直播间id（编辑的时候必填）",example = "k2019081411041917524902")
    private String roomId;

    @ApiModelProperty(value = "目录id",required = true,example = "7")
    private String catalogId;

    @ApiModelProperty(value = "课程id",required = true,example = "1")
    private String courseId;

}
