package com.ice.house.client;

import com.ice.house.Misc;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @author:ice
 * @Date: 2018/8/11 16:32
 */
@Component
public class NettyClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    @Value("${tcp.port}")
    private int tcpPort;

    @Value("${host}")
    private String host;

    @Autowired
    @Qualifier("bootstrap")
    private Bootstrap bootstrap;

    private ChannelFuture channelFuture;

    public boolean start() {

        try {
            channelFuture = bootstrap.connect(host, tcpPort).sync();
            logger.info("远程服务器已经连接，host:{},port:{}", host, tcpPort);
            return true;
        } catch (InterruptedException e) {
            logger.error("客户端启动失败，{}", Misc.trace(e));
            return false;
        }
    }

    @PreDestroy
    public void stop() throws InterruptedException {
        channelFuture.channel().closeFuture().sync();
    }

    public ChannelFuture getChannelFuture() throws InterruptedException {
        //如果管道没有被开启或者被关闭了，那么重连
        if (channelFuture == null) {
            this.start();
        }
        if (!channelFuture.channel().isActive()) {
            this.start();
        }
        return channelFuture;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }
}
