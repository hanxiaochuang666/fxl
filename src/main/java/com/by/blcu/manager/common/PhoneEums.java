package com.by.blcu.manager.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.by.blcu.core.baseservice.captcode.CaptchaCodeService;
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
import java.io.ByteArrayOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 易信通短信发送
 * 参照 CaptchaCodeService
 */
@Service
public class PhoneEums {
    private final Logger logger = LoggerFactory.getLogger(PhoneEums.class);
    private static int UniqueCaptchaCodeLen = 4;
    private final String serviceUrl = "http://smessage.qyquan.cn:29980/SmsSendReceiveHttp/";
    private final String account = "82300550";
    private final String passwd = "8a90290c75c94f4f9ab5d1ee9170ad77";

    private static ExecutorService exec_send = null;

    @PostConstruct
    private void initialize() {
        logger.info("CaptchaCodeService initialize...");
        exec_send = Executors.newFixedThreadPool(5);
    }

    private static int intNumber(int start, int end) {
        int delta = end - start;
        if (delta < 0) delta = 0 - delta;
        return (int) (delta * Math.random() + start);
    }

    private static String generateCaptchaCode() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < UniqueCaptchaCodeLen; i++) {
            sb.append(intNumber(0, 9));
        }
        return sb.toString();
    }

    private String postJson(String url, String jsonStr) throws Exception {

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
        if (statusCode == 200) {
            ByteArrayOutputStream resHolder = new ByteArrayOutputStream();
            httpEntity.writeTo(resHolder);
            resHolder.flush();
            return resHolder.toString("utf-8");
        } else {
            throw new Exception("服务异常");
        }
    }

    /**
     * 发送短信
     *
     * @param phone   电话号码。多个用逗号隔开
     * @param message 短信内容
     * @return
     */
    public boolean sendMessage(String phone, String message) {
        try{
            exec_send.submit((new Runnable() {
                @Override
                public void run() {
                    try {
                        int reqId = 1;
                        JSONObject reqJson = new JSONObject();
                        reqJson.put("jsonrpc", "2.0");
                        reqJson.put("method", "genLoginToken");
                        reqJson.put("id", reqId++);
                        JSONArray params = new JSONArray();
                        params.add(account);
                        params.add(passwd);
                        reqJson.put("params", params);

                        String resStr = postJson(serviceUrl + "genLoginToken", reqJson.toString());
                        logger.info(resStr);
                        JSONObject resJson = JSONObject.parseObject(resStr);
                        if(resJson!=null && resJson.containsKey("result")){
                            JSONObject resultJson = resJson.getJSONObject("result");
                            if(resultJson!=null && !StringHelper.IsNullOrWhiteSpace(resultJson.getString("result")) && resultJson.getString("result").equals("true")){
                                String loginToken = resultJson.getString("others");
                                //发送短信
                                reqJson = new JSONObject();
                                reqJson.put("jsonrpc", "2.0");
                                reqJson.put("method", "save");
                                reqJson.put("id", reqId++);

                                JSONObject smsJson = new JSONObject();
                                smsJson.put("sendText", message);
                                smsJson.put("sendTime", "");
                                smsJson.put("sendTo", "非学历用户");
                                smsJson.put("toDetail", phone);
                                smsJson.put("businessType", "短信发送");	//业务类型，用于统计
                                smsJson.put("status", "2");
                                smsJson.put("sendFrom", "api");
                                smsJson.put("callBack", "");

                                params = new JSONArray();
                                params.add(smsJson);
                                reqJson.put("params", params);

                                JSONObject authJson = new JSONObject();
                                authJson.put("loginToken", loginToken);
                                reqJson.put("auth", authJson);

                                resStr = postJson(serviceUrl + "smsSave", reqJson.toString());
                                logger.info(resStr);
                                resJson = JSONObject.parseObject(resStr);
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
            return true;
        }catch(Exception ex){
            ex.printStackTrace(System.out);
            return false;
        }
    }

}