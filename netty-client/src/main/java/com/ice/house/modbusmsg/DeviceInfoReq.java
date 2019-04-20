package com.ice.house.modbusmsg;

import com.ice.house.Misc;
import com.ice.house.Net;
import com.ice.house.ODateu;
import com.ice.house.msg.ModbusHeader;
import com.ice.house.msg.ModbusMsg;

import java.util.Date;

/**
 * @author:ice
 * @Date: 2018/9/2 0002
 */
public class DeviceInfoReq extends ModbusMsg {

    private static final int LENGTH = 4;

    public long date;//服务器发送时间


    public DeviceInfoReq(ModbusHeader header, byte[] bytes) {
        this.header = header;
        decode(bytes);
    }

    @Override
    public void decode(byte[] bytes) {
        this.date = Net.byte2long(bytes, 0);
    }

    @Override
    public byte[] bytes() {
        return null;
    }

    @Override
    public String toString() {
        return Misc.printf2Str("query device info msg time:{}",
                ODateu.parseDateyyyyMMddHHmmss(new Date(date)));
    }
}
