package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "ReplyNode",description = "回复的节点")
public class ReplyNode implements Serializable {

    private static final long serialVersionUID = 3599539562218357694L;

    private Integer discussId;

    private List<ReplyNode> childs;
}
