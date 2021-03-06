package com.ice.house.server;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.net.InetAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author:ice
 * @Date: 2018/8/11 14:56
 */
@Sharable
@Component
@Qualifier("serverHandler")
public class ServerHandler extends SimpleChannelInboundHandler<String> {

  private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

  @Override
  public void channelRead0(ChannelHandlerContext ctx, String msg)
      throws Exception {
    log.info("client msg:" + msg);
    String clientIdToLong = ctx.channel().id().asLongText();
    log.info("client long id:" + clientIdToLong);
    String clientIdToShort = ctx.channel().id().asShortText();
    log.info("client short id:" + clientIdToShort);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {

    log.info("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");

    ctx.channel()
        .writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n");

    super.channelActive(ctx);
  }


  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    log.info("\nChannel is disconnected");
    super.channelInactive(ctx);
  }


}
