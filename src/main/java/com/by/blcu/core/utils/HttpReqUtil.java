package com.by.blcu.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xerces.internal.xs.StringList;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

/**
 *	http工具
 */
public class HttpReqUtil  extends AsyncHttp {

	protected static Logger logger = Logger.getLogger(HttpReqUtil.class);
	private static String userAgent = "Mozilla/5.0 (Windows NT 6.2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36";

	private HttpReqUtil() {}


	/**
	 * post 对象请求
	 * @param httpUri		请求地址
	 * @param token   	参数对象
	 * @return			请求结果
	 * @throws
	 */
	public static HttpResponse postObjectReq(String httpUri, String token)throws Exception {
		HttpPost post = new HttpPost( httpUri );
		HttpResponse httpResponse = null ;
		try {
			post.setHeader("User-Agent",userAgent);					//需要加这个参数，否则返回403 状态吗
			post.addHeader("Content-type", "application/json; charset=utf-8");		//设置请求头部
			post.setHeader("Accept", "application/json");
			if(!StringUtils.isEmpty(token)) {
				post.addHeader("token", token);
			}
			/*if(null!=param) {
				ObjectMapper objectMapper = new ObjectMapper();
				String josnParm = objectMapper.writeValueAsString(param);            //将请求对象由java对象置为json行字符对象。因为请求内设定了请求数据类型为json类型
				post.setEntity(new StringEntity(josnParm));
			}*/
			httpResponse=createHttpClient().execute(post);			//将post请求交给HttpClient 请求去执行
		} catch (IOException e) {
			return null ;
		}
		return httpResponse ;
	}

	public static HttpResponse openObjectReq(String httpUri,String appkey,String appsecret){
		HttpPost post = new HttpPost( httpUri );
		HttpResponse httpResponse = null ;
		try {
			post.setHeader("User-Agent",userAgent);					//需要加这个参数，否则返回403 状态吗
			post.addHeader("Content-type", "application/json; charset=utf-8");		//设置请求头部
			post.setHeader("Accept", "application/json");
			post.addHeader("appkey",appkey);
			post.addHeader("appsecret",appsecret);
			/*if(null!=param) {
				ObjectMapper objectMapper = new ObjectMapper();
				String josnParm = objectMapper.writeValueAsString(param);            //将请求对象由java对象置为json行字符对象。因为请求内设定了请求数据类型为json类型
				post.setEntity(new StringEntity(josnParm));
			}*/
			httpResponse=createHttpClient().execute(post);			//将post请求交给HttpClient 请求去执行
		} catch (IOException e) {
			return null ;
		}
		return httpResponse ;
	}

	public static HttpResponse openObjectReqHanchuang(String httpUri,Map<String,String> param){
		HttpPost post = new HttpPost( httpUri );
		HttpResponse httpResponse = null ;
		ArrayList<BasicNameValuePair> list = new ArrayList<>();
		param.forEach((key, value) -> list.add(new BasicNameValuePair(key, value)));
		try {
			post.setHeader("User-Agent",userAgent);					//需要加这个参数，否则返回403 状态吗
			post.addHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");		//设置请求头部
			post.setHeader("Accept", "application/json");
			if(null!=param) {
				post.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
			}
			httpResponse=createHttpClient().execute(post);			//将post请求交给HttpClient 请求去执行
		} catch (IOException e) {
			return null ;
		}
		return httpResponse ;
	}

	/**
	 * get 对象请求
	 * @param httpUri		请求地址
	 * @param param   	参数对象
	 * @return			请求结果
	 * @throws
	 */
	public static HttpResponse getObjectReq(String httpUri, Object param)throws Exception {
		StringBuffer httpUriStr = new StringBuffer() ;
		httpUriStr.append( httpUri ) ;
		if( param != null ){
			ObjectMapper objectMapper = new ObjectMapper();
			String josnParm = objectMapper.writeValueAsString(param);
			Map<String,String> map = objectMapper.readValue( josnParm, Map.class ); //json转换成map
			String paramStr = urlParamterStringer( "?" , map ) ;
			httpUriStr.append( paramStr ) ;
		}
		logger.info( httpUriStr.toString() ) ;
		HttpGet httpGet = new HttpGet( httpUriStr.toString() );
		String result = "";
		HttpResponse httpResponse = null ;
		try {
			httpGet.addHeader("Content-type", "application/text");		//设置请求头部
			httpGet.setHeader("Accept-Charset", "utf-8");
			httpGet.setHeader("User-Agent",userAgent);
			httpResponse = createHttpClient().execute( httpGet );				//将get请求交给HttpClient 请求去执行
		} catch (IOException e) {
			logger.error("HTTPGET 请求出现异常：" + e.getMessage() ) ;
			return null ;
		}

		return httpResponse ;
	}

