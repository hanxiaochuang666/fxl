package com.by.blcu.manager.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.aop.CheckToken;
import com.by.blcu.core.authentication.JWTToken;
import com.by.blcu.core.authentication.JWTUtil;
import com.by.blcu.core.baseservice.captcode.CaptchaCodeService;
import com.by.blcu.core.configurer.ShiroConfigurer;
import com.by.blcu.core.constant.BussinessResponseCode;
import com.by.blcu.core.constant.RedisBusinessKeyConst;
import com.by.blcu.core.domain.ActiveUser;
import com.by.blcu.core.ret.RetCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.*;
import com.by.blcu.manager.common.*;
import com.by.blcu.manager.model.ManagerOrgType;
import com.by.blcu.manager.model.SsoUser;
import com.by.blcu.manager.model.UserFindPasswordModel;
import com.by.blcu.manager.model.extend.PermissionOrgExtend;
import com.by.blcu.manager.service.ManagerAccountService;
import com.by.blcu.manager.service.ManagerLogService;
import com.by.blcu.manager.service.ManagerRedisService;
import com.by.blcu.manager.service.SsoUserService;
import com.by.blcu.manager.umodel.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import io.swagger.models.auth.In;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import com.by.blcu.manager.common.UserHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 耿鹤闯
 * @Description: SsoUserController类
 * @date 2019/08/05 14:37
 */

@Api(tags = "SSO用户接口API", description = "包含接口：\n" +
        "1、用户管理-添加用户【addUser】\n" +
        "2、用户管理-修改用户【updateUser】\n" +
        "3、用户管理-删除用户【deleteUserByUserNameList】\n" +
        "4、用户管理-查询用户列表【search】\n" +
        "5、用户管理-重置密码【resetPassword】\n" +
        "6、用户管理-根据用户账号查询用户【selectUserByUserName】\n" +
        "7、用户管理-根据用户Id查询用户【selectUserByUserId】\n" +
        "8、用户管理-根据表Id查询用户【selectUserById】\n" +

        "9、验证-验证账号是否存在【checkUserName】\n" +
        "10、验证-验证手机号是否存在【checkPhone】\n" +
        "11、验证-验证邮箱是否存在【checkEmail】\n" +

        "12、注册-通过手机号注册【registPhone】\n" +

        "13、登录-普通登录【login】\n" +
        "14、登录-手机短信登录【loginFast】\n" +
        "15、登出【logout/{id}】\n" +

        "16、绑定-绑定电话/邮箱/QQ/微信【bindPhone】\n" +

        "17、找回密码-根据(用户账号/手机号/邮箱)查找用户【getUserByName】\n" +
        "18、找回密码-验证身份【validateUser】\n" +
        "19、找回密码-重置新密码【findPassword】\n"+

        "20、账号安全-修改密码【changePassword】\n")
@RestController
@CheckToken
@RequestMapping("/ssoUser")
public class SsoUserController extends ManagerBase {

    private static Logger log = LoggerFactory.getLogger(SsoUserController.class);
    @Resource
    private ManagerRedisService redisService;
    @Resource
    private ShiroConfigurer shiroProperties;
    @Resource
    private  PhoneHelper phoneHelper;
    @Resource
    private EmailHelper emailHelper;
    @Resource
    private RedisHelper redisHelper;
    @Resource
    private ImgHelper imgHelper;
//    @Resource
//    private CaptchaCodeService captchaCodeService;

    // region 后端接口: 增删改查

    @Resource
    private SsoUserService ssoUserService;
    //系统日志记录
    @Resource
    private ManagerLogService managerLogService;
    @Resource
    private ManagerAccountService managerAccountService;

    @ApiOperation(value = "用户管理-添加用户", notes = "添加用户")
    @PostMapping("/addUser")
    @RequiresPermissions("system:user")
    public RetResult<Integer> addUser(@RequestBody SsoUser ssoUser) throws Exception {
        if(ssoUser==null){
            return RetResponse.makeErrRsp("请传参");
        }
        ssoUser.setCreateBy(userNameBse);
        ssoUser.setModifyBy(userNameBse);
        return ssoUserService.addUser(ssoUser,userSessionHelper);
    }

