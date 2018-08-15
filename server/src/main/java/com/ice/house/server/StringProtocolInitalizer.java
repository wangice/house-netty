package com.ice.house.server;

import com.ice.house.codec.ModBusMsgDecode;
import com.ice.house.codec.ModBusMsgEncode;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
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
  private static final int MAX_FRAME_LENGTH = 1024 * 1024;  //最大长度
  private static final int LENGTH_FIELD_LENGTH = 4;  //长度字段所占的字节数
  private static final int LENGTH_FIELD_OFFSET = 3;  //长度偏移

  @Autowired
  StringDecoder stringDecoder;

  @Autowired
  StringEncoder stringEncoder;

  @Autowired
  ServerHandler serverHandler;

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline pipeline = ch.pipeline();
    pipeline.addLast("decoder", new ModBusMsgDecode(MAX_FRAME_LENGTH,LENGTH_FIELD_OFFSET,LENGTH_FIELD_LENGTH));
    pipeline.addLast("handler", serverHandler);
    pipeline.addLast("encoder", new ModBusMsgEncode());
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
