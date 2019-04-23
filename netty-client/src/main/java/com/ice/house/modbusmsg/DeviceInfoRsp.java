package com.ice.house.modbusmsg;

import com.ice.house.msg.Modbus;
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
        Modbus.encodeModeBusHeader(this.header.tid, this.header.version, this.header.fcode, by);
        System.arraycopy(deviceInfo.getBytes(), 0, by, ModbusHeader.MODBUS_HEADER_LEN, 20);
        System.arraycopy(version.getBytes(), 0, by, ModbusHeader.MODBUS_HEADER_LEN + 20, 4);
        return by;
    }
}
