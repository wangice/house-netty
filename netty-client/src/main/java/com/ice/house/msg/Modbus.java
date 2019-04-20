package com.ice.house.msg;

import com.ice.house.Net;
import com.ice.house.config.Config;
import com.ice.house.modbusmsg.DeviceInfoRsp;
import com.ice.house.modbusmsg.HeartBeatReq;

/**
 * @author:ice
 * @Date: 2018/8/24 0024
 */
public class Modbus {

    public static final byte FC_HEARTBEAT = (byte) 0x01;/*心跳.*/

    public static final byte FC_DEVICEINFO = (byte) 0x02;/*设备信息*/

    /**
     * 编码一个MODBUS-TCP公共头部.
     */
    public static final void encodeModeBusHeader(short tid, byte version, byte fcode, byte[] pdu) {
        System.arraycopy(Net.short2byte(tid), 0, pdu, 0, 2);
        pdu[2] = version;
        System.arraycopy(Net.int2byte(pdu.length - ModbusHeader.MODBUS_HEADER_LEN), 0, pdu, 3, 4);
        pdu[7] = fcode;
    }

    public static final ModbusHeader modbusHeader(short tid, byte version, int len, byte fcode) {
        ModbusHeader modbusHeader = new ModbusHeader();
        modbusHeader.tid = tid;
        modbusHeader.version = version;
        modbusHeader.len = len;
        modbusHeader.fcode = fcode;
        return modbusHeader;
    }

    public static final HeartBeatReq encodeHeartBeat(ModbusHeader header) {
        HeartBeatReq heartBeatReq = new HeartBeatReq();
        heartBeatReq.header = header;
        header.len = Config.deviceNo.length();
        heartBeatReq.deviceNo = Config.deviceNo;
        return heartBeatReq;
    }

    public static final DeviceInfoRsp encodeDeviceInfo(String deviceInfo, String version, ModbusHeader header) {
        DeviceInfoRsp deviceInfoReq = new DeviceInfoRsp();
        deviceInfoReq.header = header;
        deviceInfoReq.deviceInfo = deviceInfo;
        deviceInfoReq.version = version;
        header.len = DeviceInfoRsp.DEVICE_INFO_REQ_LENGTH;
        return deviceInfoReq;
    }

}