    @ApiOperation(value = "用户管理-修改用户", notes = "修改用户")
    @PostMapping("/updateUser")
    public RetResult<Integer> updateUser(@RequestBody SsoUser ssoUser) throws Exception {
        if(ssoUser==null){
            return RetResponse.makeErrRsp("请传参");
        }
        ssoUser.setModifyBy(userNameBse);
        return ssoUserService.updateUserByUserName(ssoUser,userSessionHelper);
    }

    @ApiOperation(value = "用户管理-删除用户", notes = "通过用户名列表，批量删除用户")
    @PostMapping("/deleteUserByUserNameList")
    @RequiresPermissions("system:user")
    public RetResult<Integer> deleteUserByUserNameList(@ApiParam(name = "userNameList", value = "用户名列表") @RequestBody List<String> userNameList) throws Exception {
        return ssoUserService.deleteUserByUserNameList(userNameList,userSessionHelper);
    }

    @ApiOperation(value = "用户管理-查询用户列表", notes = "查询用户，多条件分页查询")
    @GetMapping("/search")
    @RequiresPermissions("system:user")
    public RetResult<PageInfo<SsoUser>> search(UserSearchModel search) throws Exception {
        if (search == null) {
            return RetResponse.makeErrRsp("参数不能为空");
        }
        Integer page = 1;
        Integer size = 1;
        if (search.getPage() == null || search.getPage() < 0) {
            page = 0;
        } else {
            page = search.getPage();
        }
        if (search.getSize() == null || search.getSize() < 0) {
            size = 0;
        } else {
            size = search.getSize();
        }
        Integer count = ssoUserService.selectListAndCount(search,userSessionHelper);
        PageHelper.startPage(page, size);
        List<SsoUser> list = ssoUserService.selectListAnd(search,userSessionHelper);
        PageInfo<SsoUser> pageInfo = new PageInfo<SsoUser>(list);
        pageInfo.setTotal(count);
        return RetResponse.makeOKRsp(pageInfo);
    }

    @ApiOperation(value = "用户管理-重置密码", notes = "重置密码。示例：{\"userName\":\"test\"}")
    @PostMapping("/resetPassword")
    @RequiresPermissions("system:user")
    public RetResult<Integer> resetPassword(@RequestBody UserSearchModel userSearchModel) throws Exception {
        if(userSearchModel==null || StringHelper.IsNullOrWhiteSpace(userSearchModel.getUserName())){
            return RetResponse.makeErrRsp("[用户账号]不能为空");
        }
        return ssoUserService.resetPassword(userSearchModel.getUserName(),userSessionHelper);
    }

    @ApiOperation(value = "用户管理-根据用户账号查询用户", notes = "根据用户账号，查找用户")
    @GetMapping("/selectUserByUserName")
    public RetResult<SsoUserExtend> selectUserByUserName(@ApiParam(name = "userName", value = "用户账号") @RequestParam String userName) throws Exception {
        RetResult<SsoUser> userInfo = ssoUserService.getUserByName(userName,1,userSessionHelper);
        if(userInfo.getCode()!=200){
            return RetResponse.makeErrRsp(userInfo.getMsg());
        }
        SsoUserExtend result=null;
        if(userInfo.getCode()==200 && userInfo.getData()!=null){
            try{
                result =new SsoUserExtend();
                ReflexHelper.Copy(userInfo.getData(),result);
               Set<PermissionOrgExtend> permissionOrgExtends = ssoUserService.getUserPermissionsInter(userName);
               if(!StringHelper.IsNullOrEmpty(permissionOrgExtends)){
                  long exit =  permissionOrgExtends.stream().filter(t->!StringHelper.IsNullOrEmpty(t.getPermissionList()))
                          .filter(m->m.getPermissionList().contains("teacher")).count();
                  if(exit>0){
                      result.setIsTeacher(true);
                  }
                  else{
                      result.setIsTeacher(false);
                  }
               }
               else{
                   result.setIsTeacher(false);
               }
            }
            catch (Exception ex){
                result.setIsTeacher(false);
            }
        }
        return RetResponse.makeOKRsp(result);
    }

