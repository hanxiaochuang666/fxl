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

    public List<String> getTeacherIdList() {
        return teacherIdList;
    }

    public void setTeacherIdList(List<String> teacherIdList) {
        this.teacherIdList = teacherIdList;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId == null ? null : teacherId.trim();
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy == null ? null : modifyBy.trim();
    }
}
