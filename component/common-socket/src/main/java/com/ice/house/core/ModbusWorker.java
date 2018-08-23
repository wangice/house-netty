package com.ice.house.core;

import com.ice.house.modbus.ModbusN2H;
import com.ice.house.modbus.ModbusN2Hitrans;
import com.ice.house.task.actor.ActorBlocking;
import com.ice.house.task.schedule.TscTimerMgr;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author:ice
 * @Date: 2018/8/16 0016
 */
public class ModbusWorker extends ActorBlocking {

    private static final Logger logger = LoggerFactory.getLogger(ModbusWorker.class);

    public TscTimerMgr tscTimerMgr;

    public ConcurrentHashMap<ChannelHandlerContext, ActorNet> ans = new ConcurrentHashMap<>();


    public ModbusWorker() {
        tscTimerMgr = new TscTimerMgr();
        tscTimerMgr.init();
    }


    /**
     * 关闭一个ActorNet连接
     */
    public void removeActorNet(ActorNet an) {
        if (an == null) {
            logger.info("连接已经关闭了");
            return;
        }
        logger.info("关闭连接");
        this.ans.remove(an.ctx);
        an.ctx.close();//关闭连接
        an.est = false;
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
        logger.info("ModbusWorker addActorNet : {}", an.ctx.channel().id().asLongText());
        this.ans.put(an.ctx, an);
        return true;
    }

    /** ---------------------------------------------------------------- */
    /**                                                                  */
    /** 任务. */
    /**                                                                  */
    /** ---------------------------------------------------------------- */

    /**
     * 检查N2H心跳超时的间隔时间(毫秒).
     */
    private long n2hHbCheckInterval = 10 * 1000L;
    /**
     * 上次检查N2H心跳的时间.
     */
    private long n2hHbLastCheck = 0L;
    //
    /**
     * 检查N2H主动事务超时的间隔时间(毫秒).
     */
    private long n2hItransCheckInterval = 1 * 1000L;
    /**
     * 上次检查N2H主动事务的时间.
     */
    private long n2hItransLastCheck = 0L;
    /**
     * 用于N2H上的主动事务(MODBUS)超时处理.
     */
    private final List<ModbusN2Hitrans> tmpModbus = new ArrayList<>();

    public void quartz(long now) {
        this.tscTimerMgr.quartz(now);
        this.quartzHeartBeat(now);
    }

    /*心跳检查*/
    public void quartzHeartBeat(long now) {
        if (now - this.n2hHbLastCheck < this.n2hHbCheckInterval) {
            return;
        }
        this.n2hHbLastCheck = now;
        Iterator<Map.Entry<ChannelHandlerContext, ActorNet>> iterator = ans.entrySet().iterator();
        while (iterator.hasNext()) {
            ActorNet an = iterator.next().getValue();
            if (an.lts == 0) {//刚连接，不关心
                continue;
            }
            if (now - an.lts < 6000) {/*还未超过心跳超时*/
                continue;
            }
            iterator.remove();
            an.evnDis();//发送缓存池中的信息
            an.ctx.close();//关闭
        }
    }

    /**
     * 事务检查
     *
     * @param now
     */
    public void quartzN2Hitrans(long now) {
        if (now - this.n2hItransLastCheck < this.n2hItransCheckInterval) {
            return;
        }
        this.n2hItransLastCheck = now;
        Iterator<Map.Entry<ChannelHandlerContext, ActorNet>> it = this.ans.entrySet().iterator();
        while (it.hasNext()) {
            ActorNet an = it.next().getValue();
            if (an.lts == 0) {/*刚上来的连接*/
                continue;
            }
            ModbusN2H n2h = (ModbusN2H) an;
            Iterator<Map.Entry<Short, ModbusN2Hitrans>> iter = n2h.trans.entrySet().iterator();
            while (iter.hasNext()) {
                ModbusN2Hitrans trans = iter.next().getValue();
                long elap = now - trans.lpts;//距离事务开始的时间
                if (elap < 15000) { //事务还未超时
                    continue;
                }
                this.tmpModbus.add(trans);
                iter.remove();
            }
        }
        for (ModbusN2Hitrans t : this.tmpModbus) {
            t.tm = true;
            t.n2h.timeout(t);
        }
        this.tmpModbus.clear();
    }
}
