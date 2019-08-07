package com.by.blcu.resource.model;


import lombok.Data;

import java.io.Serializable;

@Data
public class StudentCourseInfoModel implements Serializable {
    private static final long serialVersionUID = 2182011316612311066L;
    private int     testPaperId;
    private String  testPaperName;
    private String  dateTime;
    private int     status;
}
