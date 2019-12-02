package com.by.blcu.manager.umodel;

import com.by.blcu.manager.model.ManagerTeacher;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@ApiModel(description= "名师团队显示类")
@Data
public class TeacherShowModel extends ManagerTeacher {

    /**
     * 机构名称
     */
    @ApiModelProperty(value = "机构名称")
    private String orgName;
}
