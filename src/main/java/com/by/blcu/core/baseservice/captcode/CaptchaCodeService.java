package com.by.blcu.core.baseservice.captcode;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.baseservice.redis.BaseRedisService;
import com.by.blcu.core.constant.BussinessResponseCode;
import com.by.blcu.core.constant.RedisBusinessKeyConst;
import com.by.blcu.core.utils.PhoneUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @Description: AccountCaptchaCodeController
 * @author Daniel
 * @date 2019/08/08 17:37
 */
@Service
public class CaptchaCodeService {

	private final Logger logger = LoggerFactory.getLogger(CaptchaCodeService.class);
    private static int UniqueCaptchaCodeLen = 4;
    private final String serviceUrl = "http://yxt.bbn.com.cn/eums/rpc/";
    private final String account = "beiyujiaoyu";
    private final String passwd = "Byjy@00.";

    @Resource
    private BaseRedisService redisService;

    /*@Resource
    private RedisTemplate redisTemplate;*/

	private static ExecutorService exec_send = null;
	@PostConstruct
	public void initialize() {
		logger.info("CaptchaCodeService initialize...");
		exec_send = Executors.newFixedThreadPool(5);
	}

    public static int intNumber(int start, int end) {
        int delta = end - start;
        if (delta < 0) delta = 0 - delta;
        return (int)(delta * Math.random() + start);
    }

    public static String generateCaptchaCode(){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<UniqueCaptchaCodeLen;i++){
            sb.append(intNumber(0,9));
        }
        return sb.toString();
    }

    public String postJson(String url, String jsonStr) throws Exception{

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        StringEntity entity = new StringEntity(jsonStr, "utf-8");
        post.setEntity(entity);
        HttpResponse response = null;

        try {
            response = client.execute(post);
        } catch (Exception e) {
            logger.error("http client post SMS cause exception");
            e.printStackTrace();
        }

        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity httpEntity = response.getEntity();
        //if (httpEntity != null) {
        //post.abort();
        //}
        if(statusCode == 200){
            ByteArrayOutputStream resHolder = new ByteArrayOutputStream();
            httpEntity.writeTo(resHolder);
            resHolder.flush();
            return resHolder.toString("utf-8");
        }
        else {
            throw new Exception("服务异常");
        }
    }

	public BussinessResponseCode fetchCaptchaCode( int countrycode,
			final String acc, final String act) {
		try{
			
			boolean specialCaptchaCode = false;
			String accWithCC = PhoneUtil.format(countrycode, acc);
			String cckey = RedisBusinessKeyConst.ShortMessage.SHORT_MESSAGE_PREFIX
                    + accWithCC;

			//captchacode already sent
			String oldcode = redisService.get(cckey); //(String)redisTemplate.opsForValue().get(cckey);
			if(oldcode != null){
			    String us = String.format("phone%s has a captcode:%s.", accWithCC, oldcode);
				logger.info(us);
			    return BussinessResponseCode.AUTH_CAPTCHA_CODE_NOT_EXPIRE;
			}


            String code = generateCaptchaCode();
            redisService.set(cckey, code);
            redisService.expire(cckey, 1200);

            exec_send.submit((new Runnable() {
                @Override
                public void run() {

                    //send SM to acc
                    try {
                        int reqId = 1;
                        JSONObject reqJson = new JSONObject();
                        reqJson.put("id", reqId++);
                        reqJson.put("jsonrpc", "2.0");
                        reqJson.put("method", "genLoginToken");
                        JSONArray params = new JSONArray();
                        params.add(account);
                        params.add(passwd);
                        reqJson.put("params", params);

                        String resStr = postJson(serviceUrl + "power/authService", reqJson.toString());
                        logger.info(resStr);
                        JSONObject resJson = JSONObject.parseObject(resStr);
//                        JSONObject resJson = JSONObject.fromObject(resStr);
                        if(resJson.containsKey("result")){
                            JSONObject resultJson = resJson.getJSONObject("result");
                            if(resultJson.getBoolean("result")){
                                String loginToken = resultJson.getString("others");
                                //发送短信
                                reqJson = new JSONObject();
                                reqJson.put("id", reqId++);
                                reqJson.put("jsonrpc", "2.0");
                                reqJson.put("method", "save");

                                JSONObject smsJson = new JSONObject();
                                smsJson.put("businessType", "短信发送");	//业务类型，用于统计
                                String txt = String.format("你好，北语网院非学历平台验证码：%s， 20分钟内有效，使用后失效。", code);
                                smsJson.put("sendText", txt);
                                smsJson.put("sendTo", "非学历用户");
                                smsJson.put("toDetail", acc);
                                smsJson.put("sendFrom", "api");

                                params = new JSONArray();
                                params.add(smsJson);
                                reqJson.put("params", params);

                                JSONObject authJson = new JSONObject();
                                authJson.put("loginToken", loginToken);
                                reqJson.put("auth", authJson);

                                resStr = postJson(serviceUrl + "smsSave", reqJson.toString());
                                logger.info(resStr);
                                resJson = JSONObject.parseObject(resStr);
//                                resJson = JSONObject.fromObject(resStr);
                                resultJson = resJson.getJSONObject("result");
                                if(resultJson.getBoolean("result")){
                                    logger.info("send SMS success");
                                }
                                else {
                                    logger.error("send SMS fail: " + resultJson.getString("msg"));
                                }
                            }
                            else {
                                logger.error("get loginToken fail:" + resultJson.getString("msg"));
                            }
                        }
                        else {
                            logger.error("get loginToken fail");
                        }
                    } catch (Exception e) {
                        logger.error("send SMS cause exception");
                        e.printStackTrace();
                    }
                }
            }));

			return BussinessResponseCode.AUTH_CAPTCHA_PATIENT_WAITING;
		}catch(Exception ex){
			ex.printStackTrace(System.out);
			return BussinessResponseCode.AUTH_CAPTCHA_SEND_FAIL;
		}
	}


	public BussinessResponseCode validateCaptchaCode(final int countrycode,
			final String acc, final String captcha,final String act) {
		try{

            String accWithCC = PhoneUtil.format(countrycode, acc);
            String cckey = RedisBusinessKeyConst.ShortMessage.SHORT_MESSAGE_PREFIX
                    + accWithCC;

            //captchacode already sent
            String oldcode = redisService.get(cckey);
            if(oldcode == null){
                String us = String.format("phone%s has not a captcode:%s.", accWithCC, captcha);
                logger.info(us);
                return BussinessResponseCode.AUTH_CAPTCHA_CODE_NOTEXIST;
            }

			if (oldcode.equals(captcha))
			    return BussinessResponseCode.AUTH_CAPTCHA_VALID_OK;

			return BussinessResponseCode.AUTH_CAPTCHA_VALID_FAIL;
		}catch(Exception ex){
			ex.printStackTrace(System.out);
            return BussinessResponseCode.AUTH_CAPTCHA_VALID_FAIL;
		}
	}


	public static void main(String[] args) {

        System.out.println("finish send.");
    }

}