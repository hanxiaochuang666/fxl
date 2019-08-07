package com.by.blcu.resource.dto;

public class TestResultDetail {
    /**
	 *INTEGER
	 *id
	 */
    private Integer testResultDetailId;

    /**
	 *INTEGER
	 *作答结果id
	 */
    private Integer testResultId;

    /**
	 *INTEGER
	 *试题id
	 */
    private Integer questionId;

    /**
	 *VARCHAR
	 *给出的答案
	 */
    private String giveAnswer;

    /**
	 *INTEGER
	 *获得的分数
	 */
    private Integer score;

    /**
	 *VARCHAR
	 *评语
	 */
    private String comment;

    public Integer getTestResultDetailId() {
        return testResultDetailId;
    }

    public void setTestResultDetailId(Integer testResultDetailId) {
        this.testResultDetailId = testResultDetailId;
    }

    public Integer getTestResultId() {
        return testResultId;
    }

    public void setTestResultId(Integer testResultId) {
        this.testResultId = testResultId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getGiveAnswer() {
        return giveAnswer;
    }

    public void setGiveAnswer(String giveAnswer) {
        this.giveAnswer = giveAnswer == null ? null : giveAnswer.trim();
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment == null ? null : comment.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", testResultDetailId=").append(testResultDetailId);
        sb.append(", testResultId=").append(testResultId);
        sb.append(", questionId=").append(questionId);
        sb.append(", giveAnswer=").append(giveAnswer);
        sb.append(", score=").append(score);
        sb.append(", comment=").append(comment);
        sb.append("]");
        return sb.toString();
    }
}