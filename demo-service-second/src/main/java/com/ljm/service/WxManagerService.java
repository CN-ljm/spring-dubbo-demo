package com.ljm.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ljm.base.utils.HttpClientUtil;
import com.ljm.dto.wx.WxApiDataProxyDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @author create by jiamingl on 下午3:29
 * @title 微信服务
 * @desc
 */
@Service
@Slf4j
public class WxManagerService {

    @Value("${wx.access.grantType:client_credential}")
    private String grantType;
    @Value("${wx.access.appId:wx1bdcdcbfda83cc90}")
    private String appId;
    @Value("${wx.access.secret:1a1cec36642eb4756fc3a9a98863c5e9}")
    private String secret;
    @Value("${wx.access.forceRefresh:false}")
    private boolean forceRefresh;
    @Value("${wx.access.tokenUrl:https://api.weixin.qq.com/cgi-bin/stable_token}")
    private String tokenUrl;

    /** 微信接入token **/
    private volatile static String accessToken = null;
    /** 过期时间 **/
    private volatile static int expireIn = 0;
    /** 开始时间 **/
    private volatile static long startTime = 0;
    /** 一个锁 **/
    private Object lock = new Object();

    /**
     * 请求微信接口
     * @param dataProxy
     * @return
     */
    public String invokeWxApi(WxApiDataProxyDTO dataProxy) {
        String url = dataProxy.getUrl();
        JSONObject data = dataProxy.getData();
        log.info("请求微信接口，url:{}，data:{}", url, data.toJSONString());
        String resMsg = "fail";
        try {
            Map<String, String> reqHeader = new HashMap<>();
            reqHeader.put("Content-Type", "application/json");
            Map<String, String> urlParamMap = new HashMap<>();
            urlParamMap.put("access_token", getAccessToken());
            resMsg = HttpClientUtil.doPost(url, reqHeader, urlParamMap, data.toJSONString());
            log.info("请求微信接口，返回:{}", resMsg);
            return resMsg;
        } catch (Exception e) {
            resMsg = e.getMessage();
            log.error("请求微信接口失败：{}", e.getMessage(), e);
        }
        return resMsg;
    }

    /**
     * 获取微信accessToken
     * @return
     */
    public String getAccessToken() {
        // 为空或者过期
        boolean expireFlag = Instant.now().getEpochSecond() - startTime > expireIn + 5*60;
        if (!StringUtils.hasText(accessToken) || expireFlag) {
            synchronized (lock) {
                // 再判断一次过期
                boolean flag = Instant.now().getEpochSecond() - startTime > expireIn + 5*60;
                if (flag) {
                    // 获取accessToken
                    obtainAccessToken();
                    startTime = Instant.now().getEpochSecond();
                }
            }
        }
        // 判空
        if (!StringUtils.hasText(accessToken)) {
            throw new  RuntimeException("获取微信accessToken失败！");
        }

        return accessToken;
    }


    /**
     * 获取微信接入token
     */
    private void obtainAccessToken() {
        log.info("获取微信接入token");
        Map<String, Object> param = new HashMap<>();
        param.put("grant_type", grantType);
        param.put("appid", appId);
        param.put("secret", secret);
        param.put("force_refresh", forceRefresh);
        Map<String, String> reqHeader = new HashMap<>();
        reqHeader.put("Content-Type", "application/json");
        String tokenInfo = null;
        try {
            tokenInfo = HttpClientUtil.doPost(tokenUrl, reqHeader, null, JSON.toJSONString(param));
        } catch (URISyntaxException | IOException e) {
            log.error("获取token失败：{}", e.getMessage(), e);
        }
        if (!StringUtils.hasText(tokenInfo)) {
            return;
        }
        JSONObject tokenObj = JSON.parseObject(tokenInfo);
        // 失败返回 {"errcode":40013,"errmsg":"invalid appid"}
        String errCode = tokenObj.getString("errcode");
        if (StringUtils.hasText(errCode)) {
            log.error("获取token失败，errcode:{}，errmsg:{}", errCode, tokenObj.getString("errmsg"));
            return;
        }
        // 成功返回 {"access_token":"ACCESS_TOKEN","expires_in":7200}
        accessToken = tokenObj.getString("access_token");
        expireIn = tokenObj.getInteger("expires_in");

    }


    /**
     * sha1哈希计算
     * @return
     */
    private String getShaString(String str) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
            byte[] digest = sha.digest(str.getBytes());
            // 二进制转16进制
            return Hex.encodeHexString(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error("哈希签名计算失败：{}", e.getMessage(), e);
        }

        return null;
    }
}
