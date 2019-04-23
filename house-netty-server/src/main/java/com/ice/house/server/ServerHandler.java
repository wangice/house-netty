package com.ice.house.server;

import com.ice.house.Misc;
import com.ice.house.modbus.Modbus;
import com.ice.house.modbusmsg.HeartBeatReq;
import com.ice.house.msg.ModbusMsg;
import com.ice.house.service.impl.DeviceInfoRedisServiceImpl;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author:ice
 * @Date: 2018/8/11 14:56
 */
@Sharable
@Component
@Qualifier("serverHandler")
public class ServerHandler extends SimpleChannelInboundHandler<ModbusMsg> {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);


    @Resource
    private DeviceInfoRedisServiceImpl deviceInfoRedisService;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ModbusMsg msg)
            throws Exception {
        if (msg.header.fcode == Modbus.FC_HEARTBEAT) {
            logger.info("心跳");
        } else {
            logger.info("设备信息");
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
        //设备连接后第一次总会有服务器发送一次心跳
        HeartBeatReq heartBeatReq = Modbus
                .encodeHeartBeatReq((short) 0x0000 , Modbus.version,
                        (short) Modbus.cs_cfg_collector_def_heartbeat);
        logger.info("发送心跳：{}", Misc.obj2json(heartBeatReq));
        ctx.channel().writeAndFlush(heartBeatReq);//发送心跳
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.info("抛出异常{}", Misc.trace(cause));
        logger.info("id:{}", ctx.channel().id().asLongText());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Channel is disconnected");

        super.channelInactive(ctx);
    }


}
