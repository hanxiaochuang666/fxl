package com.by.blcu.manager.model.sql;

import com.by.blcu.manager.model.ManagerPermission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Set;

/**
 * 机构权限
 */

@ApiModel(description= "机构类")
public class OrgPermission extends ManagerPermission {
    @ApiModelProperty(value = "组织机构编码")
    private String orgCode;

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }
}
