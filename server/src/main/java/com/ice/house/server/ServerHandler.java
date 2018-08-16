package com.ice.house.server;

import com.ice.house.core.ActorNet;
import com.ice.house.core.ModbusWorker;
import com.ice.house.modbus.ModbusN2H;
import com.ice.house.msg.ModbusMsg;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author:ice
 * @Date: 2018/8/11 14:56
 */
@Sharable
@Component
@Qualifier("serverHandler")
public class ServerHandler extends SimpleChannelInboundHandler<ModbusMsg> {

  private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

  @Autowired
  private ModbusWorker modbusWorker;

  @Override
  public void channelRead0(ChannelHandlerContext ctx, ModbusMsg msg)
      throws Exception {
    
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    log.info("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");

    ActorNet an = new ModbusN2H(ctx);
    modbusWorker.addActorNet(an);
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
