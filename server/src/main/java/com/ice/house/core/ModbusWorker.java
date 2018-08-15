package com.ice.house.core;

import com.ice.house.task.actor.Actor;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:ice
 * @Date: 2018/8/16 0016
 */
@Component
public class ModbusWorker extends Actor {

    private static final Logger logger = LoggerFactory.getLogger(ModbusWorker.class);

    public Map<ChannelHandlerContext, ActorNet> ans = new HashMap<>();

    public ModbusWorker() {
        super(ActorType.ITC);
    }

    public boolean addActorNet(ActorNet an) {
        ActorNet oldActor = this.ans.get(an.ctx);
        if (oldActor != null) {
            logger.error("it is a bug,old:{},new{}", oldActor, an);
            return false;
        }
        //添加一个事件去检查，是否为僵尸连接，如果是，则移除
        this.ans.put(an.ctx, an);
        return true;
    }
}
