package com.ljm.controller.wechat;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @author Created by liangjiaming on 2023/6/28
 * @title
 * @Desc
 */
@RestController
@RequestMapping("wx/")
@Slf4j
public class OfficialAccountsController {

    /**
     * 微信服务器认证 token=ljm123456
     * @param request
     * @param response
     */
    @GetMapping("/receive")
    public void getReceive(ServletRequest request, ServletResponse response) {
        log.info("getReceive");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        log.debug("timestamp:{},nonce:{},echostr:{},signature:{}", timestamp, nonce, echostr, signature);
        Set<String> sortSet = new HashSet<>();
        sortSet.add(timestamp);
        sortSet.add(nonce);
        sortSet.add(echostr);
        StringJoiner joiner = new StringJoiner("");
        sortSet.forEach(s -> joiner.add(s));
        // 计算签名
        String signStr = getShaString(joiner.toString());
        if (signature.equals(signStr) ) {
            log.info("验签成功！");
        } else {
            log.info("验签不通过！");
        }
        String resStr = echostr;
        PrintWriter writer = null;
        try {
            response.getWriter();
            writer.write(resStr);
            writer.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    @PostMapping("/receive")
    public void PostReceive(String msg) {
        log.info("request message:{}", msg);
        String answer = "<xml>\n" +
                " <ToUserName><![CDATA[粉丝号]]></ToUserName>\n" +
                " <FromUserName><![CDATA[公众号]]></FromUserName>\n" +
                " <CreateTime>1460541339</CreateTime>\n" +
                " <MsgType><![CDATA[text]]></MsgType>\n" +
                " <Content><![CDATA[hello!]]></Content>\n" +
                "</xml>";
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
            e.printStackTrace();
        }

        return null;
    }

}
