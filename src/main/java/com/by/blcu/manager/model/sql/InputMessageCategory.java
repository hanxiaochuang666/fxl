package com.by.blcu.manager.model.sql;

import com.by.blcu.manager.umodel.ManagerPagerModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Data
public class InputMessageCategory extends ManagerPagerModel {
    @ApiModelProperty(value = "消息分类Id")
    private String categoryId;
    @ApiModelProperty(value = "课程分类")
    private String ccId;
    @ApiModelProperty(value = "机构编号")
    private String orgCode;
    @ApiModelProperty(value = "分类名称")
    private String categoryName;
    @ApiModelProperty(value = "分类编码")
    private String code;
    @ApiModelProperty(value = "所属父ID(顶级为0)")
    private String parentId;
    @ApiModelProperty(value = "分类深度")
    private Integer classLayer;
    @ApiModelProperty(value = "状态（1 正常；2 停用）")
    private Integer status;

    @ApiModelProperty(value = "消息分类Id列表")
    private List<String> categoryIdList;

    //region 删除时使用

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    private String modifyBy;

    //endregion
}
