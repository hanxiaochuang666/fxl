package com.by.blcu.manager.model.sql;

import com.by.blcu.manager.model.ManagerPermission;
import lombok.Data;

import java.util.Set;

/**
 * 机构权限
 */
@Data
public class OrgPermission extends ManagerPermission {

    /**
     * 组织机构编码
     */
    private String orgCode;
}
