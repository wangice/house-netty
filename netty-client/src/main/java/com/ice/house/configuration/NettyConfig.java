package com.ice.house.configuration;

import com.ice.house.client.StringProtocolInitalizer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author:ice
 * @Date: 2018/8/11 16:24
 */
@Configuration
public class NettyConfig {

  @Value("${boss.thread.count}")
  private int bossCount;

  @Value("${tcp.port}")
  private int tcpPort;

  @Value("${host}")
  private String port;


  @Autowired
  @Qualifier("springProtocolInitializer")
  private StringProtocolInitalizer protocolInitalizer;

  @Bean(value = "bootstrap")
  public Bootstrap bootstrap() {
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(bossGroup()).channel(NioSocketChannel.class)
        .handler(protocolInitalizer);
    Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();
    Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
    for (@SuppressWarnings("rawtypes")
        ChannelOption option : keySet) {
      bootstrap.option(option, tcpChannelOptions.get(option));
    }

    return bootstrap;
  }

  @Bean(name = "tcpChannelOptions")
  public Map<ChannelOption<?>, Object> tcpChannelOptions() {
    Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
    options.put(ChannelOption.TCP_NODELAY, true);
    return options;
  }

  @Bean(name = "stringEncoder")
  public StringEncoder stringEncoder() {
    return new StringEncoder();
  }

  @Bean(name = "stringDecoder")
  public StringDecoder stringDecoder() {
    return new StringDecoder();
  }

  @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
  public NioEventLoopGroup bossGroup() {
    return new NioEventLoopGroup(bossCount);
  }
}
