package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 机构信息
 */
@Data
@ApiModel(description= "机构号以及机构类型")
public class ManagerOrgType implements Serializable {

    @ApiModelProperty(value = "机构号")
    private String orgCode;

    @ApiModelProperty(value = "机构类型：1本部；2教师；3第三方；4租户")
    private String type;

}
