package com.ice.house.modbus;

import com.ice.house.Misc;
import com.ice.house.msg.ModbusMsg;
import io.netty.channel.ChannelHandlerContext;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
        Iterator<Map.Entry<Short, ModbusN2Hitrans>> iter = this.trans.entrySet().iterator();
        List<ModbusN2Hitrans> tmp = new ArrayList<>();
        while (iter.hasNext()) {
            tmp.add(iter.next().getValue()); /* 不要在这里调用t.tmCb.accept(t), 原因是在accept中可能有对this.modbusTits的并发操作, 并引起迭代器上的ConcurrentModificationException. */
            iter.remove();
        }
        for (ModbusN2Hitrans t : tmp) {
            t.tm = true;
            Misc.exeConsumer(t.tmCb, t);
        }
        while (!this.waitSendTrans.isEmpty()) {
            ModbusN2Hitrans t = this.waitSendTrans.pop();
            t.tm = true;
            t.gts = System.currentTimeMillis();
            t.lpts = t.gts;
            Misc.exeConsumer(t.tmCb, t);
        }
    }

    /**
     * 处理网络到达事件
     */
    public boolean evnMsg(ModbusMsg modbusMsg) {
        this.sendWaitTrans();//当收到消息时，立即出栈一个等待的事务
        //服务器主动下发事件.
        if (Modbus.serverFcode.contains(modbusMsg.header.fcode)) {
            ModbusN2Hitrans modbusN2Hitrans = this.trans.get(modbusMsg.header.tid);
            if (modbusN2Hitrans == null) {//找不到事务，可能已经超时被抛弃
                logger.debug("can not found modbusN2Hitrans for tid:{}，may be it is timeout", modbusMsg.header.tid);
                return true;
            }
            modbusN2Hitrans.lpts = System.currentTimeMillis();
            this.trans.remove(modbusMsg.header.tid);//从缓存中移除事务
            return true;
        }
        //客户端主动发起事务，直接执行
        logger.info("客户端主动发起事务，需要执行某些方法");
        return true;
    }

    /**
     * 发送一个正在排队的事务
     */
    public void sendWaitTrans() {
        if (this.waitSendTrans.isEmpty()) {/* 没有未完成的事务. */
            this.enableDirectSend = true;
            return;
        }
        this.enableDirectSend = false;
        ModbusN2Hitrans trans = this.waitSendTrans.pop();
        trans.gts = System.currentTimeMillis();
        trans.lpts = trans.gts;
        this.trans.put(trans.tid, trans);/*事务缓存*/
        this.send(trans.modbusMsg);
    }

}
