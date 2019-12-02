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
     * 子题数的每个题的分
     */
    private int perScore;

    /**
     * 题型在试卷上的排名
     */
    private int sort;

    /**
     * 复合题的总排序字段复用
     */
    private int number;

    /**
     * 综合题题干
     */
    private String synthesisStr;

    /**
     * 配对题配对选项
     */
    private String matchOptStr;

    /**
     * 配对题解析
     */
    private String matchReslove;

    /**
     * 是否计分
     */
    private int  isScore;

    private List<DocQueModel> questionLst;
}
