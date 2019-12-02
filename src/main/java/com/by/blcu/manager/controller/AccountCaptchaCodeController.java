package com.by.blcu.manager.controller;


import com.by.blcu.core.constant.BussinessResponseCode;
import com.by.blcu.core.ret.RetResponse;
import com.by.blcu.core.ret.RetResult;
import com.by.blcu.core.utils.PhoneUtil;
import com.by.blcu.core.baseservice.captcode.CaptchaCodeService;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.by.blcu.core.ret.RetResponse.makeOKRsp;

/**
 * @Description: AccountCaptchaCodeController
 * @author Daniel
 * @date 2019/08/08 17:37
 */


@Controller
@RequestMapping("/user")
@Api(tags = "用户获取短信验证码接口", description = "用户获取短信验证码接口")
public class AccountCaptchaCodeController {

    @Resource
    private CaptchaCodeService captchaCodeService;
	/**
	 * 请求获取验证码接口
	 * @param response
	 * @param countrycode
	 * @param mobileno
     * @param code
	 */
	@ResponseBody()
	@RequestMapping(value="/fetch_captcha",method={RequestMethod.GET})
	public RetResult fetch_captcha(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false,value="cc",defaultValue="86") int countrycode,
            @RequestParam(required = false) String code,
            @RequestParam(required = true) String mobileno

	) {

    	Boolean ret = PhoneUtil.isValidPhoneCharacter(countrycode, mobileno);
		if(!ret){
            return RetResponse.makeRsp(BussinessResponseCode.AUTH_CAPTCHA_PHONE_NUMBER_INVALID.code,
                    BussinessResponseCode.AUTH_CAPTCHA_PHONE_NUMBER_INVALID.info);
		}

		//todo...check validate of code
        //
        /*if(!ret){
            return RetResponse.makeErrRsp("验证码无效");
        }*/


        BussinessResponseCode brc = captchaCodeService.fetchCaptchaCode(countrycode, mobileno, "");

        return RetResponse.makeRsp(brc.code, brc.info);

	}


	@ResponseBody()
	@RequestMapping(value="/validate_captcha",method={RequestMethod.POST,RequestMethod.GET})
	public RetResult validate_code(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false,value="cc",defaultValue="86") int countrycode,
			@RequestParam(required = true) String mobileno,
			@RequestParam(required = true) String captcha
			//@RequestParam(required = false) String callback
    ) {

        if(mobileno.isEmpty()){
            return RetResponse.makeRsp(BussinessResponseCode.AUTH_CAPTCHA_PHONE_NUMBER_INVALID.code,
                    BussinessResponseCode.AUTH_CAPTCHA_PHONE_NUMBER_INVALID.info);
        }

        BussinessResponseCode brc = captchaCodeService.validateCaptchaCode(countrycode, mobileno, captcha,"");

        return RetResponse.makeRsp(brc.code, brc.info);
	}

    public static void main(String[] args) {

    }

}

