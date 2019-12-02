package com.by.blcu.manager.common;

public enum ManagerLogEnum {
    User_Login("用户登录", 1), User_Logout("用户退出", 2),
    User_Add("添加用户", 11),User_Modify("修改用户",12),User_Delete("删除用户",13),User_Modify_Password("修改密码",14),User_Reset_Password("重置密码",15),
    Account_Add("机构关联用户",21),Account_Modify("机构修改用户",22) ,Account_Delete("机构删除用户",23),Accont_Role_Modify("机构用户角色变动",24),
    Role_Add("添加角色",31),Role_Modify("修改角色",32),Role_Delete("删除角色",33),Role_Permission_Modify("角色的权限变动",34),
    Permission_Add("添加权限",41),Permission_Modify("修改权限",42),Permission_Delete("删除权限",43);
    // 成员变量
    private String name;
    private int index;

    // 构造方法
    private ManagerLogEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (ManagerLogEnum c : ManagerLogEnum.values()) {
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
