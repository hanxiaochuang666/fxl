package com.by.blcu.resource.model;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class StudentCourseModel implements Serializable {
    private static final long serialVersionUID = -3809167894184884895L;
    private String courseName;
    private Integer courseId;
    List<StudentCourseInfoModel> StudentCourseInfoLst;
}
