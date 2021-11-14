/*
package com.ljm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.GetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

*/
/**
 * @author Created by liangjiaming on 2021/1/6
 * @title
 * @Desc
 *//*

@Service
@Slf4j
public class RabbitMQMessageService {

    */
/*@Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private SimpleMessageListenerContainer messageListenerContainer;*//*


    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void getDealLetterQueue() throws IOException {
        Message receive = rabbitTemplate.receive("deadLetter.test");
        System.out.println("receive 消费消息：" + new String(receive.getBody(), "utf-8"));
    }

    public void reQueue() throws IOException {
        List<GetResponse> responseList = new ArrayList<>();
        Connection connection = rabbitTemplate.getConnectionFactory().createConnection();
        Channel channel = connection.createChannel(false);
        GetResponse response = channel.basicGet("deadLetter.test", false);
        while (response != null) {
            System.out.println("channel 消费消息：" + new String(response.getBody(), "utf-8"));
            responseList.add(response);
            response = channel.basicGet("deadLetter.test", false);
        }
        */
/*MessageProperties mp = new MessageProperties();
        rabbitTemplate.getMessageConverter().fromMessage()*//*


        // 每次消费第二条消息
        GetResponse targetRes = responseList.get(1);
        ObjectMapper mapper = new ObjectMapper();
        log.info("死信消息队列配置：{}", mapper.writeValueAsString(targetRes.getProps()));
//        rabbitTemplate.convertAndSend("direct.test", "test.key", targetRes.getBody());
        log.info("死信消息队列配置--消息对象类型Type：{}", targetRes.getProps().getType());
        log.info("死信消息队列配置--消息对象类型__TypeId__：{}", targetRes.getProps().getHeaders().get("__TypeId__").toString());
        channel.basicAck(targetRes.getEnvelope().getDeliveryTag(), false);
        responseList.remove(targetRes);

        for (GetResponse res: responseList) {
            channel.basicNack(res.getEnvelope().getDeliveryTag(), false, true);
        }

    }
}
*/
