package com.by.blcu.resource.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel(value = "QuestionType",description = "试题类型实体")
public class QuestionType implements Serializable {

    private static final long serialVersionUID = -4764627332720144457L;

    /*试题类型id*/
    @ApiModelProperty(value = "试题类型")
    private Integer questionTypeId;

    /*类型名称*/
    @ApiModelProperty(value = "类型名称")
    private String name;

    /*是否是客观题（0:是;1:非）*/
    @ApiModelProperty(value = "是否是客观题（0:是;1:非）")
    private Integer isObjective;

    /*编码*/
    @ApiModelProperty(value = "编码")
    private String code;

    /*状态：0：启用；1：禁用*/
    @ApiModelProperty(value = "状态：0：启用；1：禁用")
    private Byte status;

    public Integer getQuestionTypeId() {
        return questionTypeId;
    }

    public void setQuestionTypeId(Integer questionTypeId) {
        this.questionTypeId = questionTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
    public Integer getIsObjective() {
        return isObjective;
    }

    public void setIsObjective(Integer isObjective) {
        this.isObjective = isObjective;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", questionTypeId=").append(questionTypeId);
        sb.append(", name=").append(name);
        sb.append(", isObjective=").append(isObjective);
        sb.append(", code=").append(code);
        sb.append(", status=").append(status);
        sb.append("]");
        return sb.toString();
    }
}