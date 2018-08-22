package com.ice.house.modbusmsg;

import com.ice.house.msg.ModbusHeader;
import com.ice.house.msg.ModbusMsg;

/**
 * @author:ice
 * @Date: 2018/8/22 0022
 */
public class HeartBeatRsp extends ModbusMsg {

    public String deviceNo;//设备号

    public HeartBeatRsp() {

    }

    public HeartBeatRsp(ModbusHeader header, byte[] bytes) {
        this.header = header;
        deviceNo = new String(bytes);
    }

    @Override
    public byte[] bytes() {
        return null;
    }
}
