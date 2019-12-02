package com.by.blcu.course.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "KnowledgePointNode",description = "目录节点")
public class KnowledgePointNode implements Serializable {

    private static final long serialVersionUID = 9083701279240122528L;

    @ApiModelProperty(value = "排序字段")
    private Integer sort;

    @ApiModelProperty(value = "目录名称")
    private String name;

    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty("目录状态")
    private Integer catalogStatus;

    @ApiModelProperty(value = "子节点")
    private List<KnowledgePointNode> nodes;

    @Override
    public String toString() {
        return "KnowledgePointNode{" +
                "sort=" + sort +
                ", name='" + name + '\'' +
                ", id=" + id +
                ", nodes=" + (null == nodes? "null":nodes.toString()) +
                '}';
    }
}
