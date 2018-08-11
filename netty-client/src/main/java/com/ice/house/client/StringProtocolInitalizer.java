package com.ice.house.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author:ice
 * @Date: 2018/8/11 14:53
 */
@Component
@Qualifier("springProtocolInitializer")
public class StringProtocolInitalizer extends ChannelInitializer<SocketChannel> {

  @Autowired
  StringDecoder stringDecoder;

  @Autowired
  StringEncoder stringEncoder;

  @Autowired
  ServerHandler serverHandler;

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    pipeline.addLast("decoder", stringDecoder);
    //超时handler（当服务器端与客户端在指定时间以上没有任何进行通信，则会关闭响应的通道，主要为减小服务端资源占用）
    pipeline.addLast(new ReadTimeoutHandler(30));
    pipeline.addLast("handler", serverHandler);
    pipeline.addLast("encoder", stringEncoder);
  }

  public StringDecoder getStringDecoder() {
    return stringDecoder;
  }

  public void setStringDecoder(StringDecoder stringDecoder) {
    this.stringDecoder = stringDecoder;
  }

  public StringEncoder getStringEncoder() {
    return stringEncoder;
  }

  public void setStringEncoder(StringEncoder stringEncoder) {
    this.stringEncoder = stringEncoder;
  }

  public ServerHandler getServerHandler() {
    return serverHandler;
  }

  public void setServerHandler(ServerHandler serverHandler) {
    this.serverHandler = serverHandler;
  }

}
