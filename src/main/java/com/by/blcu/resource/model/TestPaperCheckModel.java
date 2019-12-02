package com.by.blcu.resource.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class TestPaperCheckModel implements Serializable {
    private String name;
    private List<Map<String,String>> questionCheckLst;
}
