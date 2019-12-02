package com.by.blcu.manager.umodel;


import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description= "左侧新闻分类")
public class NewsLeftCategoryModel {
    @ApiModelProperty(value = "一级分类id")
    private String ccId1;
    @ApiModelProperty(value = "一级分类名称")
    private String ccName1;
    @ApiModelProperty(value = "二级分类id")
    private String ccId2;
    @ApiModelProperty(value = "二级分类名称")
    private String ccName2;
    @ApiModelProperty(value = "新闻分类列表")
    @JSONField(serialzeFeatures= {SerializerFeature.WriteMapNullValue})
    private List<NewsLeftCategoryItemModel> categoryList;

}
