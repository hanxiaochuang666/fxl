package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel(value = "LiveDetailInfo",description = "直播信息详情")
@Data
public class LiveDetailInfo implements Serializable {

    private static final long serialVersionUID = -3206591484637810268L;
    @ApiModelProperty(value = "助教密码")
    private String assistantPass;

    @ApiModelProperty(value = "学生自动登录地址")
    private String studentAutoLoginUrl;

    @ApiModelProperty(value = "调用cc返回的直播间id")
    private String roomid;

    @ApiModelProperty(value = "助教自动登录地址")
    private String assistantAutoLoginUrl;

    @ApiModelProperty(value = "推流地址")
    private String publishUrl;

    @ApiModelProperty(value = "教师自动登录地址")
    private String teacherAutoLoginUrl;

//    @ApiModelProperty(value = "学生登录初始化地址")
//    private String studentAutoLoginUrlInit;

    @ApiModelProperty(value = "直播间名称")
    private String name;

    @ApiModelProperty(value = "直播开始日期")
    private String startTime;

    @ApiModelProperty(value = "本系统直播间id")
    private String id;

    @ApiModelProperty(value = "直播结束时间")
    private String endTime;

    @ApiModelProperty(value = "直播间描述")
    private String desc;

    @ApiModelProperty(value = "状态码")
    private String status;

    @ApiModelProperty(value = "推流密码")
    private String publisherPass;

}
