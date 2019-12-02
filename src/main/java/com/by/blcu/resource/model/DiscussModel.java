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
@ApiModel(value = "DiscussModel" ,description = "讨论主题实体")
public class DiscussModel implements Serializable {

    private static final long serialVersionUID = -8850915820802846337L;

    @ApiModelProperty(value = "本条讨论主题的id")
    private Integer resourceId;

    @ApiModelProperty(value = "课程id，必填")
    @NotNull
    private Integer courseId;

    @ApiModelProperty(value = "目录id")
    private Integer catalogId;

    @ApiModelProperty(value = "模块ID",example = "2")
    private Integer modelType;

    @ApiModelProperty(value = "讨论标题，必填",example = "关于6级翻译题的做题技巧讨论")
    @NotBlank
    private String discussTitle;

    @ApiModelProperty(value = "讨论内容，必填",example = "同学们可以说一下自己做翻译题时的想法")
    @NotBlank
    private String discussContent;

    @ApiModelProperty(value = "创建人id")
    private Integer createUserId;

    @ApiModelProperty(value = "创建人名称")
    private String createUserName;

    @ApiModelProperty(value = "创建人头像")
    private String userHeadUrl;

    @ApiModelProperty(value = "创建日期")
    private Date createTime;

    @ApiModelProperty(value = "回复条数")
    private Long replyCount;

    @ApiModelProperty(value = "回复列表")
    private List<ReplyInfoModel> replyList;


}
