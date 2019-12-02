package com.by.blcu.resource.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TestPaperQuestionModel implements Serializable {
    private static final long serialVersionUID = -3982462338426345266L;
    private String  testPaperName;
    private String  dateTimeStr;
    private Integer totalNum;
    private Integer haveAnswerTotal;
    private Integer totalScore;
    private Integer time;
    private String  useTimeStr;
    private Integer userScore;
    private String objective_score;
    private String subjective_score;
    private int isScore;
    private String  categoryTwoName;
    private Integer checkStatus;
    List<TestPaperQuestionResModel> questionResModelList;
}
