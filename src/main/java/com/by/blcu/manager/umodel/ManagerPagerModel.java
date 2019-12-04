package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页查询基类
 */
@ApiModel(description= "用户")
public class ManagerPagerModel {
    /**
     *当前页
     */
    @ApiModelProperty(value = "当前页")
    private Integer page;
    /**
     *每页条数
     */
    @ApiModelProperty(value = "每页条数")
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}