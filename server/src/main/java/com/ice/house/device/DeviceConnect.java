package com.ice.house.device;

import com.ice.house.Misc;
import com.ice.house.core.Tusr;
import com.ice.house.modbus.Modbus;
import com.ice.house.modbus.ModbusN2H;
import com.ice.house.modbusmsg.DeviceInfoReq;
import com.ice.house.modbusmsg.DeviceInfoRsp;
import com.ice.house.modbusmsg.HeartBeatReq;
import com.ice.house.modbusmsg.HeartBeatRsp;
import com.ice.house.msg.ModbusMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author:ice
 * @Date: 2018/8/18 0018
 */
@Scope("prototype")
@Component("deviceConnect")
public class DeviceConnect extends Tusr<ModbusN2H, ModbusMsg> {

    private static final Logger logger = LoggerFactory.getLogger(DeviceConnect.class);

    public void setNodBusN2H(ModbusN2H modbusN2H) {
        this.n2h = modbusN2H;
    }

    /**
     * 连接建立之初，总是会有服务器向客户端发送一条心跳，确认身份
     */
    public void evnEst() {
        logger.info("send heartbeat");
        HeartBeatReq heartBeatReq = Modbus
                .encodeHeartBeatReq((short) 0x0000 /* 在future中被替换. */, Modbus.version,
                        (short) Modbus.cs_cfg_collector_def_heartbeat);
        this.n2h.future(heartBeatReq, rspCb -> {
            HeartBeatRsp rsp = (HeartBeatRsp) rspCb.modbusMsg;
            this.deviceNo = rsp.deviceNo;
            logger.debug("client to server heart:{}", Misc.obj2json(rsp));
            //获取设备的详细信息
            this.sendDeviceInfo();
        }, tmb -> {
            logger.debug("first heartbeat timeout, we will close it!");
            this.close();
        });
    }

    public void sendDeviceInfo() {
        logger.info("send msg to query device info");
        DeviceInfoReq req = Modbus.encodeDeviceInfoReq((short) 0x0000/* 在future中被替换. */, Modbus.version,
                System.currentTimeMillis());
        this.n2h.future(req, rspCb -> {
            DeviceInfoRsp rsp = (DeviceInfoRsp) rspCb.modbusMsg;
            logger.debug("query device info success! msg:{}", Misc.obj2json(rsp));
            this.deviceNo = rsp.deviceInfo;
            DeviceConnectionFactory.getInstance().put(deviceNo, this);
            this.n2h.worker.tscTimerMgr.addTimerOneTime(60, v -> sendHeartbeat());
        }, tmb -> {
            logger.warn("query device info timeout");
            if (!this.n2h.est) {
                return;
            }
            this.n2h.worker.tscTimerMgr.addTimerOneTime(60, v -> sendHeartbeat());//继续发送心跳
        });
    }

    @Override
    public void evnDis() {
        //失去连接后，通知状态改变
        logger.info("device({}) disconnect", this.deviceNo);
        if (this.deviceNo != null) {
            DeviceConnectionFactory.getInstance().remove(this.deviceNo);
        }
    }

    public void sendHeartbeat() {
        HeartBeatReq heartBeatReq = Modbus
                .encodeHeartBeatReq((short) 0x0000 /* 在future中被替换. */, Modbus.version,
                        (short) Modbus.cs_cfg_collector_def_heartbeat);
        if (this.n2h == null) {
            return;
        }
        this.n2h.future(heartBeatReq, rspCb -> {
            if (this.n2h.est) {
                this.n2h.worker.tscTimerMgr.addTimerOneTime(60, v -> sendHeartbeat());//添加一个定时器任务再过60秒后发送心跳
            }
        }, tmb -> {
            logger.debug("first heartbeat timeout, we will close it!");
//            this.close();
        });
    }
}
