package com.by.blcu.manager.umodel;

import com.by.blcu.manager.model.ManagerPermission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Data
@ApiModel(description= "省市区")
public class AreaModel {
    @ApiModelProperty(value = "编码")
    private String code;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "拼音")
    private String pingyin;
}
