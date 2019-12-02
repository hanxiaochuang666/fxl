package com.by.blcu.manager.umodel;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

/**
 * teacher相关操作类
 */
@Data
@ApiModel(description= "教师")
public class ManagerTeacherModel {
    @ApiModelProperty(value = "教师Id列表")
    private List<String> teacherIdList;
    @ApiModelProperty(value = "教师Id")
    private String teacherId;
    @ApiModelProperty(value = "入驻者组织编码")
    private String orgCode;
    @ApiModelProperty(value = "排序")
    private Integer sort;

    //修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;
    //修改人
    @Column(name = "modify_by")
    private String modifyBy;
}
