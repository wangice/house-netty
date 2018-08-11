package com.ice.house.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

  @PostConstruct
  public void start() throws Exception {
    channelFuture = bootstrap.connect(host, tcpPort).sync();
  }

  @PreDestroy
  public void stop() throws Exception {
    channelFuture.channel().closeFuture().sync();
  }




}
