package com.ice.house.configuration;

import com.ice.house.core.Tsc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author:ice
 * @Date: 2018/8/23 0023
 */
@Component
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        logger.info("SpringBoot加载完成");
        if (contextRefreshedEvent.getApplicationContext().getParent() == null) {//避免执行两次
            //容器加载完成之后，获取Tsc
            Tsc tsc = contextRefreshedEvent.getApplicationContext().getBean(Tsc.class);
            //定时器震荡
            tsc.hold();
        }
    }
}
