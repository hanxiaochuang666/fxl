package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(value = "ReplyInfoModel" ,description = "回复信息实体")
public class ReplyInfoModel implements Serializable {

    private static final long serialVersionUID = -8499516853054013177L;

    @ApiModelProperty(value = "本条讨论主题的id")
    @NotNull(message = "讨论主题id【resourceId】不能为空！")
    private Integer resourceId;

    @ApiModelProperty(value = "本条回复id")
    private Integer discussId;

    @ApiModelProperty(value = "父类回复id（被回复的那条id），如果是针对主题回复则传 0",example = "0")
    private Integer parentDiscussId;

    @ApiModelProperty(value = "回复内容",example = "我觉得翻译题主要是抓住关键词keyword！")
    @NotBlank(message = "回复内容不能为空！")
    private String replyContent;

    @ApiModelProperty(value = "创建日期")
    private Date createTime;

    @ApiModelProperty(value = "发起者名称")
    private String createUserName;

    @ApiModelProperty(value = "发起者id")
    private Integer createUserId;

    @ApiModelProperty(value = "当前登录者id")
    private Integer nowUserId;

    @ApiModelProperty(value = "发起者头像地址")
    private String createUserHeadUrl;

    @ApiModelProperty(value = "被回复者名称")
    private String replyUserName;

    @ApiModelProperty(value = "被回复者id")
    private Integer replyUserId;

    @ApiModelProperty(value = "是否可编辑 1:可以 0：不可以")
    private Integer canEdit;

    @ApiModelProperty(value = "是否可删除 1:可以 0：不可以")
    private Integer canDelete;

    @ApiModelProperty(value = "被回复者头像地址")
    private String replyUserHeadUrl;

    @ApiModelProperty(value = "回复子列表")
    private List<ReplyInfoModel> childList;

}
