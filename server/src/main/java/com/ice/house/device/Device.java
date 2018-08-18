package com.ice.house.device;

import com.ice.house.core.ActorNet;
import com.ice.house.task.actor.Actor;

/**
 * @author:ice
 * @Date: 2018/8/18 0018
 */
public class Device<ModbusN2H extends ActorNet, ModbusMsg> extends Actor {

    private String deviceNo;//设备号
    private ModbusN2H n2h;

    public Device(String deviceNo, ModbusN2H modbusN2H) {
        super(ActorType.ITC);
        this.deviceNo = deviceNo;
        this.n2h = modbusN2H;
    }

    /**
     * 连接建立之初，总是会有服务器向客户端发送一条心跳，确认身份
     */
    public void evnEst() {


    }
}
