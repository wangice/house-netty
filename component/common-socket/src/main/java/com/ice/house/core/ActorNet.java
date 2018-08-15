package com.ice.house.core;

import com.ice.house.task.actor.Actor;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author:ice
 * @Date: 2018/8/14 21:20
 */
public abstract class ActorNet extends Actor {


  public long gts = 0L;//连接建立时间.

  private ChannelHandlerContext ctx;//网络连接标识

  public ActorNet(ActorType type, ChannelHandlerContext ctx) {
    super(type);
    this.ctx = ctx;
  }

  /**
   * 失去连接
   */
  public abstract void evnDis();
}
