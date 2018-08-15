package com.ice.house.msg;

import com.ice.house.Net;

/**
 * @author:ice
 * @Date: 2018/8/12 0012
 */
public class ModbusMsg {
    public ModbusHeader header;

    public byte[] data;//数据

    public byte[] bytes() {
        byte[] by = new byte[ModbusHeader.MODBUS_HEADER_LEN + data.length];
        encodeModeBusHeader(this.header.tid, this.header.version, this.header.fcode, by);
        System.arraycopy(data, 0, by, ModbusHeader.MODBUS_HEADER_LEN, data.length);
        return by;
    }

    public ModbusMsg(ModbusHeader header, byte[] data) {
        this.header = header;
        this.data = data;
    }

    /**
     * 编码一个MODBUS-TCP公共头部.
     */
    public void encodeModeBusHeader(short tid, byte version, byte fcode, byte[] pdu) {
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
}
