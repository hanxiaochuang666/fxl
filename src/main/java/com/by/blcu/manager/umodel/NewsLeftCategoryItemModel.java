package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description= "左侧新闻分类项右侧")
public class NewsLeftCategoryItemModel {
    @ApiModelProperty(value = "新闻分类Id")
    private String categoryId;
    @ApiModelProperty(value = "新闻分类名称")
    private String categoryName;
    @ApiModelProperty(value = "数量")
    private Long amount;
}
