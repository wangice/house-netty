package com.ice.house.server;

import com.ice.house.Misc;
import com.ice.house.core.ActorNet;
import com.ice.house.core.ModbusWorker;
import com.ice.house.core.Tsc;
import com.ice.house.modbus.ModbusN2H;
import com.ice.house.model.DeviceInfo;
import com.ice.house.msg.ModbusMsg;
import com.ice.house.service.impl.DeviceInfoRedisServiceImpl;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javax.annotation.Resource;
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
  private Tsc tsc;

  @Resource
  private DeviceInfoRedisServiceImpl deviceInfoRedisService;

  @Override
  public void channelRead0(ChannelHandlerContext ctx, ModbusMsg msg)
      throws Exception {
    ModbusWorker worker = tsc.getWorker(ctx.channel().id().asLongText());
    worker.push(v -> {
      ModbusN2H modbusN2H = (ModbusN2H) worker.ans.get(ctx);
      modbusN2H.lts = System.currentTimeMillis();
      modbusN2H.evnMsg(msg);//读取某个消息
    });
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    log.info("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
    ModbusWorker worker = tsc.getWorker(ctx.channel().id().asLongText());
    worker.push(c -> {
      //
      ActorNet an = new ModbusN2H(ctx, worker);
      tsc.getWorker(ctx.channel().id().asLongText()).addActorNet(an);
      tsc.deviceLog.n2hEstab(an);
    });
  }


  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    log.info("抛出异常{}", Misc.trace(cause));
    log.info("id:{}", ctx.channel().id().asLongText());
    ModbusWorker modbusWorker = tsc.getWorker(ctx.channel().id().asLongText());
    modbusWorker.push(c -> {
      ModbusN2H modbusN2H = (ModbusN2H) modbusWorker.ans.get(ctx);
      modbusWorker.removeActorNet(modbusN2H);
    });
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    log.info("Channel is disconnected");
    ModbusWorker modbusWorker = tsc.getWorker(ctx.channel().id().asLongText());
    modbusWorker.push(c -> {
      ModbusN2H modbusN2H = (ModbusN2H) modbusWorker.ans.get(ctx);
      modbusWorker.removeActorNet(modbusN2H);
    });
    super.channelInactive(ctx);
  }


}
