package com.by.blcu.resource.model;

/**
 * 资源类型枚举类
 */
public enum ResourceTypeEnum {

    TEST(0,"测试"),
    TASK(1,"作业"),
    VIDEO(2,"视频"),
    LIVE(3,"直播"),
    DISCUSS(4,"讨论"),
    QUESTION(5,"问答"),
    DOC(6,"文档"),
    TEXT(7,"文本"),
    DATA(8,"资料");

    private Integer typeCode;
    private String typeMsg;

    private ResourceTypeEnum(Integer typeCode, String typeMsg){
        this.typeCode = typeCode;
        this.typeMsg = typeMsg;
    }

    public Integer getTypeCode(){
        return this.typeCode;
    }

    public String getTypeMsg(){
        return this.typeMsg;
    }

}
