package com.ice.house.modbusmsg;

import com.ice.house.msg.ModbusHeader;
import com.ice.house.msg.ModbusMsg;

/**
 * @author:ice
 * @Date: 2018/9/2 0002
 */
public class DeviceInfoRsp extends ModbusMsg {
    public static final int DEVICE_INFO_REQ_LENGTH = 24;
    public String deviceInfo;//设备信息
    public String version;//版本信息

    @Override
    public void decode(byte[] bytes) {

    }

    @Override
    public byte[] bytes() {
        byte[] by = new byte[DEVICE_INFO_REQ_LENGTH + ModbusHeader.MODBUS_HEADER_LEN];
        System.arraycopy(this.header, 0, by, 0, ModbusHeader.MODBUS_HEADER_LEN);
        System.arraycopy(deviceInfo, 0, by, ModbusHeader.MODBUS_HEADER_LEN, 20);
        System.arraycopy(version, 0, by, ModbusHeader.MODBUS_HEADER_LEN + 20, 4);
        return by;
    }
}
