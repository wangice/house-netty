package com.ice.house.configuration;

import com.ice.house.client.NettyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author ice
 * @Date 2019/4/20 13:14
 */
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {//避免执行两次
            NettyClient nettyClient = contextRefreshedEvent.getApplicationContext().getBean(NettyClient.class);
            if (!nettyClient.start()) {
                System.exit(0);
            }
        }
    }
}
