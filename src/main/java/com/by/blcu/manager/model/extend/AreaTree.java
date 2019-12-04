package com.by.blcu.manager.model.extend;

import com.by.blcu.manager.model.ManagerAreas;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(description= "省市区树")
public class AreaTree extends ManagerAreas {
    @ApiModelProperty(value = "子级菜单")
    private List<AreaTree> children;

    public List<AreaTree> getChildren() {
        return children;
    }

    public void setChildren(List<AreaTree> children) {
        this.children = children;
    }
}
