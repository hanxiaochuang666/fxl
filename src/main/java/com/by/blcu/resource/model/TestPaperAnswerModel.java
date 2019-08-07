package com.by.blcu.resource.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TestPaperAnswerModel implements Serializable {
    private static final long serialVersionUID = -4628019963426804256L;
    private Integer questionId;
    private String giveAnswer;
}
