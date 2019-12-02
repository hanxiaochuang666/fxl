package com.by.blcu.manager.umodel;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description= "首页分块新闻")
public class CategoryNewsModel {
    @ApiModelProperty(value = "课程分类id")
    private String ccId;
    @ApiModelProperty(value = "分类名")
    private String ccName;
    @ApiModelProperty(value = "父分类id")
    private String parentId;
    @ApiModelProperty(value = "目录级别")
    private Integer level;
    @ApiModelProperty(value = "子节点")
    @JSONField(serialzeFeatures= {SerializerFeature.WriteMapNullValue})
    private List<CategoryNewsModel> children;
    @ApiModelProperty(value = "新闻列表")
    @JSONField(serialzeFeatures= {SerializerFeature.WriteMapNullValue})
    private List<NewsTitleModel> newsList;
}
