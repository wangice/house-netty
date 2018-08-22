package com.ice.house.device;

import com.ice.house.core.ActorNet;
import com.ice.house.core.Tusr;
import com.ice.house.modbus.Modbus;
import com.ice.house.modbus.ModbusN2H;
import com.ice.house.modbusmsg.HeartBeatReq;
import com.ice.house.msg.ModbusMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author:ice
 * @Date: 2018/8/18 0018
 */
public class Device extends Tusr<ModbusN2H, ModbusMsg> {

    private static final Logger logger = LoggerFactory.getLogger(Device.class);

    private String deviceNo;//设备号

    public Device(String deviceNo, ModbusN2H modbusN2H) {
        super(deviceNo, modbusN2H);
    }


    /**
     * 连接建立之初，总是会有服务器向客户端发送一条心跳，确认身份
     */
    public void evnEst() {
        logger.info("发送心跳");
        HeartBeatReq heartBeatReq = Modbus.encodeHeartBeatReq((short) 0x0000 /* 在future中被替换. */, Modbus.version, (short) Modbus.cs_cfg_collector_def_heartbeat);
        this.n2h.future(heartBeatReq, rspCb -> {
            logger.info("接受到客户端发来的信息");
        }, tmb -> {
            logger.debug("first heartbeat timeout, we will close it!");
            this.close();
        });
    }

    @Override
    public void evnDis() {
        //失去连接后，通知状态改变
    }
}
