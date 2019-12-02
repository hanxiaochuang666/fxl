package com.by.blcu.manager.model.sql;

/**
 * 消息发送范围
 */
public enum ScopeEnum {
    All("所有学员", 1), CcId("按课程分类", 2), Commodity_id("按课程内容", 3);
    // 成员变量
    private String name;
    private Integer index;

    // 构造方法
    private ScopeEnum(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(Integer index) {
        for (ScopeEnum c : ScopeEnum.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