    @ApiOperation(value = "用户管理-根据用户Id查询用户", notes = "根据用户Id，查找用户")
    @GetMapping("/selectUserByUserId")
    public RetResult<SsoUser> selectUserByUserId(@ApiParam(name = "userId", value = "用户Id") @RequestParam String userId) throws Exception {
        return ssoUserService.getUserByName(userId,2,userSessionHelper);
    }

    @ApiOperation(value = "用户管理-根据表Id查询用户", notes = "根据表Id，查找用户")
    @GetMapping("/selectUserById")
    public RetResult<SsoUser> selectUserById(@ApiParam(name = "uid", value = "用户表Id") @RequestParam Integer uid) throws Exception {
        return ssoUserService.getUserByName(uid.toString(),3,userSessionHelper);
    }

    //endregion

    //region 验证类接口


    /**
     * 验证账号是否存在
     *
     * @param userName
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "验证-验证账号是否存在", notes = "验证账号是否存在。若存在则200；否则400")
    @GetMapping("/checkUserName")
    public RetResult<Boolean> checkUserName(@ApiParam(name = "userName", value = "用户账号")  @RequestParam String userName) throws Exception {
        return ssoUserService.checkExist(userName,1,userSessionHelper);
    }

    /**
     * 验证手机号是否存在
     *
     * @param phone
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "验证-验证手机号是否存在", notes = "验证手机号是否存在。若存在则200；否则400")
    @GetMapping("/checkPhone")
    public RetResult<Boolean> checkPhone(@ApiParam(name = "phone", value = "手机号")  @RequestParam String phone) throws Exception {
        return ssoUserService.checkExist(phone,2,userSessionHelper);
    }

    /**
     * 验证邮箱是否存在
     *
     * @param email
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "验证-验证邮箱是否存在", notes = "验证邮箱是否存在。若存在则200；否则400")
    @GetMapping("/checkEmail")
    public RetResult<Boolean> checkEmail(@ApiParam(name = "email", value = "邮箱") @RequestParam String email) throws Exception {
        return ssoUserService.checkExist(email,3,userSessionHelper);
    }

    //endregion

    //region 前端注册登录接口

    /**
     * 通过手机号注册
     *
     * @param registModel 注册类
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "注册-通过手机号注册", notes = "通过手机号注册")
    @PostMapping("/registPhone")
    public RetResult<Integer> registPhone(@RequestBody UserPhoneRegistModel registModel) throws Exception {

        if(registModel==null || StringHelper.IsNullOrWhiteSpace(registModel.getAccount()) ||  StringHelper.IsNullOrWhiteSpace(registModel.getVerifyCode()) ||StringHelper.IsNullOrWhiteSpace(registModel.getPassword())){
            return RetResponse.makeErrRsp("[手机号, 验证码，密码]不能为空");
        }

        if(RegexHelper.CheckPassword(registModel.getPassword())){
            return RetResponse.makeErrRsp("密码格式不正确，应为6~16位字符");
        }

        //验证图片验证码

        //验证短信验证码
        boolean checkVCode = phoneHelper.VerifyVCode(registModel.getAccount(),1,registModel.getVerifyCode());
        if(!checkVCode){
            return RetResponse.makeErrRsp("[手机号,短信验证码]不正确");
        }

        RetResult<Boolean> result = ssoUserService.checkExist(registModel.getAccount(),2,userSessionHelper);
        if(result.getCode()==200){
            return RetResponse.makeErrRsp("手机号已存在");
        }

        SsoUser ssoUser=new SsoUser();
        Date datetime = new Date();
        ssoUser.setUserId(ApplicationUtils.getUUID());
        String username = org.apache.commons.lang3.StringUtils.lowerCase(registModel.getAccount());
        ssoUser.setPassword(registModel.getPassword());
        ssoUser.setUserName(username);
        ssoUser.setPhone(registModel.getAccount());
        ssoUser.setStatus(1);
        ssoUser.setCreateTime(datetime);
        ssoUser.setModifyTime(datetime);
        ssoUser.setCreateBy(UserHelper.UserName);
        ssoUser.setModifyBy(UserHelper.UserName);

        return ssoUserService.addUser(ssoUser,userSessionHelper);
    }

    /**
     * 普通登录(调整开发)
     *
     * @param  loginModel 普通登录类
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "登录-普通登录", notes = "普通登录：支持手机号，邮箱，账号")
    @PostMapping("/login")
    public RetResult login(@RequestBody UserCommonLogin loginModel, HttpServletRequest request) throws Exception {

        if(loginModel==null || StringHelper.IsNullOrWhiteSpace(loginModel.getAccount()) ||StringHelper.IsNullOrWhiteSpace(loginModel.getPassword())){
            return RetResponse.makeErrRsp("[账号，密码]不能为空");
        }
        if(!StringHelper.IsNullOrWhiteSpace(loginModel.getVerifyCode())){
            if(!checkImgCode(request,loginModel.getVerifyCode())){
                return RetResponse.makeErrRsp("[图片验证码]错误");
            }
        }
        Map<String, Object> userInfo =null;
        if(ManagerHelper.isEnable && loginModel.getAccount().equals(ManagerHelper.UserName)){
            if(!loginModel.getPassword().equals(ManagerHelper.Password)){
                return RetResponse.makeErrRsp("[账号，密码]错误");
            }
            SsoUser ssoUser = ManagerHelper.ssoUser;
            String password = MD5Util.encrypt(ssoUser.getUserName(), loginModel.getPassword());
            //2.生成 Token
            List<ManagerOrgType> orgCodes = managerAccountService.verifyOrgCodeByUsername(ssoUser.getUserName(), "");
            String token = ApplicationUtils.encryptToken(JWTUtil.sign(ssoUser, password, orgCodes));
            LocalDateTime expireTime = LocalDateTime.now().plusSeconds(SpringContextUtil.getBean(ShiroConfigurer.class).getJwtTimeOut());
            String expireTimeStr = DateUtils.formatFullTime(expireTime);
            JWTToken jwtToken = new JWTToken(token, expireTimeStr);

            //3.添加用户登录记录，更新用户登录时间（database + redis）
            String userId = this.saveTokenToRedis(ssoUser, jwtToken, request);
            ssoUser.setUserId(userId);//临时ID，用于登出

            //4.封装返回前端信息
            userInfo = this.generateUserInfo(jwtToken, ssoUser);

            //session
            UserSessionHelper userSession  =new UserSessionHelper();
            userSession.setUserId(ssoUser.getUserId());
            userSession.setUserName(ssoUser.getUserName());
            userSession.setLoginTime(new Date());
            userSession.setIp(getIpAddr());
            redisHelper.setUserSession(ssoUser.getUserName(),userSession);
        }
        else{
            final String errorMessage = "用户名或密码错误";
            RetResult<SsoUser> ssoUserOpt = ssoUserService.getUserByName(loginModel.getAccount(),userSessionHelper);
            if(ssoUserOpt.getCode()!=200 || ssoUserOpt.getData()==null){
                return RetResponse.makeErrRsp("[账号，密码]错误");
            }
            SsoUser ssoUser = ssoUserOpt.getData();

            if(!StringHelper.IsNullOrZero(ssoUser.getStatus()) && ssoUser.getStatus()!=1){
                return RetResponse.makeErrRsp("[此账号已禁用]");
            }
            String password = MD5Util.encrypt(ssoUser.getUserName(), loginModel.getPassword());

            //1.需要自定义错误用户前台回显（用户应有锁定状态的判定）
            if (ssoUser == null)
                return RetResponse.makeErrRsp(errorMessage);
            if (!org.apache.commons.lang3.StringUtils.equals(ssoUser.getPassword(), password))
                return RetResponse.makeErrRsp(errorMessage);

            //2.生成 Token
            List<ManagerOrgType> orgCodes = managerAccountService.verifyOrgCodeByUsername(ssoUser.getUserName(), loginModel.getOrgCode());
            String token = ApplicationUtils.encryptToken(JWTUtil.sign(ssoUser, password, orgCodes));
            LocalDateTime expireTime = LocalDateTime.now().plusSeconds(SpringContextUtil.getBean(ShiroConfigurer.class).getJwtTimeOut());
            String expireTimeStr = DateUtils.formatFullTime(expireTime);
            JWTToken jwtToken = new JWTToken(token, expireTimeStr);

            //3.添加用户登录记录，更新用户登录时间（database + redis）
            String userId = this.saveTokenToRedis(ssoUser, jwtToken, request);
            ssoUser.setUserId(userId);//临时ID，用于登出

            //4.封装返回前端信息
            userInfo = this.generateUserInfo(jwtToken, ssoUser);

            //session
            UserSessionHelper userSession  =new UserSessionHelper();
            userSession.setUserId(ssoUser.getUserId());
            userSession.setUserName(ssoUser.getUserName());
            userSession.setLoginTime(new Date());
            userSession.setIp(getIpAddr());
            redisHelper.setUserSession(ssoUser.getUserName(),userSession);
        }
        //日志记录
        managerLogService.addLogAsync(ManagerLogEnum.User_Login.getName(),null,userSessionHelper);
        return new RetResult().setCode(RetCode.SUCCESS).setMsg("认证成功").setData(userInfo);
    }

    /**
     * 用户登出 清空对应 Token
     * @param userId
     * @throws Exception
     */
    @ApiOperation(value = "登出", notes = "快速登出")
    @GetMapping("/logout/{userId}")
    public RetResult logout(@NotBlank(message = "{required}") @PathVariable String userId) throws Exception{
        Long nowTime = Long.valueOf(DateUtils.formatFullTime(LocalDateTime.now()));

        //1.首先获取登录集合
        Set<String> userOnlineSet = redisService.zRange(RedisBusinessKeyConst.Authentication.TOKEN_ACTIVE_USER, nowTime, Long.MAX_VALUE);
        //2.查找id定位在线用户
        ActiveUser logoutUser = null;
        String logoutUserString = "";
        for(String userOnline : userOnlineSet){
            ActiveUser user = JSON.parseObject(userOnline, ActiveUser.class);
            if(userId.equals(user.getId())){
                logoutUser = user;
                logoutUserString = userOnline;
            }
        }
        //3.在线列表移除用户 移除关联 Token 信息
        if(logoutUser != null && org.apache.commons.lang3.StringUtils.isNotBlank(logoutUserString)){
            redisService.zRemove(RedisBusinessKeyConst.Authentication.TOKEN_ACTIVE_USER, logoutUserString);
            redisService.delete(RedisBusinessKeyConst.Authentication.TOKEN_CACHE_PREFIX + logoutUser.getToken() + "." + logoutUser.getIp());
            redisHelper.cleanUserSession(userNameBse);
        }
        //日志记录
        managerLogService.addLogAsync(ManagerLogEnum.User_Logout.getName(),null,userSessionHelper);
        return RetResponse.makeOKRsp();
    }

