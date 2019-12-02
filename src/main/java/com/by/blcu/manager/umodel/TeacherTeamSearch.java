package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 *名师团队查询类
 */
@ApiModel(description= "名师团队查询类")
@Data
public class TeacherTeamSearch extends ManagerPagerModel {

     /**
     *所属组织编码
     */
    @Column(name = "org_code")
    @ApiModelProperty(value = "所属组织编码")
    private String orgCode;

    /**
     * 类型（1 普通；2 推荐；）
     */
    @ApiModelProperty(value = "类型（1 普通；2 推荐；）")
    private Integer type;

    @ApiModelProperty(value = "课程分类")
    private String ccId;

    @ApiModelProperty(value = "教师姓名")
    private String teacherName;
}
