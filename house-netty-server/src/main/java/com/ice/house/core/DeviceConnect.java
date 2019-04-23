package com.ice.house.core;

import com.ice.house.modbus.Modbus;
import com.ice.house.modbusmsg.HeartBeatReq;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author ice
 * @Date 2019/4/22 16:58
 */
@Scope("prototype")
@Component("deviceConnect")
public class DeviceConnect {
    public static final Logger logger = LoggerFactory.getLogger(DeviceConnect.class);

    private String deviceId;

    private ChannelHandlerContext ctx;

    /*设备连接上服务器之后发送心跳.*/
    public void evnEst() {
        HeartBeatReq heartBeatReq = Modbus
                .encodeHeartBeatReq((short) 0x0000 /* 在future中被替换. */, Modbus.version,
                        (short) Modbus.cs_cfg_collector_def_heartbeat);
        ctx.writeAndFlush(heartBeatReq);//发送心跳
    }


    /**
     * 失去连接
     */
    public void evnDis() {
        if (deviceId != null) {
            DeviceConnectionFactory.getInstance().remove(deviceId);
        }
    }

}
