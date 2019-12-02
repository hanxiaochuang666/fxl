package com.by.blcu.manager.model.sql;

import com.by.blcu.manager.umodel.ManagerPagerModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

@Data
public class InputMessage extends ManagerPagerModel {
    @ApiModelProperty(value = "消息Id")
    private String messageId;
    @ApiModelProperty(value = "课程分类")
    private String ccId;
    @ApiModelProperty(value = "商品Id")
    private String commodityId;
    @ApiModelProperty(value = "所属分类Id")
    private String categoryId;
    @ApiModelProperty(value = "机构编号")
    private String orgCode;
    @ApiModelProperty(value = "范围( 1 所有学员，2 按课程分类，3 按课程内容)")
    private Integer scope;
    @ApiModelProperty(value = "消息内容")
    private String content;
    @ApiModelProperty(value = "消息Id列表")
    private List<String> messageIdList;

    //region 删除时使用

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private String modifyBy;

    //endregion

    //region 前端用户

    @ApiModelProperty(value = "消息阅读表Id")
    private String consumId;
    @ApiModelProperty(value = "是否已读")
    private Boolean isRead;
    @ApiModelProperty(value = "用户名")
    private String userName;

    //endregion

}
