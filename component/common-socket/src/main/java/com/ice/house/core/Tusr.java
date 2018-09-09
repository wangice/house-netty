package com.ice.house.core;

import com.ice.house.task.actor.Actor;

/**
 * @author:ice
 * @Date: 2018/8/18 0018
 */
public abstract class Tusr<T1 extends ActorNet, T2> extends Actor {

    public T1 n2h = null;

    public String deviceNo;

    public Tusr() {
        super(ActorType.ITC);
    }

    /**
     * 连接是否存在
     */
    public boolean estab() {
        return this.n2h != null && this.n2h.est;
    }

    /**
     * 关闭连接
     */
    public void close() {
        if (this.n2h != null) {
            this.n2h.close();
        }
    }

    /**
     * 与Tusr相关联的N2H连接失去.
     */
    public abstract void evnDis();
}
