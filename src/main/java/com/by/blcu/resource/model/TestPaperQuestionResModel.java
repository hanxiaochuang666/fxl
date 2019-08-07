package com.by.blcu.resource.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Data
public class TestPaperQuestionResModel implements Serializable {
    private static final long serialVersionUID = -3944580967537227297L;
    private int     questionType;
    private String  questionTypeName;
    private int     score;
    private List<Map<String, Object>> questionLst;
}
