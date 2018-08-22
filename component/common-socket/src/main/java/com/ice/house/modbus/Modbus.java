package com.ice.house.modbus;

import com.ice.house.Net;
import com.ice.house.ODateu;
import com.ice.house.modbusmsg.HeartBeatReq;
import com.ice.house.msg.ModbusHeader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * @author:ice
 * @Date: 2018/8/16 0016
 */
public class Modbus {

    public static final byte version = (byte) 1;

    public static final int cs_cfg_collector_def_heartbeat = 25;

    public static final byte FC_HEARTBEAT = (byte) 0x01; /* 服务器向客户端发起的心跳. */

    public static List<Byte> serverFcode = new ArrayList<>();

    static {
        serverFcode.add(FC_HEARTBEAT);
    }

    /**
     * 编码一个MODBUS-TCP公共头部.
     */
    public static final void encodeModeBusHeader(short tid, byte version, byte fcode, byte[] pdu) {
        System.arraycopy(Net.short2byte(tid), 0, pdu, 0, 2);
        pdu[2] = version;
        System.arraycopy(Net.int2byte(pdu.length - ModbusHeader.MODBUS_HEADER_LEN), 0, pdu, 3, 4);
        pdu[7] = fcode;
    }

    /**
     * 从MODBUS-TCP报文中解析出一个MODBUS头部.
     */
    public ModbusHeader decodeModBusHeader(byte[] pdu, int ofst, int len) {
        ModbusHeader header = new ModbusHeader();
        header.tid = Net.byte2short(pdu, ofst);
        header.version = pdu[ofst + 2];
        header.len = Net.byte2int(pdu, ofst + 3);
        header.fcode = pdu[7];
        return header;
    }

    public static final ModbusHeader modbusHeader(short tid, byte version, int len, byte fcode) {
        ModbusHeader modbusHeader = new ModbusHeader();
        modbusHeader.tid = tid;
        modbusHeader.version = version;
        modbusHeader.len = len;
        modbusHeader.fcode = fcode;
        return modbusHeader;
    }

    /**
     * FC_HEARTBEAT请求编码, 此报文总是由服务器向采集器发起.
     */
    public static final HeartBeatReq encodeHeartBeatReq(short tid, byte version, short interval) {
        HeartBeatReq heartBeatReq = new HeartBeatReq();
        heartBeatReq.header = Modbus.modbusHeader(tid, version, HeartBeatReq.HEARTBEATREQLENGTH, Modbus.FC_HEARTBEAT);
        //
        Date ts = new Date(System.currentTimeMillis() - TimeZone.getDefault().getRawOffset()); /* 0时区的绝对时间. */
        heartBeatReq.y = (byte) ((ODateu.yearInt(ts) - 2000) % 0x100);
        heartBeatReq.m = (byte) ODateu.monthInt(ts);
        heartBeatReq.d = (byte) ODateu.dayInt(ts);
        heartBeatReq.h = (byte) ODateu.hourInt(ts);
        heartBeatReq.m = (byte) ODateu.minuteInt(ts);
        heartBeatReq.mi = (byte) ((ts.getTime() / 1000) % 60);
        heartBeatReq.interval = interval;
        return heartBeatReq;
    }

    /**
     * FC_HEARTBEAT请求编码, 此报文总是由服务器向采集器发起.
     */
    public static final byte[] encodeHeartBeatReq(short tid, byte version, byte y, byte m, byte d, byte h, byte mi, byte s, short interval) {
        byte by[] = new byte[ModbusHeader.MODBUS_HEADER_LEN + 1 /* y. */ + 1 /* m. */ + 1 /* d. */ + 1 /* h. */ + 1 /* mi. */ + 1 /* s. */ + 2 /* interval. */];
        Modbus.encodeModeBusHeader(tid, version, Modbus.FC_HEARTBEAT, by);
        //
        by[8] = y;
        by[9] = m;
        by[10] = d;
        by[11] = h;
        by[12] = mi;
        by[13] = s;
        System.arraycopy(Net.short2byte(interval), 0, by, 14, 2);
        return by;
    }
}
