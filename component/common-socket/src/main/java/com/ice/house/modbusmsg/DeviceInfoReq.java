package com.ice.house.modbusmsg;

import com.ice.house.Misc;
import com.ice.house.ODateu;
import com.ice.house.msg.ModbusMsg;

import java.util.Date;

/**
 * @author:ice
 * @Date: 2018/9/2 0002
 */
public class DeviceInfoReq extends ModbusMsg {

  public static final int DEVICE_INFO_REQ_LENGTH = 4;

  public long date;//服务器发送时间

  @Override
  public byte[] bytes() {
    byte[] by = new byte[DEVICE_INFO_REQ_LENGTH];
    System.arraycopy(date, 0, by, 0, 4);
    return by;
  }

  @Override
  public String toString() {
    return Misc.printf2Str("query device info msg time:{}",
        ODateu.parseDateyyyyMMddHHmmss(new Date(date)));
  }
}
