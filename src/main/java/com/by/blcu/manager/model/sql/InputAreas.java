package com.by.blcu.manager.model.sql;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  省市区查询类
 */
@ApiModel(description= "省市区查询类")
public class InputAreas {
    @ApiModelProperty(value = "编码")
    private String code;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "批音")
    private String pingyin;
    @ApiModelProperty(value = "类型")
    private AreasTypeEnum typeEnum;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPingyin() {
        return pingyin;
    }

    public void setPingyin(String pingyin) {
        this.pingyin = pingyin == null ? null : pingyin.trim();
    }

    public AreasTypeEnum getTypeEnum() {
        return typeEnum;
    }

    public void setTypeEnum(AreasTypeEnum typeEnum) {
        this.typeEnum = typeEnum;
    }
}
