package com.ljm;

import com.ljm.service.Handle;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 应用启动类
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, RabbitAutoConfiguration.class})
public class SecondApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SecondApplication.class, args);
        SecondApplication bean = run.getBean(SecondApplication.class);

        Handle h = run.getBean(Handle.class);

        System.out.println(h.getClass() == Handle.class);

    }
}
