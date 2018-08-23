package com.ice.house.msg;

import com.ice.house.Misc;

public class ModbusHeader {
    /**
     * MODBUS-TCP fixed header length.
     */
    public static final int MODBUS_HEADER_LEN = 0x08;

    public short tid;//事务标识
    public byte version;//协议版本
    public int len;// length, 协议是四个字节
    public byte fcode;//功能码

    public String toString() {
        return Misc.printf2Str("tid: %04X, version: %04X, len: %04X, fcode: %02X(%s)", this.tid, this.version, this.len, this.fcode, this.fcode);
    }
}
