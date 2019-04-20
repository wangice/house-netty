package com.ice.house.client;

import com.ice.house.Misc;
import com.ice.house.config.Config;
import com.ice.house.modbusmsg.DeviceInfoRsp;
import com.ice.house.modbusmsg.HeartBeatReq;
import com.ice.house.msg.Modbus;
import com.ice.house.msg.ModbusMsg;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author:ice
 * @Date: 2018/8/11 14:56
 */
@Sharable
@Component
@Qualifier("serverHandler")
public class ServerHandler extends SimpleChannelInboundHandler<ModbusMsg> {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ModbusMsg msg)
            throws Exception {
        logger.debug("server to client msg:{}", Misc.obj2json(msg));
        if (msg.header.fcode == Modbus.FC_HEARTBEAT) {
            logger.debug("server to client heart beat");
            HeartBeatReq heartBeatReq = Modbus.encodeHeartBeat(msg.header);
            logger.debug("client to server heart beat:{}", Misc.obj2json(heartBeatReq));
            ctx.channel().writeAndFlush(heartBeatReq);
        } else if (msg.header.fcode == Modbus.FC_DEVICEINFO) {
            logger.debug("server to client query device info");
            DeviceInfoRsp deviceInfoReq = Modbus.encodeDeviceInfo(Config.deviceNo, Config.version, msg.header);
            ctx.channel().writeAndFlush(deviceInfoReq);

        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("client to server connect");
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
        logger.error("client exception，close：{}", Misc.trace(cause));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.warn("Channel is disconnected");
        super.channelInactive(ctx);
    }
}
