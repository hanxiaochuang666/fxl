package com.by.blcu.manager.model.sql;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  省市区查询类
 */
@Data
public class InputAreas {
    private String code;
    private String name;
    private String pingyin;
    private AreasTypeEnum typeEnum;
}
