package com.by.blcu.resource.dto;

public class TestPaperFormat {
    /**
	 *INTEGER
	 *id
	 */
    private Integer testPaperFormatId;

    /**
	 *INTEGER
	 *试卷id
	 */
    private Integer testPaperId;

    /**
	 *INTEGER
	 *试题数量
	 */
    private Integer questionNum;

    /**
	 *INTEGER
	 *试题类型id
	 */
    private Integer questionType;

    /**
	 *INTEGER
	 *试题分
	 */
    private Integer questionSpec;

    public Integer getTestPaperFormatId() {
        return testPaperFormatId;
    }

    public void setTestPaperFormatId(Integer testPaperFormatId) {
        this.testPaperFormatId = testPaperFormatId;
    }

    public Integer getTestPaperId() {
        return testPaperId;
    }

    public void setTestPaperId(Integer testPaperId) {
        this.testPaperId = testPaperId;
    }

    public Integer getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(Integer questionNum) {
        this.questionNum = questionNum;
    }

    public Integer getQuestionType() {
        return questionType;
    }

    public void setQuestionType(Integer questionType) {
        this.questionType = questionType;
    }

    public Integer getQuestionSpec() {
        return questionSpec;
    }

    public void setQuestionSpec(Integer questionSpec) {
        this.questionSpec = questionSpec;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", testPaperFormatId=").append(testPaperFormatId);
        sb.append(", testPaperId=").append(testPaperId);
        sb.append(", questionNum=").append(questionNum);
        sb.append(", questionType=").append(questionType);
        sb.append(", questionSpec=").append(questionSpec);
        sb.append("]");
        return sb.toString();
    }
}