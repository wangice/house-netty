package com.ice.house.modbusmsg;

import com.ice.house.msg.ModbusMsg;

/**
 * @author:ice
 * @Date: 2018/9/2 0002
 */
public class DeviceInfoRsp extends ModbusMsg {

  public String deviceId;//设备ID
  public String key;


  @Override
  public byte[] bytes() {
    return null;
  }
}