    /**
     * 手机短信登录
     *
     * @param loginModel 登录类
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "登录-手机短信登录", notes = "手机登录，通过手机短信验证码登录")
    @PostMapping("/loginFast")
    public RetResult loginFast(HttpServletRequest request, @RequestBody UserPhoneLogin loginModel) throws Exception {

        if(loginModel==null || StringHelper.IsNullOrWhiteSpace(loginModel.getAccount()) || StringHelper.IsNullOrWhiteSpace(loginModel.getVerifyCodePhone())){
            return RetResponse.makeErrRsp("[手机号,短信验证码]不能为空");
        }
        if(!StringHelper.IsNullOrWhiteSpace(loginModel.getVerifyCodeImg())){
            if(!checkImgCode(request,loginModel.getVerifyCodeImg())){
                return RetResponse.makeErrRsp("[图片验证码]错误");
            }
        }
        //验证短信验证码
        boolean checkVCode = phoneHelper.VerifyVCode(loginModel.getAccount(),2,loginModel.getVerifyCodePhone());
        if(!checkVCode){
            return RetResponse.makeErrRsp("[手机号,短信验证码]不正确");
        }

        final String errorMessage = "用户名或密码错误";
        RetResult<SsoUser> ssoUserResult = ssoUserService.getUserByName(loginModel.getAccount(),4,userSessionHelper);

        //1.需要自定义错误用户前台回显（用户应有锁定状态的判定）
        if (ssoUserResult.getCode() != 200)
            return RetResponse.makeErrRsp("手机号不存在");

        SsoUser ssoUser = ssoUserResult.getData();

        if(!StringHelper.IsNullOrZero(ssoUser.getStatus()) && ssoUser.getStatus()!=1){
            return RetResponse.makeErrRsp("[此账号已禁用]");
        }

        String  username = org.apache.commons.lang3.StringUtils.lowerCase(ssoUser.getUserName());
        String  password = ssoUser.getPassword();


        //2.生成 Token
        List<ManagerOrgType> orgCodes = managerAccountService.verifyOrgCodeByUsername(ssoUser.getUserName(), loginModel.getOrgCode());
        String token = ApplicationUtils.encryptToken(JWTUtil.sign(ssoUser, password, orgCodes));
        LocalDateTime expireTime = LocalDateTime.now().plusSeconds(SpringContextUtil.getBean(ShiroConfigurer.class).getJwtTimeOut());
        String expireTimeStr = DateUtils.formatFullTime(expireTime);
        JWTToken jwtToken = new JWTToken(token, expireTimeStr);

        //3.添加用户登录记录，更新用户登录时间（database + redis）
        String userId = this.saveTokenToRedis(ssoUser, jwtToken, request);
        ssoUser.setUserId(userId);//临时ID，用于登出

        //4.封装返回前端信息
        Map<String, Object> userInfo = this.generateUserInfo(jwtToken, ssoUser);

        //session
        UserSessionHelper userSession  =new UserSessionHelper();
        userSession.setUserId(ssoUser.getUserId());
        userSession.setUserName(ssoUser.getUserName());
        userSession.setLoginTime(new Date());
        userSession.setIp(getIpAddr());
        redisHelper.setUserSession(ssoUser.getUserName(),userSession);

        //日志记录
        managerLogService.addLogAsync(ManagerLogEnum.User_Login.getName(),null,userSessionHelper);

        return new RetResult().setCode(RetCode.SUCCESS).setMsg("认证成功").setData(userInfo);
    }

//    /**
//     * 退出
//     *
//     * @param userName 退出
//     * @return
//     * @throws Exception
//     */
//    @ApiOperation(value = "用户管理-退出", notes = "退出")
//    @PostMapping("/loginOut")
//    public RetResult<Boolean> loginOut(@ApiParam(name = "userName", value = "用户账号") @RequestParam String userName) throws Exception {
//        LoginInfoModel loginInfo=new LoginInfoModel();
//        return RetResponse.makeOKRsp(true);
//    }

