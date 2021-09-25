package com.ljm.base.utils;

import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;
import javafx.print.PaperSource;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author create by jiamingl on 下午2:00
 * @title http 请求工具类
 * @desc
 */
public class HttpClientUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);

    private static CloseableHttpClient httpClient = null;

    private static final Object lock = new Object();

    /**
     * http get
     * @param url 请求路径
     * @param reqHeader 请求头
     * @param paramMap 请求参数
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public static String doGet(String url, Map<String, String> reqHeader, Map<String, String> paramMap) throws URISyntaxException, IOException {
        log.info("http get,url:{},reqHeader:{},paramMap:{}", url, reqHeader, paramMap);
        String res = null;
        URIBuilder uri = new URIBuilder(url);
        Map<String, String> params = Optional.ofNullable(paramMap).orElse(Collections.emptyMap());
        for (Map.Entry<String, String> entry: params.entrySet()) {
            uri.addParameter(entry.getKey(), entry.getValue());
        }
        HttpGet get = new HttpGet(uri.build());
        Map<String, String> header = Optional.ofNullable(reqHeader).orElse(Collections.emptyMap());
        for (Map.Entry<String, String> entry: header.entrySet()) {
            get.addHeader(entry.getKey(), entry.getValue());
        }
        try(CloseableHttpResponse response = getClient().execute(get)) {
            log.info("StatusLine:{}", response.getStatusLine());
            log.info("AllHeaders:{}", Arrays.toString(response.getAllHeaders()));
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                res = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
            log.info("res:{}", res);
            return res;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    /**
     * http post
     * @param url 请求资源
     * @param reqHeader 请求头
     * @param urlParamMap URL参数
     * @param message 请求体参数
     * @return
     * @throws URISyntaxException
     * @throws IOException
     */
    public static String doPost(String url, Map<String, String> reqHeader, Map<String, String> urlParamMap, String message) throws URISyntaxException, IOException {
        log.info("http post,url:{},reqHeader:{},urlParamMap:{},message:{}", url, reqHeader, urlParamMap, message);
        String res = null;
        URIBuilder uri = new URIBuilder(url);
        Map<String, String> params = Optional.ofNullable(urlParamMap).orElse(Collections.emptyMap());
        for (Map.Entry<String, String> entry: params.entrySet()) {
            uri.addParameter(entry.getKey(), entry.getValue());
        }
        HttpPost post = new HttpPost(uri.build());
        Map<String, String> header = Optional.ofNullable(reqHeader).orElse(Collections.emptyMap());
        for (Map.Entry<String, String> entry: header.entrySet()) {
            post.addHeader(entry.getKey(), entry.getValue());
        }
        post.setEntity(new StringEntity(message, StandardCharsets.UTF_8));
        try(CloseableHttpResponse response = getClient().execute(post)) {
            log.info("StatusLine:{}", response.getStatusLine());
            log.info("AllHeaders:{}", Arrays.toString(response.getAllHeaders()));
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                res = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            }
            log.info("res:{}", res);
            return res;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    private static CloseableHttpClient getClient() {
        if (httpClient == null) {
            synchronized (lock) {
                if (httpClient == null) {
                    RequestConfig requestConfig = RequestConfig.custom()
                            .setConnectionRequestTimeout(10*1000)
                            .setConnectTimeout(30*1000)
                            .setSocketTimeout(2*60*1000)
                            .build();
                    ConnectionConfig connectionConfig = ConnectionConfig.custom()
                            .setCharset(StandardCharsets.UTF_8)
                            .build();

                    httpClient = HttpClientBuilder.create()
                            .setConnectionManager(new PoolingHttpClientConnectionManager(10, TimeUnit.SECONDS))
                            .setDefaultRequestConfig(requestConfig)
                            .setDefaultConnectionConfig(connectionConfig)
                            .build();
                }
            }
        }

        return httpClient;
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        String getUrl = "http://127.0.0.1:8082/test/getText";
        Map<String, String> params = new HashMap<>();
        params.put("username", "123456@sz.com");
        params.put("password", "123456");
        String getRes = doGet(getUrl, null, params);
        System.out.println(getRes);

        String postUrl = "http://127.0.0.1:8082/test/postText";

        String postRes = doPost(postUrl, null, null, "post context");
        System.out.println(postRes);
    }

}
