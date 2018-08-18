package com.ice.house.core;

import com.ice.house.device.Device;
import com.ice.house.modbus.ModbusN2H;

/**
 * @author:ice
 * @Date: 2018/8/18 0018
 */
public class DeviceLog implements TscLog {

    /**
     * 所有连接上device的N2H都被视为设备, 服务器总是首先下发一条心跳请求, 待对方响应后确认身份.
     */
    @Override
    public void n2hEstab(ActorNet an) {
        ModbusN2H n2h = (ModbusN2H) an;
        Device device = new Device(null, an);
        device.evnEst();
    }
}
