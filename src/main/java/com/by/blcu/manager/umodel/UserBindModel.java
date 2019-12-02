package com.by.blcu.manager.umodel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import springfox.documentation.annotations.ApiIgnore;

@Data
@ApiModel(description= "用户绑定")
public class UserBindModel {
    @ApiModelProperty(value = "用户账号")
    private String userName;

    @ApiModelProperty(value = "验证码")
    private String verifyCode;

//    @ApiIgnore
//    @ApiModelProperty(value = "图片验证码")
//    private String verifyCodeImg;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "QQ号")
    private String qq;

    @ApiModelProperty(value = "绑定微信")
    private String wechatOpenId;
}
