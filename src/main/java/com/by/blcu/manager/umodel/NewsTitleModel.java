package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description= "新闻内容")
public class NewsTitleModel {
    @ApiModelProperty(value = "新闻Id")
    private String newsId;
    @ApiModelProperty(value = "课程一级分类Id")
    private String ccId1;
    @ApiModelProperty(value = "课程二级分类Id")
    private String ccId2;
    @ApiModelProperty(value = "所属分类Id")
    private String categoryId;
    @ApiModelProperty(value = "所属分类名称")
    private String categoryName;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "状态( 1 置顶+最新，2 置顶，3 最新，4 普通)")
    private Integer status;
    @ApiModelProperty(value = "机构编号")
    private String orgCode;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "点击次数")
    private Integer clicks;
    @ApiModelProperty(value = "类型（1 普通；2 推荐；）")
    private Integer type;
}
