package com.by.blcu.course.XWPFFactory.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DocModel implements Serializable {

    /**
     * 题型名称
     **/
    private String questionTypeName;

    /**
     * 总分
     */
    private int totalScore;

    /**
     * 题型在试卷上的排名
     */
    private int sort;

    /**
     * 综合题题干
     */
    private String synthesisStr;

    /**
     * 配对题配对选项
     */
    private String matchOptStr;

    private List<DocQueModel> questionLst;
}
