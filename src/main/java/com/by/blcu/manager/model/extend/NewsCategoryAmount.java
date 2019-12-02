package com.by.blcu.manager.model.extend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description= "新闻分类统计")
public class NewsCategoryAmount {
    @ApiModelProperty(value = "一级分类id")
    private String ccId1;
    @ApiModelProperty(value = "二级分类id")
    private String ccId2;
    @ApiModelProperty(value = "所属分类Id")
    private String categoryId;
    @ApiModelProperty(value = "所属分类名称")
    private String categoryName;
    @ApiModelProperty(value = "分类下新闻数")
    private Long amount;
}