    @ApiOperation(value = "绑定-绑定电话/邮箱/QQ/微信", notes = "绑定电话/邮箱/QQ/微信")
    @PostMapping("/bindPhone")
    public RetResult<Boolean> bindPhone(@RequestBody UserBindModel bindModel){
        if(bindModel==null || StringHelper.IsNullOrWhiteSpace(bindModel.getUserName())){
            return RetResponse.makeErrRsp("[用户账号]不能为空");
        }
        SsoUser ssoUser = new SsoUser();
       try{
           ReflexHelper.Copy(bindModel,ssoUser);
       }
       catch (Exception ex){

       }
       if(!StringHelper.IsNullOrWhiteSpace(bindModel.getPhone())){
           bindModel.setEmail(null);
           bindModel.setQq(null);
           bindModel.setWechatOpenId(null);
           //验证短信验证码
           boolean checkPhone = phoneHelper.VerifyVCode(bindModel.getPhone(),4,bindModel.getVerifyCode());
           if(!checkPhone){
               return RetResponse.makeErrRsp("[手机号,短信验证码]不正确");
           }
           ssoUser.setPhone(bindModel.getPhone());
       }
        if(!StringHelper.IsNullOrWhiteSpace(bindModel.getEmail())){
            bindModel.setPhone(null);
            bindModel.setQq(null);
            bindModel.setWechatOpenId(null);
            //验证邮件验证码
            boolean checkEmail = emailHelper.VerifyVCode(bindModel.getEmail(),4,bindModel.getVerifyCode());
            if(!checkEmail){
                return RetResponse.makeErrRsp("邮箱验证码不正确");
            }
            ssoUser.setEmail(bindModel.getEmail());
        }
       ssoUser.setQq(bindModel.getQq());
       ssoUser.setWechatOpenId(bindModel.getWechatOpenId());
       RetResult<Integer> state = ssoUserService.updateUserByUserName(ssoUser,userSessionHelper);
       return RetResponse.makeOKRsp();
    }
    /**
     * 存储token到redis
     * 失效时间与jwt配置时间一致
     */
    private String saveTokenToRedis(SsoUser user, JWTToken token, HttpServletRequest request){
        String ip = IPUtil.getIpAddr(request);

        //1.构建在线用户
        ActiveUser activeUser = new ActiveUser();
        activeUser.setUsername(user.getUserName());
        activeUser.setIp(ip);
        activeUser.setToken(token.getToken());

        //2.存放 在线用户（组，用户信息，过期时间）
        this.redisService.zAdd(RedisBusinessKeyConst.Authentication.TOKEN_ACTIVE_USER, JSON.toJSONString(activeUser)
                , Double.valueOf(token.getExipreAt()));

        //3.存放 Token
        String key = RedisBusinessKeyConst.Authentication.TOKEN_CACHE_PREFIX + token.getToken() + "." + ip;
        Long expireTime = shiroProperties.getJwtTimeOut();
        this.redisService.setWithExpire(key, token.getToken(), expireTime);

        return activeUser.getId();
    }

