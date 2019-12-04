package com.by.blcu.manager.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 机构信息
 */
@ApiModel(description= "机构号以及机构类型")
public class ManagerOrgType implements Serializable {

    @ApiModelProperty(value = "机构号")
    private String orgCode;

    @ApiModelProperty(value = "机构类型：1本部；2教师；3第三方；4租户")
    private String type;

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
}
