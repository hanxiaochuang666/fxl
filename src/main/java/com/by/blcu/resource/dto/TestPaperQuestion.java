package com.by.blcu.resource.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "试卷组题参数")
public class TestPaperQuestion {
    /**
	 *INTEGER
	 *id
	 */
    @ApiModelProperty(value = "主键id")
    private Integer testPaperQuestionId;

    /**
	 *INTEGER
	 *试卷id
	 */
    @ApiModelProperty(value = "试卷id")
    private Integer testPagerId;

    /**
	 *INTEGER
	 *试题id
	 */
    @ApiModelProperty(value = "试题id")
    private Integer questionId;

    /**
	 *INTEGER
	 *排序
	 */
    @ApiModelProperty(value = "排序")
    private Integer sort;

    /**
	 *VARCHAR
	 *解析
	 */
    @ApiModelProperty(value = "解析")
    private String resolve;

    public Integer getTestPaperQuestionId() {
        return testPaperQuestionId;
    }

    public void setTestPaperQuestionId(Integer testPaperQuestionId) {
        this.testPaperQuestionId = testPaperQuestionId;
    }

    public Integer getTestPagerId() {
        return testPagerId;
    }

    public void setTestPagerId(Integer testPagerId) {
        this.testPagerId = testPagerId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getResolve() {
        return resolve;
    }

    public void setResolve(String resolve) {
        this.resolve = resolve == null ? null : resolve.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", testPaperQuestionId=").append(testPaperQuestionId);
        sb.append(", testPagerId=").append(testPagerId);
        sb.append(", questionId=").append(questionId);
        sb.append(", sort=").append(sort);
        sb.append(", resolve=").append(resolve);
        sb.append("]");
        return sb.toString();
    }
}