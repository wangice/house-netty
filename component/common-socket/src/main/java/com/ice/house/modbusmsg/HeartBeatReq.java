package com.ice.house.modbusmsg;

import com.ice.house.Misc;
import com.ice.house.Net;
import com.ice.house.ODateu;
import com.ice.house.modbus.Modbus;
import com.ice.house.msg.ModbusHeader;
import com.ice.house.msg.ModbusMsg;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author:ice
 * @Date: 2018/8/19 0019
 */
public class HeartBeatReq extends ModbusMsg {

    public static final int HEARTBEATREQLENGTH = 8;

    /**
     * 0时区绝对时间.
     */
    public byte y;  //年
    public byte m;//月.
    public byte d;//日.
    public byte h;//时.
    public byte mi;//分.
    public byte s;//秒.
    public short interval;//心跳周期.

    @Override
    public byte[] bytes() {
        byte[] by = new byte[ModbusHeader.MODBUS_HEADER_LEN + HEARTBEATREQLENGTH];
        Modbus.encodeModeBusHeader(this.header.tid, this.header.version, this.header.fcode, by);

        Date ts = new Date(System.currentTimeMillis() - TimeZone.getDefault().getRawOffset()); /* 0时区的绝对时间. */
        by[8] = (byte) ((ODateu.yearInt(ts) - 2000) % 0x100);
        by[9] = (byte) ODateu.monthInt(ts);
        by[10] = (byte) ODateu.dayInt(ts);
        by[11] = (byte) ODateu.hourInt(ts);
        by[12] = (byte) ODateu.minuteInt(ts);
        by[13] = (byte) ((ts.getTime() / 1000) % 60);
        System.arraycopy(Net.short2byte(interval), 0, by, 14, 2);
        return by;
    }

    public String toString() {
        return Misc.printf2Str("%s, y: %02X, m: %02X, d: %02X, h: %02X, mi: %02X, s: %02X, interval: %04X", this.header, this.y, this.m, this.d, this.h, this.mi, this.s, this.interval);
    }
}
