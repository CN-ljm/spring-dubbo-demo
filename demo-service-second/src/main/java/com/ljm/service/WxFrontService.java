package com.ljm.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;

/**
 * @author create by jiamingl on 下午3:29
 * @title 微信服务
 * @desc
 */
@Service
@Slf4j
public class WxFrontService {

    @Value("${wx.auth.token:123456}")
    private String authToken;

    /**
     * 服务器认证
     * @param timestamp
     * @param nonce
     * @param echoStr
     * @param signature
     * @return
     */
    public String authService(String timestamp, String nonce, String echoStr, String signature) {
        log.debug("服务器认证，timestamp:{}，nonce:{}，echostr:{}，signature:{}", timestamp, nonce, echoStr, signature);
        Set<String> sortSet = new TreeSet<>();
        sortSet.add(timestamp);
        sortSet.add(nonce);
        sortSet.add(authToken);
        StringJoiner joiner = new StringJoiner("");
        sortSet.forEach(s -> joiner.add(s));
        String resStr = "";
        // 计算签名
        String signStr = getShaString(joiner.toString());
        log.info("计算的签名结果：{}", signStr);
        if (signature.equals(signStr) ) {
            log.info("验签成功！");
            resStr = echoStr;
        }
        return resStr;
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
