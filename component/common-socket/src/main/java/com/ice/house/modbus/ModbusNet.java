package com.ice.house.modbus;

import com.ice.house.Misc;
import com.ice.house.core.ActorNet;
import com.ice.house.msg.ModbusMsg;
import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author:ice
 * @Date: 2018/8/14 21:27
 */
public abstract class ModbusNet extends ActorNet {

    /**
     * 事务ID发生器.
     */
    public AtomicInteger tid = new AtomicInteger(Misc.randInt());


    public ModbusNet(ActorType type, ChannelHandlerContext ctx) {
        super(type, ctx);
    }

    /**
     * 消息事件.
     */
    public abstract boolean evnMsg(ModbusMsg modbus);
}
