package com.ljm.service;

import com.ljm.dto.MessageVo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/*
 * @author Created by liangjiaming on 2021/1/6
 * @title
 * @Desc
 */

@Service
public class MsgProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public void sendMsg(String msg) throws UnsupportedEncodingException {

        MessageVo vo = new MessageVo();
        vo.setId(System.currentTimeMillis());
        vo.setName("Jackson序列化消息");

        Map<String, Object> map = new HashMap<>();
        map.put("id",System.currentTimeMillis());
        map.put("name", "Jackson序列化消息Map");

//        rabbitTemplate.convertAndSend("direct.test","test.key", map);
    }

}
