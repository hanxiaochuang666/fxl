package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 未读消息统计
 */
@ApiModel(description= "未读消息统计")
public class MessageUnReadModel {
    @ApiModelProperty(value = "未读消息总数")
    private Long total;
    @ApiModelProperty(value = "未读消息详细")
    private List<MessageUnReadDetail> detailList;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<MessageUnReadDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<MessageUnReadDetail> detailList) {
        this.detailList = detailList;
    }
}
