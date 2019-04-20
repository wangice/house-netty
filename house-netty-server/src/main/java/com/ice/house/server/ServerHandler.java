package com.ice.house.server;

import com.ice.house.Misc;
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

    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);


    @Resource
    private DeviceInfoRedisServiceImpl deviceInfoRedisService;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, ModbusMsg msg)
            throws Exception {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("RamoteAddress : " + ctx.channel().remoteAddress() + " active !");
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("抛出异常{}", Misc.trace(cause));
        log.info("id:{}", ctx.channel().id().asLongText());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Channel is disconnected");

        super.channelInactive(ctx);
    }


}
