package com.ice.house.modbusmsg;

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

    public DeviceInfoRsp(ModbusHeader header, byte[] bytes) {
        this.header = header;
        this.decode(bytes);
    }

    @Override
    public byte[] bytes() {
        return null;
    }

    public void decode(byte[] bytes) {
        byte[] deviceInfoByte = new byte[20];
        byte[] versionByte = new byte[4];
        System.arraycopy(bytes, 0, deviceInfoByte, 0, 20);
        System.arraycopy(bytes, 20, versionByte, 0, 4);
        this.deviceInfo = new String(deviceInfoByte);
        this.version = new String(versionByte);
    }
}
