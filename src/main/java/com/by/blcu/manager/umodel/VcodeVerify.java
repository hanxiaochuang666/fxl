package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(description= "验证验证码类")
public class VcodeVerify {
    /**
     * 所属组织编码
     */
    @ApiModelProperty(value = "邮箱")
    private String email;
    /**
     * 所属组织编码
     */
    @ApiModelProperty(value = "验证码")
    private String code;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }
}
