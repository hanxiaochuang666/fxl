package com.by.blcu.manager.common;

import com.by.blcu.manager.model.sql.AreasTypeEnum;

public enum UserValidateEnum {
    PhoneRegist("手机注册", 1), PhoneLogin("手机登录", 2), PhoneFindPassword("手机找回", 3),PhoneBind("手机绑定",4),
    EmailRegist("邮箱注册", 11), EmailLogin("邮箱登录", 12), EmailFindPassword("邮箱找回", 13),EmailBind("邮箱绑定", 14),
    ImgVCode("图片验证码",21);

    // 成员变量
    private String name;
    private long index;

    // 构造方法
    private UserValidateEnum(String name, long index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(long index) {
        for (UserValidateEnum c : UserValidateEnum.values()) {
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

    public long getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
