package com.ice.house.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author:ice
 * @Date: 2018/8/11 15:07
 */
@Component
public class NettyServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    @Autowired
    @Qualifier("serverBootstrap")
    private ServerBootstrap bootstrap;

    @Autowired
    @Qualifier("tcpSocketAddress")
    private InetSocketAddress tcpPort;

    @Autowired
    @Qualifier("httpSocketAddress")
    private InetSocketAddress httpPort;

    private ChannelFuture serverChannelFuture;
    private ChannelFuture httpChannerlFuture;

    @PostConstruct
    public void start() throws Exception {
        System.out.println("Starting server at " + tcpPort);
        serverChannelFuture = bootstrap.bind(tcpPort).sync();
        httpChannerlFuture = bootstrap.bind(httpPort).sync();
    }

    @PreDestroy
    public void stop() throws Exception {
        serverChannelFuture.channel().closeFuture().sync();
        httpChannerlFuture.channel().closeFuture().sync();
    }

    public ServerBootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(ServerBootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public InetSocketAddress getTcpPort() {
        return tcpPort;
    }

    public void setTcpPort(InetSocketAddress tcpPort) {
        this.tcpPort = tcpPort;
    }

}
