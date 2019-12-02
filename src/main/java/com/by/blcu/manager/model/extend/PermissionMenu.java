package com.by.blcu.manager.model.extend;

import com.by.blcu.manager.model.ManagerPermission;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class PermissionMenu extends ManagerPermission {
    /**
     * 子级菜单
     * @return
     */
    @ApiModelProperty(value = "子级菜单")
    private List<PermissionMenu> children;

    public List<PermissionMenu> getChildren() {
        return children;
    }

    public void setChildren(List<PermissionMenu> children) {
        this.children = children;
    }
}
