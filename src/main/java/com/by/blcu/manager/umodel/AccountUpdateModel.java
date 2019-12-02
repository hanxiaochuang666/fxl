package com.by.blcu.manager.umodel;

import com.by.blcu.manager.model.ManagerAccount;
import com.by.blcu.manager.model.ManagerAccountExtend;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

@ApiModel(description= "后台用户")
@Data
public class AccountUpdateModel extends ManagerAccount {

    /**
     *账号
     */
    @ApiModelProperty(value = "账号")
    private String userName;

    /**
     *密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     *密码
     */
    @ApiModelProperty(value = "真实姓名")
    private String realName;

    /**
     *手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     *机构名称
     */
    @ApiModelProperty(value = "机构名称")
    private String orgName;

    @ApiModelProperty(value = "用户扩展信息")
    private ManagerAccountExtend accountExtend;
}
