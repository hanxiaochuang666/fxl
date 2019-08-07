package com.by.blcu.resource.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseViewModel implements Serializable {
    private static final long serialVersionUID = -2181421576054100765L;
    @ApiModelProperty(value = "当前页")
    private Integer page;
    @ApiModelProperty(value = "当前页的大小")
    private Integer limit;
    @ApiModelProperty(value = "排序列")
    private String _sort_line;
    @ApiModelProperty(value = "排序方式")
    private String _order_;
}
