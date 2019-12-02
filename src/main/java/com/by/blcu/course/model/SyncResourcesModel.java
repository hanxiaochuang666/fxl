package com.by.blcu.course.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SyncResourcesModel implements Serializable {
    private static final long serialVersionUID = 2228215664526282907L;
    private List<Integer> courseIdLst;
    private Integer student;
}
