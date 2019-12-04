package com.by.blcu.manager.umodel;

import com.by.blcu.manager.model.ManagerPermission;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 权限树
 */
@ApiModel(description= "权限树")
public class PermissionTreeModel extends ManagerPermission {

    /**
     *子权限树
     */
    @ApiModelProperty(value = "子权限树")
    private List<ManagerPermission> childrenList;

    public List<ManagerPermission> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<ManagerPermission> childrenList) {
        this.childrenList = childrenList;
    }
}