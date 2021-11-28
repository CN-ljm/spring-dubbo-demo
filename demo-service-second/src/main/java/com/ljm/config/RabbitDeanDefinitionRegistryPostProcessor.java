package com.ljm.config;

import com.ljm.constant.RabbitMQQueueEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;

/**
 * @author Created by liangjiaming on 2021/1/8
 * @title
 * @Desc
 */
@Configuration
@Slf4j
public class RabbitDeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("postProcessBeanDefinitionRegistry");
        for (RabbitMQQueueEnum queue: RabbitMQQueueEnum.values()) {
            // 队列
            AbstractBeanDefinition queueDefinition = BeanDefinitionBuilder.genericBeanDefinition(Queue.class).addConstructorArgValue(queue.getQueueName()).getBeanDefinition();
//            queueDefinition.getPropertyValues().addPropertyValue("name", queue.getQueueName());
            registry.registerBeanDefinition(queue.getQueueName(), queueDefinition);
            // 交换机
            AbstractBeanDefinition exchangeDefinition = BeanDefinitionBuilder.genericBeanDefinition(DirectExchange.class).addConstructorArgValue(queue.getExchange()).getBeanDefinition();
//            exchangeDefinition.getPropertyValues().addPropertyValue("name", queue.getExchange());
            registry.registerBeanDefinition(queue.getExchange(), exchangeDefinition);
            // 绑定关系
            AbstractBeanDefinition bindingDefinition = BeanDefinitionBuilder.genericBeanDefinition(Binding.class)
                    .addConstructorArgValue(queue.getQueueName())
                    .addConstructorArgValue(Binding.DestinationType.QUEUE)
                    .addConstructorArgValue(queue.getExchange())
                    .addConstructorArgValue(queue.getRoutingKey())
                    .addConstructorArgValue(null).getBeanDefinition();
            /*bindingDefinition.getPropertyValues().addPropertyValue("destination", queue.getQueueName());
            bindingDefinition.getPropertyValues().addPropertyValue("destinationType", Binding.DestinationType.QUEUE);
            bindingDefinition.getPropertyValues().addPropertyValue("exchange", queue.getExchange());
            bindingDefinition.getPropertyValues().addPropertyValue("routingKey", queue.getRoutingKey());*/
            registry.registerBeanDefinition("binding." + queue.getQueueName(), bindingDefinition);
        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("RabbitDeanDefinitionRegistryPostProcessor#postProcessBeanFactory");
        Queue queue = new Queue("autoRQueue");
        beanFactory.registerSingleton("queue", queue);
        DirectExchange exchange = new DirectExchange("autoRExchange");
        beanFactory.registerSingleton("exchange", exchange);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with("auto.key");
        beanFactory.registerSingleton("binding", binding);
    }
}
