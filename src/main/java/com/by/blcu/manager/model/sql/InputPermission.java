package com.by.blcu.manager.model.sql;

import lombok.Data;

import java.util.List;

@Data
public class InputPermission {
    private String accountId;
    private String userId;
    private String userName;
    private String roleId;
    private String orgCode;

    private String permissionId;
    private List<String> permissionIdList;

    private String menuName;
}
