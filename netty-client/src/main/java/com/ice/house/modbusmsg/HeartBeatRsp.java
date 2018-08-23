package com.ice.house.modbusmsg;

import com.ice.house.Net;
import com.ice.house.ODateu;
import com.ice.house.msg.ModbusHeader;
import com.ice.house.msg.ModbusMsg;

import java.util.Date;
import java.util.TimeZone;

/**
 * @author:ice
 * @Date: 2018/8/24 0024
 */
public class HeartBeatRsp extends ModbusMsg {
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

    public HeartBeatRsp() {
        
    }

    public HeartBeatRsp(ModbusHeader header, byte[] bytes) {
        this.header = header;
        decode(bytes);
    }

    @Override
    public void decode(byte[] bytes) {
        this.y = bytes[0];
        this.m = bytes[1];
        this.d = bytes[2];
        this.h = bytes[3];
        this.mi = bytes[4];
        this.s = bytes[5];
        this.interval = Net.byte2short(bytes, 6);
    }

    @Override
    public byte[] bytes() {
        return null;
    }
}
