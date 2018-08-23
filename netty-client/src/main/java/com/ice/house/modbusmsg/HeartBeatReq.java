package com.ice.house.modbusmsg;

import com.ice.house.config.Config;
import com.ice.house.msg.Modbus;
import com.ice.house.msg.ModbusHeader;
import com.ice.house.msg.ModbusMsg;

/**
 * @author:ice
 * @Date: 2018/8/24 0024
 */
public class HeartBeatReq extends ModbusMsg {

    public String deviceNo;//设备号

    @Override
    public void decode(byte[] bytes) {

    }

    @Override
    public byte[] bytes() {
        byte[] bytes = new byte[Config.DEVICE_LENGTH + ModbusHeader.MODBUS_HEADER_LEN];
        Modbus.encodeModeBusHeader(header.tid, header.version, header.fcode, bytes);
        System.arraycopy(deviceNo.getBytes(), 0, bytes, ModbusHeader.MODBUS_HEADER_LEN, Config.DEVICE_LENGTH);
        return bytes;
    }
}
