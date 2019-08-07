package com.by.blcu.core.utils;


import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

public class AsyncHttp {
    protected static Logger logger = Logger.getLogger(AsyncHttp.class);
    private static final Integer MAX_TOTAL = 100;             //连接池最大连接数
    private static final Integer MAX_PER_ROUTE = 10;          //单个路由默认最大连接数
    private static final Integer REQ_TIMEOUT = 5 * 1000;     //请求超时时间ms
    private static final Integer CONN_TIMEOUT = 5 * 1000;     //连接超时时间ms
    private static final Integer SOCK_TIMEOUT = 10 * 1000;    //读取超时时间ms
    /**
     * http链接池
     */
    private static PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = null;
    /**
     * 请求头配置
     */
    private static RequestConfig requestConfig = null;

    /**
     *管理http链接池的线程
     * 现在的管理策略是每隔5秒钟关闭过期的链接，关闭空闲30秒的链接
     */
    private static HttpClientConnectionMonitorThread thread=null;


    //初始http化线程池支持https调用
    static {
        LayeredConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", sslsf)
                .register("http", new PlainConnectionSocketFactory())
                .build();
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolingHttpClientConnectionManager.setMaxTotal(MAX_TOTAL);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);
        requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(REQ_TIMEOUT)
                .setConnectTimeout(CONN_TIMEOUT).setSocketTimeout(SOCK_TIMEOUT)
                .build();
        AsyncHttp.thread = new HttpClientConnectionMonitorThread(poolingHttpClientConnectionManager); //管理 http连接
    }

    public static HttpClient createHttpClient() {
        return HttpClients.custom().setConnectionManager(poolingHttpClientConnectionManager).setDefaultRequestConfig(requestConfig).build();
    }
}