    /**
     * get 对象请求
     * @param httpUri		请求地址
     *   	参数对象
     * @return			请求结果
     * @throws
     */
    public static HttpResponse getOpentReq(String httpUri, String appkey,String appsecret)throws Exception {
        StringBuffer httpUriStr = new StringBuffer() ;
        httpUriStr.append( httpUri ) ;
        logger.info( httpUriStr.toString() ) ;
        HttpGet httpGet = new HttpGet( httpUriStr.toString() );
        String result = "";
        HttpResponse httpResponse = null ;
        try {
            httpGet.addHeader("Content-type", "application/text");		//设置请求头部
            httpGet.setHeader("Accept-Charset", "utf-8");
            httpGet.setHeader("User-Agent",userAgent);
            httpGet.addHeader("appkey",appkey);
            httpGet.addHeader("appsecret",appsecret);//需要加这个参数，否则返回403 状态吗//需要加这个参数，否则返回403 状态吗
            httpResponse = createHttpClient().execute( httpGet );
        } catch (IOException e) {
            logger.error("HTTPGET 请求出现异常：" + e.getMessage() ) ;
            return null ;
        }

        return httpResponse ;
    }

	/**
	 * post 支付请求
	 * @param httpUri   请求地址
	 * @param param   	参数对象
	 * @return			请求结果
	 * @throws
	 */
	public static HttpResponse getPayResponse(String httpUri, String param)throws Exception {
		StringBuffer httpUriStr = new StringBuffer() ;
		httpUriStr.append( httpUri ) ;
		if(!StringUtils.isBlank(param)){
			httpUriStr.append("?").append( param ) ;
		}
		logger.info("发起易宝订单详情查看请求：" + httpUriStr.toString() ) ;
		HttpPost post = new HttpPost( httpUriStr.toString() );
		String result = "";
		HttpResponse httpResponse = null ;
		try {
			post.addHeader("Content-type", "application/x-www-form-urlencoded");
			post.setHeader("Accept-Charset", "utf-8");
			post.setHeader("User-Agent",userAgent);
			httpResponse = createHttpClient().execute( post );
		} catch (IOException e) {
			logger.error("HTTP POST 易宝查询支付结果，请求出现异常：" + e.getMessage() ) ;
			return null ;
		}

		return httpResponse ;
	}


