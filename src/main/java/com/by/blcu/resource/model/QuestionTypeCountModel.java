package com.by.blcu.resource.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName QuestionTypeCountModel
 * @Author 焦冬冬
 * @Date 2019/6/20 14:49
 **/
@Data
public class QuestionTypeCountModel implements Serializable {
    private static final long serialVersionUID = -4558401831999586548L;
    private int questionType;
    private int countNum;
}
