package com.by.blcu.manager.model.extend;

import com.by.blcu.manager.model.WebNewsCategory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 新闻类别树
 */
@ApiModel(description= "新闻类别树")
public class NewsCategoryTree extends WebNewsCategory {
    /**
     * 子级菜单
     * @return
     */
    @ApiModelProperty(value = "子级菜单")
    private List<NewsCategoryTree> children;

    public List<NewsCategoryTree> getChildren() {
        return children;
    }

    public void setChildren(List<NewsCategoryTree> children) {
        this.children = children;
    }
}
