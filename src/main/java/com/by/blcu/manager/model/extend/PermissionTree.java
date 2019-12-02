package com.by.blcu.manager.model.extend;


import com.by.blcu.manager.model.ManagerPermission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 菜单权限树
 */
@ApiModel(description= "权限树")
public class PermissionTree extends ManagerPermission {
    /**
     * 子级菜单
     * @return
     */
    @ApiModelProperty(value = "子级菜单")
    private List<PermissionTree> children;

    public List<PermissionTree> getChildren() {
        return children;
    }

    public void setChildren(List<PermissionTree> children) {
        this.children = children;
    }
}
