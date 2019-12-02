package com.by.blcu.resource.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TestPaperFormatLstVo implements Serializable {
    private static final long serialVersionUID = 8549433405189817647L;
    private String questionTypeName;
    private int score;
    private int totalNum;
    private int questionType;
    private String questionTypeCode;
}