	/****
	 * 解析http请求返回
	 * @param httpResponse
	 * @return
	 */
	public static Map parseHttpResponse( HttpResponse httpResponse ){
		logger.info( "请求处理的结果----》parseHttpResponse 格式化 ." );
		Map<String,Object> responsemap = new HashMap<>() ;
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			String result = "" ;
			int statusCode = httpResponse.getStatusLine().getStatusCode() ;
			if ( statusCode == 200) { // 请求得到响应后，分析只有200的时候才去分析得到的数据，否认都任务失败
				result = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8"); // 将返回的数据解析成一个字符串
				logger.info( "请求处理的结果----》" + result );
				responsemap = objectMapper.readValue(  result , Map.class ); //json转换成map
			}else{
				result = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8"); // 将返回的数据解析成一个字符串
				logger.info( "请求处理的结果----》" + result );
				responsemap = objectMapper.readValue(  result , Map.class );
				logger.info( "请求处理的结果状态码----》" + statusCode );
			}
		}catch ( Exception e ){
			logger.error( "解析HTTPResponse出现异常：异常如下----" + e.getMessage() );
		}
		return responsemap ;
	}

	public static Map<String,Object> parseQQHttpResponse(HttpResponse httpResponse){
		logger.info( "请求处理的结果----》parseHttpResponse 格式化 ." );
		Map<String,Object> responsemap = new HashMap<>() ;
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			String result = "" ;
			int statusCode = httpResponse.getStatusLine().getStatusCode() ;
			if ( statusCode == 200) { // 请求得到响应后，分析只有200的时候才去分析得到的数据，否认都任务失败
				result = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8"); // 将返回的数据解析成一个字符串
				logger.info( "请求处理的结果----》" + result );

				if(result.contains("(")){
					String[] split = result.split("\\(");
					if(split[1].contains(")")){
						String[] split1 = split[1].split("\\)");
						responsemap = objectMapper.readValue(  split1[0] , Map.class );
					}
					for (String s : split) {
						if(s.contains("=")){
							String[] split1 = s.split("=");
							responsemap.put(split1[0],split1[1]);
						}
					}
				}
			}else{
				result = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8"); // 将返回的数据解析成一个字符串
				logger.info( "请求处理的结果----》" + result );
				logger.info( "请求处理的结果状态码----》" + statusCode );
			}
		}catch ( Exception e ){
			logger.error( "解析HTTPResponse出现异常：异常如下----" + e.getMessage() );
		}
		return responsemap;
	}
	/**
	 * @Author 焦冬冬
	 * @Description 解析url形式结果的返回
	 * @Date 11:17 2019/5/20
	 * @Param
	 * @return
	 **/
	public static Map<String,Object> parseUrlHttpResponse(HttpResponse httpResponse){
		logger.info( "请求处理的结果----》parseHttpResponse 格式化 ." );
		Map<String,Object> responsemap = new HashMap<>() ;
		try{
			String result = "" ;
			int statusCode = httpResponse.getStatusLine().getStatusCode() ;
			if ( statusCode == 200) { // 请求得到响应后，分析只有200的时候才去分析得到的数据，否认都任务失败
				result = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8"); // 将返回的数据解析成一个字符串
				logger.info( "请求处理的结果----》" + result );
				if(result.contains("&")){
					String[] split = result.split("&");
					for (String s : split) {
						if(s.contains("=")){
							String[] split1 = s.split("=");
							responsemap.put(split1[0],split1[1]);
						}
					}
				}
			}else{
				result = EntityUtils.toString(httpResponse.getEntity(),
						"UTF-8"); // 将返回的数据解析成一个字符串
				logger.info( "请求处理的结果----》" + result );
				logger.info( "请求处理的结果状态码----》" + statusCode );
			}
		}catch ( Exception e ){
			logger.error( "解析HTTPResponse出现异常：异常如下----" + e.getMessage() );
		}
		return responsemap;
	}


	/**
	 * 将map中的数据格式化成服务端所需的表单String(www.baidu.com/login?userName=lambdroid&
	 * password=123456的“？”以及之后的数据)
	 * @param head
	 *            url头部字串，一般为“？”，在表单方式中分隔URL和请求参数map
	 * @param map
	 *            请求参数map
	 * @return 格式化完成后的表单数据
	 */
	public static <K, V> String urlParamterStringer(String head, Map<K, V> map) {
		if (map == null || map.isEmpty()) {
			return "";
		}
		int capacity = map.size() * 30; // 设置表单长度30字节*N个请求参数
		// 参数不为空，在URL后面添加head（“？”）
		StringBuilder buffer = new StringBuilder(capacity);
		if (!map.isEmpty()) {
			buffer.append(head);
		}
		// 取出Map里面的请求参数，添加到表单String中。每个参数之间键值对之间用“=”连接，参数与参数之间用“&”连接
		Iterator<Entry<K, V>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<K, V> entry = it.next();
			Object key = entry.getKey();
			if( key.equals("class") ){
				continue ;
			}
			Object value = entry.getValue();
			if( value == null ){
				continue ;
			}
			buffer.append(key);
			buffer.append('=');
			buffer.append(value);
			if (it.hasNext()) {
				buffer.append("&");
			}
		}
		return buffer.toString();
	}


	/**
	 * 根据url获取inputStream
	 * @param urls
	 * @return
	 * @throws Exception
	 */
	public static InputStream getInputStream(String urls)throws Exception{
		if(StringUtils.isEmpty(urls))
			return null;
		// 构造URL
		URL url = new URL(urls);
		// 打开连接
		URLConnection con = url.openConnection();
		// 输入流
		InputStream is = con.getInputStream();
		return is;
	}

	/**
	 * 根据流获取byte
	 * @param inputStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] getbyteByInputStream(InputStream inputStream)throws Exception{
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024*4];
		int n = 0;
		while (-1 != (n = inputStream.read(buffer))) {
		output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}

	public static void main(String[] args) throws Exception{
		String t1="http://192.168.15.150:8000/group2/M00/00/05/wKgPmV2Jwf2ADNfNAAkVVNxhL1M467.jpg";
		String t2="http://192.168.15.150:8000/group2/M00/00/05/wKgPmV2Jwf2ADNfNAAkVVNxhL1M467.jpg";
		String t3="http://192.168.15.150:8000/group1/M00/00/02/wKgPl12J0R6AA7MjAAvqHw7LRwM792.jpg";
		String t4="http://192.168.15.150:8000/group2/M00/00/06/wKgPmV2LF_aAZg5PAAvea91MVvo753.jpg";
		String t5="http://192.168.15.150:8000/group1/M00/00/02/wKgPl12LF_KANty8AAzodXNDJVY288.jpg";

		List<String> lst=new ArrayList<>();
		lst.add(t1);
		lst.add(t2);
		lst.add(t3);
		lst.add(t4);
		lst.add(t5);
		int i=0;
		for (String s : lst) {
			// 构造URL
			URL url = new URL(s);
			// 打开连接
			URLConnection con = url.openConnection();
			// 输入流
			InputStream is = con.getInputStream();
			// 1K的数据缓冲
			byte[] bs = new byte[1024];
			// 读取到的数据长度
			int len;
			// 输出的文件流
			String filename = "D:\\pic/" + ++i + ".jpg";  //下载路径及下载图片名称
			File file = new File(filename);
			FileOutputStream os = new FileOutputStream(file, true);
			// 开始读取
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
			System.out.println(i);
			// 完毕，关闭所有链接
			os.close();
			is.close();
		}

	}
}
