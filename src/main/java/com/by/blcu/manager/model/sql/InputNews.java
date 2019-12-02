package com.by.blcu.manager.model.sql;

import com.by.blcu.manager.umodel.ManagerPagerModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Data
@ApiModel(description= "新闻内容")
public class InputNews extends ManagerPagerModel {
    @ApiModelProperty(value = "新闻Id")
    private String newsId;
    @ApiModelProperty(value = "课程一级分类Id")
    private String ccId1;
    @ApiModelProperty(value = "课程二级分类Id")
    private String ccId2;
    @ApiModelProperty(value = "所属分类Id")
    private String categoryId;
    @ApiModelProperty(value = "机构编号")
    private String orgCode;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "状态( 1 置顶+最新，2 置顶，3 最新，4 普通)")
    private Integer status;
    @ApiModelProperty(value = "点击次数")
    private Integer clicks;
    @ApiModelProperty(value = "类型（1 普通；2 推荐；）")
    private Integer type;
    @ApiModelProperty(value = "是否删除")
    private Boolean isDeleted;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeStart;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTimeEnd;
    @ApiModelProperty(value = "新闻Id列表")
    private List<String> newsIdList;

    //region 删除时使用

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private String modifyBy;

    //endregion

}