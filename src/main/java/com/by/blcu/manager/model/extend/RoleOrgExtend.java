package com.by.blcu.manager.model.extend;

import com.by.blcu.manager.model.ManagerRole;

import java.util.List;
import java.util.Set;

/**
 * 机构角色类
 */
public class RoleOrgExtend {
    /**
     * 组织机构编码
     */
    private String orgCode;

    /**
     * 用户权限列表
     */
    private Set<String> roleList;

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public Set<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(Set<String> roleList) {
        this.roleList = roleList;
    }
}
