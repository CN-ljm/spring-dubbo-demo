package com.ljm.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/*
 * @author Created by liangjiaming on 2021/1/6
 * @title
 * @Desc
 */

@Service
@Slf4j
public class MsgConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "test")
    @RabbitHandler
    public void consumerMsg(Message msg) throws InterruptedException, UnsupportedEncodingException, JsonProcessingException {
        log.info("消息的内容是：{}", msg);
        log.info("收到消息，消息内容是：{}", new String(msg.getBody(), "utf-8"));
        log.info("收到消息，消息配置是：{}", JSON.toJSONString(msg.getMessageProperties()));

        log.info("消息配置：{}", JSON.toJSONString(msg.getMessageProperties()));
        Object obj = rabbitTemplate.getMessageConverter().fromMessage(msg);
        log.info("Object:{}", obj.toString());

        ObjectMapper mapper = new ObjectMapper();
//        String objStr = mapper.writeValueAsString(msg);
        String objStr = new String(msg.getBody(), msg.getMessageProperties().getContentEncoding());
        log.info("消息内容：{}", objStr);

        Thread.currentThread().sleep(1000*10);


    }

    @RabbitListener(queues = "deadLetter.test")
    @RabbitHandler
    public void consumerDeadLetterMsg(Message msg) throws UnsupportedEncodingException {

        Object obj = rabbitTemplate.getMessageConverter().fromMessage(msg);

//        String msgBody = new String(msg.getBody(), "utf-8");
        System.out.println("死信消息，消息内容是：" + obj.toString());
        System.out.println("死信消息，消息配置是：" + JSON.toJSONString(msg.getMessageProperties()));
    }

}
