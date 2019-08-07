package com.by.blcu.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
			post.addHeader("token",token);
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
			httpGet.setHeader("User-Agent",userAgent);					//需要加这个参数，否则返回403 状态吗
			
			httpResponse = createHttpClient().execute( httpGet );				//将get请求交给HttpClient 请求去执行
		} catch (IOException e) {
			logger.error("HTTPGET 请求出现异常：" + e.getMessage() ) ;
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
						Charset.forName("UTF-8")); // 将返回的数据解析成一个字符串
				logger.info( "请求处理的结果----》" + result );
				responsemap = objectMapper.readValue(  result , Map.class ); //json转换成map
			}else{
				result = EntityUtils.toString(httpResponse.getEntity(),
						Charset.forName("UTF-8")); // 将返回的数据解析成一个字符串
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
						Charset.forName("UTF-8")); // 将返回的数据解析成一个字符串
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
						Charset.forName("UTF-8")); // 将返回的数据解析成一个字符串
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
						Charset.forName("UTF-8")); // 将返回的数据解析成一个字符串
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
						Charset.forName("UTF-8")); // 将返回的数据解析成一个字符串
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
}
