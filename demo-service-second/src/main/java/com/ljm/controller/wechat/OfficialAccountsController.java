package com.ljm.controller.wechat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Created by liangjiaming on 2023/6/28
 * @title
 * @Desc
 */
@RestController
@RequestMapping("wx/OfficialAccount")
@Slf4j
public class OfficialAccountsController {

    @PostMapping("/autoReply")
    public String autoReply(String msg) {
        log.info("request message:{}", msg);
        String answer = "<xml>\n" +
                " <ToUserName><![CDATA[粉丝号]]></ToUserName>\n" +
                " <FromUserName><![CDATA[公众号]]></FromUserName>\n" +
                " <CreateTime>1460541339</CreateTime>\n" +
                " <MsgType><![CDATA[text]]></MsgType>\n" +
                " <Content><![CDATA[hello!]]></Content>\n" +
                "</xml>";
        return answer;
    }

}
