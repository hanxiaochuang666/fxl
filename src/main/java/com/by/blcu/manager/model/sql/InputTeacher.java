package com.by.blcu.manager.model.sql;

import com.by.blcu.manager.umodel.ManagerPagerModel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description= "名师团队类")
public class InputTeacher extends ManagerPagerModel {

    @ApiModelProperty(value = "名师团队表Id")
    private String teacherId;

}
