package com.by.blcu.manager.model.sql;

import lombok.Data;

import java.util.List;

@Data
public class InputRole {
    private String roleId;
    private String roleName;
    private Integer status;
    private Boolean isSystem;
    private String orgCode;

    private List<String> roleIdList;
}
