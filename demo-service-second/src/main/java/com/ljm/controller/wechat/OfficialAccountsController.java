package com.ljm.controller.wechat;

import com.ljm.service.WxFrontService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Created by liangjiaming on 2023/6/28
 * @title 微信前端服务器，对接微信推送消息
 * @Desc
 */
@RestController
@RequestMapping("/wx")
@Slf4j
public class OfficialAccountsController {

    @Autowired
    private WxFrontService wxService;

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
        String echoStr = request.getParameter("echostr");
        log.debug("timestamp:{},nonce:{},echostr:{},signature:{}", timestamp, nonce, echoStr, signature);
        String resStr = wxService.authService(timestamp, nonce, echoStr, signature);
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
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

}
