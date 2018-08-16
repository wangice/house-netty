package com.ice.house.core;

import com.ice.house.task.actor.Actor;
import com.ice.house.task.schedule.TscTimerMgr;
import io.netty.channel.ChannelHandlerContext;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author:ice
 * @Date: 2018/8/16 0016
 */
@Component
public class ModbusWorker extends Actor {

  private static final Logger logger = LoggerFactory.getLogger(ModbusWorker.class);

  @Autowired
  private TscTimerMgr tscTimerMgr;

  public Map<ChannelHandlerContext, ActorNet> ans = new HashMap<>();

  public ModbusWorker() {
    super(ActorType.ITC);
  }

  /**
   * 添加一个ActorNet连接
   */
  public boolean addActorNet(ActorNet an) {
    ActorNet oldActor = this.ans.get(an.ctx);
    if (oldActor != null) {
      logger.error("it is a bug,old:{},new{}", oldActor, an);
      return false;
    }
    //添加一个事件去检查，是否为僵尸连接，如果是，则移除
    tscTimerMgr.addTimer(10/*等待10s*/, v -> {
      if (an.lts != 0) {/* 已经收到过消息. */
        return false;
      }
      if (!an.est) {
        return false;
      }
      logger.warn("got a zombie n2h connection:{}", an);
      this.removeActorNet(an);
      return false;
    });
    this.ans.put(an.ctx, an);
    return true;
  }

  /**
   * 关闭一个ActorNet连接
   */
  public void removeActorNet(ActorNet an) {
    this.ans.remove(an.ctx);
    an.ctx.close();//关闭连接
    an.est = false;
  }
}
