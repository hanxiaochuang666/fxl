package com.by.blcu.manager.umodel;

import com.by.blcu.manager.model.SsoUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description= "用户扩展信息")
public class SsoUserExtend extends SsoUser {
    @ApiModelProperty(value = "是否是教师")
    private Boolean IsTeacher;

    public Boolean getIsTeacher() {
        return IsTeacher;
    }

    public void setIsTeacher(Boolean IsTeacher) {
        this.IsTeacher = IsTeacher;
    }
}
