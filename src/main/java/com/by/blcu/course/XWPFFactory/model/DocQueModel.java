package com.by.blcu.course.XWPFFactory.model;


import lombok.Data;

import java.io.Serializable;

@Data
public class DocQueModel implements Serializable {
    /**
     * 序号
     */
    private int number;

    /**
     * 分数
     */
    private int score;

    /**
     * 题干
     */
    private String questionBody;

    /**
     * 是否是听力题
     */
    private boolean isSound;

    /**
     * 选项
     */
    private String questionOpt;

    /**
     * 答案
     */
    private String questionAnswer;

    /**
     * 解析
     */
    private String questionReslove;
}
