package com.ice.house.server;

import com.ice.house.ModbusMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author:ice
 * @Date: 2018/8/12 0012
 */
public class ModBusMsgDecode extends MessageToMessageDecoder<ModbusMsg> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ModbusMsg modbusMsg, List<Object> list) throws Exception {
        
    }
}
