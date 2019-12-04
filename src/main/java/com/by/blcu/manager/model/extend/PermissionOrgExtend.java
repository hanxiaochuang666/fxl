package com.by.blcu.manager.model.extend;

import com.by.blcu.manager.model.ManagerRole;

import java.util.List;
import java.util.Set;

/**
 * 机构权限类
 */
public class PermissionOrgExtend {
    /**
     * 组织机构编码
     */
    private String orgCode;
    /**
     * 用户权限列表
     */
    private Set<String> permissionList;

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public Set<String> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(Set<String> permissionList) {
        this.permissionList = permissionList;
    }
}
