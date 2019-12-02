package com.by.blcu.manager.controller;

import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.manager.common.*;
import com.by.blcu.manager.model.SsoClient;
import com.by.blcu.manager.umodel.VcodeVerify;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 登录相关
 */
@Api( tags = "SSO登录相关API", description = "包含接口：\n" +
        "1、获取图片验证码【getCode】\n" +
        "2、验证图片验证码【checkImgCode】\n" +
        "3、发送手机验证码【sendVCodePhone】\n" +
        "4、验证手机验证码【checkVCodePhone】\n"+
        "5、发送邮件验证码【sendVCodeEmail】\n" +
        "6、验证邮件验证码【checkVCodeEmail】\n")
@RestController
@RequestMapping("/ssoLogin")
public class SsoLoginController {

    @Resource
    private RedisHelper redisHelper;

    @Resource
    private EmailHelper emailHelper;

    @Resource
    private PhoneHelper phoneHelper;

    @Resource
    private ImgHelper imgHelper;

    /**
     *  获取验证码
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取验证码-获取图片验证码", notes = "获取图片验证码")
    @GetMapping(value = "/getCode")
    public String getCode(HttpServletRequest request, HttpServletResponse response) throws Exception{
        VerifyCodeHelper imageUtil = new VerifyCodeHelper(120, 40, 4,30);
        String key= imgHelper.SendVCode(imageUtil.getCode());
        response.setContentType("image/png");
        //禁止图像缓存
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Access-Control-Expose-Headers","VCode");
        response.setHeader("VCode",key);
        response.setDateHeader("Expires", 0);
        //HttpSession session = request.getSession();
        //session.setAttribute("code", imageUtil.getCode());
        imageUtil.write(response.getOutputStream());
        return null;
    }

    /**
     * 验证图片验证码
     *
     * @param code
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "验证验证码-验证图片验证码", notes = "验证图片验证码")
    @GetMapping("/checkImgCode")
    public RetResult<Boolean> checkImgCode(HttpServletRequest request,@ApiParam(name="code",value = "图片验证码")  @RequestParam String code) throws Exception {
        if(StringHelper.IsNullOrWhiteSpace(code)){
            return RetResponse.makeErrRsp("[验证码]不能为空");
        }
//        HttpSession session = request.getSession();
//        String sessionCode = session.getAttribute("code")==null?null:session.getAttribute("code").toString();
//        if(sessionCode==null || !sessionCode.toLowerCase().equals(code.toLowerCase())){
//            return RetResponse.makeErrRsp("验证码不正确");
//        }
//        session.setAttribute("code", null);
        String key = request.getHeader("VCode");
        Boolean result = imgHelper.VerifyVCode(key,code);

        if(!result){
            return RetResponse.makeErrRsp("验证码不正确");
        }
        return RetResponse.makeOKRsp(true);
    }

    /**
     * 发送短信
     * @param phone
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "发送手机验证码", notes = "发送手机验证码")
    @GetMapping("/sendVCodePhone")
    public RetResult<Boolean> sendVCodePhone(@ApiParam(name="phone",value = "手机号") @RequestParam String phone,@ApiParam(name="type",value = "类型：1注册，2登录，3找回密码，4绑定") @RequestParam Integer type) throws Exception {
        List<Integer> typeList = new ArrayList<Integer>(Arrays.asList(1,2,3,4));
        if(StringHelper.IsNullOrWhiteSpace(phone)|| type==null || !typeList.contains(type)){
            return RetResponse.makeErrRsp("[手机号]不能为空，[type]必须属于[1,2,3,4]");
        }
        if(!RegexHelper.CheckPhone(phone)){
            return RetResponse.makeErrRsp("手机号格式不正确");
        }
        boolean result = phoneHelper.SendVCode(phone,type);
        return RetResponse.makeOKRsp(result);
    }
    /**
     * 验证手机验证码
     *
     * @param code
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "验证手机验证码", notes = "验证手机验证码")
    @GetMapping("/checkVCodePhone")
    public RetResult<Boolean> checkVCodePhone(@ApiParam(name="phone",value = "手机号") @RequestParam String phone,@ApiParam(name="type",value = "类型：1注册，2登录，3找回密码，4绑定") @RequestParam Integer type, @ApiParam(name="code",value = "手机验证码")  @RequestParam String code) throws Exception {
        List<Integer> typeList = new ArrayList<Integer>(Arrays.asList(1,2,3,4));
        if(StringHelper.IsNullOrWhiteSpace(phone) || StringHelper.IsNullOrWhiteSpace(code) || type==null || !typeList.contains(type)){
            return RetResponse.makeErrRsp("[手机号，验证码]不能为空，[type]必须属于[1,2,3,4]");
        }
        if(!RegexHelper.CheckPhone(phone)){
            return RetResponse.makeErrRsp("手机号格式不正确");
        }
        Boolean result = phoneHelper.VerifyVCode(phone,type,code);

        if(!result){
            return RetResponse.makeErrRsp("验证码不正确");
        }
        return RetResponse.makeOKRsp(true);
    }


    /**
     * 发送验证码
     * @param email
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "发送邮件验证码", notes = "发送邮件验证码")
    @GetMapping("/sendVCodeEmail")
    public RetResult<Boolean> sendVCodeEmail(@ApiParam(name="email",value = "邮箱") @RequestParam String email, @ApiParam(name="type",value = "类型：1注册，2登录，3找回密码，4绑定") @RequestParam Integer type) throws Exception {
        List<Integer> typeList = new ArrayList<Integer>(Arrays.asList(1,2,3,4));
        if(StringHelper.isEmpty(email)|| type==null || !typeList.contains(type)){
            return RetResponse.makeErrRsp("[邮箱]不能为空，[type]必须属于[1,2,3,4]");
        }
        if(!RegexHelper.CheckEmail(email)){
            return RetResponse.makeErrRsp("手机号格式不正确");
        }
        boolean result = emailHelper.SendVCode(email,type);
        return  RetResponse.makeOKRsp(result);
    }
    @ApiOperation(value = "验证邮件验证码", notes = "验证邮件验证码")
    @GetMapping("/checkVCodeEmail")
    public RetResult<Boolean> checkVCodeEmail(@ApiParam(name="email",value = "邮箱") @RequestParam String email,@ApiParam(name="type",value = "类型：1注册，2登录，3找回密码，4绑定") @RequestParam Integer type,@ApiParam(name="code",value = "验证码") @RequestParam String code) throws Exception {
        List<Integer> typeList = new ArrayList<Integer>(Arrays.asList(1,2,3,4));
        if(StringHelper.IsNullOrWhiteSpace(email) || StringHelper.IsNullOrWhiteSpace(code)|| type==null || !typeList.contains(type)){
            return RetResponse.makeErrRsp("[邮箱，验证码]不能为空，[type]必须属于[1,2,3,4]");
        }
        if(!RegexHelper.CheckEmail(email)){
            return RetResponse.makeErrRsp("手机号格式不正确");
        }
        Boolean result = emailHelper.VerifyVCode(email,type,code);
        if(!result){
            return RetResponse.makeErrRsp("验证码不正确");
        }
        return RetResponse.makeOKRsp(true);
    }
}
