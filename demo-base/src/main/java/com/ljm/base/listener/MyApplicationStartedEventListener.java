package com.ljm.base.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author Created by liangjiaming on 2020/7/1
 * @title
 * @Desc
 */
public class MyApplicationStartedEventListener implements ApplicationListener<ApplicationStartedEvent> {

    private static final Logger log = LoggerFactory.getLogger(MyApplicationStartedEventListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.debug("MyApplicationStartedEventListener");
//        System.out.println("");
    }
}
