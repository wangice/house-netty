package com.ice.house.core;

import com.ice.house.device.DeviceConnect;
import com.ice.house.modbus.ModbusN2H;
import com.ice.house.msg.ModbusMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author:ice
 * @Date: 2018/8/18 0018
 */
@Service
public class DeviceLog implements TscLog {

  @Autowired
  private ApplicationContext act;

  /**
   * 所有连接上device的N2H都被视为设备, 服务器总是首先下发一条心跳请求, 待对方响应后确认身份.
   */
  @Override
  public void n2hEstab(ActorNet an) {
    ModbusN2H n2h = (ModbusN2H) an;
    n2h.tusr = (Tusr<ModbusN2H, ModbusMsg>) act.getBean("deviceConnect");
    ((DeviceConnect) n2h.tusr).setNodBusN2H(n2h);
    ((DeviceConnect) n2h.tusr).evnEst();
  }
}
