package com.ice.house.core;

import com.ice.house.task.actor.Actor;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author:ice
 * @Date: 2018/8/14 21:20
 */
public abstract class ActorNet extends Actor {

  /**
   * 连接是否已建立.
   */
  public boolean est = false;

  public long gts = 0L;//连接建立时间.
  /**
   * 最后收到消息(指的是一个完整的消息, 而不是一些字节. 原因在于协议片段报文会一直缓存在buffer中, 不被上层处理)的时间戳(毫秒).
   */
  public long lts = 0L;

  public ChannelHandlerContext ctx;//网络连接标识

  public ActorNet(ActorType type, ChannelHandlerContext ctx) {
    super(type);
    this.ctx = ctx;
  }

  /**
   * 失去连接
   */
  public abstract void evnDis();
}
