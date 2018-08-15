package com.ice.house.modbus;

import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayDeque;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author:ice
 * @Date: 2018/8/14 21:33
 */
public class ModbusN2H extends ModbusNet {

  private static final Logger logger = LoggerFactory.getLogger(ModbusN2H.class);

  /**
   * 缓存的由libtsc主动发起的MODBUS事务(已发出).
   */
  public final HashMap<Short, ModbusN2Hitrans> trans = new HashMap<>();
  /**
   * 缓存的由libtsc主动发起的MODBUS事务(未发出).
   */
  public final ArrayDeque<ModbusN2Hitrans> waitSendTrans = new ArrayDeque<>();

  private boolean enableDirectSend = true;//直接发送标志.

  public ModbusN2H(ChannelHandlerContext ctx) {
    super(ActorType.ITC, ctx);
    this.gts = System.currentTimeMillis();
  }

  @Override
  public void evnDis() {

  }
}
