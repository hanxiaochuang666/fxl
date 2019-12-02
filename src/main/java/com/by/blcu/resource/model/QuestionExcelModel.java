package com.by.blcu.resource.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionExcelModel implements Serializable {

    private static final long serialVersionUID = 1181899837188066072L;

    @Excel(name = "序号")
    private String order;

    @Excel(name = "题型")
    private String questionType;

    @Excel(name = "子题型")
    private String childQuestionType;

    @Excel(name = "试题序列")
    private String questionSec;

    @Excel(name = "题干")
    private String questionBody;

    @Excel(name = "选项")
    private String questionOpt;

    @Excel(name = "答案")
    private String questionAnswer;

    @Excel(name = "解析")
    private String questionResolve;

    @Excel(name = "难度")
    private Integer difficultyLevel;

    @Excel(name = "课程结构")
    // 即课程目录，如果有多个以英文分号分隔如：第一课时 词汇讲解;第二课时 语法讲解
    private String knowledgePoints;

}