    /**
     * 生成前端需要的用户信息，包括：
     * 1. token
     * 2. user
     *
     * @param token token
     * @param user  用户信息
     * @return UserInfo
     */
    private Map<String, Object> generateUserInfo(JWTToken token, SsoUser user) {
        String username = user.getUserName();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("token", token.getToken());
        userInfo.put("exipreTime", token.getExipreAt());

        user.setPassword("Secret");
        userInfo.put("user", user);
        return userInfo;
    }

    //endregion

    //region 找回密码接口

    /**
     * 查找用户
     *
     * @param account 用户账号/手机号/邮箱
     * @return
     */
    @ApiOperation(value = "找回密码-根据(用户账号/手机号/邮箱)查找用户", notes = "根据用户账号/手机号/邮箱，查找用户")
    @GetMapping("/getUserByName")
    public RetResult<SsoUser>  getUserByName(@ApiParam(name = "account", value = "用户账号/手机号/邮箱") @RequestParam String account){
        return ssoUserService.getUserByName(account,userSessionHelper);
    }

    /**
     * 找回密码时验证信息
     *
     * @param validatgeModel 验证类
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "找回密码-验证身份", notes = "验证身份")
    @PostMapping("/validateUser")
    public RetResult<Integer> validateUser(@RequestBody UserFindPasswordModel validatgeModel) throws Exception {
        //类型：1 手机号，2 邮箱，3 账号
        //邮件/短信验证码
        if(validatgeModel==null || StringHelper.isEmpty(validatgeModel.getAccount()) || StringHelper.IsNullOrZero(validatgeModel.getType())){
            return RetResponse.makeErrRsp("[账号，邮件/短信验证码]参数不能为空");
        }

        //验证图片验证码

        //验证手机短信验证码
        if(validatgeModel.getType() == 1){
            //验证短信验证码
            boolean checkPhone = phoneHelper.VerifyVCode(validatgeModel.getAccount(),3,validatgeModel.getVerifyCode());
            if(!checkPhone){
                return RetResponse.makeErrRsp("[手机号,短信验证码]不正确");
            }
        }
        else if(validatgeModel.getType() == 2){
            //验证邮件验证码
            boolean checkEmail = emailHelper.VerifyVCode(validatgeModel.getAccount(),3,validatgeModel.getVerifyCode());
            if(!checkEmail){
                return RetResponse.makeErrRsp("邮箱验证码不正确");
            }
        }
        else{
            return RetResponse.makeErrRsp("类型不对");
        }


        RetResult<SsoUser> ssoUserRetResult = ssoUserService.getUserByName(validatgeModel.getAccount(),userSessionHelper);
        if(ssoUserRetResult.getCode()!=200){
            return RetResponse.makeErrRsp(ssoUserRetResult.getMsg());
        }

        return RetResponse.makeOKRsp();
    }


    @ApiOperation(value = "找回密码-重置新密码", notes = "重置新密码")
    @PostMapping("/findPassword")
    public RetResult<Integer> findPassword(@RequestBody FindPasswordReset findModel){
        if(findModel==null || StringHelper.IsNullOrWhiteSpace(findModel.getAccount()) || StringHelper.IsNullOrWhiteSpace(findModel.getNewPassword())){
            return RetResponse.makeErrRsp("[用户账号/手机号/邮箱，新密码]不能为空");
        }
        String account =findModel.getAccount();
        String newPassword =findModel.getNewPassword();

        RetResult<SsoUser> ssoUserRetResult = ssoUserService.getUserByName(account,userSessionHelper);
        if(ssoUserRetResult.getCode()!=200){
            return RetResponse.makeErrRsp(ssoUserRetResult.getMsg());
        }
        SsoUser user  = ssoUserRetResult.getData();

        //验证验证码
        boolean checkResult=false;

        if(!StringHelper.IsNullOrWhiteSpace(user.getEmail()) && findModel.getAccount().equals(user.getEmail())){
            checkResult = emailHelper.VerifyVCode(user.getEmail(),3);
        }
        else if(findModel.getAccount().equals(user.getPhone())){
            checkResult =phoneHelper.VerifyVCode(findModel.getAccount(),3);
        }

        if(!checkResult){
            return RetResponse.makeErrRsp("您验证未通过或已过期，请重新找回密码");
        }

        Date datetime = new Date();

        String username = org.apache.commons.lang3.StringUtils.lowerCase(user.getUserName());
        user.setPassword(MD5Util.encrypt(username,newPassword));
        user.setModifyTime(datetime);
        user.setModifyBy(UserHelper.UserName);
        Integer result = ssoUserService.update(user);
        return RetResponse.makeOKRsp(result);
    }

    @ApiOperation(value = "账号安全-修改密码", notes = "修改密码")
    @PostMapping("/changePassword")
    public RetResult<Integer> changePassword(@RequestBody ChangePasswordModel model){
        if(model==null || StringHelper.IsNullOrWhiteSpace(model.getUserName()) || StringHelper.IsNullOrWhiteSpace(model.getOldPassword()) || StringHelper.IsNullOrWhiteSpace(model.getNewPassword())){
            return RetResponse.makeErrRsp("[用户账号，原密码，新密码]不能为空");
        }
        String account =model.getUserName();
        String oldPassword=model.getOldPassword();
        String newPassword =model.getNewPassword();

        RetResult<SsoUser> ssoUserRetResult = ssoUserService.getUserByName(account,1,userSessionHelper);
        if(ssoUserRetResult.getCode()!=200){
            return RetResponse.makeErrRsp(ssoUserRetResult.getMsg());
        }
        SsoUser user  = ssoUserRetResult.getData();
        String username = org.apache.commons.lang3.StringUtils.lowerCase(account);
        if(!MD5Util.encrypt(username,oldPassword).equals(user.getPassword())){
            return RetResponse.makeErrRsp("原密码不正确");
        }
        Date datetime = new Date();
        user.setPassword(MD5Util.encrypt(username,newPassword));
        user.setModifyTime(datetime);
        user.setModifyBy(UserHelper.UserName);
        Integer result = ssoUserService.update(user);
        return RetResponse.makeOKRsp(result);
    }
    //endregion

    //region 私有方法
    private final boolean checkImgCode(HttpServletRequest request,String code){
//        HttpSession session = request.getSession();
//        String sessionCode = session.getAttribute("code")==null?null:session.getAttribute("code").toString();
//        if(sessionCode==null || !sessionCode.toLowerCase().equals(code.toLowerCase())){
//            return false;
//        }
//        session.setAttribute("code", null);
        String key = request.getHeader("VCode");
        if(StringHelper.IsNullOrWhiteSpace(key)){
            return false;
        }
        Boolean result = imgHelper.VerifyVCode(key,code);
        return result;
    }
    //endregion
}