package com.by.blcu.resource.dto;

public class QuestionType {
    /**
	 *INTEGER
	 *试题类型id
	 */
    private Integer questionTypeId;

    /**
	 *VARCHAR
	 *类型名称
	 */
    private String name;

    /**
	 *TINYINT
	 *是否是客观题（0:是;1:非）
	 */
    private Integer isObjective;

    /**
	 *VARCHAR
	 *编码
	 */
    private String code;

    /**
	 *TINYINT
	 *状态：0：启用；1：禁用
	 */
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